Feature: Onboarding

  Scenario: Institution Onboarding
    Given new Institution requested to Onboard
    When Onboarding is triggered
    Then Institution should be onboarded
    And User should get an email