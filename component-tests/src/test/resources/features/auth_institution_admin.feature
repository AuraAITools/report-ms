@Api
Feature: Institution admin authorization

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin can view their institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "instadmin@test.com" has "admin" role on "institution" "{institution_id}"
    And user switches to "instadmin@test.com" identity
    When user queries their institution
    Then the institution should be returned with correct details

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin can create outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "instadmin@test.com" has "admin" role on "institution" "{institution_id}"
    And user switches to "instadmin@test.com" identity
    When user creates an outlet with details:
      | name        | address        | postalCode | contactNumber | description    | email            |
      | Test Outlet | 123 Test St    | 123456     | 91234567      | A test outlet  | outlet@test.com  |
    Then the outlet should be created successfully

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin can create subject
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "instadmin@test.com" has "admin" role on "institution" "{institution_id}"
    And user switches to "instadmin@test.com" identity
    When user creates a subject with name "Mathematics"
    Then the subject "Mathematics" should be created successfully

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin can create topic
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "instadmin@test.com" has "admin" role on "institution" "{institution_id}"
    And user switches to "instadmin@test.com" identity
    When user creates a topic with name "Algebra"
    Then the topic "Algebra" should be created successfully

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin can query outlets by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "instadmin@test.com" has "admin" role on "institution" "{institution_id}"
    And user switches to "instadmin@test.com" identity
    When user queries outlets by page
    Then at least one outlet should be returned

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin can query levels by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "instadmin@test.com" has "admin" role on "institution" "{institution_id}"
    And user switches to "instadmin@test.com" identity
    When user queries levels by page
    Then the levels query should respond successfully

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin cannot create institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "instadmin@test.com" has "admin" role on "institution" "{institution_id}"
    And user switches to "instadmin@test.com" identity
    When user tries to onboard an institution with details:
      | name       | email           | contactNumber | address        | uen       |
      | Other Inst | other@test.com  | 91234567      | 456 Other St   | UEN123456 |
    Then the request should fail with an error
