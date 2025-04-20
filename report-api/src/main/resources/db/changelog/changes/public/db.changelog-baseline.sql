-- changeset kevin:create-public-schema
CREATE SCHEMA IF NOT EXISTS public;

-- changeset kevin:1745158755822-1
CREATE TABLE "lesson_plans" ("is_planned" BOOLEAN DEFAULT FALSE NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_plans_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-2
CREATE TABLE "lesson_cancellation_requests" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "approved_by" VARCHAR(255) NOT NULL, "created_by" VARCHAR(255), "reasoning" VARCHAR(255) NOT NULL, "status" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_cancellation_requests_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-3
CREATE TABLE "lesson_homework_completions" ("completion" INTEGER, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "student_lesson_registration_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_homework_completions_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-4
CREATE TABLE "lesson_participation_reviews" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "student_lesson_registration_id" UUID, "created_by" VARCHAR(255), "participation_type" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_participation_reviews_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-5
CREATE TABLE "lesson_postponement_requests" ("created_at" TIMESTAMP WITH TIME ZONE, "postponed_lesson_end_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "postponed_lesson_start_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "approved_by" VARCHAR(255) NOT NULL, "created_by" VARCHAR(255), "reasoning" VARCHAR(255) NOT NULL, "status" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_postponement_requests_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-6
CREATE TABLE "lesson_topic_proficiencies" ("rating" INTEGER, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "student_lesson_registration_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_topic_proficiencies_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-7
CREATE TABLE "lessons" ("created_at" TIMESTAMP WITH TIME ZONE, "lesson_end_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "lesson_start_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "updated_at" TIMESTAMP WITH TIME ZONE, "course_id" UUID, "id" UUID NOT NULL, "outlet_id" UUID NOT NULL, "subject_id" UUID, "created_by" VARCHAR(255), "description" VARCHAR(255), "name" VARCHAR(255) NOT NULL, "recap" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lessons_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-8
CREATE TABLE "student_lesson_registrations" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "lesson_plan_id" UUID, "student_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "student_lesson_registrations_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-9
CREATE TABLE "courses" ("max_size" INTEGER NOT NULL, "course_end_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "course_start_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "level_id" UUID, "outlet_id" UUID NOT NULL, "price_record_id" UUID, "created_by" VARCHAR(255), "lesson_frequency" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "courses_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-10
CREATE TABLE "educators" ("date_of_birth" date, "start_date" date, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "educator_client_persona_id" UUID, "id" UUID NOT NULL, "created_by" VARCHAR(255), "email" VARCHAR(255) NOT NULL, "employment_type" VARCHAR(255) NOT NULL, "name" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "educators_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-11
CREATE TABLE "price_records" ("price" FLOAT8 NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "created_by" VARCHAR(255), "frequency" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "price_records_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-12
CREATE TABLE "student_client_personas" ("id" UUID NOT NULL, "relationship" VARCHAR(255) NOT NULL, CONSTRAINT "student_client_personas_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-13
CREATE TABLE "institutions" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "address" VARCHAR(255), "contact_number" VARCHAR(255), "created_by" VARCHAR(255), "email" VARCHAR(255) NOT NULL, "name" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "uen" VARCHAR(255), "updated_by" VARCHAR(255), CONSTRAINT "institutions_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-14
CREATE TABLE "lesson_outlet_room_bookings" ("created_at" TIMESTAMP WITH TIME ZONE, "end_timestampz" TIMESTAMP WITH TIME ZONE NOT NULL, "start_timestampz" TIMESTAMP WITH TIME ZONE NOT NULL, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "outlet_room_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_outlet_room_bookings_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-15
CREATE TABLE "outlet_rooms_lesson_outlet_room_bookings" ("lesson_outlet_room_bookings_id" UUID NOT NULL, "outlet_room_id" UUID NOT NULL, CONSTRAINT "outlet_rooms_lesson_outlet_room_bookings_pkey" PRIMARY KEY ("lesson_outlet_room_bookings_id", "outlet_room_id"));

-- changeset kevin:1745158755822-16
CREATE TABLE "student_lesson_attendances" ("attended" BOOLEAN NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "student_lesson_registration_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "student_lesson_attendances_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-17
CREATE TABLE "educator_course_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "course_id" UUID, "educator_id" UUID, "id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "educator_course_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-18
CREATE TABLE "educator_lesson_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "educator_id" UUID, "id" UUID NOT NULL, "lesson_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "educator_lesson_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-19
CREATE TABLE "lesson_objective_topic_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "lesson_objective_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_objective_topic_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-20
CREATE TABLE "lesson_plan_materials_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "lesson_plan_id" UUID, "material_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_plan_materials_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-21
CREATE TABLE "lesson_plan_topic_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "lesson_plan_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_plan_topic_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-22
CREATE TABLE "lesson_quiz_student_lesson_registration_attachments" ("max_score" INTEGER NOT NULL, "score" INTEGER NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "lesson_quiz_id" UUID, "student_lesson_registration_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_quiz_student_lesson_registration_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-23
CREATE TABLE "level_educator_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "educator_id" UUID, "id" UUID NOT NULL, "level_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "level_educator_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-24
CREATE TABLE "material_lesson_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "lesson_plan_id" UUID, "material_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "material_lesson_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-25
CREATE TABLE "material_topic_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "material_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "material_topic_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-26
CREATE TABLE "outlet_educator_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "educator_id" UUID, "id" UUID NOT NULL, "outlet_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "outlet_educator_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-27
CREATE TABLE "outlets" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "address" VARCHAR(255), "contact_number" VARCHAR(255), "created_by" VARCHAR(255), "description" VARCHAR(255), "email" VARCHAR(255), "name" VARCHAR(255), "postal_code" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "outlets_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-28
CREATE TABLE "student_course_registrations" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "course_id" UUID, "id" UUID NOT NULL, "student_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "student_course_registrations_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-29
CREATE TABLE "student_outlet_registrations" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "outlet_id" UUID, "student_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "student_outlet_registrations_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-30
CREATE TABLE "subject_course_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "course_id" UUID, "id" UUID NOT NULL, "subject_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_course_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-31
CREATE TABLE "subject_educator_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "educator_id" UUID, "id" UUID NOT NULL, "subject_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_educator_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-32
CREATE TABLE "subject_level_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "level_id" UUID, "subject_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_level_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-33
CREATE TABLE "subjects" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subjects_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-34
CREATE TABLE "subject_student_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "student_id" UUID, "subject_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_student_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-35
CREATE TABLE "subject_test_group_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "subject_id" UUID, "test_group_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_test_group_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-36
CREATE TABLE "accounts" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "contact" VARCHAR(255) NOT NULL, "created_by" VARCHAR(255), "email" VARCHAR(255) NOT NULL, "first_name" VARCHAR(255) NOT NULL, "last_name" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), "user_id" VARCHAR(255) NOT NULL, CONSTRAINT "accounts_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-37
CREATE TABLE "levels" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "levels_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-38
CREATE TABLE "outlet_rooms" ("size" INTEGER NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "outlet_id" UUID NOT NULL, "created_by" VARCHAR(255), "details" VARCHAR(255), "file_url" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "outlet_rooms_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-39
CREATE TABLE "topics" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "topics_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-40
CREATE TABLE "topic_subject_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "subject_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "topic_subject_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-41
CREATE VIEW "lessons_view" AS SELECT lesson_start_timestamptz,
    lesson_end_timestamptz,
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
            WHEN (EXISTS ( SELECT 1
               FROM lesson_postponement_requests lpr
              WHERE ((lpr.lesson_id = l.id) AND ((lpr.status)::text = 'APPROVED'::text)))) THEN 'POSTPONED'::text
            WHEN (EXISTS ( SELECT 1
               FROM lesson_cancellation_requests lcr
              WHERE ((lcr.lesson_id = l.id) AND ((lcr.status)::text = 'APPROVED'::text)))) THEN 'CANCELLED'::text
            WHEN (lesson_start_timestamptz < CURRENT_TIMESTAMP) THEN 'COMPLETED'::text
            WHEN ((lesson_start_timestamptz <= CURRENT_TIMESTAMP) AND (lesson_end_timestamptz >= CURRENT_TIMESTAMP)) THEN 'ONGOING'::text
            ELSE 'UPCOMING'::text
        END AS lesson_status,
        CASE
            WHEN (EXISTS ( SELECT 1
               FROM (student_lesson_registrations slr
                 JOIN lesson_topic_proficiencies ltp ON ((ltp.student_lesson_registration_id = slr.id)))
              WHERE ((slr.lesson_id = l.id) AND (ltp.rating IS NULL)))) THEN 'NOT_REVIEWED'::text
            WHEN (EXISTS ( SELECT 1
               FROM (student_lesson_registrations slr
                 JOIN lesson_participation_reviews lp ON ((lp.student_lesson_registration_id = slr.id)))
              WHERE ((slr.lesson_id = l.id) AND (lp.participation_type IS NULL)))) THEN 'NOT_REVIEWED'::text
            WHEN (EXISTS ( SELECT 1
               FROM (student_lesson_registrations slr
                 JOIN lesson_homework_completions lhc ON ((lhc.student_lesson_registration_id = slr.id)))
              WHERE ((slr.lesson_id = l.id) AND (lhc.completion IS NULL)))) THEN 'NOT_REVIEWED'::text
            ELSE 'REVIEWED'::text
        END AS lesson_review_status,
        CASE
            WHEN ((NOT (EXISTS ( SELECT 1
               FROM lesson_plans lp
              WHERE ((lp.lesson_id = l.id) AND (lp.is_planned = false))))) AND (EXISTS ( SELECT 1
               FROM lesson_plans lp
              WHERE (lp.lesson_id = l.id)))) THEN 'PLANNED'::text
            ELSE 'NOT_PLANNED'::text
        END AS lesson_plan_status
   FROM lessons l;

-- changeset kevin:1745158755822-42
ALTER TABLE "lesson_homework_completions" ADD CONSTRAINT "lesson_homework_completions_student_lesson_registration_id_key" UNIQUE ("student_lesson_registration_id");

-- changeset kevin:1745158755822-43
ALTER TABLE "lesson_participation_reviews" ADD CONSTRAINT "lesson_participation_reviews_student_lesson_registration_id_key" UNIQUE ("student_lesson_registration_id");

-- changeset kevin:1745158755822-44
ALTER TABLE "lesson_topic_proficiencies" ADD CONSTRAINT "lesson_topic_proficiencies_student_lesson_registration_id_key" UNIQUE ("student_lesson_registration_id");

-- changeset kevin:1745158755822-45
ALTER TABLE "lessons" ADD CONSTRAINT "uk_tenant_lesson_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1745158755822-46
ALTER TABLE "student_lesson_registrations" ADD CONSTRAINT "uk_student_lesson" UNIQUE ("student_id", "lesson_id");

-- changeset kevin:1745158755822-47
ALTER TABLE "courses" ADD CONSTRAINT "courses_price_record_id_key" UNIQUE ("price_record_id");

-- changeset kevin:1745158755822-48
ALTER TABLE "educators" ADD CONSTRAINT "educators_educator_client_persona_id_key" UNIQUE ("educator_client_persona_id");

-- changeset kevin:1745158755822-49
ALTER TABLE "institutions" ADD CONSTRAINT "institutions_email_key" UNIQUE ("email");

-- changeset kevin:1745158755822-50
ALTER TABLE "institutions" ADD CONSTRAINT "institutions_name_key" UNIQUE ("name");

-- changeset kevin:1745158755822-51
ALTER TABLE "lesson_outlet_room_bookings" ADD CONSTRAINT "lesson_outlet_room_bookings_lesson_id_key" UNIQUE ("lesson_id");

-- changeset kevin:1745158755822-52
ALTER TABLE "lesson_outlet_room_bookings" ADD CONSTRAINT "uk_lesson_outlet_room" UNIQUE ("lesson_id", "outlet_room_id");

-- changeset kevin:1745158755822-53
ALTER TABLE "outlet_rooms_lesson_outlet_room_bookings" ADD CONSTRAINT "outlet_rooms_lesson_outlet_ro_lesson_outlet_room_bookings_i_key" UNIQUE ("lesson_outlet_room_bookings_id");

-- changeset kevin:1745158755822-54
ALTER TABLE "student_lesson_attendances" ADD CONSTRAINT "student_lesson_attendances_student_lesson_registration_id_key" UNIQUE ("student_lesson_registration_id");

-- changeset kevin:1745158755822-55
ALTER TABLE "educator_course_attachments" ADD CONSTRAINT "uk_educator_course" UNIQUE ("educator_id", "course_id");

-- changeset kevin:1745158755822-56
ALTER TABLE "educator_lesson_attachments" ADD CONSTRAINT "uk_educator_lesson" UNIQUE ("educator_id", "lesson_id");

-- changeset kevin:1745158755822-57
ALTER TABLE "lesson_objective_topic_attachments" ADD CONSTRAINT "uk_lesson_objective_topic" UNIQUE ("lesson_objective_id", "topic_id");

-- changeset kevin:1745158755822-58
ALTER TABLE "lesson_plan_materials_attachments" ADD CONSTRAINT "uk_lesson_plan_material" UNIQUE ("lesson_plan_id", "material_id");

-- changeset kevin:1745158755822-59
ALTER TABLE "lesson_plan_topic_attachments" ADD CONSTRAINT "uk_lesson_plan_topic" UNIQUE ("lesson_plan_id", "topic_id");

-- changeset kevin:1745158755822-60
ALTER TABLE "lesson_quiz_student_lesson_registration_attachments" ADD CONSTRAINT "uk_lesson_quiz_student_lesson_registration" UNIQUE ("lesson_quiz_id", "student_lesson_registration_id");

-- changeset kevin:1745158755822-61
ALTER TABLE "level_educator_attachments" ADD CONSTRAINT "uk_level_educator" UNIQUE ("level_id", "educator_id");

-- changeset kevin:1745158755822-62
ALTER TABLE "material_lesson_attachments" ADD CONSTRAINT "uk_material_lesson" UNIQUE ("material_id", "lesson_id");

-- changeset kevin:1745158755822-63
ALTER TABLE "material_topic_attachments" ADD CONSTRAINT "uk_material_topic" UNIQUE ("material_id", "topic_id");

-- changeset kevin:1745158755822-64
ALTER TABLE "outlet_educator_attachments" ADD CONSTRAINT "uk_outlet_educator" UNIQUE ("outlet_id", "educator_id");

-- changeset kevin:1745158755822-65
ALTER TABLE "outlets" ADD CONSTRAINT "uk_outlet_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1745158755822-66
ALTER TABLE "student_course_registrations" ADD CONSTRAINT "uk_student_course" UNIQUE ("student_id", "course_id");

-- changeset kevin:1745158755822-67
ALTER TABLE "student_outlet_registrations" ADD CONSTRAINT "uk_student_outlet" UNIQUE ("student_id", "outlet_id");

-- changeset kevin:1745158755822-68
ALTER TABLE "subject_course_attachments" ADD CONSTRAINT "uk_subject_course" UNIQUE ("subject_id", "course_id");

-- changeset kevin:1745158755822-69
ALTER TABLE "subject_educator_attachments" ADD CONSTRAINT "uk_subject_educator" UNIQUE ("subject_id", "educator_id");

-- changeset kevin:1745158755822-70
ALTER TABLE "subject_level_attachments" ADD CONSTRAINT "uk_subject_level" UNIQUE ("subject_id", "level_id");

-- changeset kevin:1745158755822-71
ALTER TABLE "subjects" ADD CONSTRAINT "uk_subject_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1745158755822-72
ALTER TABLE "subject_student_attachments" ADD CONSTRAINT "uk_subject_student" UNIQUE ("subject_id", "student_id");

-- changeset kevin:1745158755822-73
ALTER TABLE "subject_test_group_attachments" ADD CONSTRAINT "uk_subject_test_group" UNIQUE ("subject_id", "test_group_id");

-- changeset kevin:1745158755822-74
ALTER TABLE "accounts" ADD CONSTRAINT "uk_tenant_email" UNIQUE ("tenant_id", "email");

-- changeset kevin:1745158755822-75
ALTER TABLE "accounts" ADD CONSTRAINT "uk_tenant_user" UNIQUE ("tenant_id", "user_id");

-- changeset kevin:1745158755822-76
ALTER TABLE "levels" ADD CONSTRAINT "uk_tenant_level_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1745158755822-77
ALTER TABLE "outlet_rooms" ADD CONSTRAINT "uk_tenant_outlet_name" UNIQUE ("name", "tenant_id");

-- changeset kevin:1745158755822-78
ALTER TABLE "topics" ADD CONSTRAINT "uk_tenant_topic_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1745158755822-79
ALTER TABLE "topic_subject_attachments" ADD CONSTRAINT "uk_topic_subject" UNIQUE ("topic_id", "subject_id");

-- changeset kevin:1745158755822-80
CREATE SEQUENCE  IF NOT EXISTS "revinfo_seq" AS bigint START WITH 1 INCREMENT BY 50 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

-- changeset kevin:1745158755822-81
CREATE TABLE "educator_client_personas" ("id" UUID NOT NULL, CONSTRAINT "educator_client_personas_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-82
CREATE TABLE "institution_admin_personas" ("id" UUID NOT NULL, "institution_id" UUID, CONSTRAINT "institution_admin_personas_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-83
CREATE TABLE "invoices" ("cost" FLOAT8 NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "invoices_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-84
CREATE TABLE "lesson_objectives" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_objectives_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-85
CREATE TABLE "lesson_quizzes" ("max_score" INTEGER NOT NULL, "score" INTEGER, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_quizzes_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-86
CREATE TABLE "materials" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "created_by" VARCHAR(255), "file_url" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "materials_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-87
CREATE TABLE "outlet_admin_personas" ("id" UUID NOT NULL, CONSTRAINT "outlet_admin_personas_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-88
CREATE TABLE "outlet_admin_personas_outlets" ("outlet_admin_id" UUID NOT NULL, "outlet_id" UUID NOT NULL, CONSTRAINT "outlet_admin_personas_outlets_pkey" PRIMARY KEY ("outlet_admin_id", "outlet_id"));

-- changeset kevin:1745158755822-89
CREATE TABLE "personas" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "account_id" UUID, "id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "personas_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-90
CREATE TABLE "students" ("date_of_birth" date, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "level_id" UUID, "student_client_persona_id" UUID NOT NULL, "created_by" VARCHAR(255), "current_school" VARCHAR(255), "email" VARCHAR(255) NOT NULL, "name" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "students_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-91
CREATE TABLE "test_groups" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "test_groups_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-92
CREATE TABLE "test_results" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "student_id" UUID, "subject_id" UUID, "test_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "test_results_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-93
CREATE TABLE "tests" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "test_group_id" UUID, "created_by" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "tests_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1745158755822-94
ALTER TABLE "lessons" ADD CONSTRAINT "fk17ucc7gjfjddsyi0gvstkqeat" FOREIGN KEY ("course_id") REFERENCES "courses" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-95
ALTER TABLE "subject_student_attachments" ADD CONSTRAINT "fk2136e2qshkkwejhs7skxh9ar9" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-96
ALTER TABLE "subject_test_group_attachments" ADD CONSTRAINT "fk2f37137p4osrhfdlxa5ybcxfm" FOREIGN KEY ("test_group_id") REFERENCES "test_groups" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-97
ALTER TABLE "lesson_plans" ADD CONSTRAINT "fk3laj7cxvxsm9dbg04a3s4dwj2" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-98
ALTER TABLE "students" ADD CONSTRAINT "fk3n1db0w32pj9ju8pscg5jokqn" FOREIGN KEY ("student_client_persona_id") REFERENCES "student_client_personas" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-99
ALTER TABLE "subject_student_attachments" ADD CONSTRAINT "fk3thygbxutid3qbfqqa2554uot" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-100
ALTER TABLE "lesson_outlet_room_bookings" ADD CONSTRAINT "fk48hq0vsrfd3h566329dmn3vhq" FOREIGN KEY ("outlet_room_id") REFERENCES "outlet_rooms" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-101
ALTER TABLE "lesson_quiz_student_lesson_registration_attachments" ADD CONSTRAINT "fk4ob5pj94d055g26r6ljxv1rf" FOREIGN KEY ("lesson_quiz_id") REFERENCES "lesson_quizzes" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-102
ALTER TABLE "educators" ADD CONSTRAINT "fk4px5jpdgfbjftejmgedyjkfw0" FOREIGN KEY ("educator_client_persona_id") REFERENCES "educator_client_personas" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-103
ALTER TABLE "subject_level_attachments" ADD CONSTRAINT "fk4rmyvfxcwgbi7ag9mnq44xpx6" FOREIGN KEY ("level_id") REFERENCES "levels" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-104
ALTER TABLE "student_course_registrations" ADD CONSTRAINT "fk5375ptutfmdqfu8s1ksi3foh6" FOREIGN KEY ("course_id") REFERENCES "courses" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-105
ALTER TABLE "courses" ADD CONSTRAINT "fk5h26i8gulbtggcwuqqkwh0yw1" FOREIGN KEY ("level_id") REFERENCES "levels" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-106
ALTER TABLE "institution_admin_personas" ADD CONSTRAINT "fk5jajikbdo8lp8d62focph5x8g" FOREIGN KEY ("institution_id") REFERENCES "institutions" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-107
ALTER TABLE "institution_admin_personas" ADD CONSTRAINT "fk6bsslqe2f7s0xo9kqc42vwvid" FOREIGN KEY ("id") REFERENCES "personas" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-108
ALTER TABLE "lesson_plan_topic_attachments" ADD CONSTRAINT "fk6gqt046drg9pp014xiwutis7k" FOREIGN KEY ("lesson_plan_id") REFERENCES "lesson_plans" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-109
ALTER TABLE "material_lesson_attachments" ADD CONSTRAINT "fk77k1j2ktpho8a6qpnaqqrrj70" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-110
ALTER TABLE "lesson_plan_materials_attachments" ADD CONSTRAINT "fk8ct5vnyqj4sdwpv86683c6sm4" FOREIGN KEY ("lesson_plan_id") REFERENCES "lesson_plans" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-111
ALTER TABLE "student_course_registrations" ADD CONSTRAINT "fk8ia9b68198r3esu5ca2sl13s4" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-112
ALTER TABLE "material_lesson_attachments" ADD CONSTRAINT "fk8jr2dhmm44pojjr8rml96jpal" FOREIGN KEY ("material_id") REFERENCES "materials" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-113
ALTER TABLE "educator_client_personas" ADD CONSTRAINT "fk8tud8kx2gic02yvuciaew0vt" FOREIGN KEY ("id") REFERENCES "personas" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-114
ALTER TABLE "subject_test_group_attachments" ADD CONSTRAINT "fk96g93x7vqct1axi3b5h0rsh9a" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-115
ALTER TABLE "lesson_plan_topic_attachments" ADD CONSTRAINT "fkaaejie359b1k0vtnkgyn8wid9" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-116
ALTER TABLE "lesson_plan_materials_attachments" ADD CONSTRAINT "fkajaarvxknbgk5ttsjsb9f9gfc" FOREIGN KEY ("material_id") REFERENCES "materials" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-117
ALTER TABLE "personas" ADD CONSTRAINT "fkavjoapmll29vr3ouvhmfon2bd" FOREIGN KEY ("account_id") REFERENCES "accounts" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-118
ALTER TABLE "test_results" ADD CONSTRAINT "fkbf0wjcwu5oyayhevc36opvyv1" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-119
ALTER TABLE "subject_course_attachments" ADD CONSTRAINT "fkbxfdmpilfnugbdjukhdw25feq" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-120
ALTER TABLE "lesson_homework_completions" ADD CONSTRAINT "fkcfvpknoinki18g9bt1lh0dtbe" FOREIGN KEY ("student_lesson_registration_id") REFERENCES "student_lesson_registrations" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-121
ALTER TABLE "student_lesson_registrations" ADD CONSTRAINT "fkcq3wjfjsemkfd7nlw6j6qtkdo" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-122
ALTER TABLE "lesson_postponement_requests" ADD CONSTRAINT "fkd2sw239pe2mgp6xex4ml8qdx6" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-123
ALTER TABLE "student_lesson_attendances" ADD CONSTRAINT "fkd3d9tnxs4prcu1tx6fkyj2ono" FOREIGN KEY ("student_lesson_registration_id") REFERENCES "student_lesson_registrations" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-124
ALTER TABLE "lesson_objective_topic_attachments" ADD CONSTRAINT "fkd3pecdl3ypiaqv16wvwocyu9g" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-125
ALTER TABLE "outlet_admin_personas_outlets" ADD CONSTRAINT "fkdk4bg03h8fshqxqnw4mnpok54" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-126
ALTER TABLE "lesson_objectives" ADD CONSTRAINT "fkdtpxux294qelayd20cq0lh8up" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-127
ALTER TABLE "outlet_rooms" ADD CONSTRAINT "fkdvhwlaeccmlum90r33ce27gwk" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-128
ALTER TABLE "lessons" ADD CONSTRAINT "fke94a0k21xpi7glv89af90lwjv" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-129
ALTER TABLE "test_results" ADD CONSTRAINT "fkeb5e15t9e5hn11gbkuub0xeln" FOREIGN KEY ("test_id") REFERENCES "tests" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-130
ALTER TABLE "level_educator_attachments" ADD CONSTRAINT "fkehpls3vu8in5k768tieaenm3i" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-131
ALTER TABLE "material_lesson_attachments" ADD CONSTRAINT "fkeisfjktwhnaqpgnbfr362a6od" FOREIGN KEY ("lesson_plan_id") REFERENCES "lesson_plans" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-132
ALTER TABLE "students" ADD CONSTRAINT "fkf387qaaiqu3c7yueklkmoa2oy" FOREIGN KEY ("level_id") REFERENCES "levels" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-133
ALTER TABLE "subject_course_attachments" ADD CONSTRAINT "fkf8nvaeqf8cck3nro3xj1vqd6g" FOREIGN KEY ("course_id") REFERENCES "courses" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-134
ALTER TABLE "educator_lesson_attachments" ADD CONSTRAINT "fkfqsl52fbg3qshyyf46a631phk" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-135
ALTER TABLE "student_lesson_registrations" ADD CONSTRAINT "fkfs6rpso3jq6jvmc25y9dkdicq" FOREIGN KEY ("lesson_plan_id") REFERENCES "lesson_plans" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-136
ALTER TABLE "lesson_outlet_room_bookings" ADD CONSTRAINT "fkg0kyl6jko5wo92djnheu0fgwq" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-137
ALTER TABLE "outlet_admin_personas_outlets" ADD CONSTRAINT "fkgbw0kr8400day5kk1tv5oramy" FOREIGN KEY ("outlet_admin_id") REFERENCES "outlet_admin_personas" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-138
ALTER TABLE "lesson_topic_proficiencies" ADD CONSTRAINT "fkgvlxk82p3g84yofimsojf1lvj" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-139
ALTER TABLE "student_client_personas" ADD CONSTRAINT "fkh1adnd4moopixmoex6uk9oikg" FOREIGN KEY ("id") REFERENCES "personas" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-140
ALTER TABLE "lesson_quizzes" ADD CONSTRAINT "fkhdf4r8mc9l91wr1xnv06cxekl" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-141
ALTER TABLE "student_outlet_registrations" ADD CONSTRAINT "fki38aohbfbpxji9l9k4n4u72qa" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-142
ALTER TABLE "student_outlet_registrations" ADD CONSTRAINT "fki5nkulcywauy3brososhynck8" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-143
ALTER TABLE "lessons" ADD CONSTRAINT "fkii11bs4ibinclr1jjbm684kbg" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-144
ALTER TABLE "outlet_admin_personas" ADD CONSTRAINT "fkilx6va20r64qna23f490q6l1p" FOREIGN KEY ("id") REFERENCES "personas" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-145
ALTER TABLE "outlet_rooms_lesson_outlet_room_bookings" ADD CONSTRAINT "fkiybrplxw7u14j6tbixij10lp7" FOREIGN KEY ("lesson_outlet_room_bookings_id") REFERENCES "lesson_outlet_room_bookings" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-146
ALTER TABLE "topic_subject_attachments" ADD CONSTRAINT "fkjwu83i77ux9qe84pe779nivpn" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-147
ALTER TABLE "educator_lesson_attachments" ADD CONSTRAINT "fkjyx3cqipc80r8dck501xkqk15" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-148
ALTER TABLE "topic_subject_attachments" ADD CONSTRAINT "fkkp2ro3xax8p3a5o8jii4bun5o" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-149
ALTER TABLE "subject_educator_attachments" ADD CONSTRAINT "fkkx2mgmpg9rv32o66m3covuimy" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-150
ALTER TABLE "level_educator_attachments" ADD CONSTRAINT "fkljknla8wosk6gm4ajrqdjvgew" FOREIGN KEY ("level_id") REFERENCES "levels" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-151
ALTER TABLE "courses" ADD CONSTRAINT "fkmt7g458qd9i0ogpaaswpvkt7v" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-152
ALTER TABLE "test_results" ADD CONSTRAINT "fknrsl6stg8tc0n50hjs9nq2hhq" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-153
ALTER TABLE "courses" ADD CONSTRAINT "fknstr1ki2i14ki21df4ykt2v05" FOREIGN KEY ("price_record_id") REFERENCES "price_records" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-154
ALTER TABLE "lesson_topic_proficiencies" ADD CONSTRAINT "fko1n7r8navcteuarxvbx7x5ndy" FOREIGN KEY ("student_lesson_registration_id") REFERENCES "student_lesson_registrations" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-155
ALTER TABLE "outlet_rooms_lesson_outlet_room_bookings" ADD CONSTRAINT "fko26gr6xyfw2u0ritu595ayw1v" FOREIGN KEY ("outlet_room_id") REFERENCES "outlet_rooms" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-156
ALTER TABLE "outlet_educator_attachments" ADD CONSTRAINT "fko4mahaaawp4rqesjtgjwk3iax" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-157
ALTER TABLE "subject_level_attachments" ADD CONSTRAINT "fko4u7oohlrjou60k45gtvdvd5l" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-158
ALTER TABLE "material_topic_attachments" ADD CONSTRAINT "fkoey987ikhm4tre93cfrkoe4q" FOREIGN KEY ("material_id") REFERENCES "materials" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-159
ALTER TABLE "material_topic_attachments" ADD CONSTRAINT "fkohrkntthiptvc4dhj5w4xrvsj" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-160
ALTER TABLE "student_lesson_registrations" ADD CONSTRAINT "fkok0kiw3mu0uh4kf0b6us9ikr3" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-161
ALTER TABLE "lesson_quiz_student_lesson_registration_attachments" ADD CONSTRAINT "fkp0s0anvsx5go7tueiar42lb60" FOREIGN KEY ("student_lesson_registration_id") REFERENCES "student_lesson_registrations" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-162
ALTER TABLE "outlet_educator_attachments" ADD CONSTRAINT "fkp6mm9ri4ymqwbm6hsj8358vai" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-163
ALTER TABLE "lesson_participation_reviews" ADD CONSTRAINT "fkq7w0lg3yybrcds3jqtxbfeh9y" FOREIGN KEY ("student_lesson_registration_id") REFERENCES "student_lesson_registrations" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-164
ALTER TABLE "educator_course_attachments" ADD CONSTRAINT "fkqd4faplkf3fnixqundx7yqtif" FOREIGN KEY ("course_id") REFERENCES "courses" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-165
ALTER TABLE "educator_course_attachments" ADD CONSTRAINT "fkqsojk1jeoqolfq6nuc165p3hh" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-166
ALTER TABLE "subject_educator_attachments" ADD CONSTRAINT "fkrma0kmnhdr914ea2gvg5kclly" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-167
ALTER TABLE "lesson_objective_topic_attachments" ADD CONSTRAINT "fkssok55nl08l3048wlf9uvu0rl" FOREIGN KEY ("lesson_objective_id") REFERENCES "lesson_objectives" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-168
ALTER TABLE "lesson_cancellation_requests" ADD CONSTRAINT "fkst5m93umekq79mlka3syg851v" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745158755822-169
ALTER TABLE "tests" ADD CONSTRAINT "fktqfc6jqrqlvpsvtu4l1t294x1" FOREIGN KEY ("test_group_id") REFERENCES "test_groups" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

