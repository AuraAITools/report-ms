@Api
Feature: Add outlet admin delegation

  @ComponentTest
  Scenario: Outlet admin delegates another user as outlet admin
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "outletadmin@test.com" has "admin" role on "outlet" "{outlet_id}"
    And user switches to "outletadmin@test.com" identity
    And there is a user in "none" group with details:
      | email             | firstName | lastName | contact  |
      | newadmin@test.com | Jane      | Smith    | 91234567 |
    When outlet admin adds user "newadmin@test.com" as outlet admin
    Then the user "newadmin@test.com" should have "admin" role on "outlet" "{outlet_id}"

  @ComponentTest
  Scenario: Institution admin delegates a user as outlet admin
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And there is a user in "none" group with details:
      | email             | firstName | lastName | contact  |
      | newadmin@test.com | Jane      | Smith    | 91234567 |
    When outlet admin adds user "newadmin@test.com" as outlet admin
    Then the user "newadmin@test.com" should have "admin" role on "outlet" "{outlet_id}"

  @ComponentTest
  Scenario: Delegated outlet admin can create courses under their outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "outletadmin@test.com" has "admin" role on "outlet" "{outlet_id}"
    And user switches to "outletadmin@test.com" identity
    And there is a user in "none" group with details:
      | email             | firstName | lastName | contact  |
      | newadmin@test.com | Jane      | Smith    | 91234567 |
    When outlet admin adds user "newadmin@test.com" as outlet admin
    And user switches to "newadmin@test.com" identity
    And user creates a course with details:
      | name     | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Math 101 | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-06-01T09:00:00Z |
    Then the course should be created successfully
