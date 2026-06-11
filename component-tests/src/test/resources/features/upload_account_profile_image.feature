@Api
Feature: Upload account profile image

  An account holder personalises their account with a profile image.
  Images are uploaded straight to S3 via a presigned url, then confirmed
  so the platform can validate content type and dimensions.

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an account exists in the institution

  @ComponentTest
  Scenario: Account admin uploads a 400x400 PNG profile image successfully
    When user requests a profile image upload for the account with content type "image/png"
    And user uploads a 400x400 PNG to the profile image presigned url
    And user confirms the profile image upload
    Then the account response should include a presigned profile image url
    And the profile image url returns 200 when fetched

  @ComponentTest
  Scenario: Request rejected when content type is application/pdf
    When user tries to request a profile image upload for the account with content type "application/pdf"
    Then the request should fail with a content-type rejection error

  @ComponentTest
  Scenario: Confirm rejected when the uploaded image is too tall (500x800)
    When user requests a profile image upload for the account with content type "image/png"
    And user uploads a 500x800 PNG to the profile image presigned url
    And user tries to confirm the profile image upload
    Then the request should fail with an aspect-ratio rejection error
    And no profile image key should be persisted on the account
