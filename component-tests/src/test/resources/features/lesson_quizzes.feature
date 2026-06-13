@Api
Feature: Lesson Quizzes

  As an educator
  I want to record, review, update, and remove quiz results on a lesson
  So that student performance in each lesson is tracked accurately

  @ComponentTest @LessonQuizCrud
  Scenario: Educator creates, reads, updates, then deletes a lesson quiz
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Quiz Outlet"
    And a course exists with name "Quiz Course"
    And a lesson exists with name "Quiz Lesson"
    When the user creates a lesson quiz with score 70 and maxScore 100
    Then the lesson quiz response should have score 70 and maxScore 100
    And the lesson quiz response createdBy should be the current user
    When the user gets the lesson quiz by id
    Then the lesson quiz response should have score 70 and maxScore 100
    When the user updates the lesson quiz with score 90 and maxScore 100
    Then the lesson quiz response should have score 90 and maxScore 100
    When the user lists lesson quizzes by lesson group plan id
    Then the lesson quiz list should have 1 entries
    When the user deletes the lesson quiz
    Then the lesson quiz should be gone from the database

  @ComponentTest @LessonQuizCrud
  Scenario: Updating a non-existent lesson quiz fails
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Quiz404 Outlet"
    And a course exists with name "Quiz404 Course"
    And a lesson exists with name "Quiz404 Lesson"
    When the user tries to update a non-existent lesson quiz
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
