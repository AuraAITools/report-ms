@Api
Feature: Lesson change-outlet-room conflict detection

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    And the outlet room "Room A" exists in outlet "Main Branch"
    And the outlet room "Room B" exists in outlet "Main Branch"
    And a course exists with name "Math 101"
    And the lesson "Lesson A" is created in course "Math 101" in room "Room A" from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"
    And the lesson "Lesson B" is created in course "Math 101" in room "Room B" from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"

  @ComponentTest
  Scenario: Moving Lesson B into Room A conflicts with Lesson A
    When the user tries to change the outlet room of lesson "Lesson B" to "Room A"
    Then the request should fail with a room conflict error

  @ComponentTest
  Scenario: Clearing the outlet room of a lesson always succeeds
    When the user clears the outlet room of lesson "Lesson A"
    Then the lesson "Lesson A" should have no outlet room

  @ComponentTest
  Scenario: Moving a lesson into a free room succeeds
    Given the outlet room "Room C" exists in outlet "Main Branch"
    When the user changes the outlet room of lesson "Lesson A" to "Room C"
    Then the lesson "Lesson A" should be in room "Room C"
