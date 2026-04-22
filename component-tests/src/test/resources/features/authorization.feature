@Api
Feature: Keycloak Authorization Server initial configuration

  @ComponentTest
  Scenario: Keycloak has the required policies
    Given Keycloak is set up
    Then Keycloak should have the following policies:
      | name                              | type     |
      | aura-admin-policy                 | group    |
      | aura-admin-institution-access     | resource |
      | aura-admin-outlet-access          | resource |
      | aura-admin-course-access          | resource |
      | aura-admin-lesson-access          | resource |
      | aura-admin-subject-access         | resource |
      | aura-admin-topic-access           | resource |
      | aura-admin-account-access         | resource |
      | aura-admin-level-access           | resource |
