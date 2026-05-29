@Api
Feature: Get All Courses In Outlet

  @ComponentTest
  Scenario: Aura admin retrieves all courses in an outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    And a course exists with name "Math 101"
    And a course exists with name "Science 101"
    When user queries all courses in the outlet
    Then the response should contain 2 courses
    And the courses should include names "Math 101,Science 101"

  @ComponentTest
  Scenario: Aura admin retrieves an empty list when the outlet has no courses
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Empty Outlet"
    When user queries all courses in the outlet
    Then the response should contain 0 courses
