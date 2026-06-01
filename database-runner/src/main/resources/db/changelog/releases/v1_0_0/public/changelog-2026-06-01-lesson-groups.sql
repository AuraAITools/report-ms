-- liquibase formatted sql

-- Introduces lesson_groups between lesson and lesson_plan, renames
-- lesson_plans -> lesson_group_plans (FK now lesson_group_id, 1:1 unique).
-- Renames lesson_plan link tables to lesson_group_plan_*. Migrates
-- student_lesson_links.lesson_plan_id to lesson_group_id pointing at the
-- new lesson_groups table. No row preservation — caller confirmed.

-- changeset kevin:2026-06-01-lesson-groups-1
CREATE TABLE public."lesson_groups" (
    "id" UUID PRIMARY KEY,
    "version" BIGINT NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL,
    "created_by" VARCHAR(255),
    "updated_at" TIMESTAMPTZ,
    "updated_by" VARCHAR(255),
    "tenant_id" VARCHAR(255) NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "lesson_id" UUID NOT NULL,
    CONSTRAINT "fk_lesson_groups_lesson_id"
        FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id")
            ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- changeset kevin:2026-06-01-lesson-groups-2
-- Truncate dependent + main tables since we're restructuring FKs.
TRUNCATE TABLE public."student_lesson_links",
               public."lesson_plan_materials_links",
               public."lesson_plan_topic_links",
               public."lesson_objectives",
               public."lesson_quizzes",
               public."lesson_plans" CASCADE;

-- changeset kevin:2026-06-01-lesson-groups-3
ALTER TABLE public."lesson_plans" RENAME TO "lesson_group_plans";

-- changeset kevin:2026-06-01-lesson-groups-4
ALTER TABLE public."lesson_group_plans"
    DROP CONSTRAINT IF EXISTS "uk_lesson_plans_lesson_id";

-- changeset kevin:2026-06-01-lesson-groups-5
-- Drop the Hibernate-generated FK on lesson_id before dropping the column.
ALTER TABLE public."lesson_group_plans"
    DROP CONSTRAINT IF EXISTS "fk3laj7cxvxsm9dbg04a3s4dwj2";

-- changeset kevin:2026-06-01-lesson-groups-6
ALTER TABLE public."lesson_group_plans" DROP COLUMN "lesson_id";

-- changeset kevin:2026-06-01-lesson-groups-7
ALTER TABLE public."lesson_group_plans" ADD COLUMN "lesson_group_id" UUID NOT NULL;

-- changeset kevin:2026-06-01-lesson-groups-8
ALTER TABLE public."lesson_group_plans"
    ADD CONSTRAINT "uk_lesson_group_plans_lesson_group_id" UNIQUE ("lesson_group_id");

-- changeset kevin:2026-06-01-lesson-groups-9
ALTER TABLE public."lesson_group_plans"
    ADD CONSTRAINT "fk_lesson_group_plans_lesson_group_id"
        FOREIGN KEY ("lesson_group_id") REFERENCES "lesson_groups" ("id")
            ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:2026-06-01-lesson-groups-10
ALTER TABLE public."lesson_plan_materials_links" RENAME TO "lesson_group_plan_materials_links";

-- changeset kevin:2026-06-01-lesson-groups-11
ALTER TABLE public."lesson_group_plan_materials_links" RENAME COLUMN "lesson_plan_id" TO "lesson_group_plan_id";

-- changeset kevin:2026-06-01-lesson-groups-12
ALTER TABLE public."lesson_group_plan_materials_links"
    DROP CONSTRAINT IF EXISTS "uk_lesson_plan_material";

-- changeset kevin:2026-06-01-lesson-groups-13
ALTER TABLE public."lesson_group_plan_materials_links"
    ADD CONSTRAINT "uk_lesson_group_plan_material" UNIQUE ("lesson_group_plan_id", "material_id");

-- changeset kevin:2026-06-01-lesson-groups-14
ALTER TABLE public."lesson_plan_topic_links" RENAME TO "lesson_group_plan_topic_links";

-- changeset kevin:2026-06-01-lesson-groups-15
ALTER TABLE public."lesson_group_plan_topic_links" RENAME COLUMN "lesson_plan_id" TO "lesson_group_plan_id";

-- changeset kevin:2026-06-01-lesson-groups-16
ALTER TABLE public."lesson_group_plan_topic_links"
    DROP CONSTRAINT IF EXISTS "uk_lesson_plan_topic";

-- changeset kevin:2026-06-01-lesson-groups-17
ALTER TABLE public."lesson_group_plan_topic_links"
    ADD CONSTRAINT "uk_lesson_group_plan_topic" UNIQUE ("lesson_group_plan_id", "topic_id");

-- changeset kevin:2026-06-01-lesson-groups-18
ALTER TABLE public."lesson_objectives" RENAME COLUMN "lesson_plan_id" TO "lesson_group_plan_id";

-- changeset kevin:2026-06-01-lesson-groups-19
ALTER TABLE public."lesson_quizzes" RENAME COLUMN "lesson_plan_id" TO "lesson_group_plan_id";

-- changeset kevin:2026-06-01-lesson-groups-20
ALTER TABLE public."student_lesson_links" DROP COLUMN "lesson_plan_id";

-- changeset kevin:2026-06-01-lesson-groups-21
ALTER TABLE public."student_lesson_links" ADD COLUMN "lesson_group_id" UUID;

-- changeset kevin:2026-06-01-lesson-groups-22
ALTER TABLE public."student_lesson_links"
    ADD CONSTRAINT "fk_student_lesson_links_lesson_group_id"
        FOREIGN KEY ("lesson_group_id") REFERENCES "lesson_groups" ("id")
            ON UPDATE NO ACTION ON DELETE NO ACTION;
