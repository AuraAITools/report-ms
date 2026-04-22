@Api
Feature: Institution Onboarding

  @ComponentTest
  Scenario: Aura Admin user onboards institution successfully
    Given user has "aura-admin" role
    When user onboards an institution with details:
      | name       | email            | contactNumber | address | uen  |
      | testing123 | testgg@gmail.com | 96228693      | address | test |
    Then institution should be created
    And a UMA resource should be registered for the institution with type "urn:aura:institution" and scopes "view,manage,delegate,create"

  @ComponentTest
  Scenario: Cannot onboard the same institution twice
    Given user has "aura-admin" role
    And user onboards an institution with details:
      | name       | email            | contactNumber | address | uen  |
      | testing123 | testgg@gmail.com | 96228693      | address | test |
    When user tries to onboard an institution with details:
      | name       | email            | contactNumber | address | uen  |
      | testing123 | testgg@gmail.com | 96228693      | address | test |
    Then the request should fail with error code "INTERNAL_ERROR"
