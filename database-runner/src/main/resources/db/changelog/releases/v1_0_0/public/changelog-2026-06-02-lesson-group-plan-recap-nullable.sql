-- liquibase formatted sql

-- Makes lesson_group_plans.recap nullable. The column was originally
-- introduced as TEXT NOT NULL DEFAULT '' (see 2026-06-01-recap-and-quizzes-
-- to-lesson-plan), but a freshly created lesson group plan has no recap
-- until the lesson is taught, so the value should be null rather than an
-- empty string.

-- changeset kevin:2026-06-02-lesson-group-plan-recap-nullable-1
ALTER TABLE public."lesson_group_plans"
    ALTER COLUMN "recap" DROP NOT NULL;

-- changeset kevin:2026-06-02-lesson-group-plan-recap-nullable-2
ALTER TABLE public."lesson_group_plans"
    ALTER COLUMN "recap" DROP DEFAULT;
