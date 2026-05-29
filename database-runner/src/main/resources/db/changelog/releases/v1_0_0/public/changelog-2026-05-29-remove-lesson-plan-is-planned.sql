-- liquibase formatted sql

-- Drops the is_planned column previously on LessonPlanModel. The boolean was
-- superseded by the lesson_plans.state enum (UNPLANNED | DRAFT | PLANNED) added
-- in changelog-2026-05-28-lesson-and-lesson-plan-state.sql, which encodes the
-- same information with finer granularity.
-- LessonPlanModel is @Audited, so the column is dropped from
-- audit.lesson_plans_aud as well.

-- changeset kevin:2026-05-29-remove-lesson-plan-is-planned-1
ALTER TABLE public."lesson_plans"
DROP
COLUMN "is_planned";

-- changeset kevin:2026-05-29-remove-lesson-plan-is-planned-2
ALTER TABLE audit."lesson_plans_aud"
DROP
COLUMN "is_planned";
