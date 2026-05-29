@Api
Feature: Delete Level By Id

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin fails to delete a non-existent level
    Given user has "aura-admin" role
    And an institution is onboarded
    When user tries to delete a level with a random id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
