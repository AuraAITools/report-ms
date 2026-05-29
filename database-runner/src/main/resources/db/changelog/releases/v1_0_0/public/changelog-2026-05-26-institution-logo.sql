-- liquibase formatted sql

-- Adds logo_key referenced by InstitutionModel#logoKey — stores the S3 object key
-- of the institution's logo image. Nullable: not every institution has a logo,
-- and the column exists before any logo is uploaded. The application layer pre-signs
-- the URL on read via AssetUrlService; the key alone is persisted.
-- InstitutionModel is @Audited, so the same column lands on audit.institutions_aud
-- (nullable, no backfill — past revisions had no logo).

-- changeset kevin:2026-05-26-institution-logo-1
ALTER TABLE public.institutions
    ADD COLUMN logo_key VARCHAR(512);

-- changeset kevin:2026-05-26-institution-logo-2
ALTER TABLE audit.institutions_aud
    ADD COLUMN logo_key VARCHAR(512);
