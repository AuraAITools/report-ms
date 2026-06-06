@Api
Feature: Request Institution Creation

  @ComponentTest
  Scenario: Unauthenticated prospect successfully requests an institution to be created
    When prospect requests an institution to be created with details:
      | institutionName | institutionEmail | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail   | requesterContactNumber |
      | Acme Edu        | acme@example.com | 91234567                 | 1 Acme Lane        | 200012345A     | Alice              | Lee               | alice@acme.edu   | 98765432               |
    Then the institution creation request should be saved in state "REQUESTED"

  @ComponentTest
  Scenario: Cannot request an institution creation when an institution with the same name already exists
    Given user has "aura-admin" role
    And an institution is onboarded with details:
      | name     | email            | contactNumber | address     | uen        |
      | Acme Edu | acme@example.com | 91234567      | 1 Acme Lane | 200012345A |
    When prospect tries to request an institution to be created with details:
      | institutionName | institutionEmail  | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail | requesterContactNumber |
      | Acme Edu        | other@example.com | 91234567                 | 2 Other Lane       | 200067890B     | Bob                | Tan               | bob@acme.edu   | 98765432               |
    Then the request should fail with error code "INTERNAL_ERROR"

  @ComponentTest
  Scenario: Cannot request an institution creation when an institution with the same email already exists
    Given user has "aura-admin" role
    And an institution is onboarded with details:
      | name     | email            | contactNumber | address     | uen        |
      | Acme Edu | acme@example.com | 91234567      | 1 Acme Lane | 200012345A |
    When prospect tries to request an institution to be created with details:
      | institutionName | institutionEmail | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail | requesterContactNumber |
      | Beta Edu        | acme@example.com | 91234567                 | 2 Other Lane       | 200067890B     | Bob                | Tan               | bob@acme.edu   | 98765432               |
    Then the request should fail with error code "INTERNAL_ERROR"

  @ComponentTest
  Scenario: Cannot request an institution creation when a pending request with the same name already exists
    Given a pending institution creation request exists with details:
      | institutionName | institutionEmail | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail | requesterContactNumber |
      | Acme Edu        | acme@example.com | 91234567                 | 1 Acme Lane        | 200012345A     | Alice              | Lee               | alice@acme.edu | 98765432               |
    When prospect tries to request an institution to be created with details:
      | institutionName | institutionEmail  | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail | requesterContactNumber |
      | Acme Edu        | other@example.com | 91234567                 | 2 Other Lane       | 200067890B     | Bob                | Tan               | bob@acme.edu   | 98765432               |
    Then the request should fail with error code "INTERNAL_ERROR"

  @ComponentTest
  Scenario: Cannot request an institution creation when a pending request with the same email already exists
    Given a pending institution creation request exists with details:
      | institutionName | institutionEmail | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail | requesterContactNumber |
      | Acme Edu        | acme@example.com | 91234567                 | 1 Acme Lane        | 200012345A     | Alice              | Lee               | alice@acme.edu | 98765432               |
    When prospect tries to request an institution to be created with details:
      | institutionName | institutionEmail | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail | requesterContactNumber |
      | Beta Edu        | acme@example.com | 91234567                 | 2 Other Lane       | 200067890B     | Bob                | Tan               | bob@acme.edu   | 98765432               |
    Then the request should fail with error code "INTERNAL_ERROR"
