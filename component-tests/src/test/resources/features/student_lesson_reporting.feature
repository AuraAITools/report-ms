@Api
Feature: Student Lesson Reporting

  As an educator
  I want to record each student's homework completion, participation, and topic proficiencies after a lesson
  So that the institution tracks individual student outcomes across every lesson taught

  @ComponentTest @StudentLessonReportLifecycle
  Scenario: Educator creates an empty report for a student assigned after lesson creation
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Report Outlet"
    And a course exists with name "Report Course"
    And the lesson "Report Lesson" is created in course "Report Course" with no room from "2026-03-01T09:00:00Z" to "2026-03-01T10:00:00Z"
    And the student "Alice" exists
    And the student "Alice" is assigned to lesson "Report Lesson"
    When the user creates a student lesson report for student "Alice" on lesson "Report Lesson"
    Then the student lesson report response should have an auto-created homework completion with completion null
    And the student lesson report response should have an auto-created participation review with type "UNFILLED"
    And the student lesson report response should have 0 auto-created topic proficiencies
    And the student lesson report response createdBy should be the current user

  @ComponentTest @StudentLessonReportLifecycle
  Scenario: Educator gets a student lesson report by id
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Get Report Outlet"
    And a course exists with name "Get Report Course"
    And the lesson "Get Report Lesson" is created in course "Get Report Course" with no room from "2026-03-01T09:00:00Z" to "2026-03-01T10:00:00Z"
    And the student "Alice" exists
    And the student "Alice" is assigned to lesson "Get Report Lesson"
    And the user creates a student lesson report for student "Alice" on lesson "Get Report Lesson"
    When the user gets the student lesson report by id
    Then the student lesson report response should have an auto-created participation review with type "UNFILLED"

  @ComponentTest @StudentLessonReportLifecycle
  Scenario: Educator fills in homework, participation, and proficiencies via updateStudentLessonReport
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Update Report Outlet"
    And a course exists with name "Update Report Course"
    And a topic exists with name "Geometry"
    And the lesson "Update Report Lesson" is created in course "Update Report Course" with no room from "2026-03-01T09:00:00Z" to "2026-03-01T10:00:00Z"
    And the student "Bob" exists
    And the student "Bob" is assigned to lesson "Update Report Lesson"
    And the user creates a student lesson report for student "Bob" on lesson "Update Report Lesson"
    When the user updates the student lesson report with homework 80 and participation "ATTENTIVE"
    Then the student lesson report response should have homework completion 80
    And the student lesson report response should have participation type "ATTENTIVE"

  @ComponentTest @StudentLessonReportLifecycle
  Scenario: Educator lists student lesson reports under a lesson group
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Seeded Outlet"
    And a course exists with name "Seeded Course"
    And the lesson "Seeded Lesson" is created in course "Seeded Course" with no room from "2026-03-01T09:00:00Z" to "2026-03-01T10:00:00Z"
    And the student "Carol" exists
    And the student "Carol" is assigned to lesson "Seeded Lesson"
    And the student "Carol" is added to the existing lesson group
    And the user creates a student lesson report for student "Carol" on lesson "Seeded Lesson"
    When the user lists student lesson reports by lesson group
    Then the student lesson report list should have 1 entries

  @ComponentTest @StudentLessonReportChildren
  Scenario: Educator updates auto-created homework completion directly via createLessonHomeworkCompletion flow
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Homework Outlet"
    And a course exists with name "Homework Course"
    And the lesson "Homework Lesson" is created in course "Homework Course" with no room from "2026-03-01T09:00:00Z" to "2026-03-01T10:00:00Z"
    And the student "Dan" exists
    And the student "Dan" is assigned to lesson "Homework Lesson"
    And the user creates a student lesson report for student "Dan" on lesson "Homework Lesson"
    When the user updates the homework completion with completion 90
    Then the homework completion response should have completion 90
    When the user gets the homework completion by id
    Then the homework completion response should have completion 90
    When the user deletes the homework completion
    And the user creates a homework completion with completion 55
    Then the homework completion response should have completion 55

  @ComponentTest @StudentLessonReportChildren
  Scenario: Educator updates and re-creates participation review directly
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Participation Outlet"
    And a course exists with name "Participation Course"
    And the lesson "Participation Lesson" is created in course "Participation Course" with no room from "2026-03-01T09:00:00Z" to "2026-03-01T10:00:00Z"
    And the student "Eve" exists
    And the student "Eve" is assigned to lesson "Participation Lesson"
    And the user creates a student lesson report for student "Eve" on lesson "Participation Lesson"
    When the user updates the participation review with type "CURIOUS"
    Then the participation review response should have type "CURIOUS"
    When the user gets the participation review by id
    Then the participation review response should have type "CURIOUS"
    When the user deletes the participation review
    And the user creates a participation review with type "QUIET"
    Then the participation review response should have type "QUIET"

  @ComponentTest @StudentLessonReportChildren
  Scenario: Educator adds, reads, and deletes an extra topic proficiency on a report
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Proficiency Outlet"
    And a course exists with name "Proficiency Course"
    And a topic exists with name "Calculus"
    And the lesson "Proficiency Lesson" is created in course "Proficiency Course" with no room from "2026-03-01T09:00:00Z" to "2026-03-01T10:00:00Z"
    And the student "Frank" exists
    And the student "Frank" is assigned to lesson "Proficiency Lesson"
    And the user creates a student lesson report for student "Frank" on lesson "Proficiency Lesson"
    When the user creates a lesson topic proficiency with rating 4
    Then the topic proficiency response should have rating 4
    When the user updates the topic proficiency with rating 5
    Then the topic proficiency response should have rating 5
    When the user gets the topic proficiency by id
    Then the topic proficiency response should have rating 5
    When the user deletes the topic proficiency

  @ComponentTest @StudentLessonReportLifecycle
  Scenario: Educator deletes a student lesson report
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Delete Report Outlet"
    And a course exists with name "Delete Report Course"
    And the lesson "Delete Report Lesson" is created in course "Delete Report Course" with no room from "2026-03-01T09:00:00Z" to "2026-03-01T10:00:00Z"
    And the student "Gina" exists
    And the student "Gina" is assigned to lesson "Delete Report Lesson"
    And the user creates a student lesson report for student "Gina" on lesson "Delete Report Lesson"
    When the user deletes the student lesson report
    Then the student lesson report should be gone from the database

  @ComponentTest @StudentLessonReportLifecycle
  Scenario: Creating a student lesson report on a non-existent lesson is forbidden
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    When the user tries to create a student lesson report on a non-existent lesson
    Then the request should fail with http status 403
