# SpiceDB Authorization Requirements Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Update SpiceDB schema, controller annotations, service relationship writes, and tests to implement the full role-based authorization model.

**Architecture:** The SpiceDB schema gains `parent_system` cascade, `modifier`/`educator`/`parent` relations, two-tier `view`/`view_all` permissions, and resource-specific `create_<type>` permissions. Controller `@PreAuthorize` annotations switch from type-level null checks to instance-level parent resource checks. Services write missing relationships for levels, subjects, topics, and institution-to-system links.

**Tech Stack:** SpiceDB (Zanzibar ReBAC), Spring Security `@PreAuthorize` with SpEL, Java 25, JUnit 5, Mockito, ArchUnit

**Spec:** `docs/superpowers/specs/2026-04-28-spicedb-authorization-requirements-design.md`

---

### Task 1: Update SpiceDB Schema

**Files:**
- Modify: `application/src/main/resources/spicedb/schema.zed`

- [ ] **Step 1: Replace the schema file contents**

```zed
definition user {}

definition system {
    relation admin: user

    permission manage = admin
    permission create_institution = admin
    permission view = admin
    permission delegate = admin
}

definition institution {
    relation parent_system: system
    relation admin: user
    relation viewer: user
    relation educator: user
    relation parent_user: user

    permission manage = admin + parent_system->manage
    permission create_outlet = admin + parent_system->manage
    permission create_account = admin + parent_system->manage
    permission create_subject = admin + parent_system->manage
    permission create_level = admin + parent_system->manage
    permission view = viewer + admin + educator + parent_user + parent_system->view
    permission view_all = viewer + admin + parent_system->view
    permission delegate = admin + parent_system->delegate
}

definition outlet {
    relation parent_institution: institution
    relation admin: user
    relation viewer: user
    relation modifier: user

    permission manage = admin + modifier + parent_institution->manage
    permission create_course = admin + modifier + parent_institution->manage
    permission create_lesson = admin + modifier + parent_institution->manage
    permission view = viewer + admin + modifier + parent_institution->view
    permission view_all = viewer + admin + modifier + parent_institution->view_all
    permission delegate = admin + parent_institution->delegate
}

definition course {
    relation parent_outlet: outlet
    relation admin: user
    relation educator: user
    relation viewer: user

    permission manage = admin + educator + parent_outlet->manage
    permission create_lesson = admin + parent_outlet->manage
    permission view = viewer + educator + admin + parent_outlet->view_all
    permission delegate = admin + parent_outlet->delegate
}

definition lesson {
    relation parent_course: course
    relation parent_outlet: outlet
    relation admin: user
    relation educator: user
    relation viewer: user

    permission manage = admin + educator + parent_course->manage + parent_outlet->manage
    permission view = viewer + admin + educator + parent_course->view + parent_outlet->view_all
}

definition account {
    relation owner: user
    relation parent_institution: institution

    permission manage = owner + parent_institution->manage
    permission view = owner + parent_institution->view
}

definition level {
    relation parent_institution: institution

    permission manage = parent_institution->manage
    permission view = parent_institution->view
}

definition subject {
    relation parent_institution: institution

    permission manage = parent_institution->manage
    permission view = parent_institution->view
}

definition topic {
    relation parent_institution: institution

    permission manage = parent_institution->manage
    permission view = parent_institution->view
}
```

- [ ] **Step 2: Commit**

```bash
git add application/src/main/resources/spicedb/schema.zed
git commit -m "feat: update SpiceDB schema with full authorization model"
```

---

### Task 2: Update PreAuthorizeConventionTest

The convention test validates all `@PreAuthorize` annotations. It must accept the new scope names and support SpEL dot-notation param references (e.g., `#createOutletInput.institutionId`) before we change the controller annotations.

**Files:**
- Modify: `application/src/test/java/com/aura/app/architecture/PreAuthorizeConventionTest.java`

- [ ] **Step 1: Update VALID_SCOPES to include new permission names**

In `PreAuthorizeConventionTest.java`, replace the VALID_SCOPES set:

```java
    private static final Set<String> VALID_SCOPES = Set.of(
            "view", "manage", "delegate",
            "create_institution", "create_outlet", "create_course",
            "create_lesson", "create_account", "create_subject"
    );
```

- [ ] **Step 2: Update param reference test to support dot notation**

The current `param_references_must_match_method_parameters` test extracts `#paramName` and checks it against `@Argument` string names. With SpEL like `#createOutletInput.institutionId`, we need to:
1. Extract the root variable name (before the first `.`)
2. Match against Java method parameter names (not just `@Argument` string values)

Replace the `param_references_must_match_method_parameters` test method:

