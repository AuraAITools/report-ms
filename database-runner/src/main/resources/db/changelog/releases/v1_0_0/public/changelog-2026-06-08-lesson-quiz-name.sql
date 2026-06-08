-- liquibase formatted sql

-- Adds the required `name` column to lesson_quizzes so that quizzes have a
-- user-meaningful label in addition to score/maxScore. Audit shadow table
-- gets the same column (nullable, per audit-table convention).

-- changeset kevin:2026-06-08-lesson-quiz-name-1
ALTER TABLE public."lesson_quizzes"
    ADD COLUMN "name" VARCHAR(255) NOT NULL DEFAULT '';

-- changeset kevin:2026-06-08-lesson-quiz-name-2
ALTER TABLE audit."lesson_quizzes_aud"
    ADD COLUMN "name" VARCHAR(255);
