@Api
Feature: Get Lesson By Id

  @ComponentTest
  Scenario: Aura admin retrieves a lesson with its associations
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    And a course exists with name "Math 101"
    And a lesson exists with name "Lesson 1"
    When user queries lesson by id
    Then the lesson should be returned with name "Lesson 1"
    And the lesson course should be returned

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin fails to retrieve a non-existent lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    When user queries lesson with non-existent id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
