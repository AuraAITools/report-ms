@Api
Feature: Create Institution Admin Account

  @ComponentTest
  Scenario: Aura admin successfully creates institution admin account
    Given an institution is onboarded with details:
      | name       | email            | contactNumber | address | uen  |
      | testing123 | testgg@gmail.com | 96228693      | address | test |
    And user in "aura-admin" group creates an institution admin account with details:
      | email          | firstName | lastName | contact  |
      | admin@test.com | John      | Doe      | 96228693 |
    Then the user "admin@test.com" should be created in Keycloak groups "institution:{institution_id}:admin"
    And the user "admin@test.com" enabled status should be "true"
    And the user "admin@test.com" email verified status should be "false"
    And the user "admin@test.com" should have required actions "VERIFY_EMAIL,UPDATE_PASSWORD"
    And an email should be sent to "admin@test.com"

  @ComponentTest
  Scenario: Cannot create an institution admin account without an institution tenant
    Given an institution is onboarded with details:
      | name       | email            | contactNumber | address | uen  |
      | testing123 | testgg@gmail.com | 96228693      | address | test |
    When user tries to create an institution admin account without a tenant with details:
      | email          | firstName | lastName | contact  |
      | admin@test.com | John      | Doe      | 96228693 |
    Then the request should fail with http status 500

  @ComponentTest
  Scenario: Institution admin adds another user as institution admin
    Given an institution is onboarded with details:
      | name       | email            | contactNumber | address | uen  |
      | testing123 | testgg@gmail.com | 96228693      | address | test |
    And there is a user in "institution:{institution_id}:admin" group with details:
      | email          | firstName | lastName | contact  |
      | admin@test.com | John      | Doe      | 96228693 |
    And there is a user in "none" group with details:
      | email              | firstName | lastName | contact  |
      | newadmin@test.com  | Jane      | Smith    | 91234567 |
    When institution admin adds user "newadmin@test.com" as institution admin
    Then the user "newadmin@test.com" should have "admin" role on "institution" "{institution_id}"