```java
    @TestFactory
    @DisplayName("#paramName references must match method parameters")
    Stream<DynamicTest> param_references_must_match_method_parameters() throws IOException {
        return findPreAuthorizeAnnotations().stream()
                .filter(match -> match.value().contains("#"))
                .map(match -> DynamicTest.dynamicTest(
                        match.file().getFileName() + ":" + match.lineNumber() + " #param must match method parameter",
                        () -> {
                            String firstArg = extractRawArgument(match.value(), 0);
                            assertThat(firstArg).as("Could not extract first argument").isNotNull();
                            assertThat(firstArg).startsWith("#");

                            // Extract root variable name (before dot for SpEL property access)
                            String fullRef = firstArg.substring(1);
                            String rootParamName = fullRef.contains(".")
                                    ? fullRef.substring(0, fullRef.indexOf('.'))
                                    : fullRef;

                            List<String> fileLines = Files.readAllLines(match.file());

                            // Collect both @Argument names and Java parameter names from method signature
                            Set<String> parameterNames = new java.util.HashSet<>();
                            for (int i = match.lineNumber(); i < Math.min(match.lineNumber() + 5, fileLines.size()); i++) {
                                // Match @Argument("name") values
                                Matcher argMatcher = ARGUMENT_PATTERN.matcher(fileLines.get(i));
                                while (argMatcher.find()) {
                                    parameterNames.add(argMatcher.group(1));
                                }
                                // Match Java parameter names (type paramName or type paramName,)
                                Matcher javaParamMatcher = JAVA_PARAM_PATTERN.matcher(fileLines.get(i));
                                while (javaParamMatcher.find()) {
                                    parameterNames.add(javaParamMatcher.group(1));
                                }
                            }

                            assertThat(parameterNames)
                                    .as("#%s at %s:%d must match a method parameter. Found: %s",
                                            rootParamName, match.file().getFileName(), match.lineNumber(), parameterNames)
                                    .contains(rootParamName);
                        }
                ));
    }
```

- [ ] **Step 3: Add JAVA_PARAM_PATTERN constant**

Add this constant alongside the existing patterns (after `ARGUMENT_PATTERN`):

```java
    private static final Pattern JAVA_PARAM_PATTERN = Pattern.compile(
            "\\w+\\s+(\\w+)\\s*[,)]");
```

- [ ] **Step 4: Run the convention test to verify it still passes with existing annotations**

Run: `mvn clean install -DskipTests && mvn test -pl application -Dtest=PreAuthorizeConventionTest`

Expected: All existing tests PASS (current annotations still valid with expanded scope set)

- [ ] **Step 5: Commit**

```bash
git add application/src/test/java/com/aura/app/architecture/PreAuthorizeConventionTest.java
git commit -m "feat: update PreAuthorizeConventionTest for new permission scopes and SpEL dot notation"
```

---

### Task 3: Update Controller @PreAuthorize Annotations

Update all controller methods to use instance-level parent resource checks with `create_<type>` permissions instead of type-level null checks.

**Files:**
- Modify: `application/src/main/java/com/aura/app/controllers/graphql/v1/institutions/InstitutionsGqlController.java:58`
- Modify: `application/src/main/java/com/aura/app/controllers/graphql/v1/outlets/OutletsGqlController.java:41,49`
- Modify: `application/src/main/java/com/aura/app/controllers/graphql/v1/courses/CourseGqlController.java:25`
- Modify: `application/src/main/java/com/aura/app/controllers/graphql/v1/lessons/LessonsGqlController.java:27,35`
- Modify: `application/src/main/java/com/aura/app/controllers/graphql/v1/subjects/SubjectsGqlController.java:32,39`
- Modify: `application/src/main/java/com/aura/app/controllers/graphql/v1/topics/TopicsGqlController.java:24`
- Modify: `application/src/main/java/com/aura/app/controllers/graphql/v1/levels/LevelsGqlController.java:39`
- Modify: `application/src/main/java/com/aura/app/controllers/graphql/v1/accounts/AccountsGqlController.java:44,52,60,69,77`

- [ ] **Step 1: Update InstitutionsGqlController**

Change line 58:
```java
// Before:
@PreAuthorize("hasPermission(null, 'institution', 'create')")
// After:
@PreAuthorize("hasPermission(null, 'institution', 'create_institution')")
```

- [ ] **Step 2: Update OutletsGqlController**

Change line 41 (createOutlet):
```java
// Before:
@PreAuthorize("hasPermission(null, 'outlet', 'create')")
// After:
@PreAuthorize("hasPermission(#createOutletInput.institutionId, 'institution', 'create_outlet')")
```

Change line 49 (getOutletsByPage):
```java
// Before:
@PreAuthorize("hasPermission(#institutionId, 'outlet', 'view')")
// After:
@PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
```

