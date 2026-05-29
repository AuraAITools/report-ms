@Api
Feature: Delete Topic By Id

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin deletes an existing topic
    Given user has "aura-admin" role
    And an institution is onboarded
    And a topic exists with name "Algebra"
    When user deletes the topic by id
    Then the topic should be deleted successfully
    And querying the topic by id should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin fails to delete a non-existent topic
    Given user has "aura-admin" role
    And an institution is onboarded
    When user tries to delete a topic with a random id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
