@Api
Feature: Add course admin delegation

  @ComponentTest
  Scenario: Course admin delegates another user as course admin
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user creates a course with details:
      | name     | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Math 101 | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-06-01T09:00:00Z |
    And user "courseadmin@test.com" has "admin" role on "course" "{course_id}"
    And user switches to "courseadmin@test.com" identity
    And there is a user in "none" group with details:
      | email             | firstName | lastName | contact  |
      | newadmin@test.com | Jane      | Smith    | 91234567 |
    When course admin adds user "newadmin@test.com" as course admin
    Then the user "newadmin@test.com" should have "admin" role on "course" "{course_id}"

  @ComponentTest
  Scenario: Outlet admin delegates a user as course admin
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user creates a course with details:
      | name     | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Math 101 | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-06-01T09:00:00Z |
    And user "outletadmin@test.com" has "admin" role on "outlet" "{outlet_id}"
    And user switches to "outletadmin@test.com" identity
    And there is a user in "none" group with details:
      | email             | firstName | lastName | contact  |
      | newadmin@test.com | Jane      | Smith    | 91234567 |
    When course admin adds user "newadmin@test.com" as course admin
    Then the user "newadmin@test.com" should have "admin" role on "course" "{course_id}"

  @ComponentTest
  Scenario: Delegated course admin can delegate another user as course admin
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user creates a course with details:
      | name     | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Math 101 | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-06-01T09:00:00Z |
    And user "courseadmin@test.com" has "admin" role on "course" "{course_id}"
    And user switches to "courseadmin@test.com" identity
    And there is a user in "none" group with details:
      | email             | firstName | lastName | contact  |
      | newadmin@test.com | Jane      | Smith    | 91234567 |
    When course admin adds user "newadmin@test.com" as course admin
    And user switches to "newadmin@test.com" identity
    And there is a user in "none" group with details:
      | email              | firstName | lastName | contact  |
      | thirduser@test.com | Bob       | Jones    | 92345678 |
    When course admin adds user "thirduser@test.com" as course admin
    Then the user "thirduser@test.com" should have "admin" role on "course" "{course_id}"
