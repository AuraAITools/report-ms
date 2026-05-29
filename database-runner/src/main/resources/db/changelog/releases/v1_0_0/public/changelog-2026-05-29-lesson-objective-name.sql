-- liquibase formatted sql

-- Adds the required `name` and `objective` columns to lesson_objectives so that
-- CRUD operations have a user-meaningful identifier and body. Audit shadow table
-- gets the same columns.

-- changeset kevin:2026-05-29-lesson-objective-name-1
ALTER TABLE public."lesson_objectives"
    ADD COLUMN "name" VARCHAR(255) NOT NULL DEFAULT '';

-- changeset kevin:2026-05-29-lesson-objective-name-2
ALTER TABLE audit."lesson_objectives_aud"
    ADD COLUMN "name" VARCHAR(255);

-- changeset kevin:2026-05-29-lesson-objective-name-3
ALTER TABLE public."lesson_objectives"
    ADD COLUMN "objective" TEXT NOT NULL DEFAULT '';

-- changeset kevin:2026-05-29-lesson-objective-name-4
ALTER TABLE audit."lesson_objectives_aud"
    ADD COLUMN "objective" TEXT;
