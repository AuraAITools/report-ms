@Api
Feature: Parent management in an institution

  An institution admin manages parent profiles attached to family accounts:
  listing every parent in the institution, inspecting a parent's profile,
  amending the relationship type when a guardian arrangement changes, and
  removing a parent profile that no longer applies.

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an account exists in the institution for "parent.one@test.com" with details:
      | firstName | lastName | contact  |
      | Jane      | Doe      | 91234567 |
    And user creates a parent in the account with relationship "PARENT"

  @ComponentTest
  Scenario: Institution admin lists parents after attaching one
    When user queries all parents in the institution
    Then the parents response should contain 1 parent

  @ComponentTest
  Scenario: Institution admin retrieves a created parent by id
    When user queries the noted parent by id
    Then the parent response should have relationship "PARENT"

  @ComponentTest
  Scenario: Institution admin updates a parent's relationship type
    When user updates the noted parent with relationship "SELF"
    Then the parent response should have relationship "SELF"

  @ComponentTest
  Scenario: Institution admin deletes a parent and the roster shrinks
    When user queries all parents in the institution
    Then the parents response should contain 1 parent
    When user deletes the noted parent by id
    Then the delete parent response should be true
    When user queries all parents in the institution
    Then the parents response should contain 0 parents
    And querying the deleted parent by id should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  Scenario: Institution admin fails to retrieve a non-existent parent
    When user queries parent by a random id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  Scenario: Institution admin fails to update a non-existent parent
    When user tries to update a parent with a random id and relationship "SELF"
    Then the request should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  Scenario: Institution admin fails to delete a non-existent parent
    When user tries to delete a parent with a random id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
