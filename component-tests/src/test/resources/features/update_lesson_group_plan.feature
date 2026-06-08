@Api
Feature: Update Lesson Group Plan

  @ComponentTest @LessonGroupPlanUpdate
  Scenario: Educator successfully updates a lesson group plan's fields
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Update Plan Outlet"
    And a course exists with name "Update Plan Course"
    And a lesson exists with name "Update Plan Lesson"
    And the user creates a lesson group plan in the lesson group with plan "Original plan" and 0 topics
    When the user updates the lesson group plan with plan "Revised plan" and recap "Recap text" and state "PLANNED"
    Then the updated lesson group plan response should have plan "Revised plan"
    And the updated lesson group plan response should have recap "Recap text"
    And the updated lesson group plan response should have state "PLANNED"
