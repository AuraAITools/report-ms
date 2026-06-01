-- liquibase formatted sql

-- Adds profile_image_key for StudentModel and ParentModel — the S3 object key of each
-- entity's profile image. Same pattern as accounts.profile_image_key (ADR-005):
-- validated squarish image, key persisted only after ProfileImageValidator accepts.
-- Both models are @Audited so the same column lands on the *_aud tables.

-- changeset kevin:2026-05-30-student-profile-image-1
ALTER TABLE public.students
    ADD COLUMN profile_image_key VARCHAR(512);

-- changeset kevin:2026-05-30-student-profile-image-2
ALTER TABLE audit.students_aud
    ADD COLUMN profile_image_key VARCHAR(512);

-- changeset kevin:2026-05-30-parent-profile-image-1
ALTER TABLE public.parents
    ADD COLUMN profile_image_key VARCHAR(512);

-- changeset kevin:2026-05-30-parent-profile-image-2
ALTER TABLE audit.parents_aud
    ADD COLUMN profile_image_key VARCHAR(512);
