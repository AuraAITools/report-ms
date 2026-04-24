@Api
Feature: Get Levels By Page

  @ComponentTest
  Scenario: Successfully retrieve levels by page
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries levels by page
    Then the levels query should respond successfully
