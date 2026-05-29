@Api
Feature: Get Course By Id

  @ComponentTest
  Scenario: Aura admin retrieves a course with its associations
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    And a course exists with name "Math 101"
    When user queries course by id
    Then the course should be returned with id and name "Math 101"
    And the course nested associations should be present

  @ComponentTest
  @PreExistingFailure
  Scenario: Aura admin fails to retrieve a non-existent course
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    When user queries course with non-existent id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"
