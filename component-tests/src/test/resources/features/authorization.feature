@Api
Feature: Keycloak Authorization Configuration

  @ComponentTest
  Scenario: Keycloak has the required policies
    Given user has "aura-admin" role
    Then Keycloak should have a "aura-admin-policy" policy of type "group"
    And Keycloak should have a "aura-admin-institution-access" policy of type "resource"
    And Keycloak should have a "aura-admin-outlet-access" policy of type "resource"
    And Keycloak should have a "aura-admin-course-access" policy of type "resource"
    And Keycloak should have a "aura-admin-lesson-access" policy of type "resource"
    And Keycloak should have a "aura-admin-subject-access" policy of type "resource"
    And Keycloak should have a "aura-admin-topic-access" policy of type "resource"
    And Keycloak should have a "aura-admin-account-access" policy of type "resource"
    And Keycloak should have a "aura-admin-level-access" policy of type "resource"
