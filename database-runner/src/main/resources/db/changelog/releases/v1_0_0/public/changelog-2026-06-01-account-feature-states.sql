-- liquibase formatted sql

-- Replaces the three boolean role-binding flags on public.accounts with
-- per-feature lifecycle state columns:
--   parent_feature_state, educator_feature_state, staff_feature_state
-- Values: NOT_ONBOARDED, ONBOARDING_IN_PROCESS, ACTIVE, INACTIVE.
-- Backfill: an existing boolean = true is treated as ACTIVE (the prior
-- semantics of "enabled"); false is NOT_ONBOARDED (the new default).
-- AccountModel is @Audited so the same columns are added to audit.accounts_aud
-- (nullable, no backfill — past revisions had no state value).

-- changeset kevin:2026-06-01-account-feature-states-1
ALTER TABLE public.accounts
    ADD COLUMN parent_feature_state VARCHAR(32) NOT NULL DEFAULT 'NOT_ONBOARDED',
    ADD COLUMN educator_feature_state VARCHAR(32) NOT NULL DEFAULT 'NOT_ONBOARDED',
    ADD COLUMN staff_feature_state VARCHAR(32) NOT NULL DEFAULT 'NOT_ONBOARDED';

-- changeset kevin:2026-06-01-account-feature-states-2
UPDATE public.accounts
SET parent_feature_state   = CASE WHEN parent_feature_enabled   THEN 'ACTIVE' ELSE 'NOT_ONBOARDED' END,
    educator_feature_state = CASE WHEN educator_feature_enabled THEN 'ACTIVE' ELSE 'NOT_ONBOARDED' END,
    staff_feature_state    = CASE WHEN staff_feature_enabled    THEN 'ACTIVE' ELSE 'NOT_ONBOARDED' END;

-- changeset kevin:2026-06-01-account-feature-states-3
ALTER TABLE public.accounts
    DROP COLUMN parent_feature_enabled,
    DROP COLUMN educator_feature_enabled,
    DROP COLUMN staff_feature_enabled;

-- changeset kevin:2026-06-01-account-feature-states-4
ALTER TABLE audit.accounts_aud
    ADD COLUMN parent_feature_state VARCHAR(32),
    ADD COLUMN educator_feature_state VARCHAR(32),
    ADD COLUMN staff_feature_state VARCHAR(32);

-- changeset kevin:2026-06-01-account-feature-states-5
ALTER TABLE audit.accounts_aud
    DROP COLUMN parent_feature_enabled,
    DROP COLUMN educator_feature_enabled,
    DROP COLUMN staff_feature_enabled;
