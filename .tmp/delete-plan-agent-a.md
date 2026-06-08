# DELETE Operations Implementation Plan — Agent A

## Executive Summary

This codebase already has DELETE mutations for **14 of ~19** GraphQL-exposed resources. The picture
is not "build deletes for all resources from scratch" — it is **fill gaps + harden existing
deletes**. Many of the implemented deletes have subtle bugs around incomplete SpiceDB
cleanup, missing parent/role unwiring, and missing cascade walks. Most of the work is closing
gaps and tightening the cascade walks for the leaves and mid-tree resources whose deletes
already exist.

## Inventory: DELETE state per resource

Legend: COVERED = mutation + service + use case + SpiceDB perm; PARTIAL = mutation exists
but cascade/SpiceDB cleanup is buggy or incomplete; MISSING = no mutation; N/A = doesn't
make sense to expose.

| # | Resource | Schema state | SpiceDB perm | Quality |
|---|---|---|---|---|
| 1 | `outlet_room` | COVERED | `outlet_room#delete` (delegates to parent outlet) | Clean leaf, good reference |
| 2 | `lesson_quiz` | COVERED | reuses `lesson#delete` | Leaf, but no link cleanup needed (no link tables) — fine |
| 3 | `lesson_objective` | COVERED | reuses `lesson#delete` | Cleans topic links, ok |
| 4 | `material` | COVERED | `material#delete` | Cleans topic + plan links + SpiceDB |
| 5 | `topic` | COVERED | `topic#delete` | **PARTIAL**: doesn't clean `lesson_objective_topic_link`, `material_topic_link`, `topic_lesson_link`, `lesson_group_plan_topic_link` — only `topic_subject_link` |
| 6 | `subject` | COVERED | `subject#delete` | **PARTIAL**: doesn't clean `subject_student_link`, `subject_educator_link`, `subject_lesson_link`, `subject_level_link`, `subject_test_group_link`, `test_result.subject` FK |
| 7 | `level` | COVERED | `level#delete` | **PARTIAL**: nulls course.level FK + cleans level_lesson_link; misses `subject_level_link`, `level_educator_link`, `student.level` FK |
| 8 | `school` | COVERED | uses `institution#delete_school` (oddly placed on parent) | **PARTIAL**: no SpiceDB cleanup at all; doesn't null `student.school` FK |
| 9 | `parent` | COVERED | `institution#delete` | **PARTIAL**: just calls deleteUseCase, doesn't unwire from `account.parent` (cascade=ALL on AccountModel.parent will hit orphan removal in the reverse direction only) |
| 10 | `student` | COVERED | `institution#delete` | Cleans link tables; **MISSING**: no SpiceDB ReBAC cleanup (student does not have own ReBAC resource yet) |
| 11 | `educator` | COVERED | `institution#delete` | Cleans link tables; **MISSING**: no SpiceDB cleanup; also doesn't revoke the educator's `course/lesson#modifier` relationships in SpiceDB → permission leak |
| 12 | `lesson` | COVERED | `lesson#delete` | Relies on `lessonRepository.deleteAllLessonLinksByLessonId` + JPA cascade=ALL; cleans SpiceDB. Does NOT explicitly delete `lesson_group` rows under it (relies on JPA cascade from `LessonModel.lessonGroups`). **Risk**: cascade-ALL deletes don't issue derived `deleteAllByX`, so Envers history is only preserved if Hibernate fires per-row deletes on the collection (it does, but verify) |
| 13 | `course` | COVERED | `course#delete` | Walks lesson children → calls lessonService.deleteById, cleans link tables, cleans SpiceDB. Good reference pattern |
| 14 | `outlet` | COVERED | `outlet#delete` | Walks course children, deletes SpiceDB. **MISSING**: no explicit delete of `outlet_room` children (relies on JPA cascade=ALL on OutletModel.outletRoom); doesn't call outletRoomService.deleteById so SpiceDB `outlet_room` parent relationships leak |
| 15 | `institution` | **MISSING** | `institution#delete` permission exists in zed | **CRITICAL GAP** — has a permission but no mutation. Tenant root; deleting one would cascade across the entire tenant data |
| 16 | `account` | **MISSING** | `account#delete` permission exists in zed | **CRITICAL GAP** — accounts can be created but never deleted via API |
| 17 | `lesson_group` | **MISSING** | (no ReBAC of its own) | Created in `lesson_groups/mutation.graphqls` but never deletable |
| 18 | `lesson_group_plan` | **MISSING** | (no ReBAC of its own; inherits from `lesson`) | Created/updated/has add+remove material/topic mutations, but plan itself never deletable |
| 19 | `institution_creation_request` | **MISSING** | (no ReBAC) | Created via `requestInstitutionCreation` mutation, no delete/decline mutation |

