@Api
Feature: Get Outlets In Institution

  @ComponentTest
  Scenario: Aura admin retrieves all outlets in an institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Bukit Timah Branch"
    And an outlet exists with name "Tampines Branch"
    When user queries all outlets in the institution
    Then the response should contain 2 outlets
    And the outlets should include names "Bukit Timah Branch,Tampines Branch"

  @ComponentTest
  Scenario: Aura admin retrieves an empty list when institution has no outlets
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries all outlets in the institution
    Then the response should contain 0 outlets
