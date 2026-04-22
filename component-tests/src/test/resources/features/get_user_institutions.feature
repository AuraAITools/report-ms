@Api
Feature: Get User Institutions

  @ComponentTest
  Scenario: User successfully gets institution details of institutions he is in
    Given user has "aura-admin" role
    And an institution is onboarded
    And user switches to the institution tenant
    When user queries their institutions
    Then the user's institutions should contain the onboarded institution
