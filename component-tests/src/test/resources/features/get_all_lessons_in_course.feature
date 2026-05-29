@Api
Feature: Get All Lessons In Course

  @ComponentTest
  Scenario: Aura admin retrieves all lessons in a course
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    And a course exists with name "Math 101"
    And a lesson exists with name "Lesson 1"
    And a lesson exists with name "Lesson 2"
    When user queries all lessons in the course
    Then the response should contain 2 lessons
    And the lessons should include names "Lesson 1,Lesson 2"

  @ComponentTest
  Scenario: Aura admin retrieves an empty list when course has no lessons
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    And a course exists with name "Empty Course"
    When user queries all lessons in the course
    Then the response should contain 0 lessons
