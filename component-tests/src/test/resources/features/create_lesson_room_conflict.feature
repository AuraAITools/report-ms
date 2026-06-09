@Api
Feature: Lesson room conflict detection on create

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    And the outlet room "Room A" exists in outlet "Main Branch"
    And a course exists with name "Math 101"
    And the lesson "Existing Lesson" is created in course "Math 101" in room "Room A" from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"

  @ComponentTest
  Scenario: A second lesson in the same room overlapping in time is rejected
    When the user tries to create lesson "Conflicting" in course "Math 101" in room "Room A" from "2026-06-10T09:30:00Z" to "2026-06-10T10:30:00Z"
    Then the request should fail with a lesson conflict of type "ROOM"
    And the conflict response should reference lesson "Existing Lesson"

  @ComponentTest
  Scenario: A non-overlapping lesson in the same room succeeds
    When the user creates lesson "Back-to-back" in course "Math 101" in room "Room A" from "2026-06-10T10:00:00Z" to "2026-06-10T11:00:00Z"
    Then the lesson "Back-to-back" should be created successfully

  @ComponentTest
  Scenario: An overlapping lesson in a different room succeeds
    Given the outlet room "Room B" exists in outlet "Main Branch"
    When the user creates lesson "Other Room" in course "Math 101" in room "Room B" from "2026-06-10T09:30:00Z" to "2026-06-10T10:30:00Z"
    Then the lesson "Other Room" should be created successfully
