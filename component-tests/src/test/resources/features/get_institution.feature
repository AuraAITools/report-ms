@Api
Feature: Get Institution

  @ComponentTest
  Scenario: Successfully retrieve the tenant's institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And user switches to the institution tenant
    When user queries their institution
    Then the institution should be returned with correct details

  @ComponentTest
  Scenario: Fail to retrieve institution with invalid tenant
    Given user has "aura-admin" role
    And an institution is onboarded
    And user switches to a non-existent tenant
    When user queries their institution
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
