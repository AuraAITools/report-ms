@Api
Feature: Get Courses By Student Id

  @ComponentTest
  Scenario: Aura admin retrieves empty courses for a student with no enrolments
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries courses by a random student id
    Then the response should contain 0 courses