System-scoped resources (no per-instance delete makes sense):
- `system:aura` — N/A (singleton)

Auth/grouping-only controllers (no entity behind them):
- `permissions` (`GroupMemberGqlController`, `GroupRelationGqlController`,
  `PermissionGqlController`, `GroupPermissionGqlController`) — these are about granting/
  revoking SpiceDB roles, not deleting entities. Already covered by `revokeUserGroupRole`.

## Cross-cutting issues to fix on existing deletes

Before adding new deletes, six existing deletes need their cascade walks completed. Fixing
these is lower-risk than building new ones and pays back immediately by removing dangling
foreign keys and SpiceDB tuples that accumulate every time an admin deletes a topic/subject
/level.

### Fix-list (do these in Phase 0 before new work)
1. **`TopicService.deleteById`** — add cleanup for `material_topic_link`,
   `lesson_objective_topic_link`, `topic_lesson_link`, `lesson_group_plan_topic_link`.
2. **`SubjectService.deleteById`** — add cleanup for `subject_student_link`,
   `subject_educator_link`, `subject_lesson_link`, `subject_level_link`,
   `subject_test_group_link`, and `test_result.subject` FK (null-out or block).
3. **`LevelService.deleteById`** — add `subject_level_link`, `level_educator_link`, and
   null-out `student.level`.
4. **`SchoolService.deleteById`** — null out `student.school` FK; no SpiceDB cleanup needed
   since school has no ReBAC presence yet, but write a brief comment to that effect.
5. **`EducatorService.deleteById`** — walk `course_user_group#modifier@user:<educatorUserId>`
   and `lesson_user_group#admin@user:<educatorUserId>` and delete them. Educator user-id
   is needed; reuse `educatorRepository.findUserIdsByEducatorIds(List.of(id))`.
6. **`OutletService.deleteById`** — explicitly walk outlet_room children:
   `for (room : outletRoomRepository.findAllByOutletId(id)) outletRoomService.deleteById(room.getId())`.
   Today the JPA cascade=ALL on `OutletModel.outletRoom` removes DB rows but leaves
   `outlet_room:<id>` SpiceDB tuples dangling forever.

These six are all "we already delete the row, we just forgot to clean up X". They share
the same pattern: derived `deleteAllByX` on the link repo + (for SpiceDB) a
`deleteAllRelationshipsForResource` call.

## New DELETE operations — prioritised plan

I'm grouping by **safe-to-delete-first**: leaves (no children) before mid-tree (some children)
before aggregate roots (everything underneath them). Inside each group I order by least-coupled.

### Phase 1 — Safe leaves and mid-tree gaps

These have a single clear parent, no children of their own (or only owned children that
delete cleanly via the existing patterns), no SpiceDB resource definition of their own, and
limited blast radius.

#### 1.1 `deleteLessonGroupPlanById`
- **Parent**: `lesson_group` (1:1) via `LessonGroupPlanModel.lessonGroup` (`@OneToOne`, unique on `lesson_group_id`)
- **Children to cascade**: `LessonObjective` (FK `lesson_group_plan`), `LessonQuiz` (FK
  `lesson_group_plan`), and the link tables `lesson_group_plan_topic_link`,
  `lesson_group_plan_materials_link`
- **Cascade strategy**: walk objectives → call `lessonObjectiveService.deleteById`
  (it cleans its own topic links); walk quizzes → call `lessonQuizService.deleteById`;
  then `deleteAllLessonGroupPlanTopicLinksByPlanId` and
  `deleteAllLessonGroupPlanMaterialsLinksByPlanId` on the repo; finally delete the plan row.
- **SpiceDB**: none of its own; lesson_group_plan permissions are checked through `lesson`.
  Nothing to revoke.
- **Permission scope**: `@PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'update')")` —
  consistent with how create+update+add/remove on this resource already authorise.
- **Schema**:
  ```graphql
  input DeleteLessonGroupPlanByIdInput { id: ID!, institutionId: ID!, lessonId: ID! }
  deleteLessonGroupPlanById(input: DeleteLessonGroupPlanByIdInput!): Boolean!
  ```
- **Business validation**: none. UI may want to disable when the parent lesson is in
  `ENDED` or `CANCELLED` state, but that's a UI concern, not a backend invariant.

#### 1.2 `deleteLessonGroupById`
- **Parent**: `lesson` via `LessonGroupModel.lesson`
- **Children to cascade**: `LessonGroupPlan` (1:1, `cascade=ALL` on `LessonGroupModel.lessonGroupPlan`)
  and `StudentLessonLink` rows where `studentLessonLink.lessonGroup == this` (the registered
  students for that group)
