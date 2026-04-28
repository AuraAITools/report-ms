@Api
Feature: Institution viewer authorization

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer can view their institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "viewer@test.com" has "viewer" role on "institution" "{institution_id}"
    And user switches to "viewer@test.com" identity
    When user queries their institution
    Then the institution should be returned with correct details

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer can query outlets by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "viewer@test.com" has "viewer" role on "institution" "{institution_id}"
    And user switches to "viewer@test.com" identity
    When user queries outlets by page
    Then at least one outlet should be returned

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer can query levels by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "viewer@test.com" has "viewer" role on "institution" "{institution_id}"
    And user switches to "viewer@test.com" identity
    When user queries levels by page
    Then the levels query should respond successfully

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer cannot create outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "viewer@test.com" has "viewer" role on "institution" "{institution_id}"
    And user switches to "viewer@test.com" identity
    When user tries to create an outlet with name "Unauthorized Outlet"
    Then the request should fail with an error

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer cannot create subject
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "viewer@test.com" has "viewer" role on "institution" "{institution_id}"
    And user switches to "viewer@test.com" identity
    When user tries to create a subject with name "Unauthorized Subject"
    Then the request should fail with an error

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer cannot create institution admin account
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "viewer@test.com" has "viewer" role on "institution" "{institution_id}"
    And user switches to "viewer@test.com" identity
    When user tries to create an institution admin account with details:
      | email              | firstName | lastName | contact  |
      | newadmin@test.com  | New       | Admin    | 91234567 |
    Then the request should fail with an error
