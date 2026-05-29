@Api
Feature: Create Level In Institution

  @ComponentTest
  Scenario: Aura admin creates a level in the institution
    Given user has "aura-admin" role
    And an institution is onboarded
    When user creates a level in the institution with name "P1" and category "PRIMARY"
    Then the level should be created successfully with name "P1" and category "PRIMARY"
