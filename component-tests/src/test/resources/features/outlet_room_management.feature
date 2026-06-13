@Api
Feature: Outlet room management
  As an institution admin
  I want to manage bookable rooms within an outlet
  So that lessons can be scheduled into physical or virtual rooms

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Branch"

  @ComponentTest
  Scenario: Institution admin creates an outlet room
    Given the outlet room "Studio A" exists in outlet "Main Branch"
    When user lists outlet rooms in the outlet
    Then the outlet room list should contain the room "Studio A"

  @ComponentTest
  Scenario: Institution admin lists all outlet rooms in the institution
    Given the outlet room "Studio A" exists in outlet "Main Branch"
    And the outlet room "Studio B" exists in outlet "Main Branch"
    When user lists outlet rooms in the institution
    Then the outlet room list should have 2 entries
    And the outlet room list should contain the room "Studio A"
    And the outlet room list should contain the room "Studio B"

  @ComponentTest
  Scenario: Institution admin retrieves outlet rooms by ids
    Given the outlet room "Studio A" exists in outlet "Main Branch"
    And the outlet room "Studio B" exists in outlet "Main Branch"
    When user retrieves outlet rooms by ids of "Studio A,Studio B"
    Then the outlet room list should have 2 entries
    And the outlet room list should contain the room "Studio A"
    And the outlet room list should contain the room "Studio B"

  @ComponentTest
  Scenario: Institution admin updates an outlet room's editable fields
    Given the outlet room "Studio A" exists in outlet "Main Branch"
    When user updates outlet room "Studio A" with
      | name      | size | type     | description       |
      | Studio A1 | 40   | PHYSICAL | Expanded capacity |
    Then the outlet room should be updated successfully with name "Studio A1" and size 40

  @ComponentTest
  Scenario: Institution admin deletes an outlet room
    Given the outlet room "Studio A" exists in outlet "Main Branch"
    When user deletes outlet room "Studio A"
    Then the outlet room delete should succeed
    And listing outlet rooms in the institution should return 0 entries

  # Authorization for update/delete is evaluated against the outlet_room id before any lookup —
  # a non-existent outlet_room is indistinguishable from a forbidden one (no id enumeration).

  @ComponentTest
  Scenario: Institution admin fails to update a non-existent outlet room
    When user tries to update an outlet room with a random id
    Then the request should fail with error code "FORBIDDEN"
