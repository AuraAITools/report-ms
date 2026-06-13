@Api
Feature: Level management
  As an institution admin
  I want to manage the institution's academic levels
  So that students and courses can be organised by level

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded

  @ComponentTest
  Scenario: Aura admin creates a level in the institution
    When user creates a level in the institution with name "P1" and category "PRIMARY"
    Then the level should be created successfully with name "P1" and category "PRIMARY"

  @ComponentTest
  Scenario: Aura admin retrieves levels by page
    When user queries levels by page
    Then the levels query should respond successfully

  @ComponentTest
  Scenario: Aura admin lists levels in the institution
    Given a level exists with name "P1" and category "PRIMARY"
    And a level exists with name "P2" and category "PRIMARY"
    When user lists levels in the institution
    Then the level list should have 2 entries
    And the level list should contain the level "P1"
    And the level list should contain the level "P2"

  @ComponentTest
  Scenario: Aura admin retrieves a level by id in the institution
    Given a level exists with name "P1" and category "PRIMARY"
    When user retrieves the level by its id
    Then the level response should have name "P1" and category "PRIMARY"

  @ComponentTest
  Scenario: Aura admin previews delete impact for a level with no linked resources
    Given a level exists with name "P1" and category "PRIMARY"
    When user previews delete impact for the level
    Then the level delete impact resource type should be "Level"
    And the level delete impact total affected count should be 0

  # Authorization is now evaluated at the institution level — a non-existent level passes the
  # institution-level permission check and then fails with RESOURCE_NOT_FOUND at the repository.

  @ComponentTest
  Scenario: Aura admin fails to delete a non-existent level
    When user tries to delete a level with a random id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  Scenario: Aura admin fails to preview delete impact for a non-existent level
    When user tries to preview delete impact for a level with a random id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
