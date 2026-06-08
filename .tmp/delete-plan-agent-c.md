# Delete Operations Plan — Authorization / SpiceDB Lens

This plan is built bottom-up from the SpiceDB schema (`application/src/main/resources/spicedb/*.zed`) and from
existing `RelationshipAuthorizationService.writeRelationship*` call-sites in `details/services/`. The goal: for
every GraphQL-exposed resource, define **who can delete it**, **what SpiceDB relationships must be torn down on
delete**, and **what's missing** in the current schema/test coverage.

Note: a monolithic `application/src/main/resources/spicedb/schema.zed` and the per-resource `*.zed` files
**co-exist**. The per-resource files are authoritative — `index.zed` imports them and they include the new
`is_admin_or_modifier` permission and group-mgmt parent cascades. The monolithic file is legacy. Schema audits
below reference the per-resource files only.

---

## 1. Authorization breakdown per resource

Every resource definition (except `outlet_room`) follows the standard `<resource>_user_group` paired-definition
pattern. `delete` on the resource resolves to `user_group->delete + parent->delete`, and on the user_group
`delete = admin`. So the principal who can delete X is:

- Direct: any `user` granted `admin` on `<resource>_user_group:<id>`
- Inherited: any user with `delete` on the resource's parent chain (recursive via `parent->delete`)

| Resource             | `delete` permission lives in         | Direct deleter (user_group role) | Inherited deleters (parent chain)                                                                |
|----------------------|--------------------------------------|----------------------------------|--------------------------------------------------------------------------------------------------|
| `system`             | `system.zed`                         | `system_user_group#admin`        | none (root)                                                                                      |
| `institution`        | `institution.zed`                    | `institution_user_group#admin`   | `system_user_group#admin`                                                                        |
| `outlet`             | `outlet.zed`                         | `outlet_user_group#admin`        | institution-admin chain → system-admin                                                           |
| `outlet_room`        | `outlet_room.zed`                    | (no own user_group)              | outlet-admin chain (`outlet_room.delete = parent->delete`)                                       |
| `course`             | `course.zed`                         | `course_user_group#admin`        | outlet-admin chain                                                                               |
| `lesson`             | `lesson.zed`                         | `lesson_user_group#admin`        | course-admin chain                                                                               |
| `account`            | `account.zed`                        | `account_user_group#admin`       | institution-admin chain                                                                          |
| `level`              | `level.zed`                          | `level_user_group#admin`         | institution-admin chain (`level.parent: institution`)                                            |
| `subject`            | `subject.zed`                        | `subject_user_group#admin`       | institution-admin chain                                                                          |
| `topic`              | `topic.zed`                          | `topic_user_group#admin`         | institution-admin chain                                                                          |
| `material`           | `material.zed`                       | `material_user_group#admin`      | institution-admin chain                                                                          |
| `school`             | only `institution.zed#delete_school` | n/a — uses `institution.delete`  | institution-admin chain (no `school` resource type in SpiceDB)                                   |

### Pre-authorize annotations (what controllers should declare)

| Mutation                       | Existing `@PreAuthorize`                                                  | Notes                                                              |
|--------------------------------|---------------------------------------------------------------------------|--------------------------------------------------------------------|
| `deleteOutletById`             | `hasPermission(#id, 'outlet', 'delete')`                                  | OK                                                                 |
| `deleteOutletRoomById`         | `hasPermission(#input.id, 'outlet_room', 'delete')`                       | OK                                                                 |
| `deleteCourseById`             | `hasPermission(#id, 'course', 'delete')`                                  | OK                                                                 |
| `deleteLessonById`             | `hasPermission(#id, 'lesson', 'delete')`                                  | OK                                                                 |
| `deleteLessonObjectiveById`    | `hasPermission(#input.lessonId, 'lesson', 'delete')`                      | OK; uses parent lesson scope                                       |
| `deleteLessonQuizById`         | `hasPermission(#input.lessonId, 'lesson', 'delete')`                      | OK                                                                 |
| `deleteSubjectById`            | `hasPermission(#id, 'subject', 'delete')`                                 | OK                                                                 |
| `deleteTopicById`              | `hasPermission(#id, 'topic', 'delete')`                                   | OK                                                                 |
| `deleteLevelById`              | `hasPermission(#id, 'level', 'delete')`                                   | OK                                                                 |
| `deleteMaterialById`           | `hasPermission(#input.materialId, 'material', 'delete')`                  | OK                                                                 |
| `deleteSchoolInInstitution`    | `hasPermission(#input.institutionId, 'institution', 'delete_school')`     | OK; aliases `institution.delete`                                   |
| `deleteEducatorById`           | `hasPermission(#input.institutionId, 'institution', 'delete')`            | Coarse — uses institution.delete because no `educator` schema      |
| `deleteStudentById`            | `hasPermission(#input.institutionId, 'institution', 'delete')`            | Same — coarse                                                      |
| `deleteParentById`             | `hasPermission(#input.institutionId, 'institution', 'delete')`            | Same — coarse                                                      |

