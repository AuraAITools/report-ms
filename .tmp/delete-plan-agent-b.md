# Delete Operations Plan — FK-First / Graph-Theoretic View (Agent B)

This plan is derived by walking every `@Entity` in `details/springdata/models/`, mapping JPA `@ManyToOne` / `@OneToMany` / `@OneToOne` edges into a directed reference graph, then deriving a safe deletion order. Each node also maps to its GraphQL controller and SpiceDB resource type.

---

## 1. Entity-Reference Graph

### 1.1 Aggregate roots (have own GraphQL surface)

| Resource | Table | Parent FK | Outgoing edges (`@ManyToOne` it owns) | SpiceDB resource? |
|---|---|---|---|---|
| Institution | `Institutions` | — (root, FK to system in SpiceDB only) | none | `institution` + `institution_user_group` |
| Outlet | `Outlets` | tenantId (institution) | none | `outlet` + `outlet_user_group` |
| OutletRoom | `OutletRooms` | `outlet_id` NOT NULL | OutletModel | `outlet_room` (no user_group; inherits from outlet) |
| Course | `Courses` | `outlet_id` NOT NULL | OutletModel, LevelModel, PriceRecordModel (`OneToOne` CASCADE.ALL) | `course` + `course_user_group` |
| Lesson | `Lessons` | `outlet_id` NOT NULL, `course_id` nullable | OutletModel, CourseModel | `lesson` + `lesson_user_group` |
| LessonGroup | `LessonGroups` | `lesson_id` NOT NULL | LessonModel | not in schema; inherits via lesson |
| LessonGroupPlan | `LessonGroupPlans` | `lesson_group_id` NOT NULL UNIQUE | LessonGroupModel | not in schema |
| LessonObjective | `LessonObjectives` | `lesson_group_plan_id` | LessonGroupPlanModel | not in schema (covered by `create_lesson_objective` on lesson) |
| LessonQuiz | `LessonQuizzes` | `lesson_group_plan_id` | LessonGroupPlanModel | not in schema (covered by `create_lesson_quiz` on lesson) |
| Subject | `Subjects` | tenantId | none | `subject` + `subject_user_group` |
| Topic | `Topics` | tenantId | none | `topic` + `topic_user_group` |
| Material | `Materials` | tenantId | none | `material` + `material_user_group` |
| Level | `Levels` | tenantId | none | `level` + `level_user_group` |
| Student | `Students` | tenantId | SchoolModel, LevelModel | no resource def (user-role-only) |
| Educator | `Educators` | tenantId | none | no resource def (user-role-only) |
| Account | `Accounts` | tenantId | ParentModel (`OneToOne` CASCADE.ALL + orphanRemoval) | `account` + `account_user_group` |
| Parent | `Parents` | tenantId | AccountModel (`OneToOne`) | not in schema (1:1 child of account) |
| School | `Schools` | tenantId | none | not in schema (`institution.delete_school` exists, no own def) |

### 1.2 Join / link tables (every reference is `@ManyToOne` to two parents)

```
AccountEducatorLinks         (account ↔ educator)
AccountStudentLinks          (account ↔ student)
EducatorCourseLinks          (educator ↔ course)
EducatorLessonLinks          (educator ↔ lesson)            // lesson cascade=ALL deletes these
LevelEducatorLinks           (level ↔ educator)
LevelLessonLinks             (level ↔ lesson)               // lesson cascade=ALL deletes these
OutletEducatorLinks          (outlet ↔ educator)
StudentCourseLinks           (student ↔ course)
StudentLessonLinks           (student ↔ lesson, lessonGroup) // lesson cascade=ALL
StudentOutletLinks           (student ↔ outlet)
SubjectCourseLinks           (subject ↔ course)
SubjectEducatorLinks         (subject ↔ educator)
SubjectLessonLinks           (subject ↔ lesson)             // lesson cascade=ALL
SubjectLevelLinks            (subject ↔ level)
SubjectStudentLinks          (subject ↔ student)
SubjectTestGroupLinks        (subject ↔ testGroup)
TopicLessonLinks             (topic ↔ lesson)               // lesson cascade=ALL
TopicSubjectLinks            (topic ↔ subject)
LessonGroupPlanTopicLinks    (plan ↔ topic)
LessonGroupPlanMaterialsLinks(plan ↔ material)
LessonObjectiveTopicLinks    (objective ↔ topic)
MaterialTopicLinks           (material ↔ topic)
```

