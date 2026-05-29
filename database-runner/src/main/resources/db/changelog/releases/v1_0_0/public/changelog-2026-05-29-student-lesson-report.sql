-- liquibase formatted sql

-- Introduces a StudentLessonReport entity that owns the link's
-- LessonHomeworkCompletion / LessonParticipationReview /
-- LessonTopicProficiency / LessonQuizStudentLessonLink children. The 1:1 is
-- owned by StudentLessonReport via student_lesson_link_id. Also adds a
-- state enum column (REGISTERED/UNREGISTERED) on student_lesson_links
-- defaulting to REGISTERED. The four child tables have their
-- student_lesson_link_id column renamed to student_lesson_report_id, FKs
-- repointed at student_lesson_reports, and unique constraints updated. No
-- production data exists so no backfill or row migration is required.

-- changeset kevin:2026-05-29-student-lesson-report-1
ALTER TABLE public."student_lesson_links"
    ADD COLUMN "state" VARCHAR(255) NOT NULL DEFAULT 'REGISTERED';

-- changeset kevin:2026-05-29-student-lesson-report-2
ALTER TABLE audit."student_lesson_links_aud"
    ADD COLUMN "state" VARCHAR(255);

-- changeset kevin:2026-05-29-student-lesson-report-3
CREATE TABLE public."student_lesson_reports"
(
    "id"                     UUID         NOT NULL,
    "version"                BIGINT,
    "created_at"             TIMESTAMP WITH TIME ZONE,
    "updated_at"             TIMESTAMP WITH TIME ZONE,
    "created_by"             VARCHAR(255),
    "updated_by"             VARCHAR(255),
    "tenant_id"              VARCHAR(255) NOT NULL,
    "student_lesson_link_id" UUID         NOT NULL,
    CONSTRAINT "student_lesson_reports_pkey" PRIMARY KEY ("id")
);

-- changeset kevin:2026-05-29-student-lesson-report-4
ALTER TABLE public."student_lesson_reports"
    ADD CONSTRAINT "student_lesson_reports_student_lesson_link_id_key" UNIQUE ("student_lesson_link_id");

-- changeset kevin:2026-05-29-student-lesson-report-5
ALTER TABLE public."student_lesson_reports"
    ADD CONSTRAINT "fk_student_lesson_reports_student_lesson_link_id"
        FOREIGN KEY ("student_lesson_link_id") REFERENCES public."student_lesson_links" ("id")
            ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:2026-05-29-student-lesson-report-6
CREATE TABLE audit."student_lesson_reports_aud"
(
    "rev"                    INTEGER NOT NULL,
    "revtype"                SMALLINT,
    "id"                     UUID    NOT NULL,
    "student_lesson_link_id" UUID,
    CONSTRAINT "student_lesson_reports_aud_pkey" PRIMARY KEY ("rev", "id")
);

-- changeset kevin:2026-05-29-student-lesson-report-7
ALTER TABLE audit."student_lesson_reports_aud"
    ADD CONSTRAINT "fk_student_lesson_reports_aud_rev"
        FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev")
            ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:2026-05-29-student-lesson-report-8
ALTER TABLE public."lesson_homework_completions"
DROP
CONSTRAINT "fkiply454dqfg4ikn9lbnl58f2";

-- changeset kevin:2026-05-29-student-lesson-report-9
ALTER TABLE public."lesson_homework_completions"
DROP
CONSTRAINT "lesson_homework_completions_student_lesson_link_id_key";

-- changeset kevin:2026-05-29-student-lesson-report-10
ALTER TABLE public."lesson_homework_completions"
    RENAME COLUMN "student_lesson_link_id" TO "student_lesson_report_id";

-- changeset kevin:2026-05-29-student-lesson-report-11
ALTER TABLE public."lesson_homework_completions"
    ADD CONSTRAINT "lesson_homework_completions_student_lesson_report_id_key" UNIQUE ("student_lesson_report_id");

-- changeset kevin:2026-05-29-student-lesson-report-12
ALTER TABLE public."lesson_homework_completions"
    ADD CONSTRAINT "fk_lesson_homework_completions_student_lesson_report_id"
        FOREIGN KEY ("student_lesson_report_id") REFERENCES public."student_lesson_reports" ("id")
            ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:2026-05-29-student-lesson-report-13
