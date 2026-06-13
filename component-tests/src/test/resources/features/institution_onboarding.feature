@Api
Feature: Institution onboarding

  As an aura platform admin
  I want prospects to request institution creation and to onboard institutions onto the platform
  So that new institutions go through a request -> review -> onboard flow with platform-wide unique names and emails

  Institution names and emails are unique across the whole platform.

  @ComponentTest
  Scenario: Aura admin onboards a new institution
    Given user has "aura-admin" role
    When user creates an institution with details:
      | name     | email            | contactNumber | address     | uen        |
      | Acme Edu | acme@example.com | 91234567      | 1 Acme Lane | 200012345A |
    Then the institution should be created successfully

  @ComponentTest
  Scenario: Aura admin cannot onboard a second institution with the same name
    Given user has "aura-admin" role
    Given an institution is onboarded with details:
      | name     | email            | contactNumber | address     | uen        |
      | Acme Edu | acme@example.com | 91234567      | 1 Acme Lane | 200012345A |
    When user tries to create an institution with details:
      | name     | email             | contactNumber | address      | uen        |
      | Acme Edu | other@example.com | 98765432      | 2 Other Lane | 200067890B |
    Then the request should fail with error code "DUPLICATE_RESOURCE"

  @ComponentTest
  Scenario: Aura admin cannot onboard a second institution with the same email
    Given user has "aura-admin" role
    Given an institution is onboarded with details:
      | name     | email            | contactNumber | address     | uen        |
      | Acme Edu | acme@example.com | 91234567      | 1 Acme Lane | 200012345A |
    When user tries to create an institution with details:
      | name     | email            | contactNumber | address      | uen        |
      | Beta Edu | acme@example.com | 98765432      | 2 Other Lane | 200067890B |
    Then the request should fail with error code "DUPLICATE_RESOURCE"

  @ComponentTest
  Scenario: Unauthenticated prospect successfully requests an institution to be created
    When prospect requests an institution to be created with details:
      | institutionName | institutionEmail | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail   | requesterContactNumber |
      | Acme Edu        | acme@example.com | 91234567                 | 1 Acme Lane        | 200012345A     | Alice              | Lee               | alice@acme.edu   | 98765432               |
    Then the institution creation request should be saved in state "REQUESTED"

  @ComponentTest
  Scenario: Prospect cannot request an institution creation when an institution with the same name already exists
    Given user has "aura-admin" role
    And an institution is onboarded with details:
      | name     | email            | contactNumber | address     | uen        |
      | Acme Edu | acme@example.com | 91234567      | 1 Acme Lane | 200012345A |
    When prospect tries to request an institution to be created with details:
      | institutionName | institutionEmail  | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail | requesterContactNumber |
      | Acme Edu        | other@example.com | 91234567                 | 2 Other Lane       | 200067890B     | Bob                | Tan               | bob@acme.edu   | 98765432               |
    Then the request should fail with error code "DUPLICATE_RESOURCE"

  @ComponentTest
  Scenario: Prospect cannot request an institution creation when an institution with the same email already exists
    Given user has "aura-admin" role
    And an institution is onboarded with details:
      | name     | email            | contactNumber | address     | uen        |
      | Acme Edu | acme@example.com | 91234567      | 1 Acme Lane | 200012345A |
    When prospect tries to request an institution to be created with details:
      | institutionName | institutionEmail | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail | requesterContactNumber |
      | Beta Edu        | acme@example.com | 91234567                 | 2 Other Lane       | 200067890B     | Bob                | Tan               | bob@acme.edu   | 98765432               |
    Then the request should fail with error code "DUPLICATE_RESOURCE"

  @ComponentTest
  Scenario: Prospect cannot request an institution creation when a pending request with the same name already exists
    Given a pending institution creation request exists with details:
      | institutionName | institutionEmail | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail | requesterContactNumber |
      | Acme Edu        | acme@example.com | 91234567                 | 1 Acme Lane        | 200012345A     | Alice              | Lee               | alice@acme.edu | 98765432               |
    When prospect tries to request an institution to be created with details:
      | institutionName | institutionEmail  | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail | requesterContactNumber |
      | Acme Edu        | other@example.com | 91234567                 | 2 Other Lane       | 200067890B     | Bob                | Tan               | bob@acme.edu   | 98765432               |
    Then the request should fail with error code "DUPLICATE_RESOURCE"

  @ComponentTest
  Scenario: Prospect cannot request an institution creation when a pending request with the same email already exists
    Given a pending institution creation request exists with details:
      | institutionName | institutionEmail | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail | requesterContactNumber |
      | Acme Edu        | acme@example.com | 91234567                 | 1 Acme Lane        | 200012345A     | Alice              | Lee               | alice@acme.edu | 98765432               |
    When prospect tries to request an institution to be created with details:
      | institutionName | institutionEmail | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail | requesterContactNumber |
      | Beta Edu        | acme@example.com | 91234567                 | 2 Other Lane       | 200067890B     | Bob                | Tan               | bob@acme.edu   | 98765432               |
    Then the request should fail with error code "DUPLICATE_RESOURCE"

  @ComponentTest
  Scenario: Aura admin onboards an institution from a pending creation request
    Given a pending institution creation request exists with details:
      | institutionName | institutionEmail | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail | requesterContactNumber |
      | Acme Edu        | acme@example.com | 91234567                 | 1 Acme Lane        | 200012345A     | Alice              | Lee               | alice@acme.edu | 98765432               |
    And user has "aura-admin" role
    When user onboards the institution from the pending request
    Then the institution should be created with name "Acme Edu" and email "acme@example.com"
    And an admin account should be created for email "alice@acme.edu"
    And the institution creation request should be saved in state "FULFILLED"

  @ComponentTest
  Scenario: Aura admin cannot onboard an institution from a non-existent creation request
    Given user has "aura-admin" role
    When user tries to onboard the institution from creation request id "00000000-0000-0000-0000-000000000000"
    Then the request should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  Scenario: Aura admin cannot onboard an institution twice from the same creation request
    Given a pending institution creation request exists with details:
      | institutionName | institutionEmail | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail | requesterContactNumber |
      | Acme Edu        | acme@example.com | 91234567                 | 1 Acme Lane        | 200012345A     | Alice              | Lee               | alice@acme.edu | 98765432               |
    And user has "aura-admin" role
    And user onboards the institution from the pending request
    When user tries to onboard the institution from the pending request again
    Then the request should fail with error code "INVALID_REQUEST_STATE"
