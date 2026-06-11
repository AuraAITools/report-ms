@Api
Feature: Create Course

  @ComponentTest
  Scenario: Aura admin successfully creates a course in an outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Yishun Outlet"
    When user creates a course with details:
      | name        | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Test Course | 20      | WEEKLY          | 200.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-12-31T09:00:00Z |
    Then the course should be created successfully

  @ComponentTest
  Scenario: Aura admin creates a course with subjects, educators, and students
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    And a subject exists with name "Mathematics"
    And the user switches to the institution tenant
    And user creates an account with details:
      | email                | firstName | lastName | contact  |
      | educator@example.com | Educator  | Admin    | 12345678 |
    And an educator exists with name "John Teacher" and email "john.teacher@example.com"
    And user creates an account with details:
      | email              | firstName | lastName | contact  |
      | parent@example.com | Parent    | User     | 87654321 |
    And user creates a parent in the account with relationship "PARENT"
    And a student exists with name "Jane Student" and email "jane.student@example.com"
    When user creates a course with associations and details:
      | name     | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Math 101 | 25      | WEEKLY          | 300.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-12-31T09:00:00Z |
    Then the course should be created successfully