ALTER TABLE audit."lesson_homework_completions_aud"
    RENAME COLUMN "student_lesson_link_id" TO "student_lesson_report_id";

-- changeset kevin:2026-05-29-student-lesson-report-14
ALTER TABLE public."lesson_participation_reviews"
DROP
CONSTRAINT "fkeem6c1tmxqeef93lj2sinov22";

-- changeset kevin:2026-05-29-student-lesson-report-15
ALTER TABLE public."lesson_participation_reviews"
DROP
CONSTRAINT "lesson_participation_reviews_student_lesson_link_id_key";

-- changeset kevin:2026-05-29-student-lesson-report-16
ALTER TABLE public."lesson_participation_reviews"
    RENAME COLUMN "student_lesson_link_id" TO "student_lesson_report_id";

-- changeset kevin:2026-05-29-student-lesson-report-17
ALTER TABLE public."lesson_participation_reviews"
    ADD CONSTRAINT "lesson_participation_reviews_student_lesson_report_id_key" UNIQUE ("student_lesson_report_id");

-- changeset kevin:2026-05-29-student-lesson-report-18
ALTER TABLE public."lesson_participation_reviews"
    ADD CONSTRAINT "fk_lesson_participation_reviews_student_lesson_report_id"
        FOREIGN KEY ("student_lesson_report_id") REFERENCES public."student_lesson_reports" ("id")
            ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:2026-05-29-student-lesson-report-19
ALTER TABLE audit."lesson_participation_reviews_aud"
    RENAME COLUMN "student_lesson_link_id" TO "student_lesson_report_id";

-- changeset kevin:2026-05-29-student-lesson-report-20
ALTER TABLE public."lesson_topic_proficiencies"
DROP
CONSTRAINT "fk814snfies19rboappn8lrutdv";

-- changeset kevin:2026-05-29-student-lesson-report-21
ALTER TABLE public."lesson_topic_proficiencies"
DROP
CONSTRAINT "lesson_topic_proficiencies_student_lesson_link_id_key";

-- changeset kevin:2026-05-29-student-lesson-report-22
ALTER TABLE public."lesson_topic_proficiencies"
    RENAME COLUMN "student_lesson_link_id" TO "student_lesson_report_id";

-- changeset kevin:2026-05-29-student-lesson-report-23
ALTER TABLE public."lesson_topic_proficiencies"
    ADD CONSTRAINT "fk_lesson_topic_proficiencies_student_lesson_report_id"
        FOREIGN KEY ("student_lesson_report_id") REFERENCES public."student_lesson_reports" ("id")
            ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:2026-05-29-student-lesson-report-25
ALTER TABLE audit."lesson_topic_proficiencies_aud"
    RENAME COLUMN "student_lesson_link_id" TO "student_lesson_report_id";

-- changeset kevin:2026-05-29-student-lesson-report-26
ALTER TABLE public."lesson_quiz_student_lesson_links"
DROP
CONSTRAINT "fkeyqechwqddfxlq55d4sijtmgf";

-- changeset kevin:2026-05-29-student-lesson-report-27
ALTER TABLE public."lesson_quiz_student_lesson_links"
DROP
CONSTRAINT "uk_lesson_quiz_student_lesson_link";

-- changeset kevin:2026-05-29-student-lesson-report-28
ALTER TABLE public."lesson_quiz_student_lesson_links"
    RENAME COLUMN "student_lesson_link_id" TO "student_lesson_report_id";

-- changeset kevin:2026-05-29-student-lesson-report-29
ALTER TABLE public."lesson_quiz_student_lesson_links"
    ADD CONSTRAINT "uk_lesson_quiz_student_lesson_report" UNIQUE ("lesson_quiz_id", "student_lesson_report_id");

-- changeset kevin:2026-05-29-student-lesson-report-30
ALTER TABLE public."lesson_quiz_student_lesson_links"
    ADD CONSTRAINT "fk_lesson_quiz_student_lesson_links_student_lesson_report_id"
        FOREIGN KEY ("student_lesson_report_id") REFERENCES public."student_lesson_reports" ("id")
            ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:2026-05-29-student-lesson-report-31
ALTER TABLE audit."lesson_quiz_student_lesson_links_aud"
    RENAME COLUMN "student_lesson_link_id" TO "student_lesson_report_id";
