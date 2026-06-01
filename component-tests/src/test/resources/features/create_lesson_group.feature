@Api
Feature: Create Lesson Group In Lesson

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
