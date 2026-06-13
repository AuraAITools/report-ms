@Api
Feature: Educator management in an institution

  An institution admin manages the educator roster: listing teaching staff,
  inspecting an educator's profile, amending employment details when they
  change, and removing an educator who has left the institution. Removing an
  educator also tears down their course, lesson, level, outlet, subject and
  account links so the roster stays consistent.

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And an account exists in the institution for "edu.one@test.com" with details:
      | firstName | lastName | contact  |
      | Edu       | One      | 91234567 |
    And an educator exists with name "Edu One" and email "edu.one@test.com"

  @ComponentTest
  Scenario: Institution admin lists educators after creating one
    When user queries all educators in the institution
    Then the educators response should contain 1 educator

  @ComponentTest
  Scenario: Institution admin retrieves a created educator by id
    When user queries the noted educator by id
    Then the educator response should contain name "Edu One" and email "edu.one@test.com"
    And the educator response should have employment type "FULL_TIME"

  @ComponentTest
  Scenario: Institution admin updates an educator's name and employment type
    When user updates the noted educator with details:
      | name        | employmentType |
      | Edu Renamed | PART_TIME      |
    Then the educator response should contain name "Edu Renamed" and email "edu.one@test.com"
    And the educator response should have employment type "PART_TIME"

  @ComponentTest
  Scenario: Institution admin deletes an educator and the roster shrinks
    When user queries all educators in the institution
    Then the educators response should contain 1 educator
    When user deletes the noted educator by id
    Then the delete educator response should be true
    When user queries all educators in the institution
    Then the educators response should contain 0 educators
    And querying the deleted educator by id should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  Scenario: Institution admin fails to retrieve a non-existent educator
    When user queries educator by a random id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  Scenario: Institution admin fails to update a non-existent educator
    When user tries to update an educator with a random id and name "Ghost Educator"
    Then the request should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  Scenario: Institution admin fails to delete a non-existent educator
    When user tries to delete an educator with a random id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
