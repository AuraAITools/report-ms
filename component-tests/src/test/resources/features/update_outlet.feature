@Api
Feature: Update Outlet

  @ComponentTest
  Scenario: Aura admin updates an existing outlet's editable fields
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Original Outlet"
    When user updates the outlet with details:
      | name           | address     | postalCode | contactNumber | description    | email            |
      | Renamed Outlet | 99 New Road | 999999     | 98765432      | Updated outlet | renamed@test.com |
    Then the outlet should be updated successfully with name "Renamed Outlet"

  @ComponentTest
  Scenario: Aura admin tries to update an outlet that does not exist
    Given user has "aura-admin" role
    And an institution is onboarded
    When user tries to update a non-existent outlet
    Then the request should fail with an error