### 1.3 Lifecycle / report sub-graph

```
StudentLessonLinks
   └─OneToOne CASCADE.ALL─> StudentLessonReports
                              ├─OneToOne CASCADE.ALL─> LessonHomeworkCompletions
                              ├─OneToOne CASCADE.ALL─> LessonParticipationReviews
                              └─OneToMany CASCADE.ALL─> LessonTopicProficiencies
LessonQuizStudentLessonLinks ─ManyToOne─> LessonQuiz, StudentLessonReport
LessonCancellationRequests   ─ManyToOne─> Lesson  (lesson cascade=ALL deletes)
LessonPostponementRequests   ─ManyToOne─> Lesson  (lesson cascade=ALL deletes)
LessonOutletRoomBookings     ─ManyToOne─> OutletRoom (cascade=ALL on room); ─OneToOne─> Lesson
PriceRecords                 ─OneToOne mappedBy course (course cascade=ALL deletes)
```

### 1.4 Cycle / cross-aggregate-string-ID edges (NOT auto-cleaned by JPA)

Per CLAUDE.md, `core/` entities use string IDs to break cycles, but JPA still keeps a real FK. The link tables above are the bidirectional manifestations. **None of these links cascade-delete from their parent unless explicitly listed** (only Lesson does `cascade=ALL` for its half-dozen link children + cancellation/postponement requests).

---

## 2. Topological Deletion Order (leaves → roots)

Numbered phases. Within a phase, items are independent and any order works. Each later phase can only be executed once every earlier phase has cleaned its inbound edges.

