@Api
Feature: Educator authorization

  @ComponentTest @AuthEducator
  Scenario: Educator can view their institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "educator@test.com" has "educator" role on "institution" "{institution_id}"
    And user switches to "educator@test.com" identity
    When user queries their institution
    Then the institution should be returned with correct details

  @ComponentTest @AuthEducator
  Scenario: Educator can query outlets by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "educator@test.com" has "educator" role on "institution" "{institution_id}"
    And user switches to "educator@test.com" identity
    When user queries outlets by page
    Then at least one outlet should be returned

  @ComponentTest @AuthEducator
  Scenario: Educator cannot create outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "educator@test.com" has "educator" role on "institution" "{institution_id}"
    And user switches to "educator@test.com" identity
    When user tries to create an outlet with name "Unauthorized Outlet"
    Then the request should fail with an error

  @ComponentTest @AuthEducator
  Scenario: Educator cannot create course
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "educator@test.com" has "educator" role on "institution" "{institution_id}"
    And user switches to "educator@test.com" identity
    When user tries to create a course with details:
      | name        | maxSize | lessonFrequency | price | priceFrequency | startDate              | endDate                |
      | Math 101    | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z   | 2026-06-01T09:00:00Z   |
    Then the request should fail with an error
