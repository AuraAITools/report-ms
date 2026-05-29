@Api
Feature: Get User Institutions

  @ComponentTest
  @PreExistingFailure
  Scenario: User successfully gets institution details of institutions he is in
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries their institutions
    Then the user's institutions should contain the onboarded institution
