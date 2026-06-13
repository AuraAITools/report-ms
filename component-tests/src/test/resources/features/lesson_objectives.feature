@Api
Feature: Lesson Objectives

  As an educator
  I want to plan and revise the learning objectives that hang off a lesson group plan
  So that every group of students has a clear set of teachable outcomes for the lesson

  @ComponentTest @LessonObjectiveCrud
  Scenario: Educator creates, reads, updates, lists, then deletes a lesson objective
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Obj Outlet"
    And a course exists with name "Obj Course"
    And a topic exists with name "Algebra"
    And a lesson exists with name "Obj Lesson" linked to the existing topics
    When the user creates a lesson objective with name "Understand fractions" and objective "Add and subtract fractions" with 1 topics
    Then the lesson objective response should have name "Understand fractions" and objective "Add and subtract fractions"
    And the lesson objective response createdBy should be the current user
    When the user gets the lesson objective by id
    Then the lesson objective response should have name "Understand fractions" and objective "Add and subtract fractions"
    When the user updates the lesson objective with name "Master fractions" and objective "Multiply fractions" with 1 topics
    Then the lesson objective response should have name "Master fractions" and objective "Multiply fractions"
    When the user lists lesson objectives by lesson group plan id
    Then the lesson objective list should have 1 entries
    When the user deletes the lesson objective
    Then the lesson objective should be gone from the database

  @ComponentTest @LessonObjectiveCrud
  Scenario: Updating a non-existent lesson objective fails with RESOURCE_NOT_FOUND
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Obj404 Outlet"
    And a course exists with name "Obj404 Course"
    And a lesson exists with name "Obj404 Lesson"
    When the user tries to update a non-existent lesson objective
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
