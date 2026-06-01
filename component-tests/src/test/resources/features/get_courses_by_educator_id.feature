@Api
Feature: Get Courses By Educator Id

  @ComponentTest
  Scenario: Aura admin retrieves empty courses for an educator with no enrolments
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries courses by a random educator id
    Then the response should contain 0 courses
