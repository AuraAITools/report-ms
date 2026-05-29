@Api
Feature: Get All Subjects In Institution

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin retrieves all subjects in an institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And a subject exists with name "Mathematics"
    And a subject exists with name "Science"
    When user queries all subjects in the institution
    Then the response should contain 2 subjects
    And the subjects should include names "Mathematics,Science"

  @ComponentTest
  Scenario: Aura admin retrieves an empty list when institution has no subjects
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries all subjects in the institution
    Then the response should contain 0 subjects
