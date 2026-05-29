-- liquibase formatted sql

-- Backfill: rename legacy state value INITIALISING -> VERIFYING in accounts rows
-- created before the Account.STATE enum was renamed. The JPA AccountModel.STATE
-- enum no longer recognises INITIALISING, so any pre-existing row with
-- state = 'INITIALISING' fails to load with:
--   org.springframework.dao.InvalidDataAccessApiUsageException: No enum constant
--   ...AccountModel.STATE.INITIALISING
-- AccountModel is @Audited, so the same backfill is applied to audit.accounts_aud
-- to keep history readable.

-- changeset kevin:2026-05-26-account-state-verifying-rename-1
UPDATE public.accounts
SET state = 'VERIFYING'
WHERE state = 'INITIALISING';

-- changeset kevin:2026-05-26-account-state-verifying-rename-2
UPDATE audit.accounts_aud
SET state = 'VERIFYING'
WHERE state = 'INITIALISING';