- [ ] **Step 3: Update CourseGqlController**

Change line 25 (createCourse):
```java
// Before:
@PreAuthorize("hasPermission(null, 'course', 'create')")
// After:
@PreAuthorize("hasPermission(#createCourseInput.outletId, 'outlet', 'create_course')")
```

- [ ] **Step 4: Update LessonsGqlController**

Change line 27 (createStandaloneLesson):
```java
// Before:
@PreAuthorize("hasPermission(null, 'lesson', 'create')")
// After:
@PreAuthorize("hasPermission(#createStandaloneLessonInput.outletId, 'outlet', 'create_lesson')")
```

Change line 35 (createLesson):
```java
// Before:
@PreAuthorize("hasPermission(null, 'lesson', 'create')")
// After:
@PreAuthorize("hasPermission(#createLessonInput.outletId, 'outlet', 'create_lesson')")
```

- [ ] **Step 5: Update SubjectsGqlController**

Change line 32 (createSubject):
```java
// Before:
@PreAuthorize("hasPermission(null, 'subject', 'create')")
// After:
@PreAuthorize("hasPermission(#createSubjectInput.institutionId, 'institution', 'create_subject')")
```

Change line 39 (createSubjectTopicLinks):
```java
// Before:
@PreAuthorize("hasPermission(null, 'subject', 'manage')")
// After:
@PreAuthorize("hasPermission(#createSubjectTopicLinksInput.institutionId, 'institution', 'manage')")
```

- [ ] **Step 6: Update TopicsGqlController**

Change line 24 (createTopic):
```java
// Before:
@PreAuthorize("hasPermission(null, 'topic', 'create')")
// After:
@PreAuthorize("hasPermission(#topicInput.institutionId, 'institution', 'create_subject')")
```

- [ ] **Step 7: Update LevelsGqlController**

Change line 39 (getLevelByPage):
```java
// Before:
@PreAuthorize("hasPermission(#institutionId, 'level', 'view')")
// After:
@PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
```

- [ ] **Step 8: Update AccountsGqlController**

Change line 44 (createAccountWithInstitutionAdminRole):
```java
// Before:
@PreAuthorize("hasPermission(null, 'account', 'create')")
// After:
@PreAuthorize("hasPermission(#createAccountInput.institutionId, 'institution', 'create_account')")
```

Change line 52 (createParentAccount):
```java
// Before:
@PreAuthorize("hasPermission(null, 'account', 'create')")
// After:
@PreAuthorize("hasPermission(#createParentAccountInput.institutionId, 'institution', 'create_account')")
```

Change line 60 (createStudentsInParentAccount):
```java
// Before:
@PreAuthorize("hasPermission(null, 'account', 'create')")
// After:
@PreAuthorize("hasPermission(null, 'institution', 'create_account')")
```

Change line 69 (createEducatorsInAccount):
```java
// Before:
@PreAuthorize("hasPermission(null, 'account', 'create')")
// After:
@PreAuthorize("hasPermission(null, 'institution', 'create_account')")
```

Change line 77 (addUserToGroup):
```java
// Before:
@PreAuthorize("hasPermission(null, 'account', 'create')")
// After:
@PreAuthorize("hasPermission(#addUserToGroupInput.institutionId, 'institution', 'delegate')")
```

- [ ] **Step 9: Run PreAuthorizeConventionTest to verify all annotations are valid**

Run: `mvn test -pl application -Dtest=PreAuthorizeConventionTest`

Expected: All tests PASS

- [ ] **Step 10: Commit**

```bash
git add application/src/main/java/com/aura/app/controllers/
git commit -m "feat: update @PreAuthorize annotations to use instance-level parent resource checks"
```

---

### Task 4: Update SpiceDbPermissionEvaluatorTest

The evaluator itself doesn't change, but test cases reference old permission names (`create`) that no longer appear in the codebase. Update tests to use the new naming.

**Files:**
- Modify: `application/src/test/java/com/aura/app/security/SpiceDbPermissionEvaluatorTest.java`

- [ ] **Step 1: Update the null-targetId test to use `create_institution` scope**

Replace the `hasPermission_nullTargetId_routesToSystemGlobal` test:

```java
    @Test
    @DisplayName("Should route type-level checks to system:global when targetId is null")
    void hasPermission_nullTargetId_routesToSystemGlobal() {
        Jwt jwt = buildJwt("user-123");
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        when(relationshipService.checkPermission("system", "global", "create_institution", "user", "user-123"))
                .thenReturn(true);

        boolean result = evaluator.hasPermission(auth, null, "institution", "create_institution");

        assertThat(result).isTrue();
        verify(relationshipService).checkPermission("system", "global", "create_institution", "user", "user-123");
    }
```

