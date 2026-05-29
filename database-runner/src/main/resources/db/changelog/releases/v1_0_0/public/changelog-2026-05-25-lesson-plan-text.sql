-- liquibase formatted sql

-- Adds the free-text `plan` column to lesson_plans and enforces
-- one-plan-per-lesson via a unique constraint on lesson_id.

-- changeset kevin:2026-05-25-lesson-plan-text-1
ALTER TABLE public."lesson_plans"
    ADD COLUMN "plan" TEXT NOT NULL DEFAULT '';

-- changeset kevin:2026-05-25-lesson-plan-text-2
ALTER TABLE public."lesson_plans"
    ADD CONSTRAINT "uk_lesson_plans_lesson_id" UNIQUE ("lesson_id");
