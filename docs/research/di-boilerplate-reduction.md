# Reducing DI Configuration Boilerplate in a Clean Architecture Codebase

**Problem.** Every new use case, repository, or service forces an edit to `application/configs/UseCaseConfig.java` or
`application/configs/ServiceConfig.java` (`new XUseCaseImpl(dep1, dep2, ...)` as a `@Bean`). The config files grow
linearly with the codebase and adding a single new constructor parameter ripples into the config layer. We want to
eliminate this while respecting the rule that `core/` has no Spring imports.

This document consolidates web research across four angles: Spring-native patterns, compile-time DI alternatives, what
the Clean Architecture community actually says, and real-world case studies of the same refactor.

---

## TL;DR — Recommendation

**Adopt a `UseCase` marker interface in `core/usecases/` plus a single `@ComponentScan` with an `AssignableTypeFilter`
in `application/configs/`.** This keeps the existing ArchUnit "no Spring in core" rule green, deletes `UseCaseConfig`
and `ServiceConfig` entirely, and is the canonical Spring-native solution recommended by Baeldung, Reflectoring, and the
Spring reference docs for "register beans the framework doesn't know about."

If the team is willing to relax the strict purity rule (which most real-world Clean Architecture Spring projects do), a
custom `@UseCase` stereotype meta-annotated with `@Component` (the `buckpal` pattern) is slightly more readable at the
cost of one `org.springframework.stereotype.Component` import in `core/`.

The compile-time DI alternatives (Avaje Inject, Dagger, custom APT) are not worth the integration cost for a single
Spring-Boot service.

---

## Philosophy: What the Clean Architecture Community Says

The "should the inner layers know about Spring?" debate has been running for over a decade, and the community has split
into two reasonably articulate camps. Neither is fringe.

### The Purist Camp

Uncle Bob's original position is that **frameworks are details** — the same category as databases and web servers. The
mechanism for honouring this is the **Main component**: the outermost, "dirtiest" partition whose job is to instantiate,
configure and wire all the inner objects. The book is explicit that Main is allowed to know about Spring; nothing inside
Main is. That is precisely the role our hand-written `UseCaseConfig` is playing — it *is* the Main partition in
Java/Spring form. From this lens, the boilerplate is not accidental complexity; it is the visible price of dependency
inversion, and the **Humble Object** pattern (Chapter 23) generalises it: push the hard-to-test, framework-coupled glue
to the very edge so the centre stays pure and trivially unit-testable.

Tom Hombergs' *Get Your Hands Dirty on Clean Architecture* (Ch. 9–10) walks through three assembly strategies — plain
Java, classpath scanning, and `@Configuration`/`@Bean` — and recommends manual `@Configuration` for: no magic, traceable
wiring, and a core module that compiles without Spring on the classpath.

### The Pragmatist Camp

Articulated forcefully in Mews Engineering's *"Clean" Architecture vs pragmatic architecture* and echoed across
Baeldung/Reflectoring write-ups: much of this ceremony is **cargo-culted abstraction** that "adds complexity" while "
hiding nothing." Pragmatists keep the *acyclic dependency rule* and ports/adapters topology, but argue that annotating a
`UseCaseImpl` with `@Component` violates no architectural invariant a customer can perceive — the dependency graph still
points inward, the use case still depends only on port interfaces, and tests still run without Spring (because
`@Component` is inert when you instantiate the class directly with `new`).

The line most teams draw: **runtime-behavioural** annotations (`@Transactional`, `@Entity`, `@JsonProperty`) must stay
out of the domain because they require framework context to test, but a *marker* annotation like `@Component` does not
change runtime semantics.

### Synthesis

Breaking the no-Spring-in-core rule is **not heretical** in 2026 — it is one of two mainstream positions. Teams that
relaxed the rule almost universally report that the predicted portability loss never materialised, while the boilerplate
savings were immediate and ongoing.

