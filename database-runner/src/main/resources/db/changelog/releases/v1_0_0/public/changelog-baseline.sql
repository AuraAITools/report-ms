-- liquibase formatted sql

-- changeset kevin:1766810234582-0
CREATE SCHEMA IF NOT EXISTS public;

-- changeset kevin:1766810234582-1
CREATE TABLE public."lesson_plans" ("is_planned" BOOLEAN DEFAULT FALSE NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_plans_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-2
CREATE TABLE public."accounts" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "contact" VARCHAR(255) NOT NULL, "created_by" VARCHAR(255), "email" VARCHAR(255) NOT NULL, "first_name" VARCHAR(255) NOT NULL, "last_name" VARCHAR(255) NOT NULL, "relationship" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), "user_id" VARCHAR(255), CONSTRAINT "accounts_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-3
CREATE TABLE public."courses" ("max_size" INTEGER NOT NULL, "course_end_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "course_start_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "level_id" UUID, "outlet_id" UUID NOT NULL, "price_record_id" UUID, "created_by" VARCHAR(255), "lesson_frequency" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "courses_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-4
CREATE TABLE public."educators" ("date_of_birth" date, "start_date" date, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "email" VARCHAR(255) NOT NULL, "employment_type" VARCHAR(255) NOT NULL, "name" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "educators_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-5
CREATE TABLE public."lesson_cancellation_requests" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_id" UUID, "approved_by" VARCHAR(255) NOT NULL, "created_by" VARCHAR(255), "reasoning" VARCHAR(255) NOT NULL, "status" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_cancellation_requests_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-6
CREATE TABLE public."lesson_participation_reviews" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "student_lesson_link_id" UUID, "created_by" VARCHAR(255), "participation_type" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_participation_reviews_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-7
CREATE TABLE public."lesson_postponement_requests" ("created_at" TIMESTAMP WITH TIME ZONE, "postponed_lesson_end_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "postponed_lesson_start_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_id" UUID, "approved_by" VARCHAR(255) NOT NULL, "created_by" VARCHAR(255), "reasoning" VARCHAR(255) NOT NULL, "status" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_postponement_requests_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-8
CREATE TABLE public."level_templates" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255) NOT NULL, "school_category" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "level_templates_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-9
CREATE TABLE public."levels" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "category" VARCHAR(255) NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "levels_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-10
CREATE TABLE public."price_records" ("price" FLOAT8 NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "frequency" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "price_records_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-11
CREATE TABLE public."school_templates" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255), "school_category" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "school_templates_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-12
CREATE TABLE public."schools" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255), "school_category" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "schools_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-13
CREATE TABLE public."account_educator_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "account_id" UUID, "educator_id" UUID, "id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "account_educator_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-14
CREATE TABLE public."account_student_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "account_id" UUID, "id" UUID NOT NULL, "student_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "account_student_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-15
CREATE TABLE public."educator_course_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "course_id" UUID, "educator_id" UUID, "id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "educator_course_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-16
CREATE TABLE public."educator_lesson_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "educator_id" UUID, "id" UUID NOT NULL, "lesson_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "educator_lesson_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-17
CREATE TABLE public."institutions" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "address" VARCHAR(255), "contact_number" VARCHAR(255), "created_by" VARCHAR(255), "email" VARCHAR(255) NOT NULL, "name" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "uen" VARCHAR(255), "updated_by" VARCHAR(255), CONSTRAINT "institutions_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-18
CREATE TABLE public."invoices" ("cost" FLOAT8 NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "invoices_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-19
CREATE TABLE public."lesson_homework_completions" ("completion" INTEGER, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "student_lesson_link_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_homework_completions_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-20
CREATE TABLE public."lesson_objective_topic_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_objective_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_objective_topic_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-21
CREATE TABLE public."lesson_objectives" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_objectives_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-22
CREATE TABLE public."lesson_outlet_room_bookings" ("created_at" TIMESTAMP WITH TIME ZONE, "end_timestampz" TIMESTAMP WITH TIME ZONE NOT NULL, "start_timestampz" TIMESTAMP WITH TIME ZONE NOT NULL, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_id" UUID, "outlet_room_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_outlet_room_bookings_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-23
CREATE TABLE public."lesson_plan_materials_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_plan_id" UUID, "material_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_plan_materials_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-24
CREATE TABLE public."lesson_plan_topic_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_plan_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_plan_topic_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-25
CREATE TABLE public."lesson_quiz_student_lesson_links" ("max_score" INTEGER NOT NULL, "score" INTEGER NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_quiz_id" UUID, "student_lesson_link_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_quiz_student_lesson_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-26
CREATE TABLE public."lesson_quizzes" ("max_score" INTEGER NOT NULL, "score" INTEGER, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_quizzes_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-27
CREATE TABLE public."lesson_topic_proficiencies" ("rating" INTEGER, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "student_lesson_link_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lesson_topic_proficiencies_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-28
CREATE TABLE public."lessons" ("created_at" TIMESTAMP WITH TIME ZONE, "lesson_end_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "lesson_start_timestamptz" TIMESTAMP WITH TIME ZONE NOT NULL, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "course_id" UUID, "id" UUID NOT NULL, "outlet_id" UUID NOT NULL, "subject_id" UUID, "created_by" VARCHAR(255), "description" VARCHAR(255), "name" VARCHAR(255) NOT NULL, "recap" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "lessons_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-29
CREATE TABLE public."level_educator_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "educator_id" UUID, "id" UUID NOT NULL, "level_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "level_educator_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-30
CREATE TABLE public."material_lesson_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_id" UUID, "lesson_plan_id" UUID, "material_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "material_lesson_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-31
CREATE TABLE public."material_topic_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "material_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "material_topic_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-32
CREATE TABLE public."materials" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "file_url" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "materials_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-33
CREATE TABLE public."outlet_educator_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "educator_id" UUID, "id" UUID NOT NULL, "outlet_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "outlet_educator_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-34
CREATE TABLE public."outlet_rooms" ("size" INTEGER NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "outlet_id" UUID NOT NULL, "created_by" VARCHAR(255), "details" VARCHAR(255), "file_url" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "outlet_rooms_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-35
CREATE TABLE public."outlet_rooms_lesson_outlet_room_bookings" ("lesson_outlet_room_bookings_id" UUID NOT NULL, "outlet_room_table_id" UUID NOT NULL, CONSTRAINT "outlet_rooms_lesson_outlet_room_bookings_pkey" PRIMARY KEY ("lesson_outlet_room_bookings_id", "outlet_room_table_id"));

-- changeset kevin:1766810234582-36
CREATE TABLE public."outlets" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "address" VARCHAR(255), "contact_number" VARCHAR(255), "created_by" VARCHAR(255), "description" VARCHAR(255), "email" VARCHAR(255), "name" VARCHAR(255), "postal_code" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "outlets_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-37
CREATE TABLE public."student_course_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "course_id" UUID, "id" UUID NOT NULL, "student_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "student_course_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-38
CREATE TABLE public."student_lesson_attendances" ("attended" BOOLEAN NOT NULL, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "student_lesson_link_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "student_lesson_attendances_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-39
CREATE TABLE public."student_lesson_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "lesson_id" UUID NOT NULL, "lesson_plan_id" UUID, "student_id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "student_lesson_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-40
CREATE TABLE public."student_outlet_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "outlet_id" UUID, "student_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "student_outlet_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-41
CREATE TABLE public."students" ("date_of_birth" date, "created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "level_id" UUID, "school_id" UUID, "created_by" VARCHAR(255), "email" VARCHAR(255) NOT NULL, "name" VARCHAR(255) NOT NULL, "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "students_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-42
CREATE TABLE public."subject_course_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "course_id" UUID, "id" UUID NOT NULL, "subject_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_course_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-43
CREATE TABLE public."subject_educator_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "educator_id" UUID, "id" UUID NOT NULL, "subject_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_educator_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-44
CREATE TABLE public."subject_level_link_templates" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "level_name" VARCHAR(255) NOT NULL, "subject_name" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_level_link_templates_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-45
CREATE TABLE public."subject_level_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "level_id" UUID, "subject_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_level_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-46
CREATE TABLE public."subject_student_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "student_id" UUID, "subject_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_student_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-47
CREATE TABLE public."subject_templates" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255), "updated_by" VARCHAR(255), CONSTRAINT "subject_templates_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-48
CREATE TABLE public."subject_test_group_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "subject_id" UUID, "test_group_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subject_test_group_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-49
CREATE TABLE public."subjects" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "subjects_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-50
CREATE TABLE public."test_groups" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "test_groups_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-51
CREATE TABLE public."test_results" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "student_id" UUID, "subject_id" UUID, "test_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "test_results_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-52
CREATE TABLE public."tests" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "test_group_id" UUID, "created_by" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "tests_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-53
CREATE TABLE public."topic_subject_link_templates" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "subject_name" VARCHAR(255) NOT NULL, "topic_name" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "topic_subject_link_templates_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-54
CREATE TABLE public."topic_subject_links" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "subject_id" UUID, "topic_id" UUID, "created_by" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "topic_subject_links_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-55
CREATE TABLE public."topics" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255), "tenant_id" VARCHAR(255) NOT NULL, "updated_by" VARCHAR(255), CONSTRAINT "topics_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-56
CREATE TABLE public."topics_templates" ("created_at" TIMESTAMP WITH TIME ZONE, "updated_at" TIMESTAMP WITH TIME ZONE, "version" BIGINT, "id" UUID NOT NULL, "created_by" VARCHAR(255), "name" VARCHAR(255), "updated_by" VARCHAR(255), CONSTRAINT "topics_templates_pkey" PRIMARY KEY ("id"));

-- changeset kevin:1766810234582-57
ALTER TABLE public."accounts" ADD CONSTRAINT "uk_tenant_email" UNIQUE ("tenant_id", "email");

-- changeset kevin:1766810234582-58
ALTER TABLE public."accounts" ADD CONSTRAINT "uk_tenant_user" UNIQUE ("tenant_id", "user_id");

-- changeset kevin:1766810234582-59
ALTER TABLE public."courses" ADD CONSTRAINT "courses_price_record_id_key" UNIQUE ("price_record_id");

-- changeset kevin:1766810234582-60
ALTER TABLE public."lesson_participation_reviews" ADD CONSTRAINT "lesson_participation_reviews_student_lesson_link_id_key" UNIQUE ("student_lesson_link_id");

-- changeset kevin:1766810234582-61
ALTER TABLE public."levels" ADD CONSTRAINT "uk_tenant_level_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1766810234582-62
ALTER TABLE public."schools" ADD CONSTRAINT "uk_school_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1766810234582-63
ALTER TABLE public."account_educator_links" ADD CONSTRAINT "uk_account_educator" UNIQUE ("account_id", "educator_id");

-- changeset kevin:1766810234582-64
ALTER TABLE public."account_student_links" ADD CONSTRAINT "uk_account_student" UNIQUE ("account_id", "student_id");

-- changeset kevin:1766810234582-65
ALTER TABLE public."educator_course_links" ADD CONSTRAINT "uk_educator_course" UNIQUE ("educator_id", "course_id");

-- changeset kevin:1766810234582-66
ALTER TABLE public."educator_lesson_links" ADD CONSTRAINT "uk_educator_lesson" UNIQUE ("educator_id", "lesson_id");

-- changeset kevin:1766810234582-67
ALTER TABLE public."institutions" ADD CONSTRAINT "institutions_email_key" UNIQUE ("email");

-- changeset kevin:1766810234582-68
ALTER TABLE public."institutions" ADD CONSTRAINT "institutions_name_key" UNIQUE ("name");

-- changeset kevin:1766810234582-69
ALTER TABLE public."lesson_homework_completions" ADD CONSTRAINT "lesson_homework_completions_student_lesson_link_id_key" UNIQUE ("student_lesson_link_id");

-- changeset kevin:1766810234582-70
ALTER TABLE public."lesson_objective_topic_links" ADD CONSTRAINT "uk_lesson_objective_topic" UNIQUE ("lesson_objective_id", "topic_id");

-- changeset kevin:1766810234582-71
ALTER TABLE public."lesson_outlet_room_bookings" ADD CONSTRAINT "lesson_outlet_room_bookings_lesson_id_key" UNIQUE ("lesson_id");

-- changeset kevin:1766810234582-72
ALTER TABLE public."lesson_outlet_room_bookings" ADD CONSTRAINT "uk_lesson_outlet_room" UNIQUE ("lesson_id", "outlet_room_id");

-- changeset kevin:1766810234582-73
ALTER TABLE public."lesson_plan_materials_links" ADD CONSTRAINT "uk_lesson_plan_material" UNIQUE ("lesson_plan_id", "material_id");

-- changeset kevin:1766810234582-74
ALTER TABLE public."lesson_plan_topic_links" ADD CONSTRAINT "uk_lesson_plan_topic" UNIQUE ("lesson_plan_id", "topic_id");

-- changeset kevin:1766810234582-75
ALTER TABLE public."lesson_quiz_student_lesson_links" ADD CONSTRAINT "uk_lesson_quiz_student_lesson_link" UNIQUE ("lesson_quiz_id", "student_lesson_link_id");

-- changeset kevin:1766810234582-76
ALTER TABLE public."lesson_topic_proficiencies" ADD CONSTRAINT "lesson_topic_proficiencies_student_lesson_link_id_key" UNIQUE ("student_lesson_link_id");

-- changeset kevin:1766810234582-77
ALTER TABLE public."lessons" ADD CONSTRAINT "uk_tenant_lesson_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1766810234582-78
ALTER TABLE public."level_educator_links" ADD CONSTRAINT "uk_level_educator" UNIQUE ("level_id", "educator_id");

-- changeset kevin:1766810234582-79
ALTER TABLE public."material_lesson_links" ADD CONSTRAINT "uk_material_lesson" UNIQUE ("material_id", "lesson_id");

-- changeset kevin:1766810234582-80
ALTER TABLE public."material_topic_links" ADD CONSTRAINT "uk_material_topic" UNIQUE ("material_id", "topic_id");

-- changeset kevin:1766810234582-81
ALTER TABLE public."outlet_educator_links" ADD CONSTRAINT "uk_outlet_educator" UNIQUE ("outlet_id", "educator_id");

-- changeset kevin:1766810234582-82
ALTER TABLE public."outlet_rooms" ADD CONSTRAINT "uk_tenant_outlet_name" UNIQUE ("name", "tenant_id");

-- changeset kevin:1766810234582-83
ALTER TABLE public."outlet_rooms_lesson_outlet_room_bookings" ADD CONSTRAINT "outlet_rooms_lesson_outlet_ro_lesson_outlet_room_bookings_i_key" UNIQUE ("lesson_outlet_room_bookings_id");

-- changeset kevin:1766810234582-84
ALTER TABLE public."outlets" ADD CONSTRAINT "uk_outlet_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1766810234582-85
ALTER TABLE public."student_course_links" ADD CONSTRAINT "uk_student_course" UNIQUE ("student_id", "course_id");

-- changeset kevin:1766810234582-86
ALTER TABLE public."student_lesson_attendances" ADD CONSTRAINT "student_lesson_attendances_student_lesson_link_id_key" UNIQUE ("student_lesson_link_id");

-- changeset kevin:1766810234582-87
ALTER TABLE public."student_lesson_links" ADD CONSTRAINT "uk_student_lesson" UNIQUE ("student_id", "lesson_id");

-- changeset kevin:1766810234582-88
ALTER TABLE public."student_outlet_links" ADD CONSTRAINT "uk_student_outlet" UNIQUE ("student_id", "outlet_id");

-- changeset kevin:1766810234582-89
ALTER TABLE public."subject_course_links" ADD CONSTRAINT "uk_subject_course" UNIQUE ("subject_id", "course_id");

-- changeset kevin:1766810234582-90
ALTER TABLE public."subject_educator_links" ADD CONSTRAINT "uk_subject_educator" UNIQUE ("subject_id", "educator_id");

-- changeset kevin:1766810234582-91
ALTER TABLE public."subject_level_link_templates" ADD CONSTRAINT "uk_subject_level_name" UNIQUE ("subject_name", "level_name");

-- changeset kevin:1766810234582-92
ALTER TABLE public."subject_level_links" ADD CONSTRAINT "uk_subject_level" UNIQUE ("subject_id", "level_id");

-- changeset kevin:1766810234582-93
ALTER TABLE public."subject_student_links" ADD CONSTRAINT "uk_subject_student" UNIQUE ("subject_id", "student_id");

-- changeset kevin:1766810234582-94
ALTER TABLE public."subject_test_group_links" ADD CONSTRAINT "uk_subject_test_group" UNIQUE ("subject_id", "test_group_id");

-- changeset kevin:1766810234582-95
ALTER TABLE public."subjects" ADD CONSTRAINT "uk_subject_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1766810234582-96
ALTER TABLE public."topic_subject_link_templates" ADD CONSTRAINT "uk_topic_subject_name" UNIQUE ("subject_name", "topic_name");

-- changeset kevin:1766810234582-97
ALTER TABLE public."topic_subject_links" ADD CONSTRAINT "uk_topic_subject" UNIQUE ("topic_id", "subject_id");

-- changeset kevin:1766810234582-98
ALTER TABLE public."topics" ADD CONSTRAINT "uk_tenant_topic_name" UNIQUE ("tenant_id", "name");

-- changeset kevin:1766810234582-99
CREATE SEQUENCE  IF NOT EXISTS "revinfo_seq" AS bigint START WITH 1 INCREMENT BY 50 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

-- changeset kevin:1766810234582-100
ALTER TABLE public."lessons" ADD CONSTRAINT "fk17ucc7gjfjddsyi0gvstkqeat" FOREIGN KEY ("course_id") REFERENCES "courses" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-101
ALTER TABLE public."material_topic_links" ADD CONSTRAINT "fk1h9be3sceoy20tfbarexsfbfr" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-102
ALTER TABLE public."topic_subject_links" ADD CONSTRAINT "fk1mtqpaci1ecklmduruvb6qn92" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-103
ALTER TABLE public."subject_test_group_links" ADD CONSTRAINT "fk2yi0ktif03taqrnnl7sl46igi" FOREIGN KEY ("test_group_id") REFERENCES "test_groups" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-104
ALTER TABLE public."student_lesson_attendances" ADD CONSTRAINT "fk35dbe30iucor8oxgbq74ut3w4" FOREIGN KEY ("student_lesson_link_id") REFERENCES "student_lesson_links" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-105
ALTER TABLE public."subject_course_links" ADD CONSTRAINT "fk3kdk8sbyt696slef1cc87j7ie" FOREIGN KEY ("course_id") REFERENCES "courses" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-106
ALTER TABLE public."lesson_plans" ADD CONSTRAINT "fk3laj7cxvxsm9dbg04a3s4dwj2" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-107
ALTER TABLE public."material_lesson_links" ADD CONSTRAINT "fk3obrk5wu6n5tvdkfyjhd1crx7" FOREIGN KEY ("material_id") REFERENCES "materials" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-108
ALTER TABLE public."lesson_outlet_room_bookings" ADD CONSTRAINT "fk48hq0vsrfd3h566329dmn3vhq" FOREIGN KEY ("outlet_room_id") REFERENCES "outlet_rooms" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-109
ALTER TABLE public."student_lesson_links" ADD CONSTRAINT "fk4dnf3tu1nj5tjv33r9bcsm97h" FOREIGN KEY ("lesson_plan_id") REFERENCES "lesson_plans" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-110
ALTER TABLE public."courses" ADD CONSTRAINT "fk5h26i8gulbtggcwuqqkwh0yw1" FOREIGN KEY ("level_id") REFERENCES "levels" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-111
ALTER TABLE public."lesson_plan_topic_links" ADD CONSTRAINT "fk5msjq1ga37eyx92qtseykdmcd" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-112
ALTER TABLE public."subject_level_links" ADD CONSTRAINT "fk620mxqh8dbv1c567ftymprxdf" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-113
ALTER TABLE public."account_educator_links" ADD CONSTRAINT "fk6ar9tv399nv5us0ee7r9lh8b3" FOREIGN KEY ("account_id") REFERENCES "accounts" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-114
ALTER TABLE public."subject_level_links" ADD CONSTRAINT "fk6vnbelj20wq6t3mb3s40sjkjh" FOREIGN KEY ("level_id") REFERENCES "levels" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-115
ALTER TABLE public."subject_test_group_links" ADD CONSTRAINT "fk6vtftg9f3kacrwkmesnp7ok3d" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-116
ALTER TABLE public."student_lesson_links" ADD CONSTRAINT "fk73pu031qt2a4sq4sex676u61n" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-117
ALTER TABLE public."student_course_links" ADD CONSTRAINT "fk7cayt38oa48h1rkrtp1hk0qai" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-118
ALTER TABLE public."lesson_topic_proficiencies" ADD CONSTRAINT "fk814snfies19rboappn8lrutdv" FOREIGN KEY ("student_lesson_link_id") REFERENCES "student_lesson_links" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-119
ALTER TABLE public."lesson_objective_topic_links" ADD CONSTRAINT "fkaf712x9rg9ajclwthexc244vm" FOREIGN KEY ("lesson_objective_id") REFERENCES "lesson_objectives" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-120
ALTER TABLE public."material_lesson_links" ADD CONSTRAINT "fkasqjqtcnosy3v937llxlimvx7" FOREIGN KEY ("lesson_plan_id") REFERENCES "lesson_plans" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-121
ALTER TABLE public."level_educator_links" ADD CONSTRAINT "fkassd6pbh4g1j9bcyspute74nj" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-122
ALTER TABLE public."test_results" ADD CONSTRAINT "fkbf0wjcwu5oyayhevc36opvyv1" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-123
ALTER TABLE public."student_course_links" ADD CONSTRAINT "fkbgvm2qrjqsx0en2imvgqild2r" FOREIGN KEY ("course_id") REFERENCES "courses" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-124
ALTER TABLE public."account_student_links" ADD CONSTRAINT "fkbqairnlxcu7eagyfabyw95a50" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-125
ALTER TABLE public."subject_educator_links" ADD CONSTRAINT "fkbsbum84jv09xa03thpq0oopue" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-126
ALTER TABLE public."lesson_quiz_student_lesson_links" ADD CONSTRAINT "fkd2ekyx2rfp1iajuwkufr571rn" FOREIGN KEY ("lesson_quiz_id") REFERENCES "lesson_quizzes" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-127
ALTER TABLE public."lesson_postponement_requests" ADD CONSTRAINT "fkd2sw239pe2mgp6xex4ml8qdx6" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-128
ALTER TABLE public."students" ADD CONSTRAINT "fkdojmg8v3rw2ow4dev2b8q5oqq" FOREIGN KEY ("school_id") REFERENCES "schools" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-129
ALTER TABLE public."lesson_objectives" ADD CONSTRAINT "fkdtpxux294qelayd20cq0lh8up" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-130
ALTER TABLE public."outlet_rooms" ADD CONSTRAINT "fkdvhwlaeccmlum90r33ce27gwk" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-131
ALTER TABLE public."lesson_objective_topic_links" ADD CONSTRAINT "fke93or59ghs8gcv2fplflau16h" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-132
ALTER TABLE public."lessons" ADD CONSTRAINT "fke94a0k21xpi7glv89af90lwjv" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-133
ALTER TABLE public."test_results" ADD CONSTRAINT "fkeb5e15t9e5hn11gbkuub0xeln" FOREIGN KEY ("test_id") REFERENCES "tests" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-134
ALTER TABLE public."lesson_participation_reviews" ADD CONSTRAINT "fkeem6c1tmxqeef93lj2sinov22" FOREIGN KEY ("student_lesson_link_id") REFERENCES "student_lesson_links" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-135
ALTER TABLE public."level_educator_links" ADD CONSTRAINT "fkegtltc9b45lqsjvljky19dsp" FOREIGN KEY ("level_id") REFERENCES "levels" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-136
ALTER TABLE public."lesson_quiz_student_lesson_links" ADD CONSTRAINT "fkeyqechwqddfxlq55d4sijtmgf" FOREIGN KEY ("student_lesson_link_id") REFERENCES "student_lesson_links" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-137
ALTER TABLE public."students" ADD CONSTRAINT "fkf387qaaiqu3c7yueklkmoa2oy" FOREIGN KEY ("level_id") REFERENCES "levels" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-138
ALTER TABLE public."lesson_outlet_room_bookings" ADD CONSTRAINT "fkg0kyl6jko5wo92djnheu0fgwq" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-139
ALTER TABLE public."lesson_plan_topic_links" ADD CONSTRAINT "fkg5qnqonq4qatvhcmsw97hpai6" FOREIGN KEY ("lesson_plan_id") REFERENCES "lesson_plans" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-140
ALTER TABLE public."account_educator_links" ADD CONSTRAINT "fkg8wp164ct6lxrtt18vl2nocga" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-141
ALTER TABLE public."subject_student_links" ADD CONSTRAINT "fkglg4rv6321ll3pkygjyu7d2nn" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-142
ALTER TABLE public."subject_educator_links" ADD CONSTRAINT "fkgltppoquh6deofe02hnntsewi" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-143
ALTER TABLE public."lesson_topic_proficiencies" ADD CONSTRAINT "fkgvlxk82p3g84yofimsojf1lvj" FOREIGN KEY ("topic_id") REFERENCES "topics" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-144
ALTER TABLE public."student_lesson_links" ADD CONSTRAINT "fkgx7i6cdxvmh1xgxpwubkj8jri" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-145
ALTER TABLE public."lesson_quizzes" ADD CONSTRAINT "fkhdf4r8mc9l91wr1xnv06cxekl" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-146
ALTER TABLE public."educator_lesson_links" ADD CONSTRAINT "fkigqytqdnqu29g7dg6trunvrvb" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-147
ALTER TABLE public."lessons" ADD CONSTRAINT "fkii11bs4ibinclr1jjbm684kbg" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-148
ALTER TABLE public."lesson_homework_completions" ADD CONSTRAINT "fkiply454dqfg4ikn9lbnl58f2" FOREIGN KEY ("student_lesson_link_id") REFERENCES "student_lesson_links" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-149
ALTER TABLE public."outlet_rooms_lesson_outlet_room_bookings" ADD CONSTRAINT "fkiybrplxw7u14j6tbixij10lp7" FOREIGN KEY ("lesson_outlet_room_bookings_id") REFERENCES "lesson_outlet_room_bookings" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-150
ALTER TABLE public."lesson_plan_materials_links" ADD CONSTRAINT "fkj2n882yys4wt6pxflo9eul4m" FOREIGN KEY ("lesson_plan_id") REFERENCES "lesson_plans" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-151
ALTER TABLE public."student_outlet_links" ADD CONSTRAINT "fkj7vcqq94aej41txbj9s3w6d3u" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-152
ALTER TABLE public."lesson_plan_materials_links" ADD CONSTRAINT "fkjm53j47t1v2ss0ioucxeanqiy" FOREIGN KEY ("material_id") REFERENCES "materials" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-153
ALTER TABLE public."account_student_links" ADD CONSTRAINT "fkk1le2hgl6rwy92wnybmq5n2tf" FOREIGN KEY ("account_id") REFERENCES "accounts" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-154
ALTER TABLE public."subject_student_links" ADD CONSTRAINT "fkkqyicw6ciqrerd40lj4tad9ma" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-155
ALTER TABLE public."educator_course_links" ADD CONSTRAINT "fkkv2g3jej0weymy6cwfm4jxyhb" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-156
ALTER TABLE public."material_topic_links" ADD CONSTRAINT "fkkvoolw2uef6itk8nw8xptkuus" FOREIGN KEY ("material_id") REFERENCES "materials" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-157
ALTER TABLE public."educator_lesson_links" ADD CONSTRAINT "fkm5ag0fn04xrffr7bq7i5gkiy7" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-158
ALTER TABLE public."topic_subject_links" ADD CONSTRAINT "fkmh6ws93de788b2922hvdk8g2b" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-159
ALTER TABLE public."educator_course_links" ADD CONSTRAINT "fkmpsvk4x7l3m8u6k2nhofigq6d" FOREIGN KEY ("course_id") REFERENCES "courses" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-160
ALTER TABLE public."courses" ADD CONSTRAINT "fkmt7g458qd9i0ogpaaswpvkt7v" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-161
ALTER TABLE public."outlet_educator_links" ADD CONSTRAINT "fkn1dtehsbimd49ap9318bowbeh" FOREIGN KEY ("educator_id") REFERENCES "educators" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-162
ALTER TABLE public."test_results" ADD CONSTRAINT "fknrsl6stg8tc0n50hjs9nq2hhq" FOREIGN KEY ("student_id") REFERENCES "students" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-163
ALTER TABLE public."courses" ADD CONSTRAINT "fknstr1ki2i14ki21df4ykt2v05" FOREIGN KEY ("price_record_id") REFERENCES "price_records" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-164
ALTER TABLE public."outlet_rooms_lesson_outlet_room_bookings" ADD CONSTRAINT "fkom9r7t1lcq530gd536vnao6yl" FOREIGN KEY ("outlet_room_table_id") REFERENCES "outlet_rooms" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-165
ALTER TABLE public."student_outlet_links" ADD CONSTRAINT "fkopbiepry2r0qkvnq4x7oxttfb" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-166
ALTER TABLE public."material_lesson_links" ADD CONSTRAINT "fkpdp4wfc746y0ln5s5mqlu2bpp" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-167
ALTER TABLE public."subject_course_links" ADD CONSTRAINT "fkrtr1oxlwkl7wf99ejr7nbxutf" FOREIGN KEY ("subject_id") REFERENCES "subjects" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-168
ALTER TABLE public."outlet_educator_links" ADD CONSTRAINT "fkrx9f4q6lsh8yue8l42xx70vcy" FOREIGN KEY ("outlet_id") REFERENCES "outlets" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-169
ALTER TABLE public."lesson_cancellation_requests" ADD CONSTRAINT "fkst5m93umekq79mlka3syg851v" FOREIGN KEY ("lesson_id") REFERENCES "lessons" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810234582-170
ALTER TABLE public."tests" ADD CONSTRAINT "fktqfc6jqrqlvpsvtu4l1t294x1" FOREIGN KEY ("test_group_id") REFERENCES "test_groups" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

