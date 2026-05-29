@Api
Feature: Delete Lesson By Id

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin deletes an existing lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Branch"
    And a course exists with name "Algebra 101"
    And a lesson exists with name "Intro Lesson"
    When user deletes the lesson by id
    Then the lesson should be deleted successfully
    And querying the lesson by id should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin fails to delete a non-existent lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    When user tries to delete a lesson with a random id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
