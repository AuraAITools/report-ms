@Api
Feature: Outlet management
  As an aura admin
  I want to create, query, update, and delete outlets within an institution
  So that I can manage where the institution's courses are run

  @ComponentTest
  Scenario: Aura admin successfully creates a new outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    When user creates an outlet with details:
      | name        | address        | postalCode | contactNumber | description   | email           |
      | Test Outlet | 123 Test St    | 123456     | 96228693      | A test outlet | outlet@test.com |
    Then the outlet should be created successfully

  @ComponentTest
  Scenario: Aura admin cannot create an outlet with a duplicate name
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Duplicate Outlet"
    When user tries to create an outlet with name "Duplicate Outlet"
    Then the request should fail with an error

  @ComponentTest
  Scenario: Aura admin successfully retrieves outlets by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Paginated Outlet"
    When user queries outlets by page
    Then at least one outlet should be returned

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

  @ComponentTest
  Scenario: Aura admin updates an existing outlet's editable fields
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Original Outlet"
    When user updates the outlet with details:
      | name           | address     | postalCode | contactNumber | description    | email            |
      | Renamed Outlet | 99 New Road | 999999     | 98765432      | Updated outlet | renamed@test.com |
    Then the outlet should be updated successfully with name "Renamed Outlet"

  @ComponentTest
  Scenario: Aura admin tries to update an outlet that does not exist
    Given user has "aura-admin" role
    And an institution is onboarded
    When user tries to update a non-existent outlet
    Then the request should fail with an error

  # Authorization is evaluated against the resource id before any lookup, and deletes remove the
  # resource's SpiceDB relationships — so deleted and never-existing resources are both FORBIDDEN
  # (no id enumeration).

  @ComponentTest
  Scenario: Aura admin deletes an outlet and cascades child courses and lessons
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Branch"
    And a course exists with name "Algebra 101"
    And a lesson exists with name "Intro Lesson"
    When user deletes the outlet by id
    Then the outlet should be deleted successfully
    And querying the course by id should fail with error code "FORBIDDEN"
    And querying the lesson by id should fail with error code "FORBIDDEN"

  @ComponentTest
  Scenario: Aura admin fails to delete a non-existent outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    When user tries to delete an outlet with a random id
    Then the request should fail with error code "FORBIDDEN"
