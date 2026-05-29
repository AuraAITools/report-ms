@PreExistingFailure
@Api
Feature: Create Material in Institution

  @ComponentTest
  Scenario: Institution admin creates a material with only a name
    Given user has "aura-admin" role
    And an institution is onboarded
    When user creates a material in the institution with
      | name        | description             |
      | Algebra 101 | Practice problems set 1 |
    Then the material should be created successfully with no file url
    And the material response should include description "Practice problems set 1"

  @ComponentTest
  Scenario: Institution admin creates a material with an uploaded PDF
    Given user has "aura-admin" role
    And an institution is onboarded
    When user requests a material upload with content type "application/pdf"
    And user uploads a PDF to the presigned url
    And user creates a material in the institution with the uploaded file and name "Worksheet"
    Then the material response should include a presigned file url
    And the material file url returns 200 when fetched

  @ComponentTest
  Scenario: Institution admin creates a material linked to topics
    Given user has "aura-admin" role
    And an institution is onboarded
    And a topic exists with name "Algebra"
    When user creates a material in the institution linked to the existing topic with name "Linked Material"
    Then the material should be created successfully with no file url
    And the material should be linked to the topic in the database