- [ ] **Step 2: Update the non-null-targetId test to use `create_outlet` scope**

Replace the `hasPermission_nonNullTargetId_passesTargetIdAsResourceId` test:

```java
    @Test
    @DisplayName("Should pass targetId as resourceId when targetId is non-null")
    void hasPermission_nonNullTargetId_passesTargetIdAsResourceId() {
        Jwt jwt = buildJwt("user-456");
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        when(relationshipService.checkPermission("institution", "inst-001", "create_outlet", "user", "user-456"))
                .thenReturn(true);

        boolean result = evaluator.hasPermission(auth, "inst-001", "institution", "create_outlet");

        assertThat(result).isTrue();
        verify(relationshipService).checkPermission("institution", "inst-001", "create_outlet", "user", "user-456");
    }
```

- [ ] **Step 3: Update the denied test to use new scope name**

Replace the `hasPermission_denied_returnsFalse` test:

```java
    @Test
    @DisplayName("Should return false when SpiceDB denies permission")
    void hasPermission_denied_returnsFalse() {
        Jwt jwt = buildJwt("user-789");
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        when(relationshipService.checkPermission("system", "global", "create_institution", "user", "user-789"))
                .thenReturn(false);

        boolean result = evaluator.hasPermission(auth, null, "institution", "create_institution");

        assertThat(result).isFalse();
    }
```

- [ ] **Step 4: Run the evaluator test**

Run: `mvn test -pl application -Dtest=SpiceDbPermissionEvaluatorTest`

Expected: All 5 tests PASS

- [ ] **Step 5: Commit**

```bash
git add application/src/test/java/com/aura/app/security/SpiceDbPermissionEvaluatorTest.java
git commit -m "test: update SpiceDbPermissionEvaluatorTest for new permission scope names"
```

---

### Task 5: Write `institution#parent_system` Relationship on Onboarding

When an institution is onboarded, write the `parent_system` relationship so platform admin permissions cascade.

**Files:**
- Modify: `details/src/main/java/com/aura/details/services/OnboardingService.java`
- Modify: `details/src/test/java/com/aura/details/services/OnboardingServiceTest.java`

- [ ] **Step 1: Update OnboardingServiceTest with failing test**

Replace the test file content:

```java
package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.usecases.onboarding.OnboardInstitutionUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Onboarding service")
@ExtendWith(MockitoExtension.class)
class OnboardingServiceTest {

    @Mock private OnboardInstitutionUseCase onboardInstitutionUseCase;
    @Mock private RelationshipAuthorizationService relationshipService;

    @InjectMocks private OnboardingService onboardingService;

    @DisplayName("Should delegate onboarding to use case and return created institution")
    @Test
    void onboard_delegatesToUseCase() {
        Institution input = Institution.builder().name("Test School").build();
        Institution created = Institution.builder().id("inst-123").name("Test School").build();
        when(onboardInstitutionUseCase.onboard(input)).thenReturn(created);

        Institution result = onboardingService.onboard(input);

        assertThat(result).isEqualTo(created);
        verify(onboardInstitutionUseCase).onboard(input);
    }

    @DisplayName("Should write institution parent_system relationship after onboarding")
    @Test
    void onboard_writesParentSystemRelationship() {
        Institution input = Institution.builder().name("Test School").build();
        Institution created = Institution.builder().id("inst-123").name("Test School").build();
        when(onboardInstitutionUseCase.onboard(input)).thenReturn(created);

        onboardingService.onboard(input);

        verify(relationshipService).writeRelationship(
                "institution", "inst-123", "parent_system", "system", "global");
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn test -pl details -Dtest=OnboardingServiceTest`

Expected: FAIL — `onboard_writesParentSystemRelationship` fails because OnboardingService doesn't have `relationshipService`

- [ ] **Step 3: Update OnboardingService to write the relationship**

```java
package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.usecases.onboarding.OnboardInstitutionUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OnboardingService {

    private final OnboardInstitutionUseCase onboardInstitutionUseCase;
    private final RelationshipAuthorizationService relationshipService;

    @Transactional
    public Institution onboard(Institution institution) {
        Institution created = onboardInstitutionUseCase.onboard(institution);
        relationshipService.writeRelationship(
                "institution", created.getId(), "parent_system", "system", "global");
        return created;
    }
}
```

- [ ] **Step 4: Update ServiceConfig bean wiring**

In `application/src/main/java/com/aura/app/configs/ServiceConfig.java`, find the `onboardingService` bean and add the `relationshipService` parameter:

```java
    @Bean
    public OnboardingService onboardingService(
            OnboardInstitutionUseCase onboardInstitutionUseCase,
            RelationshipAuthorizationService relationshipService) {
        return new OnboardingService(onboardInstitutionUseCase, relationshipService);
    }
```

