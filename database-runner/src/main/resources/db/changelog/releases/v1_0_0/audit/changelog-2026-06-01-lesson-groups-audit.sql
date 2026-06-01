-- liquibase formatted sql

-- Audit (Envers) shadow tables for the lesson_groups restructuring.

-- changeset kevin:2026-06-01-lesson-groups-audit-1
CREATE TABLE audit."lesson_groups_aud" (
    "id" UUID NOT NULL,
    "rev" INTEGER NOT NULL,
    "revtype" SMALLINT,
    "version" BIGINT,
    "created_at" TIMESTAMPTZ,
    "created_by" VARCHAR(255),
    "updated_at" TIMESTAMPTZ,
    "updated_by" VARCHAR(255),
    "tenant_id" VARCHAR(255),
    "name" VARCHAR(255),
    "lesson_id" UUID,
    PRIMARY KEY ("id", "rev")
);

-- changeset kevin:2026-06-01-lesson-groups-audit-2
TRUNCATE TABLE audit."student_lesson_links_aud",
               audit."lesson_plan_materials_links_aud",
               audit."lesson_plan_topic_links_aud",
               audit."lesson_objectives_aud",
               audit."lesson_quizzes_aud",
               audit."lesson_plans_aud";

-- changeset kevin:2026-06-01-lesson-groups-audit-3
ALTER TABLE audit."lesson_plans_aud" RENAME TO "lesson_group_plans_aud";

-- changeset kevin:2026-06-01-lesson-groups-audit-4
ALTER TABLE audit."lesson_group_plans_aud" DROP COLUMN "lesson_id";

-- changeset kevin:2026-06-01-lesson-groups-audit-5
ALTER TABLE audit."lesson_group_plans_aud" ADD COLUMN "lesson_group_id" UUID;

-- changeset kevin:2026-06-01-lesson-groups-audit-6
ALTER TABLE audit."lesson_plan_materials_links_aud" RENAME TO "lesson_group_plan_materials_links_aud";

-- changeset kevin:2026-06-01-lesson-groups-audit-7
ALTER TABLE audit."lesson_group_plan_materials_links_aud" RENAME COLUMN "lesson_plan_id" TO "lesson_group_plan_id";

-- changeset kevin:2026-06-01-lesson-groups-audit-8
ALTER TABLE audit."lesson_plan_topic_links_aud" RENAME TO "lesson_group_plan_topic_links_aud";

-- changeset kevin:2026-06-01-lesson-groups-audit-9
ALTER TABLE audit."lesson_group_plan_topic_links_aud" RENAME COLUMN "lesson_plan_id" TO "lesson_group_plan_id";

-- changeset kevin:2026-06-01-lesson-groups-audit-10
ALTER TABLE audit."lesson_objectives_aud" RENAME COLUMN "lesson_plan_id" TO "lesson_group_plan_id";

-- changeset kevin:2026-06-01-lesson-groups-audit-11
ALTER TABLE audit."lesson_quizzes_aud" RENAME COLUMN "lesson_plan_id" TO "lesson_group_plan_id";

-- changeset kevin:2026-06-01-lesson-groups-audit-12
ALTER TABLE audit."student_lesson_links_aud" DROP COLUMN "lesson_plan_id";

-- changeset kevin:2026-06-01-lesson-groups-audit-13
ALTER TABLE audit."student_lesson_links_aud" ADD COLUMN "lesson_group_id" UUID;
