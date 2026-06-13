@Api
Feature: Lesson Scheduling Conflict Detection

  As an institution administrator
  I want overlapping lessons to be rejected when they share a room, educator, or student
  So that the schedule never double-books a resource or a person

  # --- Room conflicts on lesson creation ---

  @ComponentTest
  Scenario: Aura admin fails to create a second lesson overlapping in the same room
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    And the outlet room "Room A" exists in outlet "Main Branch"
    And a course exists with name "Math 101"
    And the lesson "Existing Lesson" is created in course "Math 101" in room "Room A" from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"
    When the user tries to create lesson "Conflicting" in course "Math 101" in room "Room A" from "2026-06-10T09:30:00Z" to "2026-06-10T10:30:00Z"
    Then the request should fail with a lesson conflict of type "ROOM"
    And the conflict response should reference lesson "Existing Lesson"

  @ComponentTest
  Scenario: Aura admin creates a non-overlapping lesson in the same room
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    And the outlet room "Room A" exists in outlet "Main Branch"
    And a course exists with name "Math 101"
    And the lesson "Existing Lesson" is created in course "Math 101" in room "Room A" from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"
    When the user creates lesson "Back-to-back" in course "Math 101" in room "Room A" from "2026-06-10T10:00:00Z" to "2026-06-10T11:00:00Z"
    Then the lesson "Back-to-back" should be created successfully

  @ComponentTest
  Scenario: Aura admin creates an overlapping lesson in a different room
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    And the outlet room "Room A" exists in outlet "Main Branch"
    And a course exists with name "Math 101"
    And the lesson "Existing Lesson" is created in course "Math 101" in room "Room A" from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"
    Given the outlet room "Room B" exists in outlet "Main Branch"
    When the user creates lesson "Other Room" in course "Math 101" in room "Room B" from "2026-06-10T09:30:00Z" to "2026-06-10T10:30:00Z"
    Then the lesson "Other Room" should be created successfully

  # --- Room conflicts when changing a lesson's outlet room ---

  @ComponentTest
  Scenario: Aura admin fails to move a lesson into a room occupied by an overlapping lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    And the outlet room "Room A" exists in outlet "Main Branch"
    And the outlet room "Room B" exists in outlet "Main Branch"
    And a course exists with name "Math 101"
    And the lesson "Lesson A" is created in course "Math 101" in room "Room A" from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"
    And the lesson "Lesson B" is created in course "Math 101" in room "Room B" from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"
    When the user tries to change the outlet room of lesson "Lesson B" to "Room A"
    Then the request should fail with a lesson conflict of type "ROOM"
    And the conflict response should reference lesson "Lesson A"

  @ComponentTest
  Scenario: Aura admin clears the outlet room of a lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    And the outlet room "Room A" exists in outlet "Main Branch"
    And the outlet room "Room B" exists in outlet "Main Branch"
    And a course exists with name "Math 101"
    And the lesson "Lesson A" is created in course "Math 101" in room "Room A" from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"
    And the lesson "Lesson B" is created in course "Math 101" in room "Room B" from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"
    When the user clears the outlet room of lesson "Lesson A"
    Then the lesson "Lesson A" should have no outlet room

  @ComponentTest
  Scenario: Aura admin moves a lesson into a free room
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    And the outlet room "Room A" exists in outlet "Main Branch"
    And the outlet room "Room B" exists in outlet "Main Branch"
    And a course exists with name "Math 101"
    And the lesson "Lesson A" is created in course "Math 101" in room "Room A" from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"
    And the lesson "Lesson B" is created in course "Math 101" in room "Room B" from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"
    Given the outlet room "Room C" exists in outlet "Main Branch"
    When the user changes the outlet room of lesson "Lesson A" to "Room C"
    Then the lesson "Lesson A" should be in room "Room C"

  # --- Educator conflicts when assigning educators to a lesson ---

  @ComponentTest
  Scenario: Aura admin fails to assign an educator to an overlapping lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    And a course exists with name "Math 101"
    And the educator "Mr. Smith" exists
    And the lesson "Lesson 1" is created in course "Math 101" with no room from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"
    And the lesson "Lesson 2" is created in course "Math 101" with no room from "2026-06-10T09:30:00Z" to "2026-06-10T10:30:00Z"
    And the educator "Mr. Smith" is assigned to lesson "Lesson 1"
    When the user tries to assign educator "Mr. Smith" to lesson "Lesson 2"
    Then the request should fail with a lesson conflict of type "EDUCATOR"
    And the conflict response should reference lesson "Lesson 1"

  @ComponentTest
  Scenario: Aura admin assigns an educator to a non-overlapping lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    And a course exists with name "Math 101"
    And the educator "Mr. Smith" exists
    And the lesson "Lesson 1" is created in course "Math 101" with no room from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"
    And the lesson "Lesson 2" is created in course "Math 101" with no room from "2026-06-10T09:30:00Z" to "2026-06-10T10:30:00Z"
    And the educator "Mr. Smith" is assigned to lesson "Lesson 1"
    Given the lesson "Lesson 3" is created in course "Math 101" with no room from "2026-06-10T11:00:00Z" to "2026-06-10T12:00:00Z"
    When the user assigns educator "Mr. Smith" to lesson "Lesson 3"
    Then the lesson "Lesson 3" should have educator "Mr. Smith" assigned

  # --- Student conflicts when assigning students to a lesson ---

  @ComponentTest
  Scenario: Aura admin fails to assign a student to an overlapping lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    And a course exists with name "Math 101"
    And the student "Alice" exists
    And the lesson "Lesson 1" is created in course "Math 101" with no room from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"
    And the lesson "Lesson 2" is created in course "Math 101" with no room from "2026-06-10T09:30:00Z" to "2026-06-10T10:30:00Z"
    And the student "Alice" is assigned to lesson "Lesson 1"
    When the user tries to assign student "Alice" to lesson "Lesson 2"
    Then the request should fail with a lesson conflict of type "STUDENT"
    And the conflict response should reference lesson "Lesson 1"

  @ComponentTest
  Scenario: Aura admin assigns a student to a non-overlapping lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    And a course exists with name "Math 101"
    And the student "Alice" exists
    And the lesson "Lesson 1" is created in course "Math 101" with no room from "2026-06-10T09:00:00Z" to "2026-06-10T10:00:00Z"
    And the lesson "Lesson 2" is created in course "Math 101" with no room from "2026-06-10T09:30:00Z" to "2026-06-10T10:30:00Z"
    And the student "Alice" is assigned to lesson "Lesson 1"
    Given the lesson "Lesson 3" is created in course "Math 101" with no room from "2026-06-10T11:00:00Z" to "2026-06-10T12:00:00Z"
    When the user assigns student "Alice" to lesson "Lesson 3"
    Then the lesson "Lesson 3" should have student "Alice" assigned