- [ ] **Step 5: Run test to verify it passes**

Run: `mvn clean install -DskipTests && mvn test -pl details -Dtest=OnboardingServiceTest`

Expected: Both tests PASS

- [ ] **Step 6: Commit**

```bash
git add details/src/main/java/com/aura/details/services/OnboardingService.java \
       details/src/test/java/com/aura/details/services/OnboardingServiceTest.java \
       application/src/main/java/com/aura/app/configs/ServiceConfig.java
git commit -m "feat: write institution parent_system relationship on onboarding"
```

---

### Task 6: Write Relationships for Subjects, Topics, and Levels

Add missing `parent_institution` relationship writes when subjects, topics, and levels are created.

**Files:**
- Modify: `details/src/main/java/com/aura/details/services/SubjectService.java`
- Modify: `details/src/main/java/com/aura/details/services/TopicService.java`
- Modify: `details/src/main/java/com/aura/details/institutions/onboarding/TenantInitialDataLoaderImpl.java`
- Create: `details/src/test/java/com/aura/details/services/SubjectServiceTest.java`
- Create: `details/src/test/java/com/aura/details/services/TopicServiceTest.java`
- Create: `details/src/test/java/com/aura/details/institutions/onboarding/TenantInitialDataLoaderImplTest.java`
- Modify: `application/src/main/java/com/aura/app/configs/ServiceConfig.java`

- [ ] **Step 1: Write failing test for SubjectService**

Create `details/src/test/java/com/aura/details/services/SubjectServiceTest.java`:

```java
package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.usecases.subjects.CreateSubjectUseCase;
import com.aura.core.usecases.subjects.CreateTopicSubjectLinksUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Subject service")
@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock private CreateSubjectUseCase createSubjectUseCase;
    @Mock private CreateTopicSubjectLinksUseCase createTopicSubjectLinksUseCase;
    @Mock private RelationshipAuthorizationService relationshipService;

    @InjectMocks private SubjectService subjectService;

    @DisplayName("Should create subject and write parent_institution relationship")
    @Test
    void createSubject_writesParentInstitutionRelationship() {
        Subject input = Subject.builder().name("Math").tenantId("tenant-1").build();
        Subject created = Subject.builder().id("sub-1").name("Math").tenantId("tenant-1").build();
        when(createSubjectUseCase.createSubject(input)).thenReturn(created);

        Subject result = subjectService.createSubject(input);

        assertThat(result).isEqualTo(created);
        verify(relationshipService).writeRelationship(
                "subject", "sub-1", "parent_institution", "institution", "tenant-1");
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn test -pl details -Dtest=SubjectServiceTest`

Expected: FAIL — SubjectService doesn't have `relationshipService` field

- [ ] **Step 3: Update SubjectService**

```java
package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.entities.subjects.TopicSubjectLink;
import com.aura.core.usecases.subjects.CreateSubjectUseCase;
import com.aura.core.usecases.subjects.CreateTopicSubjectLinksUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SubjectService {

    private final CreateSubjectUseCase createSubjectUseCase;
    private final CreateTopicSubjectLinksUseCase createTopicSubjectLinksUseCase;
    private final RelationshipAuthorizationService relationshipService;

    @Transactional
    public Subject createSubject(Subject subject) {
        Subject created = createSubjectUseCase.createSubject(subject);
        relationshipService.writeRelationship(
                "subject", created.getId(), "parent_institution", "institution", created.getTenantId());
        return created;
    }

    @Transactional
    public List<TopicSubjectLink> createTopicSubjectLinks(UUID subjectId, List<UUID> topicIds, String tenantId) {
        return createTopicSubjectLinksUseCase.linkTopicsToSubject(subjectId, topicIds, tenantId);
    }
}
```

- [ ] **Step 4: Write failing test for TopicService**

Create `details/src/test/java/com/aura/details/services/TopicServiceTest.java`:

```java
package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.topics.Topic;
import com.aura.core.usecases.topics.CreateTopicUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Topic service")
@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @Mock private CreateTopicUseCase createTopicUseCase;
    @Mock private RelationshipAuthorizationService relationshipService;

    @InjectMocks private TopicService topicService;

    @DisplayName("Should create topic and write parent_institution relationship")
    @Test
    void createTopic_writesParentInstitutionRelationship() {
        Topic input = Topic.builder().name("Algebra").tenantId("tenant-1").build();
        Topic created = Topic.builder().id("topic-1").name("Algebra").tenantId("tenant-1").build();
        when(createTopicUseCase.createTopic(input)).thenReturn(created);

        Topic result = topicService.createTopic(input);

        assertThat(result).isEqualTo(created);
        verify(relationshipService).writeRelationship(
                "topic", "topic-1", "parent_institution", "institution", "tenant-1");
    }
}
```

