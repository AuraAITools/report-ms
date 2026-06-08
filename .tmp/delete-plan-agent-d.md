# DELETE Operations — Domain/Business Lens

**Author angle:** This is an *education reporting* microservice. Deletes that look fine technically can quietly destroy audit history, orphan reports, or revoke access to currently-running classes. This plan groups resources by *what deletion means in the real world* and what business invariants must hold first. Where pure deletion is wrong, it recommends an alternative verb (archive/end/retire/cancel/disable).

## Glossary of recommended verbs

| Verb | Semantic | Example | DB effect |
|---|---|---|---|
| **hard delete** | Row is removed; row never had business meaning (typo / cleanup) | Material uploaded to wrong institution | Row deleted, Envers row preserved (`@Audited` + derived `deleteAllByX`) |
| **soft delete** | Row hidden from queries but kept for foreign-key integrity / reporting | An educator who has taught lessons | `deletedAt`/`deletedBy` columns + `@Where` or query-side filter |
| **status transition** | Domain has an existing state lifecycle; use it instead of delete | Lesson → CANCELLED; Account → RETIRED; Course → ENDED | Update existing state column |
| **archive** | Same as soft delete + de-indexes from default lists; restorable | Past terms' courses | `archivedAt` flag |
| **no API** | Should never be deleteable via GraphQL — operations team only | Institution (entire tenant), TestResult, StudentLessonReport | n/a |

## Per-resource recommendations

### Tier 0 — already implemented (audit + reconcile)
These have `deleteBy*` mutations today. Each needs review against the rules below; most have **gaps**.

| Resource | Current verb | Gap / risk |
|---|---|---|
| `Course` | hard delete (cascades to lessons) | `Course.courseStartTimestamptz/EndTimestamptz` implies a *running* course. **Should require business check**: `now < courseStartTimestamptz` (not yet started) OR force user to use a future `endCourse` operation. Cascading delete of running lessons destroys student reports. |
| `Lesson` | hard delete | `Lesson.state` already has `CREATED / ENDED / CANCELLED`. Only delete in `CREATED` and only if it has no `LessonHomeworkCompletion / LessonParticipationReview / LessonTopicProficiency / StudentLessonReport` rows. After it ends, the correct verb is `cancelLesson` (writes `LessonCancellationRequest`). |
| `Educator` | hard delete + link cleanup | **Does NOT call `identityService.deleteUser()`** — Keycloak user is orphaned. Also: an educator with `EducatorLessonLink` rows for past lessons cannot be hard-deleted without destroying historical roster info. Recommend **soft delete** + Keycloak `disable` (not delete). |
| `Student` | hard delete + link cleanup | Same as educator. Plus: any `TestResult` / `StudentLessonReport` for the student becomes orphaned. Recommend **soft delete**; never destroy a student's transcript. |
| `Parent` | hard delete | OK *if* parent has no active children with outstanding `Invoice` rows. Recommend **soft delete** to preserve audit on who paid for what. Keycloak cleanup missing. |
| `Outlet` | hard delete | Outlet is a physical location. Deletion should require: no courses with status running, no future lessons booked, no active enrolments. Otherwise correct verb is `closeOutlet`. |
| `Subject`, `Topic`, `LessonObjective`, `LessonQuiz`, `Material`, `OutletRoom`, `School` | hard delete | These are mostly catalog/reference data; hard delete is acceptable for cleanup. Block if referenced by *finalised* (ended) lessons. |
| `Level` | hard delete | Level templates seed real levels at onboarding. Hard delete is risky once students are tagged to a level — preserves history badly. Recommend **archive**. |

### Tier 1 — missing deletes that are low-risk admin cleanup
Implement as **hard delete** with simple referential-integrity precheck.

| Resource | Where | Precondition | Notes |
|---|---|---|---|
| `LessonGroup` | `LessonGroupsGqlController` | Owning lesson is in `CREATED` state | Pure planning artifact pre-lesson |
| `LessonGroupPlan` | `LessonGroupPlansGqlController` | Owning lesson is in `CREATED` state | Already has `removeMaterials*/removeTopics*` granular operations — final delete is needed for full cleanup |
| `OutletRoom` (audit existing) | `OutletRoomsGqlController` | No future `LessonOutletRoomBooking` referencing the room | Otherwise will collapse the room→lesson booking referential graph |
| `InstitutionCreationRequest` | `InstitutionCreationRequestGqlController` | State is `PENDING` (only superadmin) | Mutation should be `rejectInstitutionCreationRequest`, not pure delete — captures *why* |

### Tier 2 — should be soft-delete or status-flag, NOT hard-delete
These are the domain's "people and money" entities. Hard delete is a compliance hazard for a reporting service.

