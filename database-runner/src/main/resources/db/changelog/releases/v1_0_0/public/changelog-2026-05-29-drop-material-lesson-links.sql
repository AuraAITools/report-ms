-- liquibase formatted sql

-- Drops the material_lesson_links table along with its audit twin. Material-to-
-- lesson associations now travel through lesson_plan_materials_links, so the
-- direct lesson FK is obsolete. CASCADE handles the inbound FKs without having
-- to name every constraint:
--   public.material_lesson_links:
--     uk_material_lesson UNIQUE (material_id, lesson_id)
--     FK -> materials(id), FK -> lessons(id), FK -> lesson_plans(id)
--   audit.material_lesson_links_aud:
--     FK -> audit.revinfo(rev)

-- changeset kevin:2026-05-29-drop-material-lesson-links-1
DROP TABLE public."material_lesson_links" CASCADE;

-- changeset kevin:2026-05-29-drop-material-lesson-links-2
DROP TABLE audit."material_lesson_links_aud" CASCADE;
