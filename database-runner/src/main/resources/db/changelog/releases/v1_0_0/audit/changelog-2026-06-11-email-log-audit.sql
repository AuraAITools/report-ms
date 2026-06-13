-- liquibase formatted sql

-- Envers shadow table for email_log. EmailLogModel is @Audited, so every
-- insert/update lands here as a revision row. Columns mirror public.email_log
-- (all nullable, as Envers conventions require) plus the rev/revtype columns.

-- changeset kevin:2026-06-11-email-log-audit-1
CREATE TABLE audit."email_log_aud" (
    "id" UUID NOT NULL,
    "rev" INTEGER NOT NULL,
    "revtype" SMALLINT,
    "version" BIGINT,
    "created_at" TIMESTAMPTZ,
    "created_by" VARCHAR(255),
    "updated_at" TIMESTAMPTZ,
    "updated_by" VARCHAR(255),
    "tenant_id" VARCHAR(255),
    "recipient_email" VARCHAR(255),
    "recipient_user_id" VARCHAR(255),
    "template_name" VARCHAR(255),
    "subject" VARCHAR(500),
    "dedup_key" VARCHAR(500),
    "provider_message_id" VARCHAR(255),
    "status" VARCHAR(32),
    "error" TEXT,
    "sent_at" TIMESTAMPTZ,
    PRIMARY KEY ("id", "rev")
);

-- changeset kevin:2026-06-11-email-log-audit-2
ALTER TABLE audit."email_log_aud"
    ADD CONSTRAINT "fk_email_log_aud_rev"
        FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev")
            ON UPDATE NO ACTION ON DELETE NO ACTION;