| Resource | Recommended verb | Rationale |
|---|---|---|
| `Account` | **`retireAccount` (status → RETIRED)** | `Account.STATE` already has `RETIRED`. Use it. Cascade: disable Keycloak user (`UserResource.setEnabled(false)`), revoke SpiceDB roles, retain row. *Never* delete: an account is the audit trail "who created this report". |
| `Educator` | **soft delete + Keycloak disable** | Has taught lessons → linked to historical reports |
| `Student` | **soft delete + Keycloak disable** | Has transcripts → never destroyable |
| `Parent` | **soft delete + Keycloak disable** | Linked to invoices |
| `Course` | **`endCourse` (set `courseEndTimestamptz`)** | "Ending" is the real-world semantic; supports re-enrolment reporting |
| `Lesson` | **`cancelLesson` (state → CANCELLED via `LessonCancellationRequest`)** | Domain already has this lifecycle; create a proper mutation rather than abuse `deleteLessonById` |
| `Level` | **archive** | Levels are tagged by transcripts |
| `Outlet` | **`closeOutlet` (status flag, new column)** | Physical-location closure ≠ data destruction |
| `Institution` | **NOT exposed via GraphQL — tenant teardown is ops-only** | Entire-tenant deletion involves DB schema + Keycloak realm + SpiceDB + S3 + audit dump. Out of scope for runtime mutation; build as a `database-runner` admin job or a separate internal API gated behind super-admin SpiceDB perm `system:aura#tenant_teardown`. |

### Tier 3 — must never be deleted via GraphQL (no DELETE mutation)
These are *reporting history*. Reading the codebase, none of these are currently exposed at controller level — keep it that way and reject any future PR that adds them.

- `TestResult`, `Test`, `TestGroup` — student academic record
- `StudentLessonReport`, `LessonHomeworkCompletion`, `LessonParticipationReview`, `LessonTopicProficiency` — per-lesson per-student observation history
- `Invoice`, `PriceRecord` — financial records (legally retained)
- `LessonCancellationRequest`, `LessonPostponementRequest` — these *are* deletion-events; deleting them deletes the audit trail of cancellation
- `InstitutionCreationRequest` once approved (only delete in `PENDING`)
- All `*LinkModel` link tables (e.g. `StudentLessonLink`, `EducatorLessonLink`, `LevelEducatorLink`) — link mutations should be paired `enroll/withdraw`-style verbs, not direct deletes. (Some already exist; ensure no `deleteLink` mutation is ever added at the GraphQL layer.)

### Tier 4 — currently no public mutations needed
`Permission`, `GroupMember`, `GroupRelation`, `GroupPermission` — already covered by `grantUserGroupRole` / `revokeUserGroupRole`. No further delete operations needed.

## Business-rule validations required before delete (checklist)

Implement these as guard methods in the relevant `core/usecases/<domain>/Delete*UseCaseImpl.java`. Throw a domain-specific `BusinessRuleViolationException` so the controller can surface a meaningful GraphQL error.

| Domain | Pre-delete invariants |
|---|---|
| Course | (a) `courseStartTimestamptz` in the future, OR `courseEndTimestamptz` in the past (course has ended); (b) no `StudentLessonReport` rows across child lessons; (c) no unpaid `Invoice` referencing this course |
| Lesson | (a) `state == CREATED`; (b) no `StudentLessonReport`, no `LessonHomeworkCompletion`, no `LessonTopicProficiency`, no `LessonQuizStudentLessonLink` rows |
| Educator / Student / Parent | (a) no historical lesson participation; (b) no outstanding invoices (parent); (c) no active enrolments (force admin to withdraw first) — *if* any fail, fall back to soft-delete + Keycloak disable instead of hard-delete |
| Outlet | (a) no future lessons booked in any of its rooms; (b) no active courses; (c) no active enrolments via `StudentOutletLink` |
| OutletRoom | no future `LessonOutletRoomBooking` |
| Level / Subject / Topic / LessonObjective / Material | not referenced by an *ended* lesson's plan (preserves historical pedagogical context) |
| LessonGroup / LessonGroupPlan | parent lesson is `CREATED` (not yet run) |
| LessonQuiz | parent lesson is `CREATED` and no student responses exist |
| School | no current students tagged to this school |
| InstitutionCreationRequest | status is `PENDING` |

## Special-case: account-typed resources (Keycloak + SpiceDB cleanup)

`Educator`, `Student`, `Parent` and `Account` all map to a Keycloak user via `userId`. **The existing `deleteEducatorById / deleteStudentById / deleteParentById` mutations do NOT call `identityService.deleteUser(userId)`** — this is a latent bug that leaves orphaned Keycloak users on every "delete".

Recommended pattern (soft-delete + identity disable, not destroy):

```java
@Transactional
public void retireEducator(String id) {
    Educator e = ...findById(id)... // ensures exists, throws ResourceNotFoundBusinessException
    // 1. Business-rule check: must not be teaching active courses
    if (educatorRepository.hasActiveCourses(id)) {
        throw new BusinessRuleViolationException("Educator has active courses; withdraw first");
    }
    // 2. Soft-delete: set deletedAt, NOT row removal — preserves history
    e.setDeletedAt(Instant.now());
    e.setDeletedBy(currentUser);
    educatorRepository.save(e);
    // 3. Disable Keycloak user (NOT delete — preserves login audit)
    identityService.disableUser(e.getUserId());
    // 4. Revoke SpiceDB role tuples for this user across the institution
    relationshipService.deleteAllRelationshipsForSubject("user", e.getUserId());
}
```

