@Api
Feature: Create Topic

  @ComponentTest
  Scenario: Successfully create a new topic
    Given user has "aura-admin" role
    When user creates a topic with name "Test Topic"
    Then the topic "Test Topic" should be created successfully