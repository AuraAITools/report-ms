-- liquibase formatted sql

-- Renames file_url -> file_key on public.materials to reflect that we now store the S3
-- object key, not a fully-qualified URL. Presigned GET URLs are generated on read by
-- AssetUrlService. Also adds description column to capture optional material context.
-- MaterialModel is @Audited, so the same changes apply to audit.materials_aud.

-- changeset kevin:2026-05-27-materials-file-key-1
ALTER TABLE public.materials
    RENAME COLUMN file_url TO file_key;

-- changeset kevin:2026-05-27-materials-file-key-2
ALTER TABLE public.materials
ALTER
COLUMN file_key TYPE VARCHAR(512);

-- changeset kevin:2026-05-27-materials-description-1
ALTER TABLE public.materials
    ADD COLUMN description VARCHAR(2000);

-- changeset kevin:2026-05-27-materials-file-key-3
ALTER TABLE audit.materials_aud
    RENAME COLUMN file_url TO file_key;

-- changeset kevin:2026-05-27-materials-file-key-4
ALTER TABLE audit.materials_aud
ALTER
COLUMN file_key TYPE VARCHAR(512);

-- changeset kevin:2026-05-27-materials-description-2
ALTER TABLE audit.materials_aud
    ADD COLUMN description VARCHAR(2000);
