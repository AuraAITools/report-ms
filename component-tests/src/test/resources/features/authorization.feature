@Api
Feature: Keycloak Authorization Configuration

  @ComponentTest
  Scenario: Keycloak has the required resources with correct scopes
    Given user has "aura-admin" role
    Then Keycloak should have a "institution" resource with scopes "create,view,manage,delete,delegate"
    And Keycloak should have a "outlet" resource with scopes "create,view,manage,delete,delegate"
    And Keycloak should have a "course" resource with scopes "create,view,manage,delete"
    And Keycloak should have a "lesson" resource with scopes "create,view,manage,delete"
    And Keycloak should have a "subject" resource with scopes "create,view,manage,delete"
    And Keycloak should have a "topic" resource with scopes "create,view,manage,delete"
    And Keycloak should have a "account" resource with scopes "create,view,manage,delete"
    And Keycloak should have a "level" resource with scopes "view"

  @ComponentTest
  Scenario: Keycloak has the required policies
    Given user has "aura-admin" role
    Then Keycloak should have a "aura-admin-policy" policy of type "role"
    And Keycloak should have a "aura-admin-all-access" policy of type "scope"