- [ ] **Step 5: Update TopicService**

```java
package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.topics.Topic;
import com.aura.core.usecases.topics.CreateTopicUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TopicService {

    private final CreateTopicUseCase createTopicUseCase;
    private final RelationshipAuthorizationService relationshipService;

    @Transactional
    public Topic createTopic(Topic topic) {
        Topic created = createTopicUseCase.createTopic(topic);
        relationshipService.writeRelationship(
                "topic", created.getId(), "parent_institution", "institution", created.getTenantId());
        return created;
    }
}
```

- [ ] **Step 6: Write test for TenantInitialDataLoaderImpl relationship writes**

Create `details/src/test/java/com/aura/details/institutions/onboarding/TenantInitialDataLoaderImplTest.java`:

```java
package com.aura.details.institutions.onboarding;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.levels.LevelTemplateModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.models.subjects.SubjectTemplateModel;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.models.topics.TopicTemplateModel;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelTemplateRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectLevelLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicSubjectLinkRepository;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolRepository;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolTemplateRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectLevelLinkTemplateRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectTemplateRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicSubjectLinkTemplateRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicTemplateRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Tenant initial data loader")
@ExtendWith(MockitoExtension.class)
class TenantInitialDataLoaderImplTest {

    @Mock private JpaSchoolTemplateRepository jpaSchoolTemplateRepository;
    @Mock private JpaSchoolRepository jpaSchoolRepository;
    @Mock private JpaLevelTemplateRepository jpaLevelTemplateRepository;
    @Mock private JpaLevelRepository jpaLevelRepository;
    @Mock private JpaSubjectTemplateRepository jpaSubjectTemplateRepository;
    @Mock private JpaSubjectRepository jpaSubjectRepository;
    @Mock private JpaSubjectLevelLinkTemplateRepository jpaSubjectLevelLinkTemplateRepository;
    @Mock private JpaSubjectLevelLinkRepository jpaSubjectLevelLinkRepository;
    @Mock private JpaTopicTemplateRepository jpaTopicTemplateRepository;
    @Mock private JpaTopicRepository jpaTopicRepository;
    @Mock private JpaTopicSubjectLinkTemplateRepository jpaTopicSubjectLinkTemplateRepository;
    @Mock private JpaTopicSubjectLinkRepository jpaTopicSubjectLinkRepository;
    @Mock private RelationshipAuthorizationService relationshipService;

    @InjectMocks private TenantInitialDataLoaderImpl loader;

    @DisplayName("Should write parent_institution relationships for levels, subjects, and topics")
    @Test
    void loadInitialData_writesRelationshipsForAllResources() {
        String tenantId = "tenant-123";

        LevelTemplateModel levelTemplate = new LevelTemplateModel();
        levelTemplate.setName("Primary 1");
        when(jpaLevelTemplateRepository.findAll()).thenReturn(List.of(levelTemplate));

        SubjectTemplateModel subjectTemplate = new SubjectTemplateModel();
        subjectTemplate.setName("Math");
        when(jpaSubjectTemplateRepository.findAll()).thenReturn(List.of(subjectTemplate));

        TopicTemplateModel topicTemplate = new TopicTemplateModel();
        topicTemplate.setName("Algebra");
        when(jpaTopicTemplateRepository.findAll()).thenReturn(List.of(topicTemplate));

        // Return empty lists for templates that require lookups
        when(jpaSchoolTemplateRepository.findAll()).thenReturn(List.of());
        when(jpaSubjectLevelLinkTemplateRepository.findAll()).thenReturn(List.of());
        when(jpaTopicSubjectLinkTemplateRepository.findAll()).thenReturn(List.of());

        // Mock saveAll to return the input with IDs set
        when(jpaLevelRepository.saveAll(anyList())).thenAnswer(inv -> {
            List<LevelModel> models = inv.getArgument(0);
            models.forEach(m -> m.setId(UUID.randomUUID().toString()));
            return models;
        });
        when(jpaSubjectRepository.saveAll(anyList())).thenAnswer(inv -> {
            List<SubjectModel> models = inv.getArgument(0);
            models.forEach(m -> m.setId(UUID.randomUUID().toString()));
            return models;
        });
        when(jpaTopicRepository.saveAll(anyList())).thenAnswer(inv -> {
            List<TopicModel> models = inv.getArgument(0);
            models.forEach(m -> m.setId(UUID.randomUUID().toString()));
            return models;
        });

        loader.loadInitialDataForTenant(tenantId);

        // Verify relationships written for each resource type
        ArgumentCaptor<String> resourceTypeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> resourceIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(relationshipService, atLeastOnce()).writeRelationship(
                resourceTypeCaptor.capture(), resourceIdCaptor.capture(),
                org.mockito.ArgumentMatchers.eq("parent_institution"),
                org.mockito.ArgumentMatchers.eq("institution"),
                org.mockito.ArgumentMatchers.eq(tenantId));

        assertThat(resourceTypeCaptor.getAllValues()).contains("level", "subject", "topic");
    }
}
```

