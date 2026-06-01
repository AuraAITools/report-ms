@Api
Feature: Get Lessons By Educator Id

  @ComponentTest
  Scenario: Aura admin retrieves empty lessons for an educator with no enrolments
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries lessons by a random educator id
    Then the response should contain 0 lessons
