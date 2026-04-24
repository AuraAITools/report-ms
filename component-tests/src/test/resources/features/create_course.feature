@Api
Feature: Create Course

  @ComponentTest
  Scenario: Aura admin successfully creates a course in an outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Yishun Outlet"
    When user creates a course with details:
      | name        | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Test Course | 20      | WEEKLY          | 200.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-12-31T09:00:00Z |
    Then the course should be created successfully
    And a UMA resource should be registered for the course with type "urn:aura:course" and scopes "view,manage,delegate"