```
PHASE 0  Lifecycle leaves
  - LessonHomeworkCompletion, LessonParticipationReview, LessonTopicProficiency
    (already CASCADE.ALL from StudentLessonReport — no explicit delete needed)
  - LessonQuizStudentLessonLink (no cascade; must clean before LessonQuiz or StudentLessonReport)

PHASE 1  Plan-scoped link rows
  - LessonGroupPlanTopicLinks      (by planId)
  - LessonGroupPlanMaterialsLinks  (by planId)
  - LessonObjectiveTopicLinks      (by objectiveId)

PHASE 2  Plan-scoped children
  - LessonObjective    (cleans #1 via LessonObjectiveTopicLinks)
  - LessonQuiz         (must clean LessonQuizStudentLessonLinks first)

PHASE 3  Plan & group
  - LessonGroupPlan    (cleans #1; cleans LessonObjectives & LessonQuizzes; cleans MaterialsLinks)
  - LessonGroup        (after its plan; CASCADE.ALL→LessonGroupPlan exists, so deleting group cascades plan)

PHASE 4  Lesson-scoped link rows
  - SubjectLessonLinks, TopicLessonLinks, LevelLessonLinks, EducatorLessonLinks  (cascade=ALL via Lesson, redundant if Lesson is deleted)
  - StudentLessonLinks (cascade=ALL via Lesson; itself cascades StudentLessonReport via OneToOne CASCADE.ALL)
  - LessonOutletRoomBooking (cascade=ALL via OutletRoom)
  - LessonPostponementRequests, LessonCancellationRequests (cascade=ALL via Lesson)

PHASE 5  Lesson
  - Lesson  (cascades all phase-4 children; cleans StudentLessonReports → Homework/Participation/TopicProficiency)
  - Standalone LessonGroup / LessonGroupPlan if leftover (Lesson cascades them)

PHASE 6  Course-scoped link rows
  - SubjectCourseLinks, EducatorCourseLinks, StudentCourseLinks  (NOT cascaded; explicit delete)
  - Lessons-by-courseId loop (calls Phase 5 per lesson)

PHASE 7  Course
  - Course  (CASCADE.ALL on PriceRecord deletes it; null-out by Level reference must already have happened)

PHASE 8  Outlet-scoped children
  - OutletRoom  (CASCADE.ALL deletes LessonOutletRoomBookings; must not have other lessons referencing it)
  - StudentOutletLinks, OutletEducatorLinks  (explicit delete)
  - Courses-by-outletId loop (calls Phase 7 per course)

PHASE 9  Outlet
  - Outlet

PHASE 10  Tenant-scoped leaves (Material, Topic, Subject, Level, School)
  - Material:   delete MaterialTopicLinks, LessonGroupPlanMaterialsLinks → Material
  - Topic:      delete MaterialTopicLinks, TopicSubjectLinks, TopicLessonLinks, LessonObjectiveTopicLinks, LessonGroupPlanTopicLinks → Topic
  - Subject:    delete TopicSubjectLinks, SubjectCourseLinks, SubjectStudentLinks, SubjectEducatorLinks, SubjectLevelLinks, SubjectLessonLinks, SubjectTestGroupLinks → Subject
  - Level:      delete SubjectLevelLinks, LevelEducatorLinks, LevelLessonLinks; null-out Courses.level_id, Students.level_id → Level
  - School:     null-out Students.school_id → School

PHASE 11  People (Student, Educator)
  - Student:  delete AccountStudentLinks, SubjectStudentLinks, StudentCourseLinks, StudentOutletLinks, StudentLessonLinks (→ cascades reports), TestResults → Student
  - Educator: delete AccountEducatorLinks, SubjectEducatorLinks, EducatorCourseLinks, OutletEducatorLinks, LevelEducatorLinks, EducatorLessonLinks → Educator

PHASE 12  Account & Parent
  - Parent: tied 1:1 to Account; Account.OneToOne(parent, CASCADE.ALL+orphanRemoval) handles it. Delete Parent alone leaves Account intact; delete Account cascades Parent.
  - Account: must first null out (or delete) AccountEducatorLinks & AccountStudentLinks (no cascade on Account side); then delete Account → cascades Parent.

PHASE 13  Institution
  - Institution: terminal — requires that every Outlet, Course, Lesson, Level, Subject, Topic, Material, School, Account, Educator, Student under the tenant has been deleted (or cascaded). Effectively a "burn-down-tenant" operation.
```

---

## 3. Per-Resource Delete Recipe

Each row lists the GraphQL mutation, FK cleanup (with derived-delete preference), SpiceDB cleanup, and current status.

### Lesson-tree (deepest leaves first)

| Resource | Mutation (state) | FK cleanup | SpiceDB cleanup | Notes |
|---|---|---|---|---|
| LessonObjective | `deleteLessonObjectiveById` (exists) | `deleteAllLessonObjectiveTopicLinksByLessonObjectiveId` ✓ | none (no own resource; inherits via lesson) | OK |
| LessonQuiz | `deleteLessonQuizById` (exists) | **missing** `deleteAllLessonQuizStudentLessonLinksByLessonQuizId` | none | Currently leaks LessonQuizStudentLessonLink rows |
| LessonGroupPlan | **MISSING** | derived deletes for plan-topic, plan-material, objectives, quizzes | none | New mutation: `deleteLessonGroupPlanById(institutionId: ID!, id: ID!)` |
| LessonGroup | **MISSING** | LessonGroup.CASCADE.ALL→LessonGroupPlan handles plan; StudentLessonLinks still point at this group (FK becomes orphan) — must null out | none | New mutation: `deleteLessonGroupById(institutionId: ID!, id: ID!)`. Risk: StudentLessonLinks.lesson_group is not CASCADE — must null or delete first |
| Lesson | `deleteLessonById` (exists) | `deleteAllLessonLinksByLessonId` aggregator ✓ (cascade=ALL on link children covers the rest) | `lesson`, `lesson_user_group` ✓ | OK |

### Course-tree

