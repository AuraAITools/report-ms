@Api
Feature: Delete Course By Id

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin deletes a course and cascades child lessons
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Branch"
    And a course exists with name "Algebra 101"
    And a lesson exists with name "Intro Lesson"
    When user deletes the course by id
    Then the course should be deleted successfully
    And querying the course by id should fail with error code "RESOURCE_NOT_FOUND"
    And querying the lesson by id should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin fails to delete a non-existent course
    Given user has "aura-admin" role
    And an institution is onboarded
    When user tries to delete a course with a random id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
