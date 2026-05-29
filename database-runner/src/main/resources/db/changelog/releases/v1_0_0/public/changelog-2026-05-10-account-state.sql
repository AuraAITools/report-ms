-- liquibase formatted sql

-- Adds the state column referenced by AccountModel#state, which was introduced
-- in commit c77c40c (acount creation logic) without a matching DB migration.
-- The column is @Enumerated(EnumType.STRING) over VERIFYING / ACTIVE / RETIRED
-- and is nullable on the entity, so the column is also nullable here.
-- The application is responsible for setting state on new rows; we do not
-- carry a column-level DEFAULT.
-- The UPDATE backfills already-existing rows with ACTIVE so onboarded
-- accounts keep working without a DB reset.
-- AccountModel is @Audited, so the same column lands on audit.accounts_aud
-- (nullable, no backfill — past revisions had no state value).

-- changeset kevin:2026-05-10-account-state-1
ALTER TABLE public.accounts
    ADD COLUMN state VARCHAR(255);

-- changeset kevin:2026-05-10-account-state-2
UPDATE public.accounts
SET state = 'ACTIVE'
WHERE state IS NULL;

-- changeset kevin:2026-05-10-account-state-3
ALTER TABLE audit.accounts_aud
    ADD COLUMN state VARCHAR(255);
