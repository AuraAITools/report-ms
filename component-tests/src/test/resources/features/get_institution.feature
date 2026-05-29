@Api
Feature: Get Institution

  @ComponentTest
  Scenario: Successfully retrieve the tenant's institution
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries their institution
    Then the institution should be returned with correct details

  @ComponentTest
  @PreExistingFailure
  Scenario: Fail to retrieve institution with invalid tenant
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries institution with non-existent id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
