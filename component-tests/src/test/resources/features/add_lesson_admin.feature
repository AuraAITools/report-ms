@Api
Feature: Add lesson admin delegation

  @ComponentTest
  Scenario: Lesson admin delegates another user as lesson admin
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And a standalone lesson exists with name "Test Lesson"
    And user "lessonadmin@test.com" has "admin" role on "lesson" "{lesson_id}"
    And user switches to "lessonadmin@test.com" identity
    And there is a user in "none" group with details:
      | email             | firstName | lastName | contact  |
      | newadmin@test.com | Jane      | Smith    | 91234567 |
    When lesson admin adds user "newadmin@test.com" as lesson admin
    Then the user "newadmin@test.com" should have "admin" role on "lesson" "{lesson_id}"

  @ComponentTest
  Scenario: Outlet admin delegates a user as lesson admin
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And a standalone lesson exists with name "Test Lesson"
    And user "outletadmin@test.com" has "admin" role on "outlet" "{outlet_id}"
    And user switches to "outletadmin@test.com" identity
    And there is a user in "none" group with details:
      | email             | firstName | lastName | contact  |
      | newadmin@test.com | Jane      | Smith    | 91234567 |
    When lesson admin adds user "newadmin@test.com" as lesson admin
    Then the user "newadmin@test.com" should have "admin" role on "lesson" "{lesson_id}"

  @ComponentTest
  Scenario: Delegated lesson admin can delegate another user as lesson admin
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And a standalone lesson exists with name "Test Lesson"
    And user "lessonadmin@test.com" has "admin" role on "lesson" "{lesson_id}"
    And user switches to "lessonadmin@test.com" identity
    And there is a user in "none" group with details:
      | email             | firstName | lastName | contact  |
      | newadmin@test.com | Jane      | Smith    | 91234567 |
    When lesson admin adds user "newadmin@test.com" as lesson admin
    And user switches to "newadmin@test.com" identity
    And there is a user in "none" group with details:
      | email              | firstName | lastName | contact  |
      | thirduser@test.com | Bob       | Jones    | 92345678 |
    When lesson admin adds user "thirduser@test.com" as lesson admin
    Then the user "thirduser@test.com" should have "admin" role on "lesson" "{lesson_id}"
