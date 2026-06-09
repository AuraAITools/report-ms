@Api
Feature: Lesson assign-students conflict detection

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    And a course exists with name "Math 101"
    And the student "Alice" exists
    And the lesson "Lesson 1" is created in course "Math 101" with no room from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"
    And the lesson "Lesson 2" is created in course "Math 101" with no room from "2026-06-10T09:30:00Z" to "2026-06-10T10:30:00Z"
    And the student "Alice" is assigned to lesson "Lesson 1"

  @ComponentTest
  Scenario: Assigning Alice to Lesson 2 conflicts with Lesson 1
    When the user tries to assign student "Alice" to lesson "Lesson 2"
    Then the request should fail with a room conflict error

  @ComponentTest
  Scenario: Assigning Alice to a non-overlapping lesson succeeds
    Given the lesson "Lesson 3" is created in course "Math 101" with no room from "2026-06-10T11:00:00Z" to "2026-06-10T12:00:00Z"
    When the user assigns student "Alice" to lesson "Lesson 3"
    Then the lesson "Lesson 3" should have student "Alice" assigned
