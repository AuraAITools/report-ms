-- liquibase formatted sql

-- changeset kevin:1766810291486-0
CREATE SCHEMA IF NOT EXISTS audit;

-- changeset kevin:1766810291486-1
CREATE TABLE audit."lesson_plans_aud" ("is_planned" BOOLEAN DEFAULT FALSE, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_id" UUID, CONSTRAINT "lesson_plans_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-2
CREATE TABLE audit."accounts_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "contact" VARCHAR(255), "email" VARCHAR(255), "first_name" VARCHAR(255), "last_name" VARCHAR(255), "relationship" VARCHAR(255), "user_id" VARCHAR(255), CONSTRAINT "accounts_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-3
CREATE TABLE audit."courses_aud" ("max_size" INTEGER, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "course_end_timestamptz" TIMESTAMP WITH TIME ZONE, "course_start_timestamptz" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "level_id" UUID, "outlet_id" UUID, "price_record_id" UUID, "lesson_frequency" VARCHAR(255), "name" VARCHAR(255), CONSTRAINT "courses_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-4
CREATE TABLE audit."educators_aud" ("date_of_birth" date, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "start_date" date, "id" UUID NOT NULL, "email" VARCHAR(255), "employment_type" VARCHAR(255), "name" VARCHAR(255), CONSTRAINT "educators_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-5
CREATE TABLE audit."lesson_cancellation_requests_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_id" UUID, "approved_by" VARCHAR(255), "reasoning" VARCHAR(255), "status" VARCHAR(255), CONSTRAINT "lesson_cancellation_requests_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-6
CREATE TABLE audit."lesson_participation_reviews_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "student_lesson_link_id" UUID, "participation_type" VARCHAR(255), CONSTRAINT "lesson_participation_reviews_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-7
CREATE TABLE audit."lesson_postponement_requests_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "postponed_lesson_end_timestamptz" TIMESTAMP WITH TIME ZONE, "postponed_lesson_start_timestamptz" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "approved_by" VARCHAR(255), "reasoning" VARCHAR(255), "status" VARCHAR(255), CONSTRAINT "lesson_postponement_requests_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-8
CREATE TABLE audit."level_templates_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "name" VARCHAR(255), "school_category" VARCHAR(255), CONSTRAINT "level_templates_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-9
CREATE TABLE audit."levels_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "category" VARCHAR(255), "name" VARCHAR(255), CONSTRAINT "levels_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-10
CREATE TABLE audit."price_records_aud" ("price" FLOAT8, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "frequency" VARCHAR(255), CONSTRAINT "price_records_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-11
CREATE TABLE audit."school_templates_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "name" VARCHAR(255), "school_category" VARCHAR(255), CONSTRAINT "school_templates_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-12
CREATE TABLE audit."schools_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "name" VARCHAR(255), "school_category" VARCHAR(255), CONSTRAINT "schools_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-13
CREATE TABLE audit."account_educator_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "account_id" UUID, "educator_id" UUID, "id" UUID NOT NULL, CONSTRAINT "account_educator_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-14
CREATE TABLE audit."account_student_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "account_id" UUID, "id" UUID NOT NULL, "student_id" UUID, CONSTRAINT "account_student_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-15
CREATE TABLE audit."educator_course_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "course_id" UUID, "educator_id" UUID, "id" UUID NOT NULL, CONSTRAINT "educator_course_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-16
CREATE TABLE audit."educator_lesson_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "educator_id" UUID, "id" UUID NOT NULL, "lesson_id" UUID, CONSTRAINT "educator_lesson_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-17
CREATE TABLE audit."institutions_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "address" VARCHAR(255), "contact_number" VARCHAR(255), "email" VARCHAR(255), "name" VARCHAR(255), "random" VARCHAR(255), "tenant_id" VARCHAR(255), "uen" VARCHAR(255), CONSTRAINT "institutions_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-18
CREATE TABLE audit."invoices_aud" ("cost" FLOAT8, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, CONSTRAINT "invoices_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-19
CREATE TABLE audit."lesson_homework_completions_aud" ("completion" INTEGER, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "student_lesson_link_id" UUID, CONSTRAINT "lesson_homework_completions_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-20
CREATE TABLE audit."lesson_objective_topic_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_objective_id" UUID, "topic_id" UUID, CONSTRAINT "lesson_objective_topic_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-21
CREATE TABLE audit."lesson_objectives_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_id" UUID, CONSTRAINT "lesson_objectives_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-22
CREATE TABLE audit."lesson_outlet_room_bookings_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "end_timestampz" TIMESTAMP WITH TIME ZONE, "start_timestampz" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "outlet_room_id" UUID, CONSTRAINT "lesson_outlet_room_bookings_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-23
CREATE TABLE audit."lesson_plan_materials_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_plan_id" UUID, "material_id" UUID, CONSTRAINT "lesson_plan_materials_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-24
CREATE TABLE audit."lesson_plan_topic_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_plan_id" UUID, "topic_id" UUID, CONSTRAINT "lesson_plan_topic_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-25
CREATE TABLE audit."lesson_quiz_student_lesson_links_aud" ("max_score" INTEGER, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "score" INTEGER, "id" UUID NOT NULL, "lesson_quiz_id" UUID, "student_lesson_link_id" UUID, CONSTRAINT "lesson_quiz_student_lesson_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-26
CREATE TABLE audit."lesson_quizzes_aud" ("max_score" INTEGER, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "score" INTEGER, "id" UUID NOT NULL, "lesson_id" UUID, CONSTRAINT "lesson_quizzes_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-27
CREATE TABLE audit."lesson_topic_proficiencies_aud" ("rating" INTEGER, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "student_lesson_link_id" UUID, "topic_id" UUID, CONSTRAINT "lesson_topic_proficiencies_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-28
CREATE TABLE audit."lessons_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "lesson_end_timestamptz" TIMESTAMP WITH TIME ZONE, "lesson_start_timestamptz" TIMESTAMP WITH TIME ZONE, "course_id" UUID, "id" UUID NOT NULL, "outlet_id" UUID, "subject_id" UUID, "description" VARCHAR(255), "name" VARCHAR(255), "recap" VARCHAR(255), CONSTRAINT "lessons_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-29
CREATE TABLE audit."level_educator_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "educator_id" UUID, "id" UUID NOT NULL, "level_id" UUID, CONSTRAINT "level_educator_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-30
CREATE TABLE audit."material_lesson_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_id" UUID, "lesson_plan_id" UUID, "material_id" UUID, CONSTRAINT "material_lesson_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-31
CREATE TABLE audit."material_topic_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "material_id" UUID, "topic_id" UUID, CONSTRAINT "material_topic_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-32
CREATE TABLE audit."materials_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "file_url" VARCHAR(255), "name" VARCHAR(255), CONSTRAINT "materials_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-33
CREATE TABLE audit."outlet_educator_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "educator_id" UUID, "id" UUID NOT NULL, "outlet_id" UUID, CONSTRAINT "outlet_educator_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-34
CREATE TABLE audit."outlet_rooms_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "size" INTEGER, "id" UUID NOT NULL, "outlet_id" UUID, "details" VARCHAR(255), "file_url" VARCHAR(255), "name" VARCHAR(255), CONSTRAINT "outlet_rooms_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-35
CREATE TABLE audit."outlet_rooms_lesson_outlet_room_bookings_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "lesson_outlet_room_bookings_id" UUID NOT NULL, "outlet_room_table_id" UUID NOT NULL, CONSTRAINT "outlet_rooms_lesson_outlet_room_bookings_aud_pkey" PRIMARY KEY ("rev", "lesson_outlet_room_bookings_id", "outlet_room_table_id"));

-- changeset kevin:1766810291486-36
CREATE TABLE audit."outlets_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "address" VARCHAR(255), "contact_number" VARCHAR(255), "description" VARCHAR(255), "email" VARCHAR(255), "name" VARCHAR(255), "postal_code" VARCHAR(255), CONSTRAINT "outlets_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-37
CREATE TABLE audit."revinfo" ("rev" INTEGER NOT NULL, "revtstmp" BIGINT, CONSTRAINT "revinfo_pkey" PRIMARY KEY ("rev"));

-- changeset kevin:1766810291486-38
CREATE TABLE audit."student_course_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "course_id" UUID, "id" UUID NOT NULL, "student_id" UUID, CONSTRAINT "student_course_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-39
CREATE TABLE audit."student_lesson_attendances_aud" ("attended" BOOLEAN, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "student_lesson_link_id" UUID, CONSTRAINT "student_lesson_attendances_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-40
CREATE TABLE audit."student_lesson_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_id" UUID, "lesson_plan_id" UUID, "student_id" UUID, CONSTRAINT "student_lesson_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-41
CREATE TABLE audit."student_outlet_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "outlet_id" UUID, "student_id" UUID, CONSTRAINT "student_outlet_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-42
CREATE TABLE audit."students_aud" ("date_of_birth" date, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "level_id" UUID, "school_id" UUID, "email" VARCHAR(255), "name" VARCHAR(255), CONSTRAINT "students_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-43
CREATE TABLE audit."subject_course_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "course_id" UUID, "id" UUID NOT NULL, "subject_id" UUID, CONSTRAINT "subject_course_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-44
CREATE TABLE audit."subject_educator_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "educator_id" UUID, "id" UUID NOT NULL, "subject_id" UUID, CONSTRAINT "subject_educator_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-45
CREATE TABLE audit."subject_level_link_templates_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "level_name" VARCHAR(255), "subject_name" VARCHAR(255), CONSTRAINT "subject_level_link_templates_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-46
CREATE TABLE audit."subject_level_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "level_id" UUID, "subject_id" UUID, CONSTRAINT "subject_level_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-47
CREATE TABLE audit."subject_student_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "student_id" UUID, "subject_id" UUID, CONSTRAINT "subject_student_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-48
CREATE TABLE audit."subject_templates_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "name" VARCHAR(255), CONSTRAINT "subject_templates_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-49
CREATE TABLE audit."subject_test_group_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "subject_id" UUID, "test_group_id" UUID, CONSTRAINT "subject_test_group_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-50
CREATE TABLE audit."subjects_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "name" VARCHAR(255), CONSTRAINT "subjects_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-51
CREATE TABLE audit."test_groups_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, CONSTRAINT "test_groups_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-52
CREATE TABLE audit."test_results_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "student_id" UUID, "subject_id" UUID, "test_id" UUID, CONSTRAINT "test_results_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-53
CREATE TABLE audit."tests_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "test_group_id" UUID, "name" VARCHAR(255), CONSTRAINT "tests_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-54
CREATE TABLE audit."topic_subject_link_templates_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "subject_name" VARCHAR(255), "topic_name" VARCHAR(255), CONSTRAINT "topic_subject_link_templates_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-55
CREATE TABLE audit."topic_subject_links_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "subject_id" UUID, "topic_id" UUID, CONSTRAINT "topic_subject_links_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-56
CREATE TABLE audit."topics_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "name" VARCHAR(255), CONSTRAINT "topics_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-57
CREATE TABLE audit."topics_templates_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "name" VARCHAR(255), CONSTRAINT "topics_templates_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1766810291486-58
ALTER TABLE audit."topics_templates_aud" ADD CONSTRAINT "fk1bg8mpbliyd30i1fjwnl5tuvt" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-59
ALTER TABLE audit."lesson_postponement_requests_aud" ADD CONSTRAINT "fk1tr6vg5jl5ve6hcd6ejxbo5bn" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-60
ALTER TABLE audit."schools_aud" ADD CONSTRAINT "fk24ywgb7e0a79j12c2cwledu90" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-61
ALTER TABLE audit."student_lesson_links_aud" ADD CONSTRAINT "fk2m68kfxo0st6x0a8gbs9msfrh" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-62
ALTER TABLE audit."level_educator_links_aud" ADD CONSTRAINT "fk2u8qyj9kcsqdkku82lrqquiwh" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-63
ALTER TABLE audit."outlets_aud" ADD CONSTRAINT "fk36v22eesrvh4vx0usq6mib9mt" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-64
ALTER TABLE audit."outlet_rooms_aud" ADD CONSTRAINT "fk3d5jo2rysuem2ma0hq263ovgt" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-65
ALTER TABLE audit."lesson_topic_proficiencies_aud" ADD CONSTRAINT "fk486q29emwthms9k7fwokgqr8m" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-66
ALTER TABLE audit."institutions_aud" ADD CONSTRAINT "fk4oir4hsqtgpk8d805mpj8i9ti" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-67
ALTER TABLE audit."levels_aud" ADD CONSTRAINT "fk4ssb09udr8304fkrm2359521s" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-68
ALTER TABLE audit."account_student_links_aud" ADD CONSTRAINT "fk4urqbo74veypfvsekeqv6r8bi" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-69
ALTER TABLE audit."lesson_plans_aud" ADD CONSTRAINT "fk51opmofy0qw594di86ohjy7hd" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-70
ALTER TABLE audit."lesson_quiz_student_lesson_links_aud" ADD CONSTRAINT "fk55naepk6xc15rt539p4nv5mgv" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-71
ALTER TABLE audit."account_educator_links_aud" ADD CONSTRAINT "fk5qbmnttir8ipn8dmq0u2hrbpa" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-72
ALTER TABLE audit."student_lesson_attendances_aud" ADD CONSTRAINT "fk5yvo7e6mwu4rqq9ud8su0d6xk" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-73
ALTER TABLE audit."lesson_objectives_aud" ADD CONSTRAINT "fk6ocm6gm4yp0yxjgba7igkduwr" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-74
ALTER TABLE audit."lesson_plan_materials_links_aud" ADD CONSTRAINT "fk7fn5ccvtu7mbfuxkun1m1phq" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-75
ALTER TABLE audit."lesson_cancellation_requests_aud" ADD CONSTRAINT "fk84fw9993gykabg0pequulrp4r" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-76
ALTER TABLE audit."subject_templates_aud" ADD CONSTRAINT "fk8ju9yew2anuqclgf0441f5exa" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-77
ALTER TABLE audit."material_topic_links_aud" ADD CONSTRAINT "fk8qfi7sxqugmvd0mjkoskh4cg4" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-78
ALTER TABLE audit."lesson_homework_completions_aud" ADD CONSTRAINT "fk91ubqvu47dbq1i960d6aloxc5" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-79
ALTER TABLE audit."subject_course_links_aud" ADD CONSTRAINT "fk9rfc1uyol66plkaet3o2osdce" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-80
ALTER TABLE audit."subjects_aud" ADD CONSTRAINT "fkal3wfn2pmr284otulkg6v7k0t" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-81
ALTER TABLE audit."materials_aud" ADD CONSTRAINT "fkbxe3tk9a4903jh6yd3xfftuif" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-82
ALTER TABLE audit."level_templates_aud" ADD CONSTRAINT "fkc683kru58katnjn1p69me05h6" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-83
ALTER TABLE audit."student_course_links_aud" ADD CONSTRAINT "fkc6t72hyms14skysansy1lgxbq" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-84
ALTER TABLE audit."price_records_aud" ADD CONSTRAINT "fkckx4bc9h1v2yl5qpwrxfkabgc" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-85
ALTER TABLE audit."subject_test_group_links_aud" ADD CONSTRAINT "fkclmqehylw23p0uqqo680ganei" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-86
ALTER TABLE audit."test_results_aud" ADD CONSTRAINT "fkd4ktcnkekxl7eij86nrhhq4w" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-87
ALTER TABLE audit."accounts_aud" ADD CONSTRAINT "fkd4olimrdvgjr27d6gcjbqljic" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-88
ALTER TABLE audit."courses_aud" ADD CONSTRAINT "fkdn24rhy01k8t0rf868n9xuhos" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-89
ALTER TABLE audit."topic_subject_link_templates_aud" ADD CONSTRAINT "fke53nuvsp9v6q1fhw37c7ydyy6" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-90
ALTER TABLE audit."lesson_quizzes_aud" ADD CONSTRAINT "fke8mxmlcose8orf3mkxxwrffy4" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-91
ALTER TABLE audit."tests_aud" ADD CONSTRAINT "fkesp7m8l2ni8555a98uc531qqo" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-92
ALTER TABLE audit."outlet_rooms_lesson_outlet_room_bookings_aud" ADD CONSTRAINT "fkg14c5a47f6cbacp4g8hvjs1dl" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-93
ALTER TABLE audit."educators_aud" ADD CONSTRAINT "fkgv3tydorrunmafg8p5ytpctys" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-94
ALTER TABLE audit."test_groups_aud" ADD CONSTRAINT "fkhk1vr2idqpiftqm0psjmy7x5f" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-95
ALTER TABLE audit."subject_student_links_aud" ADD CONSTRAINT "fkhs5ntkrjjvq73oi7a8tbu5wnk" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-96
ALTER TABLE audit."students_aud" ADD CONSTRAINT "fkjdrcy8gujy8jxcrsmqrrg8ew2" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-97
ALTER TABLE audit."educator_lesson_links_aud" ADD CONSTRAINT "fkji2i6pipjj0bpwmi4p4yjsuo4" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-98
ALTER TABLE audit."school_templates_aud" ADD CONSTRAINT "fkkt9nov5vp2rw3msbwlirj4793" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-99
ALTER TABLE audit."topic_subject_links_aud" ADD CONSTRAINT "fkm21wtvbthhcd8wicjgu3mkr0v" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-100
ALTER TABLE audit."subject_level_links_aud" ADD CONSTRAINT "fkm4bau36etc32qlbcw6hqxs0ei" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-101
ALTER TABLE audit."lesson_plan_topic_links_aud" ADD CONSTRAINT "fkmrj1aftp0dnirco3knxrxo9jn" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-102
ALTER TABLE audit."topics_aud" ADD CONSTRAINT "fkmsop6s61s75dytn3e3nb1kp9x" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-103
ALTER TABLE audit."lesson_objective_topic_links_aud" ADD CONSTRAINT "fko1xl20ojfsmb6hnovoq0ufool" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-104
ALTER TABLE audit."educator_course_links_aud" ADD CONSTRAINT "fkou95uy3357xtwurrtsp62rr6f" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-105
ALTER TABLE audit."outlet_educator_links_aud" ADD CONSTRAINT "fkp9qsqsafwmawadv3tj9119gsh" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-106
ALTER TABLE audit."lesson_outlet_room_bookings_aud" ADD CONSTRAINT "fkqa47s3t3klbw9ol3ct4fekus3" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-107
ALTER TABLE audit."lesson_participation_reviews_aud" ADD CONSTRAINT "fkqfpe74eumpnhk6l2r7r8ontp" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-108
ALTER TABLE audit."lessons_aud" ADD CONSTRAINT "fkqpldir3ptpq5nchxf5tytlj2h" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-109
ALTER TABLE audit."subject_educator_links_aud" ADD CONSTRAINT "fkrpmuoh44h9ok95vo51ey5rej3" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-110
ALTER TABLE audit."invoices_aud" ADD CONSTRAINT "fks9k8kou8kq40xqm3h98t6n9s1" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-111
ALTER TABLE audit."student_outlet_links_aud" ADD CONSTRAINT "fkssx5hibt59jc9tujqb06sbeha" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-112
ALTER TABLE audit."material_lesson_links_aud" ADD CONSTRAINT "fkswnsj2eypt6lxqtuwbfyxb0yg" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1766810291486-113
ALTER TABLE audit."subject_level_link_templates_aud" ADD CONSTRAINT "fkthlwt1blwg26rj3j2ovka0gc8" FOREIGN KEY ("rev") REFERENCES "audit"."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