- [ ] **Step 7: Update TenantInitialDataLoaderImpl to write relationships**

Add `RelationshipAuthorizationService` as a dependency and write relationships after each `saveAll`:

```java
// Add to imports:
import com.aura.core.clients.RelationshipAuthorizationService;

// Add field (with @RequiredArgsConstructor it's auto-injected):
private final RelationshipAuthorizationService relationshipService;
```

After the level `saveAll` on line 67, add:
```java
        jpaLevelRepository.saveAll(levelModels);
        levelModels.forEach(level -> relationshipService.writeRelationship(
                "level", level.getId(), "parent_institution", "institution", tenantId));
```

After the subject `saveAll` on line 72, add:
```java
        jpaSubjectRepository.saveAll(subjects);
        subjects.forEach(subject -> relationshipService.writeRelationship(
                "subject", subject.getId(), "parent_institution", "institution", tenantId));
```

After the topic `saveAll` on line 77, add:
```java
        jpaTopicRepository.saveAll(topics);
        topics.forEach(topic -> relationshipService.writeRelationship(
                "topic", topic.getId(), "parent_institution", "institution", tenantId));
```

- [ ] **Step 8: Update ServiceConfig bean wiring for SubjectService and TopicService**

In `application/src/main/java/com/aura/app/configs/ServiceConfig.java`, update the beans:

For `subjectService` bean:
```java
    @Bean
    public SubjectService subjectService(
            CreateSubjectUseCase createSubjectUseCase,
            CreateTopicSubjectLinksUseCase createTopicSubjectLinksUseCase,
            RelationshipAuthorizationService relationshipService) {
        return new SubjectService(createSubjectUseCase, createTopicSubjectLinksUseCase, relationshipService);
    }
```

For `topicService` bean:
```java
    @Bean
    public TopicService topicService(
            CreateTopicUseCase createTopicUseCase,
            RelationshipAuthorizationService relationshipService) {
        return new TopicService(createTopicUseCase, relationshipService);
    }
```

For `tenantInitialDataLoader` bean — add `RelationshipAuthorizationService` parameter to the existing bean definition.

- [ ] **Step 9: Run all new and modified tests**

Run: `mvn clean install -DskipTests && mvn test -pl details -Dtest="SubjectServiceTest,TopicServiceTest,TenantInitialDataLoaderImplTest"`

Expected: All tests PASS

- [ ] **Step 10: Commit**

```bash
git add details/src/main/java/com/aura/details/services/SubjectService.java \
       details/src/main/java/com/aura/details/services/TopicService.java \
       details/src/main/java/com/aura/details/institutions/onboarding/TenantInitialDataLoaderImpl.java \
       details/src/test/java/com/aura/details/services/SubjectServiceTest.java \
       details/src/test/java/com/aura/details/services/TopicServiceTest.java \
       details/src/test/java/com/aura/details/institutions/onboarding/TenantInitialDataLoaderImplTest.java \
       application/src/main/java/com/aura/app/configs/ServiceConfig.java
git commit -m "feat: write parent_institution relationships for subjects, topics, and levels"
```

---

### Task 7: Write Educator and Parent Relationships in AccountService

When educator accounts are created, assign the `educator` relation on the institution. When parent accounts are created, assign the `parent_user` relation.

**Files:**
- Modify: `details/src/main/java/com/aura/details/services/AccountService.java`
- Modify: `details/src/test/java/com/aura/details/services/AccountServiceTest.java`

- [ ] **Step 1: Add failing test for createParentAccount writing parent_user relationship**

Add this test to `AccountServiceTest.java`:

```java
    @DisplayName("Should write parent_user relationship on institution when creating parent account")
    @Test
    void createParentAccount_writesParentUserRelationship() {
        Account input = Account.builder().tenantId("tenant-1").build();
        Account created = Account.builder().userId("user-1").tenantId("tenant-1").build();
        when(createParentAccountUseCase.createParentAccount(input)).thenReturn(created);

        accountService.createParentAccount(input);

        verify(relationshipService).writeRelationship(
                "institution", "tenant-1", "parent_user", "user", "user-1");
    }
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn test -pl details -Dtest="AccountServiceTest#createParentAccount_writesParentUserRelationship"`

Expected: FAIL — no interaction with `relationshipService`

- [ ] **Step 3: Update createParentAccount in AccountService**

