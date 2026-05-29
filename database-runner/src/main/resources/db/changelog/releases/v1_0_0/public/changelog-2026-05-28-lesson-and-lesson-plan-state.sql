-- liquibase formatted sql

-- Adds the state column referenced by LessonModel#state and LessonPlanModel#state.
-- Both columns are @Enumerated(EnumType.STRING) and @Column(nullable = false):
--   lessons.state           -> CREATED | ENDED | CANCELLED        (default CREATED)
--   lesson_plans.state      -> UNPLANNED | DRAFT | PLANNED        (default UNPLANNED)
-- DEFAULT + NOT NULL backfills existing rows in a single ALTER. New rows default
-- to the value set by the builder in the entity layer, so the default here is
-- only relevant for existing rows.
-- LessonModel and LessonPlanModel are @Audited, so the same columns land on
-- audit.lessons_aud / audit.lesson_plans_aud (nullable, no backfill — past
-- revisions had no state value).

-- changeset kevin:2026-05-28-lesson-state-1
ALTER TABLE public."lessons"
    ADD COLUMN "state" VARCHAR(255) NOT NULL DEFAULT 'CREATED';

-- changeset kevin:2026-05-28-lesson-state-2
ALTER TABLE audit."lessons_aud"
    ADD COLUMN "state" VARCHAR(255);

-- changeset kevin:2026-05-28-lesson-plan-state-1
ALTER TABLE public."lesson_plans"
    ADD COLUMN "state" VARCHAR(255) NOT NULL DEFAULT 'UNPLANNED';

-- changeset kevin:2026-05-28-lesson-plan-state-2
ALTER TABLE audit."lesson_plans_aud"
    ADD COLUMN "state" VARCHAR(255);
