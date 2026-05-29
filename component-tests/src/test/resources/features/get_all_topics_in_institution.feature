@Api
Feature: Get All Topics In Institution

  @ComponentTest
  Scenario: Aura admin retrieves all topics in an institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And a topic exists with name "Algebra"
    And a topic exists with name "Geometry"
    When user queries all topics in the institution
    Then the response should contain 2 topics
    And the topics should include names "Algebra,Geometry"

  @ComponentTest
  Scenario: Aura admin retrieves an empty list when institution has no topics
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries all topics in the institution
    Then the response should contain 0 topics
