@Api
Feature: Lesson Planning

  As an educator
  I want to organize a lesson into lesson groups, each with a plan I can enrich with topics and materials
  So that every group of students has a clear, up-to-date plan for the lesson

  @ComponentTest
  Scenario: Institution admin creates a lesson group in an existing lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "LG Outlet"
    And a course exists with name "LG Course"
    And a lesson exists with name "LG Lesson"
    When the user creates a lesson group in the lesson with the following details:
      | name    | plan       |
      | Group A | Day 1 Plan |
    Then the lesson group should be created successfully
    And the lesson group response should have name "Group A"
    And the lesson group response should reference the lesson
    And the lesson group response lessonGroupPlan should have state "UNPLANNED"

  # Every lesson group is born with exactly one plan, supplied when the lesson
  # is created. The plan is then enriched (topics, materials, objectives) or
  # updated — never created standalone.

  @ComponentTest @LessonGroupPlanCreate
  Scenario: Aura admin sees each lesson group seeded with its plan on lesson creation
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Plan Outlet"
    And a course exists with name "Plan Course"
    And a lesson exists with name "Plan Lesson"
    Then the lesson group plan should exist in the database with plan "Plan Lesson plan"

  @ComponentTest @LessonGroupPlanCreate
  Scenario: Aura admin fails to create a second plan for a lesson group
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Plan Outlet"
    And a course exists with name "Plan Course"
    And a lesson exists with name "Plan Lesson"
    When the user tries to create a second lesson group plan in the lesson group with plan "Second plan"
    Then the request should fail with error code "DUPLICATE_RESOURCE"

  @ComponentTest @LessonGroupPlanCreate
  Scenario: Unauthorized user cannot create a lesson group plan
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Plan Outlet"
    And a course exists with name "Plan Course"
    And a lesson exists with name "Plan Lesson"
    Given a topic exists with name "Algebra"
    And user "lessongroupplanviewer@test.com" has "viewer" role on "lesson_user_group" "{lesson_id}"
    And user switches to "lessongroupplanviewer@test.com" identity
    When the unauthorized user tries to create a lesson group plan in the lesson group with plan "Unauthorized plan" and 1 topics
    Then the request should fail with an error

  @ComponentTest @LessonGroupPlanUpdate
  Scenario: Educator successfully updates a lesson group plan's fields
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Update Plan Outlet"
    And a course exists with name "Update Plan Course"
    And a lesson exists with name "Update Plan Lesson"
    When the user updates the lesson group plan with plan "Revised plan" and recap "Recap text" and state "PLANNED"
    Then the updated lesson group plan response should have plan "Revised plan"
    And the updated lesson group plan response should have recap "Recap text"
    And the updated lesson group plan response should have state "PLANNED"

  # A plan may only reference topics that are taught in its lesson, so the
  # lesson is created linked to the topics before the plan is enriched.

  @ComponentTest @LessonGroupPlanTopics
  Scenario: Educator adds then removes topics on a lesson group plan
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Top Mgmt Outlet"
    And a course exists with name "Top Mgmt Course"
    And a topic exists with name "Algebra"
    And a lesson exists with name "Top Mgmt Lesson" linked to the existing topics
    When the user adds 1 topics to the lesson group plan
    Then the lesson group plan should have 1 linked topics in the database
    When the user removes 1 topics from the lesson group plan
    Then the lesson group plan should have 0 linked topics in the database

  @ComponentTest @LessonGroupPlanMaterials
  Scenario: Educator adds then removes materials on a lesson group plan
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Mat Mgmt Outlet"
    And a course exists with name "Mat Mgmt Course"
    And a lesson exists with name "Mat Mgmt Lesson"
    And user creates a material in the institution with
      | name        | description     |
      | Worksheet B | Adjustable mat  |
    When the user adds the material to the lesson group plan
    Then the lesson group plan should have 1 linked materials in the database
    When the user removes the material from the lesson group plan
    Then the lesson group plan should have 0 linked materials in the database

  @ComponentTest @LessonGroupDelete
  Scenario: Aura admin deletes an existing lesson group
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Del LG Outlet"
    And a course exists with name "Del LG Course"
    And a lesson exists with name "Del LG Lesson"
    When the user deletes the existing lesson group
    Then the lesson group should be deleted successfully
    And the lesson group should no longer exist in the database

  @ComponentTest @LessonGroupDelete
  Scenario: Modifier cannot delete a lesson group
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Del LG Outlet"
    And a course exists with name "Del LG Course"
    And a lesson exists with name "Del LG Lesson"
    And user "lessongroupmodifier@test.com" has "modifier" role on "lesson_user_group" "{lesson_id}"
    And user switches to "lessongroupmodifier@test.com" identity
    When the user tries to delete the existing lesson group
    Then the request should fail with error code "FORBIDDEN"

  # Students must first be assigned to the lesson before being added to a
  # group within that lesson — the group's roster is a subset of the lesson roster.

  @ComponentTest @LessonGroupAddStudents
  Scenario: Aura admin adds a student already in the lesson to the existing lesson group
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Add Stu Outlet"
    And a course exists with name "Add Stu Course"
    And the lesson "Add Stu Lesson" is created in course "Add Stu Course" with no room from "2026-03-01T09:00:00Z" to "2026-03-01T10:00:00Z"
    And the student "Alice" exists
    And the student "Alice" is assigned to lesson "Add Stu Lesson"
    When the user adds student "Alice" to the existing lesson group
    Then the lesson group response should include student "Alice"

  @ComponentTest @LessonGroupAddStudents
  Scenario: Aura admin fails to add a student that is not in the lesson to the lesson group
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Add Stu Outlet"
    And a course exists with name "Add Stu Course"
    And the lesson "Add Stu Lesson" is created in course "Add Stu Course" with no room from "2026-03-01T09:00:00Z" to "2026-03-01T10:00:00Z"
    And the student "Bob" exists
    When the user tries to add student "Bob" to the existing lesson group
    Then the request should fail with an error