A new `IdentityService.disableUser(userId)` method is needed (`deleteUser` already exists).

Add `deletedAt`, `deletedBy` to `TenantBaseModel` or a new `SoftDeletableModel` mixin. Add a JPA `@Filter` / repository-level predicate so `findById`/`findAll*` skip soft-deleted rows by default. Reads via `getAllAccountsInInstitution(includeRetired: Boolean = false)` flag.

## Special-case: Institution (tenant teardown)

Out of scope for a GraphQL `deleteInstitution` mutation. The blast radius (DB schema rows across 60+ tables, Envers audit, Liquibase rollback, SpiceDB schema for that institution's resources, Keycloak users in the realm, S3 profile/material assets) is too large for a normal mutation. Recommend:

1. Short term: no GraphQL mutation; mark institution `state = SUSPENDED` only.
2. Long term: a `database-runner` job `teardown-institution.yaml` with explicit confirmation token, runs offline.

## Implementation phasing (ordered by business risk, low → high)

**Phase 1 — pure admin cleanup (low risk).** Add hard-delete mutations only:
- `deleteLessonGroupById`, `deleteLessonGroupPlanById` (block if parent lesson not `CREATED`)
- Audit & guard existing `deleteSubjectById / deleteTopicById / deleteMaterialById / deleteLessonObjectiveById / deleteLessonQuizById / deleteOutletRoomById / deleteSchoolById` — they exist but most lack the precondition checks above
- `rejectInstitutionCreationRequest` (replaces the missing delete; only `PENDING` state)

**Phase 2 — retrofit guards on existing risky deletes.**
- Add precondition checks to `deleteCourseById`, `deleteLessonById`, `deleteOutletById`, `deleteLevelById` as listed in the checklist
- Add `IdentityService.disableUser` and call it from `deleteEducatorById/deleteStudentById/deleteParentById` (interim fix until Phase 3)

**Phase 3 — replace people-deletes with retire/soft-delete.**
- Schema: deprecate `deleteEducatorById/deleteStudentById/deleteParentById`, introduce `retireEducator/retireStudent/retireParent`
- Domain: introduce `deletedAt/deletedBy` columns and a base `SoftDeletableModel`
- Repositories filter out soft-deleted by default; new `getRetired*` queries for admin
- Migrate any existing hard-delete callers

**Phase 4 — replace business-lifecycle deletes with proper verbs.**
- `endCourse(id, endDate)` — replaces `deleteCourseById`
- `cancelLesson(id, reason)` — writes `LessonCancellationRequest` and sets `state = CANCELLED`
- `closeOutlet(id)` — new status column + mutation
- `archiveLevel(id)` — new `archivedAt`

**Phase 5 (deferred / not scope).**
- Institution tenant teardown (operational tool, not GraphQL)
- Test result/report deletion — explicitly never

For every mutation added, this repo's conventions require: GraphQL schema fragment, generated types regenerate, controller, service `@Transactional`, use case interface+impl, repository, **SpiceDB relationship delete via `relationshipService.deleteAllRelationshipsForResource(...)`**, **SpiceDB `.zed` test YAML updated with negative/positive assertions for the new delete permission**, Cucumber feature + `.graphql` doc + step defs, unit tests with `@DisplayName`.

## Open questions

1. **Soft delete semantics across `@Audited`.** Envers already records the `deletedAt` column change as an audit revision (REVTYPE=DEL via lifecycle). Do we still want a hard-delete shadow operation behind a feature flag for true GDPR "right-to-erasure"? Recommend yes — gated to super-admin SpiceDB perm.
2. **`@PreAuthorize` resource for retire-operations on accounts.** Today `deleteParentById` checks `institution:delete`. For `retireEducator`, is `institution:delete` still right, or do we need a new `institution:retire_account` permission so an institution-admin can retire users without being able to delete the entire institution? Recommend adding a discrete permission.
3. **Default visibility of soft-deleted rows.** Most existing `findAllByX` repository methods will need a `WHERE deleted_at IS NULL` predicate. Is it OK to retrofit this across all repositories at once (consistent), or only on a per-domain basis as we soft-delete each entity (incremental)?
4. **Re-instatement.** Do we need `unretire*` mutations? Likely yes for student/educator (employee comes back) — design now or punt?
5. **What does deleting an `Account` do to its child `Parent` / `Student` / `Educator` rows?** Currently the *child* delete cleans up links but does not delete the parent account. Should retiring an account auto-retire all its child role-rows, or vice-versa? Recommend: retiring an account cascades to disabling all child roles, but each child role can be retired independently without retiring the account.
6. **Course "ending" vs "deleting".** A course that ran for a term and has 30 student transcripts should never be deletable — but currently `deleteCourseById` accepts it. Phase 2 guard is essential before this gets used in production.
7. **`LessonQuiz` student responses.** Need confirmation that quiz responses are stored (look for a `LessonQuizResponse` entity); none was found in this scan. If responses are stored elsewhere (e.g. another microservice), the precondition check needs an inter-service call.
