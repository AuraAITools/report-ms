-- liquibase formatted sql

-- Adds profile_image_key referenced by InstitutionModel#profileImageKey — the S3 object key
-- of the institution's profile image. Coexists with logoKey (a separate concept):
-- profile_image_key is the validated, squarish image (per ADR-005 / ProfileImageValidator);
-- logo_key remains the unvalidated corporate logo asset.
-- Nullable: not every institution has one set on creation.
-- InstitutionModel is @Audited, so the same column lands on audit.institutions_aud.

-- changeset kevin:2026-05-30-institution-profile-image-1
ALTER TABLE public.institutions
    ADD COLUMN profile_image_key VARCHAR(512);

-- changeset kevin:2026-05-30-institution-profile-image-2
ALTER TABLE audit.institutions_aud
    ADD COLUMN profile_image_key VARCHAR(512);
