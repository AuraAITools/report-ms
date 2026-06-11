@Api
Feature: Upload educator profile image

  An educator's profile image follows the same presigned-upload-then-confirm
  flow as account images, scoped to the educator profile.

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Yishun Outlet"
    And an account exists in the institution
    And an educator exists in the institution

  @ComponentTest
  Scenario: Aura admin uploads a 400x400 PNG educator profile image successfully
    When user requests a profile image upload for the educator with content type "image/png"
    And user uploads a 400x400 PNG to the educator profile image presigned url
    And user confirms the educator profile image upload
    Then the educator response should include a presigned profile image url
    And the educator profile image url returns 200 when fetched

  @ComponentTest
  Scenario: Request rejected when content type is application/pdf
    When user tries to request a profile image upload for the educator with content type "application/pdf"
    Then the educator request should fail with a content-type rejection error

  @ComponentTest
  Scenario: Confirm rejected when the uploaded image is too tall (500x800)
    When user requests a profile image upload for the educator with content type "image/png"
    And user uploads a 500x800 PNG to the educator profile image presigned url
    And user tries to confirm the educator profile image upload
    Then the educator request should fail with an aspect-ratio rejection error
    And no profile image key should be persisted on the educator
