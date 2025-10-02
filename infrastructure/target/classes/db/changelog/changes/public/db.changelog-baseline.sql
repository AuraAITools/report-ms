-- liquibase formatted sql

-- changeset kevin:1748070311079-1
CREATE TABLE "public"."lesson_plans" ("is_planned" BOOLEAN DEFAULT FALSE NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_plans_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-2
CREATE TABLE "public"."lesson_cancellation_requests" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_id" UUID, "approved_by" VARCHAR(255) NOT NULL, "created_by" VARCHAR(255), "reasoning" VARCHAR(255) NOT NULL, "status" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_cancellation_requests_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-3
CREATE TABLE "public"."lesson_homework_completions" ("completion" INTEGER, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "student_lesson_registration_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_homework_completions_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-4
CREATE TABLE "public"."lesson_participation_reviews" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "student_lesson_registration_id" UUID, "created_by" VARCHAR(255), "participation_type" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_participation_reviews_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-5
CREATE TABLE "public"."lesson_postponement_requests" ("created_at" TIMESTAMP WITH TIME ZONE, "postponed_lesson_end_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "postponed_lesson_start_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_id" UUID, "approved_by" VARCHAR(255) NOT NULL, "created_by" VARCHAR(255), "reasoning" VARCHAR(255) NOT NULL, "status" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_postponement_requests_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-6
CREATE TABLE "public"."lesson_topic_proficiencies" ("rating" INTEGER, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "student_lesson_registration_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_topic_proficiencies_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-7
CREATE TABLE "public"."lessons" ("created_at" TIMESTAMP WITH TIME ZONE, "lesson_end_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "lesson_start_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "course_id" UUID, "id" UUID NOT NULL, "outlet_id" UUID NOT NULL, "subject_id" UUID, "created_by" VARCHAR(255), "description" VARCHAR(255), "name" VARCHAR(255) NOT NULL, "recap" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lessons_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-8
CREATE TABLE "public"."student_lesson_registrations" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_id" UUID NOT NULL, "lesson_plan_id" UUID, "student_id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "student_lesson_registrations_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-9
CREATE TABLE "public"."accounts" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "contact" VARCHAR(255) NOT NULL, "created_by" VARCHAR(255), "email" VARCHAR(255) NOT NULL, "first_name" VARCHAR(255) NOT NULL, "last_name" VARCHAR(255) NOT NULL, "relationship" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), "user_id" VARCHAR(255), CONSTRAINT "accounts_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-10
CREATE TABLE "public"."courses" ("max_size" INTEGER NOT NULL, "course_end_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "course_start_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "level_id" UUID, "outlet_id" UUID NOT NULL, "price_record_id" UUID, "created_by" VARCHAR(255), "lesson_frequency" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "courses_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-11
CREATE TABLE "public"."educators" ("date_of_birth" date, "start_date" date, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "email" VARCHAR(255) NOT NULL, "employment_type" VARCHAR(255) NOT NULL, "name" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "educators_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-12
CREATE TABLE "public"."levels" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "category" VARCHAR(255) NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "levels_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-13
CREATE TABLE "public"."price_records" ("price" FLOAT8 NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "frequency" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "price_records_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-14
CREATE TABLE "public"."schools" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255), "school_category" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "schools_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-15
CREATE TABLE "public"."institutions" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "address" VARCHAR(255), "contact_number" VARCHAR(255), "created_by" VARCHAR(255), "email" VARCHAR(255) NOT NULL, "name" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "uen" VARCHAR(255), "updated_by" VARCHAR(255), CONSTRAINT "institutions_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-16
CREATE TABLE "public"."lesson_outlet_room_bookings" ("created_at" TIMESTAMP WITH TIME ZONE, "end_timestampz" TIMESTAMP WITH TIME ZONE NOT NULL, "start_timestampz" TIMESTAMP WITH TIME ZONE NOT NULL, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_id" UUID, "outlet_room_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_outlet_room_bookings_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-17
CREATE TABLE "public"."outlet_rooms_lesson_outlet_room_bookings" ("lesson_outlet_room_bookings_id" UUID NOT NULL, "outlet_room_id" UUID NOT NULL, CONSTRAINT "outlet_rooms_lesson_outlet_room_bookings_pkey" PRIMARY KEY ("lesson_outlet_room_bookings_id", "outlet_room_id"));

-- changeset kevin:1748070311079-18
CREATE TABLE "public"."student_lesson_attendances" ("attended" BOOLEAN NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "student_lesson_registration_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "student_lesson_attendances_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-19
CREATE TABLE "public"."account_educator_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "account_id" UUID, "educator_id" UUID, "id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "account_educator_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-20
CREATE TABLE "public"."account_student_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "account_id" UUID, "id" UUID NOT NULL, "student_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "account_student_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-21
CREATE TABLE "public"."educator_course_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "course_id" UUID, "educator_id" UUID, "id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "educator_course_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-22
CREATE TABLE "public"."educator_lesson_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "educator_id" UUID, "id" UUID NOT NULL, "lesson_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "educator_lesson_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-23
CREATE TABLE "public"."lesson_objective_topic_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_objective_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_objective_topic_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-24
CREATE TABLE "public"."lesson_plan_materials_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_plan_id" UUID, "material_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_plan_materials_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-25
CREATE TABLE "public"."lesson_plan_topic_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_plan_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_plan_topic_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-26
CREATE TABLE "public"."lesson_quiz_student_lesson_registration_attachments" ("max_score" INTEGER NOT NULL, "score" INTEGER NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_quiz_id" UUID, "student_lesson_registration_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_quiz_student_lesson_registration_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-27
CREATE TABLE "public"."level_educator_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "educator_id" UUID, "id" UUID NOT NULL, "level_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "level_educator_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-28
CREATE TABLE "public"."material_lesson_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_id" UUID, "lesson_plan_id" UUID, "material_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "material_lesson_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-29
CREATE TABLE "public"."material_topic_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "material_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "material_topic_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-30
CREATE TABLE "public"."outlet_educator_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "educator_id" UUID, "id" UUID NOT NULL, "outlet_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "outlet_educator_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-31
CREATE TABLE "public"."outlets" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "address" VARCHAR(255), "contact_number" VARCHAR(255), "created_by" VARCHAR(255), "description" VARCHAR(255), "email" VARCHAR(255), "name" VARCHAR(255), "postal_code" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "outlets_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-32
CREATE TABLE "public"."student_course_registrations" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "course_id" UUID, "id" UUID NOT NULL, "student_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "student_course_registrations_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-33
CREATE TABLE "public"."student_outlet_registrations" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "outlet_id" UUID, "student_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "student_outlet_registrations_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-34
CREATE TABLE "public"."subject_course_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "course_id" UUID, "id" UUID NOT NULL, "subject_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_course_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-35
CREATE TABLE "public"."subject_educator_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "educator_id" UUID, "id" UUID NOT NULL, "subject_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_educator_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-36
CREATE TABLE "public"."subject_level_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "level_id" UUID, "subject_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_level_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-37
CREATE TABLE "public"."subjects" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subjects_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-38
CREATE TABLE "public"."subject_student_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "student_id" UUID, "subject_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_student_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-39
CREATE TABLE "public"."subject_test_group_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "subject_id" UUID, "test_group_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_test_group_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-40
CREATE TABLE "public"."outlet_rooms" ("size" INTEGER NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "outlet_id" UUID NOT NULL, "created_by" VARCHAR(255), "details" VARCHAR(255), "file_url" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "outlet_rooms_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-41
CREATE TABLE "public"."topics" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "topics_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-42
CREATE TABLE "public"."topic_subject_attachments" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "subject_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "topic_subject_attachments_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-43
CREATE or replace VIEW "public"."lessons_view" AS SELECT lesson_start_timestamptz,
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
    version,
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

-- changeset kevin:1748070311079-44
ALTER TABLE "public"."lesson_homework_completions" ADD CONSTRAINT "lesson_homework_completions_student_lesson_registration_id_key" UNIQUE ("student_lesson_registration_id");

-- changeset kevin:1748070311079-45
ALTER TABLE "public"."lesson_participation_reviews" ADD CONSTRAINT "lesson_participation_reviews_student_lesson_registration_id_key" UNIQUE ("student_lesson_registration_id");

-- changeset kevin:1748070311079-46
ALTER TABLE "public"."lesson_topic_proficiencies" ADD CONSTRAINT "lesson_topic_proficiencies_student_lesson_registration_id_key" UNIQUE ("student_lesson_registration_id");

-- changeset kevin:1748070311079-47
ALTER TABLE "public"."lessons" ADD CONSTRAINT "uk_tenant_lesson_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1748070311079-48
ALTER TABLE "public"."student_lesson_registrations" ADD CONSTRAINT "uk_student_lesson" UNIQUE ("student_id", "lesson_id");

-- changeset kevin:1748070311079-49
ALTER TABLE "public"."accounts" ADD CONSTRAINT "uk_tenant_email" UNIQUE ("tenant_id", "email");

-- changeset kevin:1748070311079-50
ALTER TABLE "public"."accounts" ADD CONSTRAINT "uk_tenant_user" UNIQUE ("tenant_id", "user_id");

-- changeset kevin:1748070311079-51
ALTER TABLE "public"."courses" ADD CONSTRAINT "courses_price_record_id_key" UNIQUE ("price_record_id");

-- changeset kevin:1748070311079-52
ALTER TABLE "public"."levels" ADD CONSTRAINT "uk_tenant_level_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1748070311079-53
ALTER TABLE "public"."schools" ADD CONSTRAINT "uk_school_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1748070311079-54
ALTER TABLE "public"."institutions" ADD CONSTRAINT "institutions_email_key" UNIQUE ("email");

-- changeset kevin:1748070311079-55
ALTER TABLE "public"."institutions" ADD CONSTRAINT "institutions_name_key" UNIQUE ("name");

-- changeset kevin:1748070311079-56
ALTER TABLE "public"."lesson_outlet_room_bookings" ADD CONSTRAINT "lesson_outlet_room_bookings_lesson_id_key" UNIQUE ("lesson_id");

-- changeset kevin:1748070311079-57
ALTER TABLE "public"."lesson_outlet_room_bookings" ADD CONSTRAINT "uk_lesson_outlet_room" UNIQUE ("lesson_id", "outlet_room_id");

-- changeset kevin:1748070311079-58
ALTER TABLE "public"."outlet_rooms_lesson_outlet_room_bookings" ADD CONSTRAINT "outlet_rooms_lesson_outlet_ro_lesson_outlet_room_bookings_i_key" UNIQUE ("lesson_outlet_room_bookings_id");

-- changeset kevin:1748070311079-59
ALTER TABLE "public"."student_lesson_attendances" ADD CONSTRAINT "student_lesson_attendances_student_lesson_registration_id_key" UNIQUE ("student_lesson_registration_id");

-- changeset kevin:1748070311079-60
ALTER TABLE "public"."account_educator_attachments" ADD CONSTRAINT "uk_account_educator" UNIQUE ("account_id", "educator_id");

-- changeset kevin:1748070311079-61
ALTER TABLE "public"."account_student_attachments" ADD CONSTRAINT "uk_account_student" UNIQUE ("account_id", "student_id");

-- changeset kevin:1748070311079-62
ALTER TABLE "public"."educator_course_attachments" ADD CONSTRAINT "uk_educator_course" UNIQUE ("educator_id", "course_id");

-- changeset kevin:1748070311079-63
ALTER TABLE "public"."educator_lesson_attachments" ADD CONSTRAINT "uk_educator_lesson" UNIQUE ("educator_id", "lesson_id");

-- changeset kevin:1748070311079-64
ALTER TABLE "public"."lesson_objective_topic_attachments" ADD CONSTRAINT "uk_lesson_objective_topic" UNIQUE ("lesson_objective_id", "topic_id");

-- changeset kevin:1748070311079-65
ALTER TABLE "public"."lesson_plan_materials_attachments" ADD CONSTRAINT "uk_lesson_plan_material" UNIQUE ("lesson_plan_id", "material_id");

-- changeset kevin:1748070311079-66
ALTER TABLE "public"."lesson_plan_topic_attachments" ADD CONSTRAINT "uk_lesson_plan_topic" UNIQUE ("lesson_plan_id", "topic_id");

-- changeset kevin:1748070311079-67
ALTER TABLE "public"."lesson_quiz_student_lesson_registration_attachments" ADD CONSTRAINT "uk_lesson_quiz_student_lesson_registration" UNIQUE ("lesson_quiz_id", "student_lesson_registration_id");

-- changeset kevin:1748070311079-68
ALTER TABLE "public"."level_educator_attachments" ADD CONSTRAINT "uk_level_educator" UNIQUE ("level_id", "educator_id");

-- changeset kevin:1748070311079-69
ALTER TABLE "public"."material_lesson_attachments" ADD CONSTRAINT "uk_material_lesson" UNIQUE ("material_id", "lesson_id");

-- changeset kevin:1748070311079-70
ALTER TABLE "public"."material_topic_attachments" ADD CONSTRAINT "uk_material_topic" UNIQUE ("material_id", "topic_id");

-- changeset kevin:1748070311079-71
ALTER TABLE "public"."outlet_educator_attachments" ADD CONSTRAINT "uk_outlet_educator" UNIQUE ("outlet_id", "educator_id");

-- changeset kevin:1748070311079-72
ALTER TABLE "public"."outlets" ADD CONSTRAINT "uk_outlet_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1748070311079-73
ALTER TABLE "public"."student_course_registrations" ADD CONSTRAINT "uk_student_course" UNIQUE ("student_id", "course_id");

-- changeset kevin:1748070311079-74
ALTER TABLE "public"."student_outlet_registrations" ADD CONSTRAINT "uk_student_outlet" UNIQUE ("student_id", "outlet_id");

-- changeset kevin:1748070311079-75
ALTER TABLE "public"."subject_course_attachments" ADD CONSTRAINT "uk_subject_course" UNIQUE ("subject_id", "course_id");

-- changeset kevin:1748070311079-76
ALTER TABLE "public"."subject_educator_attachments" ADD CONSTRAINT "uk_subject_educator" UNIQUE ("subject_id", "educator_id");

-- changeset kevin:1748070311079-77
ALTER TABLE "public"."subject_level_attachments" ADD CONSTRAINT "uk_subject_level" UNIQUE ("subject_id", "level_id");

-- changeset kevin:1748070311079-78
ALTER TABLE "public"."subjects" ADD CONSTRAINT "uk_subject_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1748070311079-79
ALTER TABLE "public"."subject_student_attachments" ADD CONSTRAINT "uk_subject_student" UNIQUE ("subject_id", "student_id");

-- changeset kevin:1748070311079-80
ALTER TABLE "public"."subject_test_group_attachments" ADD CONSTRAINT "uk_subject_test_group" UNIQUE ("subject_id", "test_group_id");

-- changeset kevin:1748070311079-81
ALTER TABLE "public"."outlet_rooms" ADD CONSTRAINT "uk_tenant_outlet_name" UNIQUE ("name", "tenant_id");

-- changeset kevin:1748070311079-82
ALTER TABLE "public"."topics" ADD CONSTRAINT "uk_tenant_topic_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1748070311079-83
ALTER TABLE "public"."topic_subject_attachments" ADD CONSTRAINT "uk_topic_subject" UNIQUE ("topic_id", "subject_id");

-- changeset kevin:1748070311079-84
CREATE SEQUENCE  IF NOT EXISTS "revinfo_seq" AS bigint START WITH 1 INCREMENT BY 50 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

-- changeset kevin:1748070311079-85
CREATE TABLE "public"."invoices" ("cost" FLOAT8 NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "invoices_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-86
CREATE TABLE "public"."lesson_objectives" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_objectives_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-87
CREATE TABLE "public"."lesson_quizzes" ("max_score" INTEGER NOT NULL, "score" INTEGER, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_quizzes_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-88
CREATE TABLE "public"."materials" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "file_url" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "materials_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-89
CREATE TABLE "public"."students" ("date_of_birth" date, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "level_id" UUID, "school_id" UUID, "created_by" VARCHAR(255), "email" VARCHAR(255) NOT NULL, "name" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "students_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-90
CREATE TABLE "public"."test_groups" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "test_groups_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-91
CREATE TABLE "public"."test_results" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "student_id" UUID, "subject_id" UUID, "test_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "test_results_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-92
CREATE TABLE "public"."tests" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "test_group_id" UUID, "created_by" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "tests_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1748070311079-93
ALTER TABLE "public"."lessons" ADD CONSTRAINT "fk17ucc7gjfjddsyi0gvstkqeat" FOREIGN KEY ("course_id") REFERENCES "courses" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-94
ALTER TABLE "public"."account_educator_attachments" ADD CONSTRAINT "fk1in2nolgud1iql5iuuo0a1o1t" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-95
ALTER TABLE "public"."subject_student_attachments" ADD CONSTRAINT "fk2136e2qshkkwejhs7skxh9ar9" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-96
ALTER TABLE "public"."subject_test_group_attachments" ADD CONSTRAINT "fk2f37137p4osrhfdlxa5ybcxfm" FOREIGN KEY ("test_group_id") REFERENCES "test_groups" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-97
ALTER TABLE "public"."lesson_plans" ADD CONSTRAINT "fk3laj7cxvxsm9dbg04a3s4dwj2" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-98
ALTER TABLE "public"."subject_student_attachments" ADD CONSTRAINT "fk3thygbxutid3qbfqqa2554uot" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-99
ALTER TABLE "public"."lesson_outlet_room_bookings" ADD CONSTRAINT "fk48hq0vsrfd3h566329dmn3vhq" FOREIGN KEY ("outlet_room_id") REFERENCES "outlet_rooms" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-100
ALTER TABLE "public"."lesson_quiz_student_lesson_registration_attachments" ADD CONSTRAINT "fk4ob5pj94d055g26r6ljxv1rf" FOREIGN KEY ("lesson_quiz_id") REFERENCES "lesson_quizzes" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-101
ALTER TABLE "public"."subject_level_attachments" ADD CONSTRAINT "fk4rmyvfxcwgbi7ag9mnq44xpx6" FOREIGN KEY ("level_id") REFERENCES "levels" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-102
ALTER TABLE "public"."student_course_registrations" ADD CONSTRAINT "fk5375ptutfmdqfu8s1ksi3foh6" FOREIGN KEY ("course_id") REFERENCES "courses" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-103
ALTER TABLE "public"."courses" ADD CONSTRAINT "fk5h26i8gulbtggcwuqqkwh0yw1" FOREIGN KEY ("level_id") REFERENCES "levels" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-104
ALTER TABLE "public"."lesson_plan_topic_attachments" ADD CONSTRAINT "fk6gqt046drg9pp014xiwutis7k" FOREIGN KEY ("lesson_plan_id") REFERENCES "lesson_plans" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-105
ALTER TABLE "public"."material_lesson_attachments" ADD CONSTRAINT "fk77k1j2ktpho8a6qpnaqqrrj70" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-106
ALTER TABLE "public"."lesson_plan_materials_attachments" ADD CONSTRAINT "fk8ct5vnyqj4sdwpv86683c6sm4" FOREIGN KEY ("lesson_plan_id") REFERENCES "lesson_plans" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-107
ALTER TABLE "public"."student_course_registrations" ADD CONSTRAINT "fk8ia9b68198r3esu5ca2sl13s4" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-108
ALTER TABLE "public"."material_lesson_attachments" ADD CONSTRAINT "fk8jr2dhmm44pojjr8rml96jpal" FOREIGN KEY ("material_id") REFERENCES "materials" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-109
ALTER TABLE "public"."subject_test_group_attachments" ADD CONSTRAINT "fk96g93x7vqct1axi3b5h0rsh9a" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-110
ALTER TABLE "public"."lesson_plan_topic_attachments" ADD CONSTRAINT "fkaaejie359b1k0vtnkgyn8wid9" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-111
ALTER TABLE "public"."lesson_plan_materials_attachments" ADD CONSTRAINT "fkajaarvxknbgk5ttsjsb9f9gfc" FOREIGN KEY ("material_id") REFERENCES "materials" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-112
ALTER TABLE "public"."test_results" ADD CONSTRAINT "fkbf0wjcwu5oyayhevc36opvyv1" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-113
ALTER TABLE "public"."subject_course_attachments" ADD CONSTRAINT "fkbxfdmpilfnugbdjukhdw25feq" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-114
ALTER TABLE "public"."lesson_homework_completions" ADD CONSTRAINT "fkcfvpknoinki18g9bt1lh0dtbe" FOREIGN KEY ("student_lesson_registration_id") REFERENCES "student_lesson_registrations" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-115
ALTER TABLE "public"."student_lesson_registrations" ADD CONSTRAINT "fkcq3wjfjsemkfd7nlw6j6qtkdo" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-116
ALTER TABLE "public"."lesson_postponement_requests" ADD CONSTRAINT "fkd2sw239pe2mgp6xex4ml8qdx6" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-117
ALTER TABLE "public"."student_lesson_attendances" ADD CONSTRAINT "fkd3d9tnxs4prcu1tx6fkyj2ono" FOREIGN KEY ("student_lesson_registration_id") REFERENCES "student_lesson_registrations" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-118
ALTER TABLE "public"."lesson_objective_topic_attachments" ADD CONSTRAINT "fkd3pecdl3ypiaqv16wvwocyu9g" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-119
ALTER TABLE "public"."students" ADD CONSTRAINT "fkdojmg8v3rw2ow4dev2b8q5oqq" FOREIGN KEY ("school_id") REFERENCES "schools" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-120
ALTER TABLE "public"."lesson_objectives" ADD CONSTRAINT "fkdtpxux294qelayd20cq0lh8up" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-121
ALTER TABLE "public"."outlet_rooms" ADD CONSTRAINT "fkdvhwlaeccmlum90r33ce27gwk" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-122
ALTER TABLE "public"."lessons" ADD CONSTRAINT "fke94a0k21xpi7glv89af90lwjv" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-123
ALTER TABLE "public"."test_results" ADD CONSTRAINT "fkeb5e15t9e5hn11gbkuub0xeln" FOREIGN KEY ("test_id") REFERENCES "tests" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-124
ALTER TABLE "public"."level_educator_attachments" ADD CONSTRAINT "fkehpls3vu8in5k768tieaenm3i" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-125
ALTER TABLE "public"."material_lesson_attachments" ADD CONSTRAINT "fkeisfjktwhnaqpgnbfr362a6od" FOREIGN KEY ("lesson_plan_id") REFERENCES "lesson_plans" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-126
ALTER TABLE "public"."students" ADD CONSTRAINT "fkf387qaaiqu3c7yueklkmoa2oy" FOREIGN KEY ("level_id") REFERENCES "levels" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-127
ALTER TABLE "public"."subject_course_attachments" ADD CONSTRAINT "fkf8nvaeqf8cck3nro3xj1vqd6g" FOREIGN KEY ("course_id") REFERENCES "courses" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-128
ALTER TABLE "public"."educator_lesson_attachments" ADD CONSTRAINT "fkfqsl52fbg3qshyyf46a631phk" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-129
ALTER TABLE "public"."student_lesson_registrations" ADD CONSTRAINT "fkfs6rpso3jq6jvmc25y9dkdicq" FOREIGN KEY ("lesson_plan_id") REFERENCES "lesson_plans" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-130
ALTER TABLE "public"."lesson_outlet_room_bookings" ADD CONSTRAINT "fkg0kyl6jko5wo92djnheu0fgwq" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-131
ALTER TABLE "public"."lesson_topic_proficiencies" ADD CONSTRAINT "fkgvlxk82p3g84yofimsojf1lvj" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-132
ALTER TABLE "public"."lesson_quizzes" ADD CONSTRAINT "fkhdf4r8mc9l91wr1xnv06cxekl" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-133
ALTER TABLE "public"."student_outlet_registrations" ADD CONSTRAINT "fki38aohbfbpxji9l9k4n4u72qa" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-134
ALTER TABLE "public"."student_outlet_registrations" ADD CONSTRAINT "fki5nkulcywauy3brososhynck8" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-135
ALTER TABLE "public"."lessons" ADD CONSTRAINT "fkii11bs4ibinclr1jjbm684kbg" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-136
ALTER TABLE "public"."outlet_rooms_lesson_outlet_room_bookings" ADD CONSTRAINT "fkiybrplxw7u14j6tbixij10lp7" FOREIGN KEY ("lesson_outlet_room_bookings_id") REFERENCES "lesson_outlet_room_bookings" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-137
ALTER TABLE "public"."account_student_attachments" ADD CONSTRAINT "fkj80wbmvsn4nhkqc1mvcg5wltq" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-138
ALTER TABLE "public"."topic_subject_attachments" ADD CONSTRAINT "fkjwu83i77ux9qe84pe779nivpn" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-139
ALTER TABLE "public"."educator_lesson_attachments" ADD CONSTRAINT "fkjyx3cqipc80r8dck501xkqk15" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-140
ALTER TABLE "public"."topic_subject_attachments" ADD CONSTRAINT "fkkp2ro3xax8p3a5o8jii4bun5o" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-141
ALTER TABLE "public"."subject_educator_attachments" ADD CONSTRAINT "fkkx2mgmpg9rv32o66m3covuimy" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-142
ALTER TABLE "public"."level_educator_attachments" ADD CONSTRAINT "fkljknla8wosk6gm4ajrqdjvgew" FOREIGN KEY ("level_id") REFERENCES "levels" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-143
ALTER TABLE "public"."account_student_attachments" ADD CONSTRAINT "fklv9sjd3sdcodpiwqyi1c82jy4" FOREIGN KEY ("account_id") REFERENCES "accounts" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-144
ALTER TABLE "public"."courses" ADD CONSTRAINT "fkmt7g458qd9i0ogpaaswpvkt7v" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-145
ALTER TABLE "public"."test_results" ADD CONSTRAINT "fknrsl6stg8tc0n50hjs9nq2hhq" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-146
ALTER TABLE "public"."courses" ADD CONSTRAINT "fknstr1ki2i14ki21df4ykt2v05" FOREIGN KEY ("price_record_id") REFERENCES "price_records" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-147
ALTER TABLE "public"."account_educator_attachments" ADD CONSTRAINT "fkntmmu50ha3yl3smfqlcpm4h69" FOREIGN KEY ("account_id") REFERENCES "accounts" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-148
ALTER TABLE "public"."lesson_topic_proficiencies" ADD CONSTRAINT "fko1n7r8navcteuarxvbx7x5ndy" FOREIGN KEY ("student_lesson_registration_id") REFERENCES "student_lesson_registrations" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-149
ALTER TABLE "public"."outlet_rooms_lesson_outlet_room_bookings" ADD CONSTRAINT "fko26gr6xyfw2u0ritu595ayw1v" FOREIGN KEY ("outlet_room_id") REFERENCES "outlet_rooms" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-150
ALTER TABLE "public"."outlet_educator_attachments" ADD CONSTRAINT "fko4mahaaawp4rqesjtgjwk3iax" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-151
ALTER TABLE "public"."subject_level_attachments" ADD CONSTRAINT "fko4u7oohlrjou60k45gtvdvd5l" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-152
ALTER TABLE "public"."material_topic_attachments" ADD CONSTRAINT "fkoey987ikhm4tre93cfrkoe4q" FOREIGN KEY ("material_id") REFERENCES "materials" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-153
ALTER TABLE "public"."material_topic_attachments" ADD CONSTRAINT "fkohrkntthiptvc4dhj5w4xrvsj" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-154
ALTER TABLE "public"."student_lesson_registrations" ADD CONSTRAINT "fkok0kiw3mu0uh4kf0b6us9ikr3" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-155
ALTER TABLE "public"."lesson_quiz_student_lesson_registration_attachments" ADD CONSTRAINT "fkp0s0anvsx5go7tueiar42lb60" FOREIGN KEY ("student_lesson_registration_id") REFERENCES "student_lesson_registrations" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-156
ALTER TABLE "public"."outlet_educator_attachments" ADD CONSTRAINT "fkp6mm9ri4ymqwbm6hsj8358vai" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-157
ALTER TABLE "public"."lesson_participation_reviews" ADD CONSTRAINT "fkq7w0lg3yybrcds3jqtxbfeh9y" FOREIGN KEY ("student_lesson_registration_id") REFERENCES "student_lesson_registrations" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-158
ALTER TABLE "public"."educator_course_attachments" ADD CONSTRAINT "fkqd4faplkf3fnixqundx7yqtif" FOREIGN KEY ("course_id") REFERENCES "courses" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-159
ALTER TABLE "public"."educator_course_attachments" ADD CONSTRAINT "fkqsojk1jeoqolfq6nuc165p3hh" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-160
ALTER TABLE "public"."subject_educator_attachments" ADD CONSTRAINT "fkrma0kmnhdr914ea2gvg5kclly" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-161
ALTER TABLE "public"."lesson_objective_topic_attachments" ADD CONSTRAINT "fkssok55nl08l3048wlf9uvu0rl" FOREIGN KEY ("lesson_objective_id") REFERENCES "lesson_objectives" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-162
ALTER TABLE "public"."lesson_cancellation_requests" ADD CONSTRAINT "fkst5m93umekq79mlka3syg851v" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1748070311079-163
ALTER TABLE "public"."tests" ADD CONSTRAINT "fktqfc6jqrqlvpsvtu4l1t294x1" FOREIGN KEY ("test_group_id") REFERENCES "test_groups" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

