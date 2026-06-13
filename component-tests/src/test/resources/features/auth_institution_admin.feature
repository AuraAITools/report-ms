@Api
Feature: Institution admin authorization

  A user holding the "admin" role on their institution's user group can manage
  and view resources within that institution, but cannot perform system-scoped
  operations like onboarding new institutions.

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "instadmin@test.com" has "admin" role on "institution_user_group" "{institution_id}"
    And user switches to "instadmin@test.com" identity

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin views their institution
    When user queries their institution
    Then the institution should be returned with correct details

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin creates an outlet in their institution
    When user creates an outlet with details:
      | name         | address     | postalCode | contactNumber | description     | email          |
      | Admin Outlet | 123 Test St | 123456     | 91234567      | An admin outlet | admin@test.com |
    Then the outlet should be created successfully

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin creates a subject in their institution
    When user creates a subject with name "Mathematics"
    Then the subject "Mathematics" should be created successfully

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin creates a topic in their institution
    When user creates a topic with name "Algebra"
    Then the topic "Algebra" should be created successfully

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin queries outlets by page
    When user queries outlets by page
    Then at least one outlet should be returned

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin queries levels by page
    When user queries levels by page
    Then the levels query should respond successfully

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin cannot onboard a new institution
    When user tries to create an institution with details:
      | name       | email          | contactNumber | address      | uen       |
      | Other Inst | other@test.com | 91234567      | 456 Other St | UEN123456 |
    Then the request should fail with error code "FORBIDDEN"
