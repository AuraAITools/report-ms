-- liquibase formatted sql

-- Adds profile_image_key referenced by EducatorModel#profileImageKey — the S3 object key
-- of the educator's profile image. Nullable: not every educator has a profile image set,
-- and the column exists before any image is uploaded. The application layer pre-signs
-- the GET URL on read via AssetUrlService; only the key is persisted here.
-- EducatorModel is @Audited, so the same column lands on audit.educators_aud (nullable,
-- no backfill — past revisions had no profile image).

-- changeset kevin:2026-06-01-educator-profile-image-1
ALTER TABLE public.educators
    ADD COLUMN profile_image_key VARCHAR(512);

-- changeset kevin:2026-06-01-educator-profile-image-2
ALTER TABLE audit.educators_aud
    ADD COLUMN profile_image_key VARCHAR(512);
