@Api
Feature: Account Minimal Detail resolved in audit fields

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded with details:
      | name       | email            | contactNumber | address | uen  |
      | testing123 | testgg@gmail.com | 96228693      | address | test |
    And the user switches to the institution tenant
    And an outlet exists with name "AccountMinimalDetail Test Outlet"

  @ComponentTest
  Scenario: Aura admin fetches an outlet and sees AccountMinimalDetail resolved on audit fields
    When user fetches outlets in the institution selecting createdBy fields
    Then the response createdBy.userId should equal the aura-admin user id
    And the response createdBy.accountId should be non-null
    And the response createdBy.name should match "^.+ .+$"
    And the response createdBy.url should be null
