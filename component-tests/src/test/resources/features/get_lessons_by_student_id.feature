@Api
Feature: Get Lessons By Student Id

  @ComponentTest
  Scenario: Aura admin retrieves empty lessons for a student with no enrolments
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries lessons by a random student id
    Then the response should contain 0 lessons
