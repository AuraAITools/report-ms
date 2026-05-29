-- liquibase formatted sql

-- changeset kevin:2026-05-18-001
CREATE TABLE audit."subject_lesson_links_aud"
(
    "rev"        INTEGER NOT NULL,
    "revtype"    SMALLINT,
    "id"         UUID    NOT NULL,
    "lesson_id"  UUID,
    "subject_id" UUID,
    CONSTRAINT "subject_lesson_links_aud_pkey" PRIMARY KEY ("rev", "id")
);

-- changeset kevin:2026-05-18-002
ALTER TABLE audit."subject_lesson_links_aud"
    ADD CONSTRAINT "fk_subject_lesson_links_aud_rev"
        FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev")
            ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:2026-05-18-003
CREATE TABLE audit."topic_lesson_links_aud"
(
    "rev"       INTEGER NOT NULL,
    "revtype"   SMALLINT,
    "id"        UUID    NOT NULL,
    "lesson_id" UUID,
    "topic_id"  UUID,
    CONSTRAINT "topic_lesson_links_aud_pkey" PRIMARY KEY ("rev", "id")
);

-- changeset kevin:2026-05-18-004
ALTER TABLE audit."topic_lesson_links_aud"
    ADD CONSTRAINT "fk_topic_lesson_links_aud_rev"
        FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev")
            ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:2026-05-18-005
CREATE TABLE audit."level_lesson_links_aud"
(
    "rev"       INTEGER NOT NULL,
    "revtype"   SMALLINT,
    "id"        UUID    NOT NULL,
    "lesson_id" UUID,
    "level_id"  UUID,
    CONSTRAINT "level_lesson_links_aud_pkey" PRIMARY KEY ("rev", "id")
);

-- changeset kevin:2026-05-18-006
ALTER TABLE audit."level_lesson_links_aud"
    ADD CONSTRAINT "fk_level_lesson_links_aud_rev"
        FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev")
            ON UPDATE NO ACTION ON DELETE NO ACTION;
