@Api
Feature: Get Subject By Id

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin retrieves a subject with its linked topics
    Given user has "aura-admin" role
    And an institution is onboarded
    And a subject exists with name "Mathematics"
    And a topic exists with name "Algebra"
    When user links the topic to the subject
    And user queries subject by id
    Then the subject should be returned with name "Mathematics"
    And the subject should have 1 topic

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin retrieves a subject without topics
    Given user has "aura-admin" role
    And an institution is onboarded
    And a subject exists with name "Mathematics"
    When user queries subject by id
    Then the subject should be returned with name "Mathematics"
    And the subject should have 0 topics

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin fails to retrieve a non-existent subject
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries subject with non-existent id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
