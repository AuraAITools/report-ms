@Api
Feature: Grant user group role

  @ComponentTest
  Scenario: System group_admin grants institution admin role
    Given user has "aura-admin" role
    And the aura admin is a group_admin of system_user_group "aura"
    And the institution with id "inst-1" exists in SpiceDB
    When the user grants role "ADMIN" on resource "INSTITUTION_USER_GROUP" "inst-1" to user "target-user-1"
    Then the grant request should succeed
    And SpiceDB should have relationship "institution_user_group:inst-1#admin@user:target-user-1"

  @ComponentTest
  Scenario: System group_admin grants outlet modifier role via system parent cascade
    Given user has "aura-admin" role
    And the aura admin is a group_admin of system_user_group "aura"
    And the outlet with id "outlet-1" exists in SpiceDB under institution "inst-1"
    When the user grants role "MODIFIER" on resource "OUTLET_USER_GROUP" "outlet-1" to user "target-user-1"
    Then the grant request should succeed
    And SpiceDB should have relationship "outlet_user_group:outlet-1#modifier@user:target-user-1"

  @ComponentTest
  Scenario: System group_admin grants meta-role GROUP_ADMIN on institution
    Given user has "aura-admin" role
    And the aura admin is a group_admin of system_user_group "aura"
    And the institution with id "inst-1" exists in SpiceDB
    When the user grants role "GROUP_ADMIN" on resource "INSTITUTION_USER_GROUP" "inst-1" to user "target-user-1"
    Then the grant request should succeed
    And SpiceDB should have relationship "institution_user_group:inst-1#group_admin@user:target-user-1"

  @ComponentTest
  Scenario: Non-group_admin user cannot grant any role
    Given user has "aura-admin" role
    And the aura admin has no group_admin role anywhere
    And the institution with id "inst-1" exists in SpiceDB
    When the user tries to grant role "ADMIN" on resource "INSTITUTION_USER_GROUP" "inst-1" to user "target-user-1"
    Then the request should fail with http status 403
    And SpiceDB should not have relationship "institution_user_group:inst-1#admin@user:target-user-1"
