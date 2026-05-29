@Api
Feature: Create Institution

  @ComponentTest
  Scenario: Aura admin successfully creates an institution
    Given user has "aura-admin" role
    When user creates an institution with details:
      | name     | email            | contactNumber | address     | uen        |
      | Acme Edu | acme@example.com | 91234567      | 1 Acme Lane | 200012345A |
    Then the institution should be created successfully

  @ComponentTest
  Scenario: Cannot create an institution with a duplicate name
    Given user has "aura-admin" role
    And an institution is onboarded with details:
      | name     | email            | contactNumber | address     | uen        |
      | Acme Edu | acme@example.com | 91234567      | 1 Acme Lane | 200012345A |
    When user tries to create an institution with details:
      | name     | email             | contactNumber | address      | uen        |
      | Acme Edu | other@example.com | 98765432      | 2 Other Lane | 200067890B |
    Then the request should fail with error code "INTERNAL_ERROR"

  @ComponentTest
  Scenario: Cannot create an institution with a duplicate email
    Given user has "aura-admin" role
    And an institution is onboarded with details:
      | name     | email            | contactNumber | address     | uen        |
      | Acme Edu | acme@example.com | 91234567      | 1 Acme Lane | 200012345A |
    When user tries to create an institution with details:
      | name     | email            | contactNumber | address      | uen        |
      | Beta Edu | acme@example.com | 98765432      | 2 Other Lane | 200067890B |
    Then the request should fail with error code "INTERNAL_ERROR"
