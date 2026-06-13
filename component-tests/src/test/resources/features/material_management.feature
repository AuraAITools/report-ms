@Api
Feature: Material management
  As an institution admin
  I want to create teaching materials, optionally with uploaded files and topic links
  So that lesson plans can reference shared curriculum resources

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded

  @ComponentTest
  Scenario: Institution admin creates a material with only a name
    When user creates a material in the institution with
      | name        | description             |
      | Algebra 101 | Practice problems set 1 |
    Then the material should be created successfully with no file url
    And the material response should include description "Practice problems set 1"

  @ComponentTest
  Scenario: Institution admin creates a material with an uploaded PDF
    When user requests a material upload with content type "application/pdf"
    And user uploads a PDF to the presigned url
    And user creates a material in the institution with the uploaded file and name "Worksheet"
    Then the material response should include a presigned file url
    And the material file url returns 200 when fetched

  @ComponentTest
  Scenario: Institution admin creates a material linked to topics
    Given a topic exists with name "Algebra"
    When user creates a material in the institution linked to the existing topic with name "Linked Material"
    Then the material should be created successfully with no file url
    And the material should be linked to the topic in the database

  @ComponentTest
  Scenario: Institution admin lists materials in the institution
    Given a material exists with name "Worksheet A"
    And a material exists with name "Worksheet B"
    When user lists materials in the institution
    Then the material list should have 2 entries
    And the material list should contain the material "Worksheet A"
    And the material list should contain the material "Worksheet B"

  @ComponentTest
  Scenario: Institution admin retrieves a material by id
    Given a material exists with name "Worksheet A"
    When user retrieves the material by its id
    Then the material response should have name "Worksheet A"

  @ComponentTest
  Scenario: Institution admin deletes a material
    Given a material exists with name "Worksheet A"
    When user deletes the material by id
    Then the material delete should succeed
    And listing materials in the institution should return 0 entries

  # Authorization for getMaterialById/delete is evaluated against the material id before any lookup —
  # a non-existent material is indistinguishable from a forbidden one (no id enumeration).

  @ComponentTest
  Scenario: Institution admin fails to delete a non-existent material
    When user tries to delete a material with a random id
    Then the request should fail with error code "FORBIDDEN"
