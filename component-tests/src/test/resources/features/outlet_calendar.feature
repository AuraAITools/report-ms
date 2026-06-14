@Api
Feature: Outlet Calendar View

  As an institution administrator
  I want to query lessons within an outlet across a date range
  So that the frontend can render a calendar of upcoming sessions

  @ComponentTest
  Scenario: Aura admin retrieves lessons in an outlet within a date range
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    And a course exists with name "Math 101"
    And the lesson "Morning Lesson" is created in course "Math 101" with no room from "2026-09-01T09:00:00Z" to "2026-09-01T10:00:00Z"
    And the lesson "Afternoon Lesson" is created in course "Math 101" with no room from "2026-09-01T13:00:00Z" to "2026-09-01T14:00:00Z"
    And the lesson "Future Lesson" is created in course "Math 101" with no room from "2026-09-15T09:00:00Z" to "2026-09-15T10:00:00Z"
    When the user queries lessons in outlet "Main Branch" from "2026-09-01T00:00:00Z" to "2026-09-02T00:00:00Z"
    Then the calendar response should contain exactly the lessons "Morning Lesson, Afternoon Lesson"

  @ComponentTest
  Scenario: Aura admin does not see cancelled lessons in the calendar
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    And a course exists with name "Math 101"
    And the lesson "Active Lesson" is created in course "Math 101" with no room from "2026-09-01T09:00:00Z" to "2026-09-01T10:00:00Z"
    And the lesson "Cancelled Lesson" is created in course "Math 101" with no room from "2026-09-01T11:00:00Z" to "2026-09-01T12:00:00Z"
    And the lesson "Cancelled Lesson" is cancelled
    When the user queries lessons in outlet "Main Branch" from "2026-09-01T00:00:00Z" to "2026-09-02T00:00:00Z"
    Then the calendar response should contain exactly the lessons "Active Lesson"

  @ComponentTest
  Scenario: Aura admin does not see lessons from other outlets
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Other Branch"
    And an outlet exists with name "Main Branch"
    And a course exists with name "Math 101"
    And the lesson "Main Branch Lesson" is created in course "Math 101" with no room from "2026-09-01T09:00:00Z" to "2026-09-01T10:00:00Z"
    When the user queries lessons in outlet "Other Branch" from "2026-09-01T00:00:00Z" to "2026-09-02T00:00:00Z"
    Then the calendar response should contain no lessons

  @ComponentTest
  Scenario: Aura admin gets a validation error when the date range is inverted
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    When the user tries to query lessons in outlet "Main Branch" from "2026-09-08T00:00:00Z" to "2026-09-01T00:00:00Z"
    Then the request should fail with an "INTERNAL_ERROR" error code

  @ComponentTest
  Scenario: Aura admin gets a validation error when the date range exceeds 92 days
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    When the user tries to query lessons in outlet "Main Branch" from "2026-01-01T00:00:00Z" to "2026-05-15T00:00:00Z"
    Then the request should fail with an "INTERNAL_ERROR" error code

  @ComponentTest
  Scenario: Aura admin querying a calendar with no lessons gets an empty result
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Main Branch"
    When the user queries lessons in outlet "Main Branch" from "2026-09-01T00:00:00Z" to "2026-09-02T00:00:00Z"
    Then the calendar response should contain no lessons