---

## 2. Relationships that must be cleaned up on delete

The codebase already uses `RelationshipAuthorizationService.deleteAllRelationshipsForResource(type, id)`. That
helper deletes by **resource side only** — it does NOT clean up incoming references (i.e. when something else
points to this id as `subject`). Below: for each resource the existing pattern and any gaps.

### Pattern audit (what's currently done)

| Service              | Cleanup invoked                                                                                      |
|----------------------|------------------------------------------------------------------------------------------------------|
| `OutletService`      | `deleteAllRelationshipsForResource("outlet", id)` + `("outlet_user_group", id)`                      |
| `OutletRoomService`  | `deleteAllRelationshipsForResource("outlet_room", id)` only — correct (no own user_group)            |
| `CourseService`      | `("course", id)` + `("course_user_group", id)`                                                       |
| `LessonService`      | `("lesson", id)` + `("lesson_user_group", id)`                                                       |
| `LevelService`       | `("level", id)` + `("level_user_group", id)`                                                         |
| `SubjectService`     | `("subject", id)` + `("subject_user_group", id)`                                                     |
| `TopicService`       | `("topic", id)` + `("topic_user_group", id)`                                                         |
| `MaterialService`    | `("material", id)` + `("material_user_group", id)`                                                   |
| `LessonObjective/Quiz/Group/Plan/Educator/Student/Parent/School/Account` | **no SpiceDB cleanup at all** in `deleteById` |

### Critical gaps (resources whose deletes leak SpiceDB state)

1. **`account`** — `AccountService` has no `deleteById` at all. Onboarding writes 4 relationships per account
   (`account#parent`, `account_user_group#group_admin`, `account_user_group#admin`, `account_user_group#parent`)
   plus institution-level role grants (`institution_user_group#admin`/`#group_admin` from
   `InstitutionCreationRequestService`). A future `deleteAccount` must remove **both**
   `account_user_group:<id>` AND `account:<id>` AND any per-resource role grants the account's user accumulated
   (`course_user_group#modifier`, `lesson_user_group#admin`, etc — see Course/Lesson creation).

2. **`educator` / `student` / `parent`** — these are not SpiceDB resource types, but creating them writes role
   grants keyed by `user:<userId>`:
   - educator creation grants `course_user_group:<courseId>#modifier@user:<educatorUserId>` (in CourseService L75)
   - educator creation grants `lesson_user_group:<lessonId>#admin@user:<educatorUserId>` and `#group_admin` (in
     LessonService L103-104)
   - student creation grants `course_user_group:<courseId>#viewer@user:<studentUserId>` and
     `lesson_user_group:<lessonId>#viewer@user:<studentUserId>`

   When deleting an educator/student, the application currently only purges link tables — **all of these
   per-resource role grants remain in SpiceDB**, so a re-used `userId` would inherit dead permissions. Fix:
   either look up the user's relationships before the JPA cascade (`readRelationships(... subjectId=userId)`),
   or accept the orphan grants and rely on the JPA link-table delete to make them harmless in practice.
   **Decision needed.** Recommend explicit cleanup — see Phase 3 below.

