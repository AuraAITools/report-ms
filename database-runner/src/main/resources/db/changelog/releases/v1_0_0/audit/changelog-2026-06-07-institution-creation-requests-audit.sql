-- liquibase formatted sql

-- Envers shadow table for institution_creation_requests. InstitutionCreationRequestModel
-- is @Audited, so every insert/update lands here as a revision row.

-- changeset kevin:2026-06-07-institution-creation-requests-audit-1
CREATE TABLE audit."institution_creation_requests_aud" (
    "id" UUID NOT NULL,
    "rev" INTEGER NOT NULL,
    "revtype" SMALLINT,
    "created_at" TIMESTAMPTZ,
    "created_by" VARCHAR(255),
    "updated_at" TIMESTAMPTZ,
    "updated_by" VARCHAR(255),
    "institution_name" VARCHAR(255),
    "institution_email" VARCHAR(255),
    "institution_contact_number" VARCHAR(255),
    "institution_address" VARCHAR(255),
    "institution_uen" VARCHAR(255),
    "requester_first_name" VARCHAR(255),
    "requester_last_name" VARCHAR(255),
    "requester_email" VARCHAR(255),
    "requester_contact_number" VARCHAR(255),
    "state" VARCHAR(50),
    PRIMARY KEY ("id", "rev")
);
