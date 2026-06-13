@Api
Feature: Institution viewer authorization

  A user holding the "viewer" role on their institution's user group can read
  resources within that institution, but cannot create or modify anything.

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "viewer@test.com" has "viewer" role on "institution_user_group" "{institution_id}"
    And user switches to "viewer@test.com" identity

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer views their institution
    When user queries their institution
    Then the institution should be returned with correct details

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer queries outlets by page
    When user queries outlets by page
    Then at least one outlet should be returned

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer queries levels by page
    When user queries levels by page
    Then the levels query should respond successfully

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer cannot create an outlet in their institution
    When user tries to create an outlet with name "Unauthorized Outlet"
    Then the request should fail with error code "FORBIDDEN"

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer cannot create a subject in their institution
    When user tries to create a subject with name "Unauthorized Subject"
    Then the request should fail with error code "FORBIDDEN"

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer cannot create accounts in their institution
    When user tries to create an account with details:
      | email             | firstName | lastName | contact  |
      | newadmin@test.com | New       | Admin    | 91234567 |
    Then the request should fail with error code "FORBIDDEN"
