-- liquibase formatted sql

-- Adds the state column referenced by InstitutionModel#state. The column is
-- @Enumerated(EnumType.STRING) over PENDING / ACTIVE / INACTIVE. New institutions
-- created via the GraphQL endpoint default to PENDING (set by the application
-- mapper). The column is nullable at the DB level; the entity layer enforces
-- @Column(nullable = false) so inserts fail if state is unset.
-- The UPDATE backfills already-onboarded institutions with ACTIVE so existing
-- tenants keep working without a DB reset.
-- InstitutionModel is @Audited, so the same column lands on audit.institutions_aud
-- (nullable, no backfill — past revisions had no state value).

-- changeset kevin:2026-05-26-institution-state-1
ALTER TABLE public.institutions
    ADD COLUMN state VARCHAR(255);

-- changeset kevin:2026-05-26-institution-state-2
UPDATE public.institutions
SET state = 'ACTIVE'
WHERE state IS NULL;

-- changeset kevin:2026-05-26-institution-state-3
ALTER TABLE audit.institutions_aud
    ADD COLUMN state VARCHAR(255);
