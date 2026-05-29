-- liquibase formatted sql

-- Replaces the StudentLessonAttendances entity with an attendance_state enum
-- column on student_lesson_links. StudentLessonLinkModel#attendanceState is
-- @Enumerated(EnumType.STRING) @Column(nullable = false) defaulting to
-- UNFILLED in the entity builder, so existing rows are backfilled to UNFILLED.
-- StudentLessonLinkModel is @Audited, so the audit twin gets the same column
-- (nullable, no backfill — past revisions had no attendance_state value).
-- The old StudentLessonAttendances table and its audit twin are dropped via
-- CASCADE to clear inbound FKs.

-- changeset kevin:2026-05-29-attendance-state-1
ALTER TABLE public."student_lesson_links"
    ADD COLUMN "attendance_state" VARCHAR(255) NOT NULL DEFAULT 'UNFILLED';

-- changeset kevin:2026-05-29-attendance-state-2
ALTER TABLE audit."student_lesson_links_aud"
    ADD COLUMN "attendance_state" VARCHAR(255);

-- changeset kevin:2026-05-29-attendance-state-3
DROP TABLE public."student_lesson_attendances" CASCADE;

-- changeset kevin:2026-05-29-attendance-state-4
DROP TABLE audit."student_lesson_attendances_aud" CASCADE;
