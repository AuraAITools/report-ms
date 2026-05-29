@Api
Feature: Create Lesson Plan In Lesson

  @ComponentTest @LessonPlanCreate
  Scenario: Educator successfully creates a lesson plan in a lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Plan Outlet"
    And a course exists with name "Plan Course"
    And a lesson exists with name "Plan Lesson"
    And a topic exists with name "Algebra"
    And a topic exists with name "Geometry"
    When the user creates a lesson plan in the lesson with plan "Introduce variables and shapes" and 2 topics
    Then the lesson plan response should have plan "Introduce variables and shapes"
    And the lesson plan response should have 2 topics
    And the lesson plan response createdBy should be the current user
    And the lesson plan response should reference the lesson

  @ComponentTest @LessonPlanCreate
  Scenario: Educator successfully creates a lesson plan in a lesson with nested lesson objectives
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Nested Plan Outlet"
    And a course exists with name "Nested Plan Course"
    And a lesson exists with name "Nested Plan Lesson"
    And a topic exists with name "Algebra"
    And a topic exists with name "Geometry"
    When the user creates a lesson plan in the lesson with plan "Intro plan" and 2 topics and 3 lesson objectives
    Then the lesson plan response should have plan "Intro plan"
    And the lesson plan response should have 2 topics
    And the lesson plan should have 3 lesson objectives

  @ComponentTest @LessonPlanCreate
  Scenario: Educator successfully creates a lesson plan with attached materials
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Mat Plan Outlet"
    And a course exists with name "Mat Plan Course"
    And a lesson exists with name "Mat Plan Lesson"
    And user creates a material in the institution with
      | name        | description     |
      | Worksheet A | A test material |
    When the user creates a lesson plan in the lesson with plan "Plan with material" and 0 topics and 0 students and 1 materials
    Then the lesson plan response should have plan "Plan with material"
    And the lesson plan should have 1 linked materials in the database

  @ComponentTest @LessonPlanCreate
  Scenario: Unauthorized user cannot create a lesson plan
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Auth Plan Outlet"
    And a course exists with name "Auth Plan Course"
    And a lesson exists with name "Auth Plan Lesson"
    And a topic exists with name "Algebra"
    And user "lessonplanviewer@test.com" has "viewer" role on "lesson_user_group" "{lesson_id}"
    And user switches to "lessonplanviewer@test.com" identity
    When the unauthorized user tries to create a lesson plan in the lesson with plan "Unauthorized plan" and 1 topics
    Then the request should fail with an error
