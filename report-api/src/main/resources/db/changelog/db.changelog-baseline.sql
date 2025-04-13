-- liquibase formatted sql

-- changeset kevin:1744559939507-1
CREATE TABLE "lesson_plans" ("lesson_plan_status" SMALLINT, "created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_plans_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-2
CREATE TABLE "lessons" ("date" date, "day" SMALLINT NOT NULL, "end_time" time(6) WITHOUT TIME ZONE NOT NULL, "start_time" time(6) WITHOUT TIME ZONE NOT NULL, "created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "course_id" UUID, "id" UUID NOT NULL, "outlet_id" UUID NOT NULL, "subject_id" UUID, "created_by" VARCHAR(255), "description" VARCHAR(255), "name" VARCHAR(255) NOT NULL, "recap" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lessons_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-3
CREATE TABLE "educators" ("date_of_birth" date, "start_date" date, "created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "educator_client_persona_id" UUID, "id" UUID NOT NULL, "created_by" VARCHAR(255), "email" VARCHAR(255) NOT NULL, "employment_type" VARCHAR(255) NOT NULL, "name" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "educators_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-4
CREATE TABLE "lesson_postponement_request" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "created_by" VARCHAR(255), "reasoning" VARCHAR(255) NOT NULL, "status" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_postponement_request_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-5
CREATE TABLE "price_records" ("price" FLOAT8 NOT NULL, "created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "created_by" VARCHAR(255), "frequency" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "price_records_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-6
CREATE TABLE "student_client_personas" ("id" UUID NOT NULL, "relationship" VARCHAR(255) NOT NULL, CONSTRAINT "student_client_personas_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-7
CREATE TABLE "courses" ("end_date" date NOT NULL, "end_time" time(6) WITHOUT TIME ZONE NOT NULL, "lesson_number_frequency" INTEGER NOT NULL, "lesson_weekly_frequency" INTEGER NOT NULL, "max_size" INTEGER NOT NULL, "start_date" date NOT NULL, "start_time" time(6) WITHOUT TIME ZONE NOT NULL, "created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "level_id" UUID, "outlet_id" UUID NOT NULL, "price_record_id" UUID, "created_by" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "courses_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-8
CREATE TABLE "institutions" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "address" VARCHAR(255), "contact_number" VARCHAR(255), "created_by" VARCHAR(255), "email" VARCHAR(255) NOT NULL, "name" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "uen" VARCHAR(255), "updated_by" VARCHAR(255), CONSTRAINT "institutions_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-9
CREATE TABLE "lesson_homework_completions" ("rating" INTEGER NOT NULL, "created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "student_lesson_registration_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_homework_completions_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-10
CREATE TABLE "lesson_participation_reviews" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "student_lesson_registration_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_participation_reviews_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-11
CREATE TABLE "lesson_quizzes" ("max_score" INTEGER NOT NULL, "score" INTEGER, "created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "student_lesson_registration_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_quizzes_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-12
CREATE TABLE "student_lesson_attendances" ("attended" BOOLEAN NOT NULL, "created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "student_lesson_registration_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "student_lesson_attendances_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-13
CREATE TABLE "student_reports" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "student_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "student_reports_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-14
CREATE TABLE "student_lesson_registrations" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "lesson_plan_id" UUID, "student_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), "student_report_id" UUID, CONSTRAINT "student_lesson_registrations_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-15
CREATE TABLE "educator_course_attachments" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "course_id" UUID, "educator_id" UUID, "id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "educator_course_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-16
CREATE TABLE "educator_lesson_attachments" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "educator_id" UUID, "id" UUID NOT NULL, "lesson_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "educator_lesson_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-17
CREATE TABLE "lesson_objective_topic_attachments" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "lesson_objective_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_objective_topic_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-18
CREATE TABLE "level_educator_attachments" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "educator_id" UUID, "id" UUID NOT NULL, "level_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "level_educator_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-19
CREATE TABLE "material_lesson_attachments" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "lesson_plan_id" UUID, "material_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "material_lesson_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-20
CREATE TABLE "material_topic_attachments" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "material_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "material_topic_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-21
CREATE TABLE "outlet_educator_attachments" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "educator_id" UUID, "id" UUID NOT NULL, "outlet_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "outlet_educator_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-22
CREATE TABLE "outlets" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "address" VARCHAR(255), "contact_number" VARCHAR(255), "created_by" VARCHAR(255), "description" VARCHAR(255), "email" VARCHAR(255), "name" VARCHAR(255), "postal_code" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "outlets_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-23
CREATE TABLE "student_course_registrations" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "course_id" UUID, "id" UUID NOT NULL, "student_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "student_course_registrations_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-24
CREATE TABLE "student_outlet_registrations" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "outlet_id" UUID, "student_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "student_outlet_registrations_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-25
CREATE TABLE "subject_course_attachments" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "course_id" UUID, "id" UUID NOT NULL, "subject_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_course_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-26
CREATE TABLE "subject_educator_attachments" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "educator_id" UUID, "id" UUID NOT NULL, "subject_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_educator_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-27
CREATE TABLE "subject_level_attachments" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "level_id" UUID, "subject_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_level_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-28
CREATE TABLE "subjects" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subjects_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-29
CREATE TABLE "subject_student_attachments" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "student_id" UUID, "subject_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_student_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-30
CREATE TABLE "subject_test_group_attachments" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "subject_id" UUID, "test_group_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_test_group_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-31
CREATE VIEW "lessons_view" AS SELECT date,
    day,
    end_time,
    start_time,
    created_at,
    updated_at,
    course_id,
    id,
    outlet_id,
    subject_id,
    created_by,
    description,
    name,
    recap,
    tenant_id,
    updated_by,
        CASE
            WHEN (date < CURRENT_DATE) THEN 'COMPLETED'::text
            WHEN ((date = CURRENT_DATE) AND ((end_time)::time with time zone < CURRENT_TIME)) THEN 'COMPLETED'::text
            WHEN ((date = CURRENT_DATE) AND ((start_time)::time with time zone <= CURRENT_TIME) AND ((end_time)::time with time zone >= CURRENT_TIME)) THEN 'ONGOING'::text
            WHEN ((date = CURRENT_DATE) AND ((start_time)::time with time zone > CURRENT_TIME)) THEN 'UPCOMING'::text
            ELSE 'UPCOMING'::text
        END AS lesson_status,
        CASE
            WHEN ((recap IS NULL) OR (TRIM(BOTH FROM recap) = ''::text)) THEN 'NOT_REVIEWED'::text
            ELSE 'REVIEWED'::text
        END AS lesson_review_status,
        CASE
            WHEN (EXISTS ( SELECT 1
               FROM lesson_plans lp
              WHERE (lp.lesson_id = l.id))) THEN 'PLANNED'::text
            ELSE 'NOT_PLANNED'::text
        END AS lesson_plan_status
   FROM lessons l;

-- changeset kevin:1744559939507-32
ALTER TABLE "educators" ADD CONSTRAINT "educators_educator_client_persona_id_key" UNIQUE ("educator_client_persona_id");

-- changeset kevin:1744559939507-33
ALTER TABLE "courses" ADD CONSTRAINT "courses_price_record_id_key" UNIQUE ("price_record_id");

-- changeset kevin:1744559939507-34
ALTER TABLE "institutions" ADD CONSTRAINT "institutions_email_key" UNIQUE ("email");

-- changeset kevin:1744559939507-35
ALTER TABLE "institutions" ADD CONSTRAINT "institutions_name_key" UNIQUE ("name");

-- changeset kevin:1744559939507-36
ALTER TABLE "lesson_homework_completions" ADD CONSTRAINT "lesson_homework_completions_student_lesson_registration_id_key" UNIQUE ("student_lesson_registration_id");

-- changeset kevin:1744559939507-37
ALTER TABLE "lesson_participation_reviews" ADD CONSTRAINT "lesson_participation_reviews_student_lesson_registration_id_key" UNIQUE ("student_lesson_registration_id");

-- changeset kevin:1744559939507-38
ALTER TABLE "lesson_quizzes" ADD CONSTRAINT "lesson_quizzes_student_lesson_registration_id_key" UNIQUE ("student_lesson_registration_id");

-- changeset kevin:1744559939507-39
ALTER TABLE "student_lesson_attendances" ADD CONSTRAINT "student_lesson_attendances_student_lesson_registration_id_key" UNIQUE ("student_lesson_registration_id");

-- changeset kevin:1744559939507-40
ALTER TABLE "student_reports" ADD CONSTRAINT "student_reports_student_id_key" UNIQUE ("student_id");

-- changeset kevin:1744559939507-41
ALTER TABLE "student_lesson_registrations" ADD CONSTRAINT "uk1j61goqiepde7e1q2ow4x6ra0" UNIQUE ("student_report_id");

-- changeset kevin:1744559939507-42
ALTER TABLE "student_lesson_registrations" ADD CONSTRAINT "uk_student_lesson" UNIQUE ("student_id", "lesson_id");

-- changeset kevin:1744559939507-43
ALTER TABLE "educator_course_attachments" ADD CONSTRAINT "uk_educator_course" UNIQUE ("educator_id", "course_id");

-- changeset kevin:1744559939507-44
ALTER TABLE "educator_lesson_attachments" ADD CONSTRAINT "uk_educator_lesson" UNIQUE ("educator_id", "lesson_id");

-- changeset kevin:1744559939507-45
ALTER TABLE "lesson_objective_topic_attachments" ADD CONSTRAINT "uk_lesson_objective_topic" UNIQUE ("lesson_objective_id", "topic_id");

-- changeset kevin:1744559939507-46
ALTER TABLE "level_educator_attachments" ADD CONSTRAINT "uk_level_educator" UNIQUE ("level_id", "educator_id");

-- changeset kevin:1744559939507-47
ALTER TABLE "material_lesson_attachments" ADD CONSTRAINT "uk_material_lesson" UNIQUE ("material_id", "lesson_id");

-- changeset kevin:1744559939507-48
ALTER TABLE "material_topic_attachments" ADD CONSTRAINT "uk_material_topic" UNIQUE ("material_id", "topic_id");

-- changeset kevin:1744559939507-49
ALTER TABLE "outlet_educator_attachments" ADD CONSTRAINT "uk_outlet_educator" UNIQUE ("outlet_id", "educator_id");

-- changeset kevin:1744559939507-50
ALTER TABLE "outlets" ADD CONSTRAINT "uk_outlet_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1744559939507-51
ALTER TABLE "student_course_registrations" ADD CONSTRAINT "uk_student_course" UNIQUE ("student_id", "course_id");

-- changeset kevin:1744559939507-52
ALTER TABLE "student_outlet_registrations" ADD CONSTRAINT "uk_student_outlet" UNIQUE ("student_id", "outlet_id");

-- changeset kevin:1744559939507-53
ALTER TABLE "subject_course_attachments" ADD CONSTRAINT "uk_subject_course" UNIQUE ("subject_id", "course_id");

-- changeset kevin:1744559939507-54
ALTER TABLE "subject_educator_attachments" ADD CONSTRAINT "uk_subject_educator" UNIQUE ("subject_id", "educator_id");

-- changeset kevin:1744559939507-55
ALTER TABLE "subject_level_attachments" ADD CONSTRAINT "uk_subject_level" UNIQUE ("subject_id", "level_id");

-- changeset kevin:1744559939507-56
ALTER TABLE "subjects" ADD CONSTRAINT "uk_subject_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1744559939507-57
ALTER TABLE "subject_student_attachments" ADD CONSTRAINT "uk_subject_student" UNIQUE ("subject_id", "student_id");

-- changeset kevin:1744559939507-58
ALTER TABLE "subject_test_group_attachments" ADD CONSTRAINT "uk_subject_test_group" UNIQUE ("subject_id", "test_group_id");

-- changeset kevin:1744559939507-59
CREATE TABLE "accounts" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "contact" VARCHAR(255) NOT NULL, "created_by" VARCHAR(255), "email" VARCHAR(255) NOT NULL, "first_name" VARCHAR(255) NOT NULL, "last_name" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), "user_id" VARCHAR(255) NOT NULL, CONSTRAINT "accounts_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-60
CREATE TABLE "educator_client_personas" ("id" UUID NOT NULL, CONSTRAINT "educator_client_personas_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-61
CREATE TABLE "institution_admin_personas" ("id" UUID NOT NULL, "institution_id" UUID, CONSTRAINT "institution_admin_personas_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-62
CREATE TABLE "invoices" ("cost" FLOAT8 NOT NULL, "created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "invoices_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-63
CREATE TABLE "lesson_objectives" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_objectives_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-64
CREATE TABLE "levels" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "levels_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-65
CREATE TABLE "materials" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "created_by" VARCHAR(255), "file_url" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "materials_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-66
CREATE TABLE "outlet_admin_personas" ("id" UUID NOT NULL, CONSTRAINT "outlet_admin_personas_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-67
CREATE TABLE "outlet_admin_personas_outlets" ("outlet_admin_id" UUID NOT NULL, "outlet_id" UUID NOT NULL, CONSTRAINT "outlet_admin_personas_outlets_pkey" PRIMARY KEY ("outlet_admin_id", "outlet_id"));

-- changeset kevin:1744559939507-68
CREATE TABLE "outlet_rooms" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "outlet_id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "outlet_rooms_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-69
CREATE TABLE "personas" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "account_id" UUID, "id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "personas_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-70
CREATE TABLE "students" ("date_of_birth" date, "created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "level_id" UUID, "student_client_persona_id" UUID NOT NULL, "created_by" VARCHAR(255), "current_school" VARCHAR(255), "email" VARCHAR(255) NOT NULL, "name" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "students_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-71
CREATE TABLE "test_groups" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "test_groups_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-72
CREATE TABLE "test_results" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "student_id" UUID, "subject_id" UUID, "test_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "test_results_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-73
CREATE TABLE "tests" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "test_group_id" UUID, "created_by" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "tests_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-74
CREATE TABLE "topics" ("created_at" TIMESTAMP WITHOUT TIME ZONE, "updated_at" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "topics_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1744559939507-75
ALTER TABLE "lessons" ADD CONSTRAINT "fk17ucc7gjfjddsyi0gvstkqeat" FOREIGN KEY ("course_id") REFERENCES "courses" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-76
ALTER TABLE "student_lesson_registrations" ADD CONSTRAINT "fk1kf39o3ko24fg3pu06jra0n7u" FOREIGN KEY ("student_report_id") REFERENCES "student_reports" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-77
ALTER TABLE "subject_student_attachments" ADD CONSTRAINT "fk2136e2qshkkwejhs7skxh9ar9" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-78
ALTER TABLE "subject_test_group_attachments" ADD CONSTRAINT "fk2f37137p4osrhfdlxa5ybcxfm" FOREIGN KEY ("test_group_id") REFERENCES "test_groups" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-79
ALTER TABLE "lesson_plans" ADD CONSTRAINT "fk3laj7cxvxsm9dbg04a3s4dwj2" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-80
ALTER TABLE "students" ADD CONSTRAINT "fk3n1db0w32pj9ju8pscg5jokqn" FOREIGN KEY ("student_client_persona_id") REFERENCES "student_client_personas" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-81
ALTER TABLE "subject_student_attachments" ADD CONSTRAINT "fk3thygbxutid3qbfqqa2554uot" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-82
ALTER TABLE "educators" ADD CONSTRAINT "fk4px5jpdgfbjftejmgedyjkfw0" FOREIGN KEY ("educator_client_persona_id") REFERENCES "educator_client_personas" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-83
ALTER TABLE "subject_level_attachments" ADD CONSTRAINT "fk4rmyvfxcwgbi7ag9mnq44xpx6" FOREIGN KEY ("level_id") REFERENCES "levels" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-84
ALTER TABLE "student_course_registrations" ADD CONSTRAINT "fk5375ptutfmdqfu8s1ksi3foh6" FOREIGN KEY ("course_id") REFERENCES "courses" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-85
ALTER TABLE "courses" ADD CONSTRAINT "fk5h26i8gulbtggcwuqqkwh0yw1" FOREIGN KEY ("level_id") REFERENCES "levels" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-86
ALTER TABLE "institution_admin_personas" ADD CONSTRAINT "fk5jajikbdo8lp8d62focph5x8g" FOREIGN KEY ("institution_id") REFERENCES "institutions" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-87
ALTER TABLE "institution_admin_personas" ADD CONSTRAINT "fk6bsslqe2f7s0xo9kqc42vwvid" FOREIGN KEY ("id") REFERENCES "personas" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-88
ALTER TABLE "material_lesson_attachments" ADD CONSTRAINT "fk77k1j2ktpho8a6qpnaqqrrj70" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-89
ALTER TABLE "student_course_registrations" ADD CONSTRAINT "fk8ia9b68198r3esu5ca2sl13s4" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-90
ALTER TABLE "material_lesson_attachments" ADD CONSTRAINT "fk8jr2dhmm44pojjr8rml96jpal" FOREIGN KEY ("material_id") REFERENCES "materials" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-91
ALTER TABLE "educator_client_personas" ADD CONSTRAINT "fk8tud8kx2gic02yvuciaew0vt" FOREIGN KEY ("id") REFERENCES "personas" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-92
ALTER TABLE "subject_test_group_attachments" ADD CONSTRAINT "fk96g93x7vqct1axi3b5h0rsh9a" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-93
ALTER TABLE "personas" ADD CONSTRAINT "fkavjoapmll29vr3ouvhmfon2bd" FOREIGN KEY ("account_id") REFERENCES "accounts" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-94
ALTER TABLE "test_results" ADD CONSTRAINT "fkbf0wjcwu5oyayhevc36opvyv1" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-95
ALTER TABLE "subject_course_attachments" ADD CONSTRAINT "fkbxfdmpilfnugbdjukhdw25feq" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-96
ALTER TABLE "lesson_postponement_request" ADD CONSTRAINT "fkc65wyxntiq8fuwdfkqwb39m6c" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-97
ALTER TABLE "lesson_homework_completions" ADD CONSTRAINT "fkcfvpknoinki18g9bt1lh0dtbe" FOREIGN KEY ("student_lesson_registration_id") REFERENCES "student_lesson_registrations" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-98
ALTER TABLE "student_lesson_registrations" ADD CONSTRAINT "fkcq3wjfjsemkfd7nlw6j6qtkdo" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-99
ALTER TABLE "student_lesson_attendances" ADD CONSTRAINT "fkd3d9tnxs4prcu1tx6fkyj2ono" FOREIGN KEY ("student_lesson_registration_id") REFERENCES "student_lesson_registrations" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-100
ALTER TABLE "lesson_objective_topic_attachments" ADD CONSTRAINT "fkd3pecdl3ypiaqv16wvwocyu9g" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-101
ALTER TABLE "outlet_admin_personas_outlets" ADD CONSTRAINT "fkdk4bg03h8fshqxqnw4mnpok54" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-102
ALTER TABLE "lesson_objectives" ADD CONSTRAINT "fkdtpxux294qelayd20cq0lh8up" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-103
ALTER TABLE "outlet_rooms" ADD CONSTRAINT "fkdvhwlaeccmlum90r33ce27gwk" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-104
ALTER TABLE "lessons" ADD CONSTRAINT "fke94a0k21xpi7glv89af90lwjv" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-105
ALTER TABLE "test_results" ADD CONSTRAINT "fkeb5e15t9e5hn11gbkuub0xeln" FOREIGN KEY ("test_id") REFERENCES "tests" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-106
ALTER TABLE "level_educator_attachments" ADD CONSTRAINT "fkehpls3vu8in5k768tieaenm3i" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-107
ALTER TABLE "lesson_quizzes" ADD CONSTRAINT "fkei1iuov0uwy8u4aebbkx5o9g" FOREIGN KEY ("student_lesson_registration_id") REFERENCES "student_lesson_registrations" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-108
ALTER TABLE "material_lesson_attachments" ADD CONSTRAINT "fkeisfjktwhnaqpgnbfr362a6od" FOREIGN KEY ("lesson_plan_id") REFERENCES "lesson_plans" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-109
ALTER TABLE "student_reports" ADD CONSTRAINT "fkekcg7id9gd9wdvj0rxwx5lij8" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-110
ALTER TABLE "students" ADD CONSTRAINT "fkf387qaaiqu3c7yueklkmoa2oy" FOREIGN KEY ("level_id") REFERENCES "levels" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-111
ALTER TABLE "subject_course_attachments" ADD CONSTRAINT "fkf8nvaeqf8cck3nro3xj1vqd6g" FOREIGN KEY ("course_id") REFERENCES "courses" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-112
ALTER TABLE "educator_lesson_attachments" ADD CONSTRAINT "fkfqsl52fbg3qshyyf46a631phk" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-113
ALTER TABLE "student_lesson_registrations" ADD CONSTRAINT "fkfs6rpso3jq6jvmc25y9dkdicq" FOREIGN KEY ("lesson_plan_id") REFERENCES "lesson_plans" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-114
ALTER TABLE "outlet_admin_personas_outlets" ADD CONSTRAINT "fkgbw0kr8400day5kk1tv5oramy" FOREIGN KEY ("outlet_admin_id") REFERENCES "outlet_admin_personas" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-115
ALTER TABLE "student_client_personas" ADD CONSTRAINT "fkh1adnd4moopixmoex6uk9oikg" FOREIGN KEY ("id") REFERENCES "personas" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-116
ALTER TABLE "student_outlet_registrations" ADD CONSTRAINT "fki38aohbfbpxji9l9k4n4u72qa" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-117
ALTER TABLE "student_outlet_registrations" ADD CONSTRAINT "fki5nkulcywauy3brososhynck8" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-118
ALTER TABLE "lessons" ADD CONSTRAINT "fkii11bs4ibinclr1jjbm684kbg" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-119
ALTER TABLE "outlet_admin_personas" ADD CONSTRAINT "fkilx6va20r64qna23f490q6l1p" FOREIGN KEY ("id") REFERENCES "personas" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-120
ALTER TABLE "educator_lesson_attachments" ADD CONSTRAINT "fkjyx3cqipc80r8dck501xkqk15" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-121
ALTER TABLE "subject_educator_attachments" ADD CONSTRAINT "fkkx2mgmpg9rv32o66m3covuimy" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-122
ALTER TABLE "level_educator_attachments" ADD CONSTRAINT "fkljknla8wosk6gm4ajrqdjvgew" FOREIGN KEY ("level_id") REFERENCES "levels" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-123
ALTER TABLE "student_reports" ADD CONSTRAINT "fkm5l3csus62woyfv3valwhnbkp" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-124
ALTER TABLE "courses" ADD CONSTRAINT "fkmt7g458qd9i0ogpaaswpvkt7v" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-125
ALTER TABLE "test_results" ADD CONSTRAINT "fknrsl6stg8tc0n50hjs9nq2hhq" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-126
ALTER TABLE "courses" ADD CONSTRAINT "fknstr1ki2i14ki21df4ykt2v05" FOREIGN KEY ("price_record_id") REFERENCES "price_records" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-127
ALTER TABLE "outlet_educator_attachments" ADD CONSTRAINT "fko4mahaaawp4rqesjtgjwk3iax" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-128
ALTER TABLE "subject_level_attachments" ADD CONSTRAINT "fko4u7oohlrjou60k45gtvdvd5l" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-129
ALTER TABLE "material_topic_attachments" ADD CONSTRAINT "fkoey987ikhm4tre93cfrkoe4q" FOREIGN KEY ("material_id") REFERENCES "materials" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-130
ALTER TABLE "material_topic_attachments" ADD CONSTRAINT "fkohrkntthiptvc4dhj5w4xrvsj" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-131
ALTER TABLE "student_lesson_registrations" ADD CONSTRAINT "fkok0kiw3mu0uh4kf0b6us9ikr3" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-132
ALTER TABLE "outlet_educator_attachments" ADD CONSTRAINT "fkp6mm9ri4ymqwbm6hsj8358vai" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-133
ALTER TABLE "lesson_participation_reviews" ADD CONSTRAINT "fkq7w0lg3yybrcds3jqtxbfeh9y" FOREIGN KEY ("student_lesson_registration_id") REFERENCES "student_lesson_registrations" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-134
ALTER TABLE "educator_course_attachments" ADD CONSTRAINT "fkqd4faplkf3fnixqundx7yqtif" FOREIGN KEY ("course_id") REFERENCES "courses" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-135
ALTER TABLE "educator_course_attachments" ADD CONSTRAINT "fkqsojk1jeoqolfq6nuc165p3hh" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-136
ALTER TABLE "subject_educator_attachments" ADD CONSTRAINT "fkrma0kmnhdr914ea2gvg5kclly" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-137
ALTER TABLE "lesson_objective_topic_attachments" ADD CONSTRAINT "fkssok55nl08l3048wlf9uvu0rl" FOREIGN KEY ("lesson_objective_id") REFERENCES "lesson_objectives" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1744559939507-138
ALTER TABLE "tests" ADD CONSTRAINT "fktqfc6jqrqlvpsvtu4l1t294x1" FOREIGN KEY ("test_group_id") REFERENCES "test_groups" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

