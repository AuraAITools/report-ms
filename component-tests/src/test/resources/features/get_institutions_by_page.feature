@Api
Feature: Get Institutions By Page

  @ComponentTest
  Scenario: Successfully retrieve institutions by page
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries institutions by page
    Then at least one institution should be returned
