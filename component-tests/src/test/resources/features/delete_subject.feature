@Api
Feature: Delete Subject By Id

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin deletes an existing subject
    Given user has "aura-admin" role
    And an institution is onboarded
    And a subject exists with name "Mathematics"
    When user deletes the subject by id
    Then the subject should be deleted successfully
    And querying the subject by id should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin fails to delete a non-existent subject
    Given user has "aura-admin" role
    And an institution is onboarded
    When user tries to delete a subject with a random id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
