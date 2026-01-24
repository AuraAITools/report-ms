@Api
Feature: Institution Onboarding

  @ComponentTest
  Scenario: Aura Admin user onboards institution successfully
    Given user has "aura-admin" role
    When user onboards a institution with details:
      | name       | email            | contactNumber | address | uen  |
      | testing123 | testgg@gmail.com | 96228693      | address | test |
    Then institution should be created
    And default initial data should be loaded