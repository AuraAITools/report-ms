@Api
Feature: Topic catalog management
  As an institution admin
  I want to maintain the institution's topic catalog
  So that subjects, materials and lesson plans can reference shared topics

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded

  @ComponentTest
  Scenario: Aura admin creates a new topic
    When user creates a topic with name "Test Topic"
    Then the topic "Test Topic" should be created successfully

  @ComponentTest
  Scenario: Aura admin retrieves a topic by id
    Given a topic exists with name "Algebra"
    When user queries topic by id
    Then the topic should be returned with name "Algebra"

  @ComponentTest
  Scenario: Aura admin retrieves all topics in an institution
    Given a topic exists with name "Algebra"
    And a topic exists with name "Geometry"
    When user queries all topics in the institution
    Then the response should contain 2 topics
    And the topics should include names "Algebra,Geometry"

  @ComponentTest
  Scenario: Aura admin retrieves an empty list when institution has no topics
    When user queries all topics in the institution
    Then the response should contain 0 topics

  # Authorization is now evaluated at the institution level — a non-existent or deleted topic
  # passes the institution-level permission check and then fails with RESOURCE_NOT_FOUND at the
  # repository.

  @ComponentTest
  Scenario: Aura admin deletes an existing topic
    Given a topic exists with name "Algebra"
    When user deletes the topic by id
    Then the topic should be deleted successfully
    And querying the topic by id should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  Scenario: Aura admin fails to retrieve a non-existent topic
    When user queries topic with non-existent id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  Scenario: Aura admin fails to delete a non-existent topic
    When user tries to delete a topic with a random id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
