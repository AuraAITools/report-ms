@Api
Feature: Institution management

  As an aura admin
  I want to retrieve institutions by id, in bulk, and with their logo urls
  So that I can view and manage onboarded institutions on the platform

  @ComponentTest
  Scenario: Aura admin retrieves the tenant's institution
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries their institution
    Then the institution should be returned with correct details

  @ComponentTest
  Scenario: Aura admin fails to retrieve a non-existent institution
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries institution with non-existent id
    # Authorization is evaluated against the institution id before any lookup, so a non-existent
    # institution is indistinguishable from a forbidden one (no id enumeration)
    Then the request should fail with error code "FORBIDDEN"

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

  @ComponentTest
  Scenario: Aura admin retrieves null logo url for an institution without a logo
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries their institution with logo url
    Then the institution response includes no logo url

  @ComponentTest
  Scenario: Aura admin retrieves a fetchable presigned logo url for an institution with a logo
    Given user has "aura-admin" role
    And an institution is onboarded
    And the institution has a logo uploaded
    When user queries their institution with logo url
    Then the institution response includes a logo url
    And the logo url returns 200 when fetched
