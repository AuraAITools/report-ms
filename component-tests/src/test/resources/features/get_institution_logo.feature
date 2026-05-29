@Api
Feature: Institution Logo URL

  @ComponentTest
  Scenario: Institution without a logo returns null logo url
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries their institution with logo url
    Then the institution response includes no logo url

  @ComponentTest
  Scenario: Institution with a logo returns a fetchable presigned url
    Given user has "aura-admin" role
    And an institution is onboarded
    And the institution has a logo uploaded
    When user queries their institution with logo url
    Then the institution response includes a logo url
    And the logo url returns 200 when fetched