- **Cascade strategy**: call `lessonGroupPlanService.deleteById(plan.getId())` if a plan exists;
  null out / delete `student_lesson_link` rows pointing at this group (derived `deleteAllByLessonGroupId` or null-out the `lessonGroup` field — null-out preserves the student's lesson registration even when their group assignment is removed; this is the semantically correct choice)
- **SpiceDB**: no own resource definition; permissions inherit through lesson. Nothing to revoke.
- **Permission scope**: `@PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'update')")`
- **Schema**:
  ```graphql
  input DeleteLessonGroupByIdInput { id: ID!, institutionId: ID!, lessonId: ID! }
  ```
- **Business validation**: none.

#### 1.3 `deleteInstitutionCreationRequestById`
- **Parent**: system-scoped, not tenant-scoped
- **Children**: none
- **SpiceDB**: no resource definition — pure DB row
- **Permission scope**: `@PreAuthorize("hasPermission('aura', 'system', 'create_institution')")`
  (same gate as creating institutions — only Aura admins can decline/clean up requests)
- **Schema**: probably nicer as `declineInstitutionCreationRequest(id: ID!): Boolean!` to
  match the domain language. Either name works; pick `declineInstitutionCreationRequest`.
- **Business validation**: refuse deletion if the request has already been onboarded (i.e.
  has spawned an institution). The model already tracks state — gate on that.

### Phase 2 — Account aggregate

Accounts are the second-trickiest aggregate after institutions because they own users in
Keycloak (an external system).

#### 2.1 `deleteAccountById`
- **Parent**: `institution` via `account#parent`
- **Children to cascade**:
  - `Parent` (1:1, `cascade=ALL, orphanRemoval=true` on `AccountModel.parent`) — JPA handles
    DB delete via orphan removal when we null parent or delete the account. To keep audit
    history clean we should explicitly call `parentService.deleteById(parent.getId())` first.
  - `AccountStudentLinkModel` and `AccountEducatorLinkModel` rows pointing at this account
    (these are the link rows, not the Student/Educator entities themselves — those keep
    living, just disconnected from the account).
- **External**: should we delete the underlying Keycloak user? **No** — accounts are tenant-
  scoped projections of a possibly-cross-tenant Keycloak user. The `uk_tenant_user` constraint
  shows a single userId can map to multiple accounts across tenants. Deleting the Keycloak user
  would break those other tenants. Leave Keycloak alone; let an Aura-admin "disable user"
  flow handle that separately.
- **SpiceDB**: delete `account:<id>` relationships and `account_user_group:<id>` relationships.
- **Permission scope**: `@PreAuthorize("hasPermission(#input.accountId, 'account', 'delete')")`
  — uses the existing `account#delete` permission in the zed schema (currently unused at the
  app level).
- **Schema**:
  ```graphql
  input DeleteAccountByIdInput { accountId: ID!, institutionId: ID! }
  ```
- **Business validation**:
  - **Refuse if** the account is the institution's only `account_user_group#admin` — would
    lock the institution out. (Look up admin members via SpiceDB before deleting.)
  - **Refuse if** the account is currently the `createdBy` user for active resources — or
    decide to null those `createdBy` references. Recommend not blocking on this; allow delete
    and accept null `createdBy`. The audit (Envers) row still has the FK to the userId.
- **Caveats**: this surgery should be `@Transactional` so the partial unwiring (parent +
  links + Spice tuples) is atomic. SpiceDB cleanup is NOT in the DB transaction — accept
  that, and document it.

### Phase 3 — Institution aggregate root (DANGER)

#### 3.1 `deleteInstitutionById`
- **Parent**: `system:aura`
- **Children**: **everything** in the institution — all outlets, courses, lessons, accounts,
  parents, students, educators, levels, subjects, topics, materials, schools, plus every
  `*Template` row (no, those are global), plus all SpiceDB tuples for every resource.
- **Cascade strategy**: I recommend **soft-delete only**. Hard-delete of an institution
  should not be a synchronous mutation:
  - The blast radius is enormous (thousands of rows across 30+ tables) — should be a
    background job.
  - Hibernate Envers history of every deleted entity stays in the audit schema regardless —
    so even a "soft delete" preserves the data trail. The actual data archival/erasure for
    GDPR is a separate concern that needs explicit data-retention policy handling.
  - **Recommended GraphQL surface**:
    `transitionInstitutionState(institutionId: ID!, state: INSTITUTION_STATE!): InstitutionResponse!`
    — already half-modelled on `InstitutionModel.STATE { PENDING, ACTIVE, INACTIVE }`.
    Set state to `INACTIVE`; gate every other mutation on state != INACTIVE.
  - **If a true hard-delete is required** (e.g. for failed onboarding teardown):
    - Permission: `@PreAuthorize("hasPermission('aura', 'system', 'delete')")` — only system
      admin can do this.
    - Implementation: walk outlets → courses → lessons via existing services, then accounts,
      then institution-scoped resources (levels/subjects/topics/materials/schools), then
      finally `relationshipService.deleteAllRelationshipsForResource("institution", id)` +
      `("institution_user_group", id)`.
    - Run asynchronously; mutation only enqueues the job and returns Boolean.
    - **Strongly prefer soft-delete in v1**; offer hard-delete only if there is a concrete
      operational need.

## What about `student`, `educator`, `parent` — should those have their own ReBAC?

Today the delete-mutations for these three resources gate on `institution#delete`, which
means **anyone with institution-level delete permission can delete any educator/student/
parent inside that institution**. This is probably the right default for v1 — most product
flows want institution admins to manage accounts — but worth flagging:
- If granular delete (e.g. an account-admin who can only delete their own students) is ever
  needed, those three need their own `*_user_group` definitions and the existing delete
  mutations need to switch from `institution#delete` to e.g.
  `hasPermission(#studentId, 'student', 'delete')`. Not in scope right now.

## Resources where DELETE is unsafe / should not be exposed

| Resource | Reason |
|---|---|
| `institution` (hard) | Cascade across entire tenant; should be soft-delete via state transition |
| `course` (with active lessons in progress / billed) | Currently allowed; recommend adding a "active enrollments / billed lessons" guard. Today `CourseService.deleteById` just walks children blindly. |
| `lesson` (state = ENDED with reports) | Today no guard. Probably fine to leave deletable, but flag for product. |
| Template tables (`SubjectTemplate`, `TopicTemplate`, `LevelTemplate`, `SchoolTemplate`, `SubjectLevelLinkTemplate`, `TopicSubjectLinkTemplate`) | These are global seed data, not exposed via GraphQL — keep that way. |

## Implementation checklist per new delete (the 5-step + 5-companion pattern)

For each new delete, the work is:
1. `core/usecases/<domain>/Delete<Entity>UseCase.java` (interface)
2. `core/usecases/<domain>/Delete<Entity>UseCaseImpl.java` (impl — typically just calls the repo)
3. Register in `application/configs/UseCaseConfig.java`
4. Add `deleteById` to `details/services/<Entity>Service.java`:
   - walk children (calling child services, not raw repos)
   - clean link tables via derived `deleteAllByX` (NOT `@Modifying @Query` — preserves Envers)
   - `relationshipService.deleteAllRelationshipsForResource("<resource>", id)` for each
     ReBAC resource + its `*_user_group`
5. Service already registered in `ServiceConfig.java` (only update bean wiring if a new dep needed)
6. Add the GraphQL input type in `application/src/main/resources/graphql/types/<entity>.graphqls`
   (include `institutionId: ID!`)
7. Add the mutation in `application/src/main/resources/graphql/operations/<entity>/mutation.graphqls`
8. Run `mvn generate-sources -f application/pom.xml`
9. Add controller method with `@PreAuthorize(...)` matching the SpiceDB perm
10. Update `spicedb/tests/<resource>.test.yaml` to add positive+negative assertions for delete
11. Add the JUnit unit test for the new `Delete<Entity>UseCaseImpl` and service method
12. Write a Cucumber component test: `delete_<entity>.feature` + `delete-<entity>.graphql` +
    `Delete<Entity>StepDefs.java`

## Risk register

| Risk | Mitigation |
|---|---|
| SpiceDB cleanup outside DB transaction → partial state on crash | Accept it; add a `SpiceDbReconciler` background sweep job for orphan tuples (out of scope here) |
| `@Modifying @Query("DELETE FROM ...")` would bypass Envers | Rule already exists in CLAUDE.md — use derived `deleteAllByX`; enforce via code review |
| Hibernate `cascade=ALL` deletes also bypass `@PreRemove` hooks if any are added later | Prefer explicit service-level walks over JPA cascade for newly-added children |
| `EducatorService.deleteById` not revoking SpiceDB educator-role grants leaves permission leaks | Phase 0 fix |
| Hard-deleting institutions is irreversible and slow | Phase 3 — strongly prefer soft-delete via state transition |
| Deleting an account where the user is the sole institution admin locks tenant out | Phase 2.1 business validation |

## Final ordering recommendation

1. **Phase 0** (4–6h): fix the 6 incomplete cascades in existing deletes (topic, subject,
   level, school, educator, outlet). Highest ROI per hour because each one is small and
   removes ongoing data corruption from the existing system.
2. **Phase 1** (3–4h): add `deleteLessonGroupPlanById`, `deleteLessonGroupById`,
   `declineInstitutionCreationRequest`.
3. **Phase 2** (4–6h): add `deleteAccountById` with sole-admin guard.
4. **Phase 3** (decision needed, then 1–2d): soft-delete institution via state transition,
   OR (only if forced) hard-delete with background-job teardown.

Each phase ships independently. Phase 0 has no API surface change so can ship in one PR.
Phases 1–3 each add new GraphQL surface and require component tests; treat as separate PRs.