| Resource | Mutation (state) | FK cleanup | SpiceDB cleanup | Notes |
|---|---|---|---|---|
| Course | `deleteCourseById` (exists) | Per-lesson Phase-5; subject/educator/student-course links ✓ | `course`, `course_user_group` ✓ | OK. **Bug:** `id` only — should accept `institutionId: ID!` per tenant convention |
| Outlet | `deleteOutletById` (exists, has `institutionId`) | Per-course Phase-7 loop ✓ | `outlet`, `outlet_user_group` ✓ | **Missing**: StudentOutletLinks, OutletEducatorLinks, OutletRooms not cleaned. OutletRoom is `cascade=ALL` via Outlet so OK; StudentOutletLinks & OutletEducatorLinks are NOT and will block FK |
| OutletRoom | `deleteOutletRoomById` (exists) | LessonOutletRoomBookings cascade ✓ | `outlet_room` ✓ | OK |

### Tenant-scoped catalogs

| Resource | Mutation (state) | FK cleanup | SpiceDB cleanup | Notes |
|---|---|---|---|---|
| Subject | `deleteSubjectById` (exists) | Topic-Subject + Subject-Course ✓; **missing** SubjectStudent, SubjectEducator, SubjectLevel, SubjectLesson, SubjectTestGroup | `subject`, `subject_user_group` ✓ | Partial — current impl will FK-violate if any of the missing links exist |
| Topic | `deleteTopicById` (exists) | TopicSubject ✓; **missing** MaterialTopic, TopicLesson, LessonObjectiveTopic, LessonGroupPlanTopic | `topic`, `topic_user_group` ✓ | Partial |
| Material | `deleteMaterialById` (exists) | MaterialTopic + LessonGroupPlanMaterials ✓ | `material`, `material_user_group` ✓ | OK |
| Level | `deleteLevelById` (exists) | nullOut courses, LevelLesson ✓; **missing** SubjectLevel, LevelEducator, null-out Students.level_id | `level`, `level_user_group` ✓ | Partial |
| School | `deleteSchoolInInstitution` (exists) | **missing** null-out Students.school_id | none in code (schema has `delete_school` on institution) | Will FK-violate if any Student references this school |

### People

| Resource | Mutation (state) | FK cleanup | SpiceDB cleanup | Notes |
|---|---|---|---|---|
| Student | `deleteStudentById` (exists) | 5 link types ✓ | **missing** — Student is granted as `viewer` on various course/lesson user_groups via user_id; orphan relationships remain | Needs cleanup of relationships where this student's user_id is the subject. Requires Keycloak user_id mapping |
| Educator | `deleteEducatorById` (exists) | 6 link types ✓ | **missing** — Educator is granted as `admin`/`modifier`/`group_admin` on course/lesson user_groups | Same as Student. Plus may break running lessons/courses where they're the only educator |
| Parent | `deleteParentById` (exists) | none in service | none | OK; account cascade handles via account.parent.CASCADE.ALL+orphanRemoval |
| Account | **MISSING** | AccountEducatorLinks, AccountStudentLinks, then account.parent cascades | `account`, `account_user_group` | Add `deleteAccountById(institutionId: ID!, id: ID!)`; SpiceDB has full schema for it |

### Top-level

| Resource | Mutation (state) | FK cleanup | SpiceDB cleanup | Notes |
|---|---|---|---|---|
| Institution | **MISSING** | Massive — burn down entire tenant graph | `institution`, `institution_user_group` | Very high risk; recommend soft delete or admin-only |
| InstitutionCreationRequest | **MISSING** | none (no children) | none in schema | Optional — usually rejected via state transition not delete |
| Lesson sub-children (LessonGroup, LessonGroupPlan) | **MISSING** | See above | none | Should be exposed |

---

## 4. Naming Concretes

