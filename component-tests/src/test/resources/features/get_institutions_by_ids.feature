@Api
Feature: Get Institutions By Ids

  @ComponentTest
  Scenario: Aura admin successfully retrieves an onboarded institution by its id
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries institutions by ids using their onboarded institution id
    Then the institutions response should contain the onboarded institution

  @ComponentTest
  Scenario: Aura admin queries with empty ids list and receives empty result
    Given user has "aura-admin" role
    When user queries institutions by ids with an empty list
    Then the institutions response should be empty
