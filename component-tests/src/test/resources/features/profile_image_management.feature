@Api
Feature: Profile image management

  As an institution admin
  I want to upload profile images for accounts and educators via a presigned-upload-then-confirm flow
  So that profiles display images validated for content type and dimensions

  Images are uploaded straight to S3 via a presigned url, then confirmed
  so the platform can validate content type and dimensions. An educator's
  profile image follows the same flow as account images, scoped to the
  educator profile.

  @ComponentTest
  Scenario: Account admin uploads a 400x400 PNG profile image successfully
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an account exists in the institution
    When user requests a profile image upload for the account with content type "image/png"
    And user uploads a 400x400 PNG to the profile image presigned url
    And user confirms the profile image upload
    Then the account response should include a presigned profile image url
    And the profile image url returns 200 when fetched

  @ComponentTest
  Scenario: Account admin cannot request an account profile image upload with content type application/pdf
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an account exists in the institution
    When user tries to request a profile image upload for the account with content type "application/pdf"
    Then the request should fail with a content-type rejection error

  @ComponentTest
  Scenario: Account admin cannot confirm an account profile image that is too tall (500x800)
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an account exists in the institution
    When user requests a profile image upload for the account with content type "image/png"
    And user uploads a 500x800 PNG to the profile image presigned url
    And user tries to confirm the profile image upload
    Then the request should fail with an aspect-ratio rejection error
    And no profile image key should be persisted on the account

  @ComponentTest
  Scenario: Aura admin uploads a 400x400 PNG educator profile image successfully
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Yishun Outlet"
    And an account exists in the institution
    And an educator exists in the institution
    When user requests a profile image upload for the educator with content type "image/png"
    And user uploads a 400x400 PNG to the educator profile image presigned url
    And user confirms the educator profile image upload
    Then the educator response should include a presigned profile image url
    And the educator profile image url returns 200 when fetched

  @ComponentTest
  Scenario: Aura admin cannot request an educator profile image upload with content type application/pdf
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Yishun Outlet"
    And an account exists in the institution
    And an educator exists in the institution
    When user tries to request a profile image upload for the educator with content type "application/pdf"
    Then the educator request should fail with a content-type rejection error

  @ComponentTest
  Scenario: Aura admin cannot confirm an educator profile image that is too tall (500x800)
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Yishun Outlet"
    And an account exists in the institution
    And an educator exists in the institution
    When user requests a profile image upload for the educator with content type "image/png"
    And user uploads a 500x800 PNG to the educator profile image presigned url
    And user tries to confirm the educator profile image upload
    Then the educator request should fail with an aspect-ratio rejection error
    And no profile image key should be persisted on the educator
