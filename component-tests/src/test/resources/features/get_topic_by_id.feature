@Api
Feature: Get Topic By Id

  @ComponentTest
  Scenario: Aura admin retrieves a topic by id
    Given user has "aura-admin" role
    And an institution is onboarded
    And a topic exists with name "Algebra"
    When user queries topic by id
    Then the topic should be returned with name "Algebra"

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin fails to retrieve a non-existent topic
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries topic with non-existent id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