3. **`lesson_objective` / `lesson_quiz` / `lesson_group` / `lesson_group_plan`** — none of these have their own
   SpiceDB resource definitions; the parent `lesson` schema gates their create-verbs as `create_lesson_objective`
   / `create_lesson_quiz` / `create_lesson_plan`. So no incoming SpiceDB cleanup is required — JPA cascade is
   sufficient. Confirm by `grep`: there are no `writeRelationship(... "lesson_objective" ...)` calls anywhere.

4. **`level`** (potential leak) — `TenantInitialDataLoaderImpl` writes `level:<id>#parent@institution:<tenantId>`
   for every seeded level (lines 74-77) but **does not** write a `user_group` relation or the
   `level_user_group:<id>#parent@system_user_group:aura` link. So seeded levels have a parent chain only.
   `LevelService.deleteById` calls `deleteAllRelationshipsForResource("level_user_group", id)` which is a no-op
   for seeded ones — harmless, but worth noting.

5. **`subject` / `topic`** — same as level. `TenantInitialDataLoaderImpl` writes only the `parent` relation;
   no `user_group` link. Subject/Topic `createSubject`/`createTopic` in the service layer write only the
   `parent` too (no user_group link). **Schema declares `relation user_group: subject_user_group` and a
   `view = user_group->view + parent->view` permission, but nothing in the code ever writes the user_group
   tuple.** Effectively: subjects/topics today are deletable only by institution-admin (via inheritance);
   subject-level / topic-level admins exist in the schema but cannot be assigned. **Mark as schema-vs-code
   drift; see Phase 1 risk.**

