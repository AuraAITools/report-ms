@Api
Feature: Lookup group permissions

  @ComponentTest
  @PreExistingFailure
  Scenario: User looks up their user_group permissions scoped to an institution
    Given an institution is onboarded
    When the user looks up their group permissions for the institution
    Then the group permissions response should contain resource type "institution_user_group"
    And every group permissions entry should have a non-empty displayName

  @ComponentTest
  @PreExistingFailure
  Scenario: User_group permissions response is scoped to the requested institution only
    Given an institution is onboarded
    When the user looks up their group permissions for the institution
    Then the group permissions response should not contain resource type "system_user_group"
