-- liquibase formatted sql

-- changeset kevin:2026-05-25-lesson-plan-text-audit-1
ALTER TABLE audit."lesson_plans_aud"
    ADD COLUMN "plan" TEXT;
