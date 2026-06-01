-- liquibase formatted sql

-- Moves the `recap` text column from `lessons` to `lesson_plans`, and
-- re-parents the `lesson_quizzes` association from `lessons` to
-- `lesson_plans`. Mirrors the LessonObjective parent-shift pattern from
-- 2026-05-29. No backfill: `lessons.recap` was never written by any
-- service, and `lesson_quizzes` has zero rows.

-- changeset kevin:2026-06-01-recap-and-quizzes-to-lesson-plan-1
ALTER TABLE public."lessons"
DROP
COLUMN "recap";

-- changeset kevin:2026-06-01-recap-and-quizzes-to-lesson-plan-2
ALTER TABLE audit."lessons_aud"
DROP
COLUMN "recap";

-- changeset kevin:2026-06-01-recap-and-quizzes-to-lesson-plan-3
ALTER TABLE public."lesson_plans"
    ADD COLUMN "recap" TEXT NOT NULL DEFAULT '';

-- changeset kevin:2026-06-01-recap-and-quizzes-to-lesson-plan-4
ALTER TABLE audit."lesson_plans_aud"
    ADD COLUMN "recap" TEXT;

-- changeset kevin:2026-06-01-recap-and-quizzes-to-lesson-plan-5
ALTER TABLE public."lesson_quizzes"
DROP
CONSTRAINT "fkhdf4r8mc9l91wr1xnv06cxekl";

-- changeset kevin:2026-06-01-recap-and-quizzes-to-lesson-plan-6
ALTER TABLE public."lesson_quizzes"
DROP
COLUMN "lesson_id";

-- changeset kevin:2026-06-01-recap-and-quizzes-to-lesson-plan-7
ALTER TABLE audit."lesson_quizzes_aud"
DROP
COLUMN "lesson_id";

-- changeset kevin:2026-06-01-recap-and-quizzes-to-lesson-plan-8
ALTER TABLE public."lesson_quizzes"
    ADD COLUMN "lesson_plan_id" UUID;

-- changeset kevin:2026-06-01-recap-and-quizzes-to-lesson-plan-9
ALTER TABLE audit."lesson_quizzes_aud"
    ADD COLUMN "lesson_plan_id" UUID;

-- changeset kevin:2026-06-01-recap-and-quizzes-to-lesson-plan-10
ALTER TABLE public."lesson_quizzes"
    ADD CONSTRAINT "fk_lesson_quizzes_lesson_plan_id"
        FOREIGN KEY ("lesson_plan_id") REFERENCES "lesson_plans" ("id")
            ON UPDATE NO ACTION ON DELETE NO ACTION;
