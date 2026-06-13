@Api
Feature: Access control

  As a platform operator
  I want the SpiceDB authorization schema loaded, user group roles granted and revoked, and permissions visible to users
  So that access to platform resources is correctly enforced and auditable

  @ComponentTest
  Scenario: SpiceDB has the authorization schema loaded
    Given SpiceDB is set up with the authorization schema
    Then the schema should define the following resource types:
      | type        |
      | user        |
      | system      |
      | institution |
      | outlet      |
      | course      |
      | lesson      |
      | account     |

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

  @ComponentTest
  Scenario: Aura admin successfully looks up their permissions
    Given an institution is onboarded
    When the aura admin looks up their permissions
    Then the response should contain permissions for resource type "system" with resource id "aura" including:
      | view | update | delete | create_institution |
    And the response should contain permissions for resource type "institution" including:
      | view | update | delete | create_account | create_outlet | create_level | create_subject | create_topic | create_material | create_school | delete_school |

  @ComponentTest
  Scenario: User looks up their user_group permissions scoped to an institution
    Given an institution is onboarded
    When the user looks up their group permissions for the institution
    Then the group permissions response should contain resource type "institution_user_group"
    And every group permissions entry should have a non-empty displayName

  @ComponentTest
  Scenario: User_group permissions response is scoped to the requested institution only
    Given an institution is onboarded
    When the user looks up their group permissions for the institution
    Then the group permissions response should not contain resource type "system_user_group"
