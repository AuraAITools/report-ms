@Api
Feature: Institution onboarding

  The aura platform team onboards a new institution onto the platform.
  Institution names and emails are unique across the whole platform.

  Background:
    Given user has "aura-admin" role

  @ComponentTest
  Scenario: Aura admin onboards a new institution
    When user creates an institution with details:
      | name     | email            | contactNumber | address     | uen        |
      | Acme Edu | acme@example.com | 91234567      | 1 Acme Lane | 200012345A |
    Then the institution should be created successfully

  @ComponentTest
  Scenario: Aura admin cannot onboard a second institution with the same name
    Given an institution is onboarded with details:
      | name     | email            | contactNumber | address     | uen        |
      | Acme Edu | acme@example.com | 91234567      | 1 Acme Lane | 200012345A |
    When user tries to create an institution with details:
      | name     | email             | contactNumber | address      | uen        |
      | Acme Edu | other@example.com | 98765432      | 2 Other Lane | 200067890B |
    Then the request should fail with error code "DUPLICATE_RESOURCE"

  @ComponentTest
  Scenario: Aura admin cannot onboard a second institution with the same email
    Given an institution is onboarded with details:
      | name     | email            | contactNumber | address     | uen        |
      | Acme Edu | acme@example.com | 91234567      | 1 Acme Lane | 200012345A |
    When user tries to create an institution with details:
      | name     | email            | contactNumber | address      | uen        |
      | Beta Edu | acme@example.com | 98765432      | 2 Other Lane | 200067890B |
    Then the request should fail with error code "DUPLICATE_RESOURCE"