```java
    @Transactional
    public Account createParentAccount(Account account) {
        Account created = createParentAccountUseCase.createParentAccount(account);
        relationshipService.writeRelationship(
                "institution", created.getTenantId(), "parent_user", "user", created.getUserId());
        return created;
    }
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn test -pl details -Dtest="AccountServiceTest#createParentAccount_writesParentUserRelationship"`

Expected: PASS

- [ ] **Step 5: Add failing test for createAndLinkEducatorToAccount writing educator relationship**

Add this test to `AccountServiceTest.java`:

```java
    @DisplayName("Should write educator relationship on institution when creating educator")
    @Test
    void createAndLinkEducatorToAccount_writesEducatorRelationship() {
        Educator educator = Educator.builder().name("Jane").build();
        Educator createdEducator = Educator.builder().id("edu-1").name("Jane").build();
        Account account = Account.builder().userId("user-1").tenantId("tenant-1").build();

        var params = AccountService.CreateEducatorAccountParams.builder()
                .educator(educator)
                .accountId("acc-1")
                .courseIds(List.of())
                .subjectIds(List.of())
                .outletIds(List.of())
                .levelIds(List.of())
                .build();

        when(createEducatorInAccountUseCase.createEducator("acc-1", educator)).thenReturn(createdEducator);
        when(accountRepository.findById("acc-1")).thenReturn(account);

        accountService.createAndLinkEducatorToAccount(params);

        verify(relationshipService).writeRelationship(
                "institution", "tenant-1", "educator", "user", "user-1");
    }
```

- [ ] **Step 6: Update createAndLinkEducatorToAccount in AccountService**

Add the relationship write after retrieving the account (after line 96):

```java
    @Transactional
    public CreatedEducatorResult createAndLinkEducatorToAccount(CreateEducatorAccountParams params) {
        Educator createdEducator = createEducatorInAccountUseCase.createEducator(params.accountId(), params.educator());
        Account account = ofNullable(accountRepository.findById(params.accountId())).orElseThrow(() -> new RuntimeException("account not found"));
        relationshipService.writeRelationship(
                "institution", account.getTenantId(), "educator", "user", account.getUserId());
        List<Level> taggedLevels = params.levelIds().stream().map(levelId -> tagEducatorToLevelUseCase.tag(createdEducator.getId(), levelId)).toList();
        List<Course> taggedCourses = params.courseIds().stream().map(courseId -> tagEducatorToCourseUseCase.tag(createdEducator.getId(), courseId)).toList();
        List<Subject> taggedSubjects = params.subjectIds().stream().map(sid -> tagEducatorToSubjectUseCase.tag(createdEducator.getId(), sid)).toList();
        List<Outlet> enrolledOutlets = params.outletIds().stream().map(oid -> enrollEducatorToOutletUseCase.enroll(createdEducator.getId(), oid)).toList();
        return CreatedEducatorResult.builder()
                .account(account)
                .educator(createdEducator)
                .courses(taggedCourses)
                .levels(taggedLevels)
                .subjects(taggedSubjects)
                .outlets(enrolledOutlets)
                .build();
    }
```

- [ ] **Step 7: Run all AccountService tests**

Run: `mvn test -pl details -Dtest=AccountServiceTest`

Expected: All tests PASS

- [ ] **Step 8: Commit**

```bash
git add details/src/main/java/com/aura/details/services/AccountService.java \
       details/src/test/java/com/aura/details/services/AccountServiceTest.java
git commit -m "feat: write educator and parent_user relationships on account creation"
```

---

### Task 8: Run Full Test Suite

Verify all changes work together across all modules.

- [ ] **Step 1: Clean build**

Run: `mvn clean install -DskipTests`

Expected: BUILD SUCCESS

- [ ] **Step 2: Run all unit tests**

Run: `mvn test -pl core,details,application`

Expected: All tests PASS (core, details, application modules)

- [ ] **Step 3: Fix any compilation or test failures**

If there are failures, diagnose and fix. Common issues:
- MapStruct corrupted generated sources → run `mvn clean` first
- ServiceConfig bean wiring mismatches → ensure constructor arg order matches `@RequiredArgsConstructor` field order
- PreAuthorizeConventionTest failures → verify annotation syntax matches VALID_SCOPES and VALID_RESOURCES

- [ ] **Step 4: Run component tests**

Run: `mvn test -pl component-tests`

Expected: All 13 existing component tests PASS. The `DatabaseCleanupHooks` already re-creates `system:global#admin` and the schema is loaded from the updated `schema.zed`. The `parent_system` relationship is now written during onboarding, so platform admin permissions cascade correctly.

- [ ] **Step 5: Commit any fixes**

```bash
git add -A
git commit -m "fix: resolve test failures from authorization model update"
```