6. **`institution`** — `OnboardingService.onboard` writes 3 relationships (institution, institution_user_group,
   institution#parent). `InstitutionCreationRequestService.onboardFromRequest` writes 2 more
   (institution_user_group#admin and #group_admin granting the requester). There is **no deleteInstitution
   mutation, service method, or use case**. Building one requires the most cleanup of any resource — see Phase 3.

7. **`outlet_room`** — `OutletRoomService.deleteById` only deletes `outlet_room` relationships. Since
   `outlet_room` is a child of `outlet` and has no `user_group`, this is correct.

### Required cleanup template

For any resource X with a paired `X_user_group`, the canonical delete sequence is:

```
1. Delete all JPA link rows where X appears as either side
2. Delete X aggregate (cascades child rows audited via Envers due to derived deleteAllByX)
3. relationshipService.deleteAllRelationshipsForResource("X", id)
4. relationshipService.deleteAllRelationshipsForResource("X_user_group", id)
```

For descendants (e.g. `outlet → courses → lessons`), recurse children-first (Outlet already does this).
**Cascade order matters** — purge SpiceDB only after the DB rows are gone, so a partial failure doesn't leave
the resource alive but orphaned from its authorization graph.

---

## 3. Test-fixture additions needed (per-resource `.test.yaml`)

The schema test convention (CLAUDE.md): every relationship/permission change requires a paired
`assertTrue`/`assertFalse` update. The bar of "covers delete" for each fixture is:

- positive: `<resource>:<id>#delete@user:<resource>_admin_user` (direct admin)
- positive: at least one `<resource>:<id>#delete@user:<parent>_admin_user` (one hop up)
- negative: `<resource>:<id>#delete@user:<resource>_modifier_user`
- negative: `<resource>:<id>#delete@user:<resource>_viewer_user`
- negative: `<resource>:<id>#delete@user:non_member_user`

| Fixture                         | Current `#delete` coverage           | Required additions                                                              |
|---------------------------------|--------------------------------------|---------------------------------------------------------------------------------|
| `system.test.yaml`              | (re-verify — system has only admin)  | Add one negative `system:aura#delete@user:non_member_user`                      |
| `institution.test.yaml`         | Comprehensive — keep as is           | No change                                                                       |
| `outlet.test.yaml`              | Verify present                       | Add direct+inherited delete assertions if missing                               |
| `course.test.yaml`              | Verify present                       | Add direct+inherited delete assertions if missing                               |
| `lesson.test.yaml`              | Comprehensive — keep as is           | No change                                                                       |
| `outlet_room.test.yaml`         | Verify `parent->delete` works        | Add `outlet_room:1#delete@user:out_admin_user` + negative for `out_viewer_user` |
| `level.test.yaml`               | Verify present                       | Direct admin + inherited institution-admin                                      |
| `subject.test.yaml`             | Verify present                       | Direct admin + inherited institution-admin                                      |
| `topic.test.yaml`               | Verify present                       | Direct admin + inherited institution-admin                                      |
| `material.test.yaml`            | Verify present                       | Direct admin + inherited institution-admin                                      |
| `account.test.yaml`             | Verify `account_user_group#admin`    | Add delete coverage as part of Phase 3                                          |

**No new test fixture files needed** — every resource definition that exists in `*.zed` already has a paired
`*.test.yaml`. The work is asserting `#delete` cases on the existing fixtures.

---

## 4. Risk assessment — where the auth model is unclear or incomplete

### High-risk (blocks delete implementation)

1. **Account deletion path doesn't exist.** No use case, no service method, no controller. Deleting an account
   means revoking every role that account's user has across institutions, courses, lessons. Today these are
   written piecemeal during creation but no inventory exists. Without `readRelationships(... subjectType="user",
   subjectId=<userId>)` to enumerate them, a delete cannot be made safe. **Build a `findAllRolesForUser`
   helper or accept the orphan-relationship risk explicitly.**

2. **Institution deletion path doesn't exist.** Highest blast radius — must cascade through every outlet,
   course, lesson, level, subject, topic, material, account, school, and their user_groups, then clean up
   the institution itself. Likely deferred behind a feature flag or restricted to system-admin only. Recommend
   **NOT** building this in the first pass.

3. **Educator/Student/Parent role-grant orphans.** When an educator account is deleted, every
   `course_user_group:<courseId>#modifier@user:<educatorUserId>` they were granted remains. SpiceDB has no
   referential integrity — orphan grants persist until something rewrites/deletes them. **Add a "purge user
   relationships" step keyed off the user's id when deleting an account.**

### Medium-risk (model drift)

4. **Subject / Topic / Level / Material `user_group` is defined but never instantiated.** Code only writes the
   `parent` relation; `user_group` link is missing. This means:
   - The schema's `view = user_group->view + parent->view` resolves to `parent->view` only
   - Direct subject-admins (etc.) cannot exist today
   - But `deleteAllRelationshipsForResource("subject_user_group", id)` runs on every delete, which is a
     wasted gRPC call — works but is misleading

   **Decision: either (a) start writing the user_group tuples on create to align code with schema, or (b)
   drop the user_group relation from the schema for these resources.** Recommend (a) for consistency with
   outlet/course/lesson which DO write it.

5. **`outlet_room` has no `delete` test coverage today.** The schema is one-line (delete = parent->delete)
   but `outlet_room.test.yaml` likely doesn't exercise inheritance from a populated outlet user_group.
   Verify and add.

6. **`institution_creation_request`** is a domain entity with no SpiceDB resource type. Today it has no
   delete mutation. If/when a delete is added, the only gate available is `system.create_institution` or a
   new permission. Acceptable to skip in first pass.

### Low-risk

7. **Coarse `institution.delete` on `educator/student/parent` deletes.** This is fine — institution-admin
   should be able to delete any account in their institution. The risk is the institution-admin scope is
   broader than ideal (anyone with institution.delete can delete any educator). Could be tightened later by
   introducing per-account `delete` checks via `account_user_group#admin`, but not required for the first
   pass.

---

## 5. Implementation phasing (auth-first sequence)

### Phase 0 — Schema audit (no code change)

- Run `zed validate application/src/main/resources/spicedb/tests/*.test.yaml` to confirm current state.
- For every resource definition, confirm `delete = admin` (resource user_group) and `delete = user_group->delete
  + parent->delete` (resource). All current files already conform.
- Decide on the **subject/topic/level/material `user_group` drift**: write the missing link tuple on create or
  remove the relation from the schema. (Recommendation: write the tuple — consistent with outlet/course.)

### Phase 1 — Lock down the SpiceDB schema and tests

For every resource that exposes a delete mutation:

1. Confirm `<resource>.zed` has `permission delete = user_group->delete + parent->delete` (or the documented
   variant).
2. Update `<resource>.test.yaml` with the standard delete coverage block:
   ```yaml
   assertTrue:
     - <resource>:1#delete@user:<resource>_admin_user
     - <resource>:1#delete@user:<parent>_admin_user
   assertFalse:
     - <resource>:1#delete@user:<resource>_modifier_user
     - <resource>:1#delete@user:<resource>_viewer_user
     - <resource>:1#delete@user:non_member_user
   ```
3. CI runs `zed validate` and blocks on regressions.

### Phase 2 — Confirm existing delete services purge SpiceDB correctly

Audit each service's `deleteById`:

- ✅ Outlet, Course, Lesson, Level, Subject, Topic, Material, OutletRoom — already call
  `deleteAllRelationshipsForResource` on both `X` and `X_user_group`.
- ❌ LessonObjective, LessonQuiz, LessonGroup, LessonGroupPlan — no SpiceDB calls. **No-op required** (no
  resource type for them) — leave as is. Document this.
- ❌ Educator, Student, Parent — no SpiceDB calls and have user-id–keyed orphans. See Phase 3.
- ❌ School — no SpiceDB calls (`school` has no resource type) — leave as is.

### Phase 3 — Add missing user-keyed cleanup

Introduce a helper in `RelationshipAuthorizationService` that purges every relationship pointing at a given
user across all role types. Use it in `Educator/Student/Parent/Account` deletes. Sketch:

```java
List<RelationshipTuple> roles = readRelationships(<each user_group type>, null, null, "user", userId);
for (var t : roles) deleteRelationship(...);
```

The list of user_group types to sweep is the closed set: `course_user_group`, `lesson_user_group`,
`account_user_group`, `institution_user_group`, `outlet_user_group`, plus the optional subject/topic/level/material
user_groups if Phase 0 keeps them.

### Phase 4 — Add the missing delete mutations

Resources with services that already support delete but no GraphQL mutation:

- (none) — every controller in the table at top has a delete mutation today.

Resources with no service-level delete yet:

- **Account / Institution / InstitutionCreationRequest** — defer. Document as out-of-scope for first pass.

### Phase 5 — Component tests

For every delete mutation, add a `@ComponentTest` Cucumber scenario that:

1. Creates the resource (writes SpiceDB relationships)
2. Reads back the SpiceDB relationships and confirms they exist
3. Calls delete
4. Confirms both the DB row and the SpiceDB relationships are gone (via
   `relationshipService.readRelationships(...)` returning empty)
5. Confirms negative auth — a non-admin user gets a permission denied on delete

This closes the loop: today there are tests proving creation writes the right relationships but no symmetric
proof that delete tears them down.

---

## Key file references

- Schemas: `application/src/main/resources/spicedb/{system,institution,outlet,outlet_room,course,lesson,level,
  subject,topic,material,account,artifact,user}.zed` + `index.zed`
- Tests: `application/src/main/resources/spicedb/tests/*.test.yaml`
- Cleanup helper: `details/src/main/java/com/aura/details/spicedb/SpiceDbRelationshipService.java` (especially
  `deleteAllRelationshipsForResource`, `readRelationships`)
- Service-layer delete patterns (good reference): `OutletService.deleteById`, `CourseService.deleteById`,
  `LessonService.deleteById`
- Service-layer delete patterns (need updating): `EducatorService.deleteById`, `StudentService.deleteById`,
  `ParentService.deleteById`, `AccountService` (no deleteById at all)
- Creation paths to mirror on delete: `OnboardingService.onboard`,
  `InstitutionCreationRequestService.onboardFromRequest`, `AccountService.createAccount`,
  `TenantInitialDataLoaderImpl.loadInitialDataForTenant`
