-- liquibase formatted sql

-- Adds three boolean role-binding flags on public.accounts:
--   parent_feature_enabled, educator_feature_enabled, staff_feature_enabled
-- These distinguish whether an account is acting in a given role (parent /
-- educator / staff). Backfilled to FALSE for existing rows; application sets
-- them explicitly on creation/update. AccountModel is @Audited so the same
-- columns land on audit.accounts_aud (nullable, no backfill — past revisions
-- had no flag value).

-- changeset kevin:2026-05-27-account-feature-flags-1
ALTER TABLE public.accounts
    ADD COLUMN parent_feature_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN educator_feature_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN staff_feature_enabled BOOLEAN NOT NULL DEFAULT FALSE;

-- changeset kevin:2026-05-27-account-feature-flags-2
ALTER TABLE audit.accounts_aud
    ADD COLUMN parent_feature_enabled BOOLEAN,
    ADD COLUMN educator_feature_enabled BOOLEAN,
    ADD COLUMN staff_feature_enabled BOOLEAN;
