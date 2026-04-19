@Api
Feature: Get Outlets By Page

  @ComponentTest
  Scenario: Successfully retrieve outlets by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And user switches to the institution tenant
    And an outlet exists with name "Paginated Outlet"
    When user queries outlets by page
    Then at least one outlet should be returned
