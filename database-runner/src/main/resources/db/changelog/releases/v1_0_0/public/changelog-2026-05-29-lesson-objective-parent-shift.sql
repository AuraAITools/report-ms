-- liquibase formatted sql

-- Re-parents LessonObjective from Lesson to LessonPlan: the one-to-many
-- lessonObjectives collection moved from LessonModel to LessonPlanModel.
-- public.lesson_objectives.lesson_id is dropped and replaced with
-- lesson_plan_id pointing at lesson_plans(id). Since LessonObjectiveModel is
-- @Audited, audit.lesson_objectives_aud gets the same column shift (no FK on
-- audit rows by convention).

-- changeset kevin:2026-05-29-lesson-objective-parent-shift-1
ALTER TABLE public."lesson_objectives"
DROP
CONSTRAINT "fkdtpxux294qelayd20cq0lh8up";

-- changeset kevin:2026-05-29-lesson-objective-parent-shift-2
ALTER TABLE public."lesson_objectives"
DROP
COLUMN "lesson_id";

-- changeset kevin:2026-05-29-lesson-objective-parent-shift-3
ALTER TABLE public."lesson_objectives"
    ADD COLUMN "lesson_plan_id" UUID;

-- changeset kevin:2026-05-29-lesson-objective-parent-shift-4
ALTER TABLE public."lesson_objectives"
    ADD CONSTRAINT "fk_lesson_objectives_lesson_plan_id"
        FOREIGN KEY ("lesson_plan_id") REFERENCES "lesson_plans" ("id")
            ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:2026-05-29-lesson-objective-parent-shift-5
ALTER TABLE audit."lesson_objectives_aud"
DROP
COLUMN "lesson_id";

-- changeset kevin:2026-05-29-lesson-objective-parent-shift-6
ALTER TABLE audit."lesson_objectives_aud"
    ADD COLUMN "lesson_plan_id" UUID;
