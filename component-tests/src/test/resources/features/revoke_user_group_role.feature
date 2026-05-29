@Api
Feature: Revoke user group role

  @ComponentTest
  Scenario: System group_admin revokes institution admin from a user
    Given user has "aura-admin" role
    And the aura admin is a group_admin of system_user_group "aura"
    And the institution with id "inst-1" exists in SpiceDB
    And user "target-user-1" has role "admin" on "institution_user_group" "inst-1"
    When the user revokes role "ADMIN" on resource "INSTITUTION_USER_GROUP" "inst-1" from user "target-user-1"
    Then the revoke request should succeed
    And SpiceDB should not have relationship "institution_user_group:inst-1#admin@user:target-user-1"

  @ComponentTest
  Scenario: Non-group_admin user cannot revoke any role
    Given user has "aura-admin" role
    And the aura admin has no group_admin role anywhere
    And the institution with id "inst-1" exists in SpiceDB
    And user "target-user-1" has role "admin" on "institution_user_group" "inst-1"
    When the user tries to revoke role "ADMIN" on resource "INSTITUTION_USER_GROUP" "inst-1" from user "target-user-1"
    Then the request should fail with http status 403
    And SpiceDB should have relationship "institution_user_group:inst-1#admin@user:target-user-1"
