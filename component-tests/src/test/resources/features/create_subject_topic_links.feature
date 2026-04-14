@Api
Feature: Create Subject Topic Links

  @ComponentTest
  Scenario: Successfully link a topic to a subject
    Given user has "aura-admin" role
    And a topic exists with name "Algebra"
    And a subject exists with name "Mathematics"
    When user links the topic to the subject
    Then the subject should have the topic linked
