-- liquibase formatted sql

-- Captures public-facing requests to create a new institution. Submitted by
-- prospective institutions before any Aura account exists, so the table is
-- system-scoped (no tenant_id) and extends AuditMetadata only. Uniqueness on
-- (institution_name, institution_email) is enforced in the application layer
-- against active requests + the institutions table, because REJECTED requests
-- must be allowed to free the name for reuse.

-- changeset kevin:2026-06-07-institution-creation-requests-1
CREATE TABLE public."institution_creation_requests" (
    "id" UUID PRIMARY KEY,
    "created_at" TIMESTAMPTZ,
    "created_by" VARCHAR(255),
    "updated_at" TIMESTAMPTZ,
    "updated_by" VARCHAR(255),
    "institution_name" VARCHAR(255) NOT NULL,
    "institution_email" VARCHAR(255) NOT NULL,
    "institution_contact_number" VARCHAR(255) NOT NULL,
    "institution_address" VARCHAR(255) NOT NULL,
    "institution_uen" VARCHAR(255) NOT NULL,
    "requester_first_name" VARCHAR(255) NOT NULL,
    "requester_last_name" VARCHAR(255) NOT NULL,
    "requester_email" VARCHAR(255) NOT NULL,
    "requester_contact_number" VARCHAR(255) NOT NULL,
    "state" VARCHAR(50) NOT NULL
);

-- changeset kevin:2026-06-07-institution-creation-requests-2
CREATE INDEX "idx_institution_creation_requests_state_name"
    ON public."institution_creation_requests" ("state", "institution_name");

-- changeset kevin:2026-06-07-institution-creation-requests-3
CREATE INDEX "idx_institution_creation_requests_state_email"
    ON public."institution_creation_requests" ("state", "institution_email");
