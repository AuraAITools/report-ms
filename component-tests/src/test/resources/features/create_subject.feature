@Api
Feature: Create Subject

  @ComponentTest
  Scenario: Successfully create a new subject
    Given user has "aura-admin" role
    And an institution is onboarded
    When user creates a subject with name "Mathematics"
    Then the subject "Mathematics" should be created successfully
