@Api
Feature: Lookup permissions

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin successfully looks up their permissions
    Given an institution is onboarded
    When the aura admin looks up their permissions
    Then the response should contain permissions for resource type "system" with resource id "global"
    And the response should contain permissions for resource type "institution"
