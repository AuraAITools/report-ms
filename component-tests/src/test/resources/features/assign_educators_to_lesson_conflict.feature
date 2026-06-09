@Api
Feature: Lesson assign-educators conflict detection

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    And a course exists with name "Math 101"
    And the educator "Mr. Smith" exists
    And the lesson "Lesson 1" is created in course "Math 101" with no room from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"
    And the lesson "Lesson 2" is created in course "Math 101" with no room from "2026-06-10T09:30:00Z" to "2026-06-10T10:30:00Z"
    And the educator "Mr. Smith" is assigned to lesson "Lesson 1"

  @ComponentTest
  Scenario: Assigning Mr. Smith to Lesson 2 conflicts with Lesson 1
    When the user tries to assign educator "Mr. Smith" to lesson "Lesson 2"
    Then the request should fail with a lesson conflict of type "EDUCATOR"
    And the conflict response should reference lesson "Lesson 1"

  @ComponentTest
  Scenario: Assigning Mr. Smith to a non-overlapping lesson succeeds
    Given the lesson "Lesson 3" is created in course "Math 101" with no room from "2026-06-10T11:00:00Z" to "2026-06-10T12:00:00Z"
    When the user assigns educator "Mr. Smith" to lesson "Lesson 3"
    Then the lesson "Lesson 3" should have educator "Mr. Smith" assigned
