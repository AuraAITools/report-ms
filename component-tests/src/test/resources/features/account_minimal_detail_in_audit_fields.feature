@Api
Feature: Account Minimal Detail resolved in audit fields

  Audit fields (createdBy/updatedBy) resolve to the matching account in the
  institution. When the creating user has no account row in the tenant (e.g.
  a platform admin), the resolver falls back to the system placeholder so
  clients always receive a complete AccountMinimalDetail.

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded with details:
      | name       | email            | contactNumber | address | uen  |
      | testing123 | testgg@gmail.com | 96228693      | address | test |
    And the user switches to the institution tenant
    And an outlet exists with name "AccountMinimalDetail Test Outlet"

  @ComponentTest
  Scenario: Audit fields fall back to the system placeholder when the creator has no account in the institution
    When user fetches outlets in the institution selecting createdBy fields
    Then the response createdBy.userId should equal the aura-admin user id
    And the response createdBy.accountId should be non-null
    And the response createdBy.name should match "^system$"
    And the response createdBy.url should be empty
