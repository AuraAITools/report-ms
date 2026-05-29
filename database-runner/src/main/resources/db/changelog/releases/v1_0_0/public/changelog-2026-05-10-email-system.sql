-- liquibase formatted sql

-- changeset kevin:2026-05-10-email-1
CREATE TABLE public.email_log
(
    id                  UUID         NOT NULL,
    tenant_id           VARCHAR(255) NOT NULL,
    recipient_email     VARCHAR(255) NOT NULL,
    recipient_user_id   VARCHAR(255),
    template_name       VARCHAR(255) NOT NULL,
    subject             VARCHAR(500) NOT NULL,
    dedup_key           VARCHAR(500) NOT NULL,
    provider_message_id VARCHAR(255),
    status              VARCHAR(32)  NOT NULL,
    error               TEXT,
    sent_at             TIMESTAMP WITH TIME ZONE,
    created_at          TIMESTAMP WITH TIME ZONE,
    updated_at          TIMESTAMP WITH TIME ZONE,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255),
    version             BIGINT,
    CONSTRAINT email_log_pkey PRIMARY KEY (id)
);

-- changeset kevin:2026-05-10-email-2
CREATE UNIQUE INDEX uk_email_log_active_dedup
    ON public.email_log (tenant_id, dedup_key) WHERE status IN ('PENDING', 'SENT');

-- changeset kevin:2026-05-10-email-3
CREATE INDEX idx_email_log_recipient
    ON public.email_log (tenant_id, recipient_user_id, sent_at DESC);

-- changeset kevin:2026-05-10-email-4
ALTER TABLE public.accounts
    ADD COLUMN email_opted_out BOOLEAN NOT NULL DEFAULT FALSE;

-- changeset kevin:2026-05-10-email-5
CREATE TABLE public.shedlock
(
    name       VARCHAR(64)              NOT NULL,
    lock_until TIMESTAMP WITH TIME ZONE NOT NULL,
    locked_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    locked_by  VARCHAR(255)             NOT NULL,
    CONSTRAINT shedlock_pkey PRIMARY KEY (name)
);

-- AccountModel is @Audited, so the email_opted_out column added above also
-- needs to exist on the Envers shadow table. Nullable, no backfill — past
-- revisions had no opt-out flag.
-- changeset kevin:2026-05-10-email-6
ALTER TABLE audit.accounts_aud
    ADD COLUMN email_opted_out BOOLEAN;