**Sources:** Uncle
Bob, [A Little Architecture](https://blog.cleancoder.com/uncle-bob/2016/01/04/ALittleArchitecture.html).
Reflectoring, [Clean Architecture Boundaries with Spring Boot and ArchUnit](https://reflectoring.io/java-components-clean-boundaries/).
Mews, [Clean Architecture vs pragmatic architecture](https://developers.mews.com/clean-architecture-vs-pragmatic-architecture/).

---

## Approach A: Spring-Native Patterns

Five techniques, ranked from least to most invasive.

### A1. Custom `@UseCase` stereotype meta-annotated with `@Component`

Put the annotation in a tiny shared module so `core/` only depends on `spring-context` for compile-time annotation
resolution.

```java
@Target(TYPE) @Retention(RUNTIME) @Documented
@Component
public @interface UseCase {}
```

Then `@UseCase` on `CreateCourseUseCaseImpl`; Spring auto-scans and constructor-injects.

- **Pros:** zero per-use-case wiring; intent (`@UseCase`) is louder than `@Service`; works with
  `@RequiredArgsConstructor`.
- **Cons:** `core/` imports `org.springframework.stereotype.Component`. Purists object; pragmatists shrug.
- **Clean Architecture fit:** *Mostly intact* — the dependency rule is about behavioural coupling, not annotation jars.

### A2. `@ComponentScan` with `AssignableTypeFilter` (zero annotations in core) — RECOMMENDED

Keep `core/` 100% Spring-free. Add a marker interface in `core/`:

```java
// core/usecases/UseCase.java
public interface UseCase {}
```

Every `*UseCase` interface extends it. Then in `application/configs/`:

```java
@Configuration
@ComponentScan(
  basePackages = "com.aura.core.usecases",
  useDefaultFilters = false,
  includeFilters = @Filter(type = ASSIGNABLE_TYPE, classes = UseCase.class))
class UseCaseScanConfig {}
```

- **Pros:** core has zero Spring imports — annotation *or* runtime; one config file, never edited again. Constructor
  injection resolves automatically.
- **Cons:** Spring instantiates classes that lack any stereotype, which can surprise new contributors. Document it once
  in CLAUDE.md.
- **Clean Architecture fit:** **Purest option** that still eliminates the boilerplate. Recommended by Baeldung's
  component-scan filter guide.

### A3. `ImportBeanDefinitionRegistrar` + `ClassPathScanningCandidateComponentProvider`

Programmatic A2 — useful if you need custom bean names, qualifiers, or to register from JARs you don't own.

```java
public class UseCaseRegistrar implements ImportBeanDefinitionRegistrar {
  public void registerBeanDefinitions(AnnotationMetadata m, BeanDefinitionRegistry r) {
    var scanner = new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AssignableTypeFilter(UseCase.class));
    scanner.findCandidateComponents("com.aura.core.usecases")
           .forEach(bd -> r.registerBeanDefinition(decapitalize(bd), bd));
  }
}
```

- **Pros:** maximum flexibility.
- **Cons:** more code than A2 for negligible gain unless you need the flexibility. Spring Data, MyBatis, Feign all use
  this pattern internally.

### A4. `@ImportSelector` + auto-configuration

Pair with Spring Boot's `META-INF/spring/...AutoConfiguration.imports`. Lets `application/` pick up use cases the way it
picks up `JpaRepositoriesAutoConfiguration`. **Overkill** for one consumer; only worth it if `core/` becomes a published
artifact consumed by multiple services.

### A5. Sticking with manual wiring

The status quo. Worth keeping only if the team values explicit grep-able wiring more than maintenance time, or if
`core/` may genuinely be lifted onto a non-Spring runtime (which most teams never do).

**Sources:
** [Spring Framework — Classpath Scanning](https://docs.spring.io/spring-framework/reference/core/beans/classpath-scanning.html), [Baeldung —
`@ComponentScan` Filter Types](https://www.baeldung.com/spring-componentscan-filter-type), [Spring Annotation Programming Model](https://github.com/spring-projects/spring-framework/wiki/Spring-Annotation-Programming-Model).

---

## Approach B: Compile-Time and Standards-Based DI

Considered for completeness; none beat Approach A for this codebase.

### B1. Spring + JSR-330 (`jakarta.inject.Singleton`)

`jakarta.inject.Singleton` is a **JCP standard**, not a Spring annotation. Spring has treated it like `@Component` since
3.0.

```java
@Singleton  // jakarta.inject.Singleton — standard Java, not Spring
public class CreateCourseUseCaseImpl implements CreateCourseUseCase { ... }
```

Plus one `@ComponentScan("com.aura.core.usecases")` in `application/`.

- **`core/` runtime dep:** `jakarta.inject-api` (≈3 KB, pure annotations, JCP standard). Many Clean Architecture teams
  accept this as a *standard*, not a *framework*.
- **Verdict:** A good middle ground between A1 (Spring stereotype) and A2 (zero annotations). Cleaner than A1 to a
  purist eye.

### B2. Avaje Inject

JSR-330 compile-time DI, zero reflection. Coexisting with Spring requires building a parallel `BeanScope` and bridging —
effectively replacing Spring DI for `core/`. **Migration cost > payoff** for a single Spring-Boot service.

### B3. Dagger 2 in Spring

No canonical bridge exists. Treat them as alternatives, not co-residents. **Not realistic.**

### B4. Custom annotation processor that generates `@Configuration`

Write an APT processor that finds `*UseCaseImpl` and emits
`application/target/generated-sources/.../GeneratedUseCaseConfig.java`. Preserves "zero annotations in core" but costs
medium-high build effort (custom processor, IDE integration, ServiceLoader registration). Worth it **only if** option A2
or B1 are judged unacceptable.

### B5. Spring AOT (`BeanRegistrationAotProcessor`)

Designed for native image optimization of *already-registered* beans. Does not discover new ones. **Wrong tool.**

**Sources:
** [Avaje Inject](https://avaje.io/inject/), [Spring — Using JSR-330 Standard Annotations](https://docs.spring.io/spring-framework/reference/core/beans/standard-annotations.html), [Micronaut Guide](https://docs.micronaut.io/latest/guide/).

---

## Real-World Case Studies

Across the most-cited Spring Clean Architecture examples, three camps are visible.

### Purist — keep `core/` framework-free, pay the `@Bean` tax

**[mattia-battiston/clean-architecture-example](https://github.com/mattia-battiston/clean-architecture-example)** is the
canonical reference. `application/core` has zero Spring imports. All wiring lives in one `UseCaseConfiguration.java`:

```java
@Bean
public ReconcileBroadbandAccessDevicesUseCase reconcileBroadbandAccessDevicesUseCase(
    GetDeviceDetailsFromModel a, GetSerialNumberFromModel b,
    GetSerialNumberFromReality c, UpdateSerialNumberInModel d) {
  return new ReconcileBroadbandAccessDevicesUseCase(a, b, c, d);
}
```

This is **exactly our current pattern.** Same posture appears in Red Hat Developer's Clean Architecture article and most
Kotlin/Spring-Boot purist write-ups.

### Hybrid — custom `@UseCase` stereotype (the buckpal pattern)

**[thombergs/buckpal](https://github.com/thombergs/buckpal)** (companion code for *Get Your Hands Dirty on Clean
Architecture*, 2nd ed.) is the most-cited example:

```java
@Target(ElementType.TYPE) @Retention(RUNTIME) @Documented
@Component
public @interface UseCase {
  @AliasFor(annotation = Component.class) String value() default "";
}
```

```java
@RequiredArgsConstructor @UseCase @Transactional
public class SendMoneyService implements SendMoneyUseCase { ... }
```

Same shape in Arho Huttunen's *Hexagonal Architecture With Spring Boot* walkthrough.

### Pragmatic — `@Component`/`@Service` directly in core

Reflectoring's *Clean Architecture Boundaries with Spring Boot and ArchUnit* (also Hombergs) drops the custom annotation
entirely. Defended position: `@Component` is a one-token cost, ArchUnit still enforces direction-of-dependency rules,
reviewers prefer scanning over hand-written `@Bean` factories. Baeldung's *Clean Architecture with Spring Boot* lands
here too.

### Spring Modulith — orthogonal but relevant

Doesn't solve the `@Bean` question, but reframes it: "replace cross-module bean dependencies with an application event"
via `@ApplicationModuleListener`. Removes a class of cross-module injection points entirely.

### What the field actually does

Hybrid `@UseCase` (buckpal) and pragmatic plain `@Component` (Reflectoring, Baeldung, Huttunen) dominate roughly **4:1**
over strict-purist examples in starter repos. Mattia Battiston's purist setup is widely respected but almost always
cited as *reference*, rarely as the chosen pattern. Teams that keep manual `@Bean` wiring tend to do so for a specific
reason — multi-module Gradle/Maven boundaries where `core/` literally does not have Spring on its classpath — not for
aesthetic purity.

**Sources:
** [buckpal](https://github.com/thombergs/buckpal), [clean-architecture-example](https://github.com/mattia-battiston/clean-architecture-example), [Reflectoring: Clean Boundaries with ArchUnit](https://reflectoring.io/java-components-clean-boundaries/), [Baeldung: Clean Architecture with Spring Boot](https://www.baeldung.com/spring-boot-clean-architecture), [Arho Huttunen: Hexagonal Architecture With Spring Boot](https://www.arhohuttunen.com/hexagonal-architecture-spring-boot/), [Spring Modulith intro](https://spring.io/blog/2022/10/21/introducing-spring-modulith/).

---

## Decision Matrix for This Codebase

| Option                                            | Boilerplate eliminated | `core/` stays Spring-free           | ArchUnit "no Spring in core" still passes | Migration cost                                                                      |
|---------------------------------------------------|------------------------|-------------------------------------|-------------------------------------------|-------------------------------------------------------------------------------------|
| **A2: Marker interface + `AssignableTypeFilter`** | ✅ fully                | ✅ yes                               | ✅ yes                                     | Low: 1 marker interface, 1 `@Configuration`, delete `UseCaseConfig`/`ServiceConfig` |
| **A1: `@UseCase` meta-stereotype**                | ✅ fully                | ❌ imports `@Component`              | ❌ needs rule relaxation                   | Low: 1 annotation, 1 line per impl                                                  |
| **B1: `jakarta.inject.Singleton`**                | ✅ fully                | ⚠️ imports JCP standard, not Spring | Depends on how rule is worded             | Low: 1 annotation, 1 line per impl                                                  |
| **A3: `ImportBeanDefinitionRegistrar`**           | ✅ fully                | ✅ yes                               | ✅ yes                                     | Medium                                                                              |
| **B4: Custom APT processor**                      | ✅ fully                | ✅ yes                               | ✅ yes                                     | High                                                                                |
| **Status quo (manual `@Bean`)**                   | ❌ no                   | ✅ yes                               | ✅ yes                                     | Zero                                                                                |

**Recommendation: A2.** It is the only option that fully eliminates boilerplate, keeps `core/` 100% Spring-free, and
passes the existing ArchUnit constraint without any rule changes. The "surprise factor" cost (Spring instantiates
classes without stereotype annotations) is paid once in a CLAUDE.md note.

### Concrete migration sketch

1. Add `core/usecases/UseCase.java` — empty marker interface.
2. Make every `*UseCase` interface in `core/usecases/<domain>/` extend `UseCase`.
3. Create `application/src/main/java/com/aura/app/configs/UseCaseScanConfig.java` with the `@ComponentScan` +
   `AssignableTypeFilter` shown in section A2.
4. Repeat the pattern for repositories (marker `Repository` interface in `core/repositories/`, scan
   `details/springdata/repositories/impl/`) and services (marker in `details/services/`, scan accordingly).
5. Delete `UseCaseConfig.java` and `ServiceConfig.java`.
6. Update CLAUDE.md "Adding a new use case" instructions: drop step 2 ("Register as `@Bean`"). New steps: create
   interface extending `UseCase`, create impl, done.
7. Verify `PreAuthorizeConventionTest` and any other ArchUnit tests still pass.
8. Run `mvn test -pl component-tests` to confirm no regression.

Constructor injection on use case impls already works via `@RequiredArgsConstructor` — no other code changes needed.
