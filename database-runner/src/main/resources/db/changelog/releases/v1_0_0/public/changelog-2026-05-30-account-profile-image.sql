-- liquibase formatted sql

-- Adds profile_image_key referenced by AccountModel#profileImageKey — the S3 object key
-- of the account's profile image. Nullable: not every account has a profile image set,
-- and the column exists before any image is uploaded. The application layer pre-signs
-- the GET URL on read via AssetUrlService; only the key is persisted here.
-- AccountModel is @Audited, so the same column lands on audit.accounts_aud (nullable,
-- no backfill — past revisions had no profile image).

-- changeset kevin:2026-05-30-account-profile-image-1
ALTER TABLE public.accounts
    ADD COLUMN profile_image_key VARCHAR(512);

-- changeset kevin:2026-05-30-account-profile-image-2
ALTER TABLE audit.accounts_aud
    ADD COLUMN profile_image_key VARCHAR(512);
