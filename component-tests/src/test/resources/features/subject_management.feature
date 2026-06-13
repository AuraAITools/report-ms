@Api
Feature: Subject catalog management
  As an institution admin
  I want to maintain the institution's subject catalog and its topic links
  So that courses and lesson plans can reference a consistent curriculum

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded

  @ComponentTest
  Scenario: Aura admin creates a new subject
    When user creates a subject with name "Mathematics"
    Then the subject "Mathematics" should be created successfully

  @ComponentTest
  Scenario: Aura admin links a topic to a subject
    Given a topic exists with name "Algebra"
    And a subject exists with name "Mathematics"
    When user links the topic to the subject
    Then the subject should have the topic linked

  @ComponentTest
  Scenario: Aura admin retrieves a subject with its linked topics
    Given a subject exists with name "Mathematics"
    And a topic exists with name "Algebra"
    When user links the topic to the subject
    And user queries subject by id
    Then the subject should be returned with name "Mathematics"
    And the subject should have 1 topic

  @ComponentTest
  Scenario: Aura admin retrieves a subject without topics
    Given a subject exists with name "Mathematics"
    When user queries subject by id
    Then the subject should be returned with name "Mathematics"
    And the subject should have 0 topics

  @ComponentTest
  Scenario: Aura admin retrieves all subjects in an institution
    Given a subject exists with name "Mathematics"
    And a subject exists with name "Science"
    When user queries all subjects in the institution
    Then the response should contain 2 subjects
    And the subjects should include names "Mathematics,Science"

  @ComponentTest
  Scenario: Aura admin retrieves an empty list when institution has no subjects
    When user queries all subjects in the institution
    Then the response should contain 0 subjects

  # Authorization is evaluated against the resource id before any lookup, and deletes remove the
  # resource's SpiceDB relationships — so deleted and never-existing resources are both FORBIDDEN
  # (no id enumeration).

  @ComponentTest
  Scenario: Aura admin deletes an existing subject
    Given a subject exists with name "Mathematics"
    When user deletes the subject by id
    Then the subject should be deleted successfully
    And querying the subject by id should fail with error code "FORBIDDEN"

  @ComponentTest
  Scenario: Aura admin fails to retrieve a non-existent subject
    When user queries subject with non-existent id
    # Authorization is evaluated against the resource id before any lookup, so a non-existent
    # subject is indistinguishable from a forbidden one (no id enumeration)
    Then the request should fail with error code "FORBIDDEN"

  @ComponentTest
  Scenario: Aura admin fails to delete a non-existent subject
    When user tries to delete a subject with a random id
    Then the request should fail with error code "FORBIDDEN"
