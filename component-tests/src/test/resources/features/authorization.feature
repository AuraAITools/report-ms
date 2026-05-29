@Api
Feature: SpiceDB Authorization Schema

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
      | level       |
      | subject     |
      | topic       |
