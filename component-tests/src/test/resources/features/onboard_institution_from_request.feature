@Api
Feature: Onboard Institution From Request

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
  Scenario: Cannot onboard an institution from a non-existent creation request
    Given user has "aura-admin" role
    When user tries to onboard the institution from creation request id "00000000-0000-0000-0000-000000000000"
    Then the request should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  Scenario: Cannot onboard an institution twice from the same creation request
    Given a pending institution creation request exists with details:
      | institutionName | institutionEmail | institutionContactNumber | institutionAddress | institutionUen | requesterFirstName | requesterLastName | requesterEmail | requesterContactNumber |
      | Acme Edu        | acme@example.com | 91234567                 | 1 Acme Lane        | 200012345A     | Alice              | Lee               | alice@acme.edu | 98765432               |
    And user has "aura-admin" role
    And user onboards the institution from the pending request
    When user tries to onboard the institution from the pending request again
    Then the request should fail with error code "INVALID_REQUEST_STATE"
