@Api
Feature: School management
  As an institution admin
  I want to maintain the directory of schools my students attend
  So that students can be associated with their external schools

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded

  @ComponentTest
  Scenario: Institution admin creates a school in the institution
    When user creates a school in the institution with name "Raffles Primary" and category "PRIMARY"
    Then the school should be created successfully with name "Raffles Primary" and category "PRIMARY"

  @ComponentTest
  Scenario: Institution admin lists schools in the institution
    Given a school exists with name "Raffles Primary" and category "PRIMARY"
    And a school exists with name "Hwa Chong Institution" and category "SECONDARY"
    When user lists schools in the institution
    Then the school list should have 2 entries
    And the school list should contain the school "Raffles Primary"
    And the school list should contain the school "Hwa Chong Institution"

  @ComponentTest
  Scenario: Institution admin previews delete impact for a school with no linked students
    Given a school exists with name "Raffles Primary" and category "PRIMARY"
    When user previews delete impact for the school
    Then the school delete impact resource type should be "School"
    And the school delete impact total affected count should be 0
    And the school delete impact should include a "NULL_OUT" group for "Student" with 0 affected ids

  @ComponentTest
  Scenario: Institution admin deletes a school in the institution
    Given a school exists with name "Raffles Primary" and category "PRIMARY"
    When user deletes the school in the institution
    Then the school delete should succeed
    And listing schools in the institution should return 0 entries

  # Authorization for school operations is evaluated against the institution id, then the school is
  # looked up by id — so a non-existent school surfaces as RESOURCE_NOT_FOUND.

  @ComponentTest
  Scenario: Institution admin fails to delete a non-existent school
    When user tries to delete a school with a random id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
