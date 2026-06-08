@Api
Feature: Manage Topics On Lesson Group Plan

  @ComponentTest @LessonGroupPlanTopics
  Scenario: Educator adds then removes topics on a lesson group plan
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Top Mgmt Outlet"
    And a course exists with name "Top Mgmt Course"
    And a lesson exists with name "Top Mgmt Lesson"
    And a topic exists with name "Algebra"
    And the user creates a lesson group plan in the lesson group with plan "Plan to manage topics" and 1 topics
    When the user removes 1 topics from the lesson group plan
    Then the lesson group plan should have 0 linked topics in the database
    When the user adds 1 topics to the lesson group plan
    Then the lesson group plan should have 1 linked topics in the database