| Resource | Mutation | Service method | Use-case impl | SpiceDB permission |
|---|---|---|---|---|
| Institution | `deleteInstitutionById(id: ID!)` | `InstitutionService.deleteById(String)` | `DeleteInstitutionUseCaseImpl` | `institution.delete` |
| Outlet | `deleteOutletById(institutionId: ID!, id: ID!)` ✓ | `OutletService.deleteById` ✓ | `DeleteOutletUseCaseImpl` ✓ | `outlet.delete` |
| OutletRoom | `deleteOutletRoomById(input: ...!)` ✓ | ✓ | ✓ | `outlet_room.delete` |
| Course | `deleteCourseById(institutionId: ID!, id: ID!)` (add `institutionId`) | ✓ | ✓ | `course.delete` |
| Lesson | ✓ | ✓ | ✓ | `lesson.delete` |
| LessonGroup | `deleteLessonGroupById(institutionId: ID!, id: ID!)` | `LessonGroupService.deleteById` | `DeleteLessonGroupUseCaseImpl` | `lesson.delete` (use parent lesson) |
| LessonGroupPlan | `deleteLessonGroupPlanById(institutionId: ID!, id: ID!)` | `LessonGroupPlanService.deleteById` | `DeleteLessonGroupPlanUseCaseImpl` | `lesson.delete` (use parent lesson via plan→group→lesson) |
| LessonObjective | ✓ | ✓ | ✓ | `lesson.delete` |
| LessonQuiz | ✓ | ✓ + add link cleanup | ✓ | `lesson.delete` |
| Subject | ✓ + add link cleanups | ✓ + fix | ✓ | `subject.delete` |
| Topic | ✓ + add link cleanups | ✓ + fix | ✓ | `topic.delete` |
| Material | ✓ | ✓ | ✓ | `material.delete` |
| Level | ✓ + add link cleanups | ✓ + fix | ✓ | `level.delete` |
| School | ✓ + add Student null-out | ✓ + fix | ✓ | `institution.delete_school` |
| Student | ✓ + add SpiceDB user-id cleanup | ✓ + fix | ✓ | `student.delete` (via account or institution scope) |
| Educator | ✓ + add SpiceDB user-id cleanup | ✓ + fix | ✓ | `educator.delete` (via account or institution scope) |
| Parent | ✓ | ✓ | ✓ | `account.delete` (via parent's account) |
| Account | `deleteAccountById(institutionId: ID!, id: ID!)` | `AccountService.deleteById` | `DeleteAccountUseCaseImpl` | `account.delete` |

---

## 5. Implementation Phases (recommended PR sequence)

1. **Phase A — fix existing partial deletes (bug-bash)**
   - SubjectService: add missing 5 link cleanups
   - TopicService: add missing 4 link cleanups
   - LevelService: add SubjectLevel, LevelEducator, null-out students.level_id
   - SchoolService: add null-out students.school_id
   - LessonQuizService: add LessonQuizStudentLessonLink cleanup
   - StudentService / EducatorService: add SpiceDB relationship cleanup by user_id
   - OutletService: add OutletEducatorLinks + StudentOutletLinks cleanup
   - CourseService GraphQL: add `institutionId: ID!`

2. **Phase B — fill missing leaf deletes**
   - `deleteLessonObjectiveById` already exists ✓
   - `deleteLessonQuizById` already exists ✓ (just fix internals)
   - Add `deleteLessonGroupPlanById`
   - Add `deleteLessonGroupById`

3. **Phase C — add `deleteAccountById`**
   - Most demanding because parent OneToOne cascade exists but account link tables don't cascade
   - Add Keycloak user retirement consideration

4. **Phase D — add `deleteInstitutionById`** (last)
   - Burn-down of the entire tenant graph. Iterate Outlets → Courses → Lessons; then catalogs (Subjects, Topics, Materials, Levels, Schools); then Students/Educators; then Accounts. Then the institution. Then SpiceDB.
   - Strongly recommend this be **soft delete** (state=INACTIVE) plus a separate admin-only hard-delete job. Hard-delete is irreversible and audit-history-destroying.

---

## 6. Risks & Soft-Delete Candidates

| Resource | Risk | Recommendation |
|---|---|---|
| **Institution** | Wipes entire tenant. Envers history vanishes for cascade-deleted rows. Keycloak users keyed to this tenant become orphans | Soft delete: `state=INACTIVE` + admin-only job |
| **Account** | Owns Keycloak user mapping. Hard delete may leave Keycloak user but break login | Soft delete via `state=RETIRED` (enum already exists) |
| **Educator / Student** | If lessons are in-progress, future StudentLessonReports for those lessons will lose subject. Cancellation/postponement requests already approved disappear | Guard: refuse delete if lesson with future `lessonStartTimestamptz` references them. Or soft-delete |
| **Course** | Deleting drops historical lessons. Possibly attached to invoices (InvoiceModel has no FK back to Course but business logic may) | Guard: refuse if course has lessons in `ENDED` state with reports |
| **Lesson** | LessonCancellationRequest with `APPROVED` status implies refund/business event happened — hard-deleting destroys the audit trail | Soft-delete or refuse when status=APPROVED/ENDED |
| **LessonGroupPlan / LessonGroup** | StudentLessonLinks reference lessonGroup via FK without cascade; deleting plan/group leaves dangling pointer | Must null-out or refuse if registered students exist |
| **Subject / Topic** | Used as catalog references; deleting may surprise users who built reports on them | OK as hard delete if link counts are zero; otherwise refuse |
| **Material** | Has S3 `fileKey`; deleting the row should also call `assetUrlService.deleteObject(fileKey)` | Add S3 cleanup in service; currently leaks orphan blobs |
| **Level / School** | Students/courses reference these; nulling out vs. refusing is a UX call | Prefer null-out (current Level pattern) |
| **OutletRoom** | `cascade=ALL` on bookings will delete future bookings silently | Guard: refuse if bookings exist for `startTimestampz > now()` |
| **InvoiceModel** | No FK to anything but tenantId — orphan-safe. No GraphQL surface yet; ignore | — |

### S3 / Keycloak side-effects not yet implemented

- **Material.fileKey** — never deleted from S3 on row delete
- **Student.profileImageKey / Educator.profileImageKey / Account.profileImageKey / Institution.logoKey / profileImageKey / Parent.profileImageKey** — same orphan-S3-object problem on hard delete
- **Account.userId** — Keycloak user is not retired/deleted; deleting the row strands the Keycloak identity
- **Educator/Student.userId** — same Keycloak orphan concern

Add an `AssetCleanupService` step in each delete service that owns a `*Key` field, and a `KeycloakUserRetirementService` integration for Account/Educator/Student deletes.

---

## 7. Cycle-Detection Output

Running the entity graph through topological sort with link tables as separate nodes shows **no true cycles** — all "cycles" suggested by JPA bidirectional `mappedBy` are owned on one side only. The only places that look cyclic and would block a naïve `DELETE FROM` are:

- `Account ↔ Parent` (`Account.OneToOne(cascade=ALL+orphanRemoval) → Parent`, `Parent.OneToOne → Account` non-owning) — resolved by always deleting from the Account side.
- `Course ↔ PriceRecord` (`Course.OneToOne(cascade=ALL) → PriceRecord`, `PriceRecord.OneToOne(mappedBy) → Course`) — resolved by deleting Course.
- `Lesson ↔ LessonOutletRoomBooking` (1:1 each way) — booking's FK to lesson is owning; deleting the booking first is safe.

Every other "back-reference" goes through a link table whose two `@ManyToOne` halves are both fully addressable via repository derived-delete methods, so the cycle is broken at the link-table level.

---

## 8. ArchUnit / Test Convention Reminders (from CLAUDE.md)

For every new delete:
1. Unit test for the `Delete<X>UseCaseImpl` (JUnit5 + Mockito + AssertJ; `@DisplayName` on class + method).
2. Unit test for the corresponding service `deleteById` verifying every link-cleanup call + every `deleteAllRelationshipsForResource` call.
3. Cucumber feature `delete_<x>.feature` tagged `@Api @ComponentTest` exercising full GraphQL flow + DB & SpiceDB assertions.
4. `PreAuthorizeConventionTest` will fail if the new mutation lacks a valid `@PreAuthorize` referencing the right resource type / scope / argument.
5. SpiceDB schema test (`spicedb/tests/<schema>.test.yaml`) must add positive + negative assertions if any new permission is added.
