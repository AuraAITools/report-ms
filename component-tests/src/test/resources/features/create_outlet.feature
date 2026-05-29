@Api
Feature: Create Outlet

  @ComponentTest
  Scenario: Successfully create a new outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    When user creates an outlet with details:
      | name        | address        | postalCode | contactNumber | description   | email           |
      | Test Outlet | 123 Test St    | 123456     | 96228693      | A test outlet | outlet@test.com |
    Then the outlet should be created successfully

  @ComponentTest
  Scenario: Cannot create an outlet with a duplicate name
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Duplicate Outlet"
    When user tries to create an outlet with name "Duplicate Outlet"
    Then the request should fail with an error
