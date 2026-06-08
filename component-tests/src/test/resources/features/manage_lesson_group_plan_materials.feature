@Api
Feature: Manage Materials On Lesson Group Plan

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
    And the user creates a lesson group plan in the lesson group with plan "Plan to manage materials" and 0 topics
    When the user adds the material to the lesson group plan
    Then the lesson group plan should have 1 linked materials in the database
    When the user removes the material from the lesson group plan
    Then the lesson group plan should have 0 linked materials in the database
