@Api
Feature: Institution Onboarding

  @ComponentTest
  Scenario: Aura Admin user onboards institution successfully
    Given user has "aura-admin" role
    When user onboards an institution with details:
      | name       | email            | contactNumber | address | uen  |
      | testing123 | testgg@gmail.com | 96228693      | address | test |
    Then institution should be created

  @ComponentTest
  Scenario: Cannot onboard the same institution twice
    Given user has "aura-admin" role
    And an institution is onboarded
    When user tries to onboard the same institution again
    Then the request should fail with an error
