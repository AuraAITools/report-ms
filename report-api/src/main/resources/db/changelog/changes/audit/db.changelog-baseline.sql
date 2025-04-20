-- changeset kevin:create-audit-schema
CREATE SCHEMA IF NOT EXISTS audit;

-- changeset kevin:1745159867096-1
CREATE TABLE audit."lesson_plans_aud" ("is_planned" BOOLEAN DEFAULT FALSE, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_id" UUID, CONSTRAINT "lesson_plans_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-2
CREATE TABLE audit."courses_aud" ("max_size" INTEGER, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "course_end_timestamptz" TIMESTAMP WITH TIME ZONE, "course_start_timestamptz" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "level_id" UUID, "outlet_id" UUID, "price_record_id" UUID, "lesson_frequency" VARCHAR(255), "name" VARCHAR(255), CONSTRAINT "courses_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-3
CREATE TABLE audit."educators_aud" ("date_of_birth" date, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "start_date" date, "educator_client_persona_id" UUID, "id" UUID NOT NULL, "email" VARCHAR(255), "employment_type" VARCHAR(255), "name" VARCHAR(255), CONSTRAINT "educators_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-4
CREATE TABLE audit."lesson_cancellation_requests_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_id" UUID, "approved_by" VARCHAR(255), "reasoning" VARCHAR(255), "status" VARCHAR(255), CONSTRAINT "lesson_cancellation_requests_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-5
CREATE TABLE audit."lesson_participation_reviews_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "student_lesson_registration_id" UUID, "participation_type" VARCHAR(255), CONSTRAINT "lesson_participation_reviews_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-6
CREATE TABLE audit."lesson_postponement_requests_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "postponed_lesson_end_timestamptz" TIMESTAMP WITH TIME ZONE, "postponed_lesson_start_timestamptz" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "approved_by" VARCHAR(255), "reasoning" VARCHAR(255), "status" VARCHAR(255), CONSTRAINT "lesson_postponement_requests_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-7
CREATE TABLE audit."lesson_view_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "lesson_end_timestamptz" TIMESTAMP WITH TIME ZONE, "lesson_start_timestamptz" TIMESTAMP WITH TIME ZONE, "course_id" UUID, "id" UUID NOT NULL, "outlet_id" UUID, "subject_id" UUID, "description" VARCHAR(255), "lesson_plan_status" VARCHAR(255), "lesson_review_status" VARCHAR(255), "lesson_status" VARCHAR(255), "name" VARCHAR(255), "recap" VARCHAR(255), "tenant_id" VARCHAR(255), CONSTRAINT "lesson_view_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-8
CREATE TABLE audit."price_records_aud" ("price" FLOAT8, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "frequency" VARCHAR(255), CONSTRAINT "price_records_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-9
CREATE TABLE audit."student_client_personas_aud" ("rev" INTEGER NOT NULL, "id" UUID NOT NULL, "relationship" VARCHAR(255), CONSTRAINT "student_client_personas_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-10
CREATE TABLE audit."accounts_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "contact" VARCHAR(255), "email" VARCHAR(255), "first_name" VARCHAR(255), "last_name" VARCHAR(255), "user_id" VARCHAR(255), CONSTRAINT "accounts_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-11
CREATE TABLE audit."educator_client_personas_aud" ("rev" INTEGER NOT NULL, "id" UUID NOT NULL, CONSTRAINT "educator_client_personas_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-12
CREATE TABLE audit."educator_course_attachments_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "course_id" UUID, "educator_id" UUID, "id" UUID NOT NULL, CONSTRAINT "educator_course_attachments_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-13
CREATE TABLE audit."educator_lesson_attachments_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "educator_id" UUID, "id" UUID NOT NULL, "lesson_id" UUID, CONSTRAINT "educator_lesson_attachments_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-14
CREATE TABLE audit."institution_admin_personas_aud" ("rev" INTEGER NOT NULL, "id" UUID NOT NULL, "institution_id" UUID, CONSTRAINT "institution_admin_personas_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-15
CREATE TABLE audit."institutions_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "address" VARCHAR(255), "contact_number" VARCHAR(255), "email" VARCHAR(255), "name" VARCHAR(255), "uen" VARCHAR(255), CONSTRAINT "institutions_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-16
CREATE TABLE audit."invoices_aud" ("cost" FLOAT8, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, CONSTRAINT "invoices_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-17
CREATE TABLE audit."lesson_homework_completions_aud" ("completion" INTEGER, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "student_lesson_registration_id" UUID, CONSTRAINT "lesson_homework_completions_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-18
CREATE TABLE audit."lesson_objective_topic_attachments_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_objective_id" UUID, "topic_id" UUID, CONSTRAINT "lesson_objective_topic_attachments_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-19
CREATE TABLE audit."lesson_objectives_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_id" UUID, CONSTRAINT "lesson_objectives_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-20
CREATE TABLE audit."lesson_outlet_room_bookings_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "end_timestampz" TIMESTAMP WITH TIME ZONE, "start_timestampz" TIMESTAMP WITH TIME ZONE, "id" UUID NOT NULL, "lesson_id" UUID, "outlet_room_id" UUID, CONSTRAINT "lesson_outlet_room_bookings_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-21
CREATE TABLE audit."lesson_plan_materials_attachments_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_plan_id" UUID, "material_id" UUID, CONSTRAINT "lesson_plan_materials_attachments_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-22
CREATE TABLE audit."lesson_plan_topic_attachments_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_plan_id" UUID, "topic_id" UUID, CONSTRAINT "lesson_plan_topic_attachments_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-23
CREATE TABLE audit."lesson_quiz_student_lesson_registration_attachments_aud" ("max_score" INTEGER, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "score" INTEGER, "id" UUID NOT NULL, "lesson_quiz_id" UUID, "student_lesson_registration_id" UUID, CONSTRAINT "lesson_quiz_student_lesson_registration_attachments_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-24
CREATE TABLE audit."lesson_quizzes_aud" ("max_score" INTEGER, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "score" INTEGER, "id" UUID NOT NULL, "lesson_id" UUID, CONSTRAINT "lesson_quizzes_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-25
CREATE TABLE audit."lesson_topic_proficiencies_aud" ("rating" INTEGER, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "student_lesson_registration_id" UUID, "topic_id" UUID, CONSTRAINT "lesson_topic_proficiencies_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-26
CREATE TABLE audit."lesson_view_educator_lesson_attachment_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_id" UUID NOT NULL, CONSTRAINT "lesson_view_educator_lesson_attachment_aud_pkey" PRIMARY KEY ("rev", "id", "lesson_id"));

-- changeset kevin:1745159867096-27
CREATE TABLE audit."lesson_view_lesson_objective_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_id" UUID NOT NULL, CONSTRAINT "lesson_view_lesson_objective_aud_pkey" PRIMARY KEY ("rev", "id", "lesson_id"));

-- changeset kevin:1745159867096-28
CREATE TABLE audit."lesson_view_lesson_plan_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_id" UUID NOT NULL, CONSTRAINT "lesson_view_lesson_plan_aud_pkey" PRIMARY KEY ("rev", "id", "lesson_id"));

-- changeset kevin:1745159867096-29
CREATE TABLE audit."lesson_view_lesson_postponement_request_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_id" UUID NOT NULL, CONSTRAINT "lesson_view_lesson_postponement_request_aud_pkey" PRIMARY KEY ("rev", "id", "lesson_id"));

-- changeset kevin:1745159867096-30
CREATE TABLE audit."lesson_view_material_lesson_attachment_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_id" UUID NOT NULL, CONSTRAINT "lesson_view_material_lesson_attachment_aud_pkey" PRIMARY KEY ("rev", "id", "lesson_id"));

-- changeset kevin:1745159867096-31
CREATE TABLE audit."lesson_view_student_lesson_registration_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_id" UUID NOT NULL, CONSTRAINT "lesson_view_student_lesson_registration_aud_pkey" PRIMARY KEY ("rev", "id", "lesson_id"));

-- changeset kevin:1745159867096-32
CREATE TABLE audit."lessons_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "lesson_end_timestamptz" TIMESTAMP WITH TIME ZONE, "lesson_start_timestamptz" TIMESTAMP WITH TIME ZONE, "course_id" UUID, "id" UUID NOT NULL, "outlet_id" UUID, "subject_id" UUID, "description" VARCHAR(255), "name" VARCHAR(255), "recap" VARCHAR(255), CONSTRAINT "lessons_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-33
CREATE TABLE audit."level_educator_attachments_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "educator_id" UUID, "id" UUID NOT NULL, "level_id" UUID, CONSTRAINT "level_educator_attachments_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-34
CREATE TABLE audit."levels_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "name" VARCHAR(255), CONSTRAINT "levels_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-35
CREATE TABLE audit."material_lesson_attachments_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_id" UUID, "lesson_plan_id" UUID, "material_id" UUID, CONSTRAINT "material_lesson_attachments_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-36
CREATE TABLE audit."material_topic_attachments_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "material_id" UUID, "topic_id" UUID, CONSTRAINT "material_topic_attachments_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-37
CREATE TABLE audit."materials_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "file_url" VARCHAR(255), "name" VARCHAR(255), CONSTRAINT "materials_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-38
CREATE TABLE audit."outlet_admin_personas_aud" ("rev" INTEGER NOT NULL, "id" UUID NOT NULL, CONSTRAINT "outlet_admin_personas_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-39
CREATE TABLE audit."outlet_admin_personas_outlets_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "outlet_admin_id" UUID NOT NULL, "outlet_id" UUID NOT NULL, CONSTRAINT "outlet_admin_personas_outlets_aud_pkey" PRIMARY KEY ("rev", "outlet_admin_id", "outlet_id"));

-- changeset kevin:1745159867096-40
CREATE TABLE audit."outlet_educator_attachments_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "educator_id" UUID, "id" UUID NOT NULL, "outlet_id" UUID, CONSTRAINT "outlet_educator_attachments_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-41
CREATE TABLE audit."outlet_rooms_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "size" INTEGER, "id" UUID NOT NULL, "outlet_id" UUID, "details" VARCHAR(255), "file_url" VARCHAR(255), "name" VARCHAR(255), CONSTRAINT "outlet_rooms_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-42
CREATE TABLE audit."outlet_rooms_lesson_outlet_room_bookings_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "lesson_outlet_room_bookings_id" UUID NOT NULL, "outlet_room_id" UUID NOT NULL, CONSTRAINT "outlet_rooms_lesson_outlet_room_bookings_aud_pkey" PRIMARY KEY ("rev", "lesson_outlet_room_bookings_id", "outlet_room_id"));

-- changeset kevin:1745159867096-43
CREATE TABLE audit."outlets_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "address" VARCHAR(255), "contact_number" VARCHAR(255), "description" VARCHAR(255), "email" VARCHAR(255), "name" VARCHAR(255), "postal_code" VARCHAR(255), CONSTRAINT "outlets_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-44
CREATE TABLE audit."personas_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "account_id" UUID, "id" UUID NOT NULL, CONSTRAINT "personas_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-45
CREATE TABLE audit."revinfo" ("rev" INTEGER NOT NULL, "revtstmp" BIGINT, CONSTRAINT "revinfo_pkey" PRIMARY KEY ("rev"));

-- changeset kevin:1745159867096-46
CREATE TABLE audit."student_course_registrations_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "course_id" UUID, "id" UUID NOT NULL, "student_id" UUID, CONSTRAINT "student_course_registrations_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-47
CREATE TABLE audit."student_lesson_attendances_aud" ("attended" BOOLEAN, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "student_lesson_registration_id" UUID, CONSTRAINT "student_lesson_attendances_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-48
CREATE TABLE audit."student_lesson_registrations_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "lesson_id" UUID, "lesson_plan_id" UUID, "student_id" UUID, CONSTRAINT "student_lesson_registrations_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-49
CREATE TABLE audit."student_outlet_registrations_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "outlet_id" UUID, "student_id" UUID, CONSTRAINT "student_outlet_registrations_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-50
CREATE TABLE audit."students_aud" ("date_of_birth" date, "rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "level_id" UUID, "student_client_persona_id" UUID, "current_school" VARCHAR(255), "email" VARCHAR(255), "name" VARCHAR(255), CONSTRAINT "students_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-51
CREATE TABLE audit."subject_course_attachments_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "course_id" UUID, "id" UUID NOT NULL, "subject_id" UUID, CONSTRAINT "subject_course_attachments_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-52
CREATE TABLE audit."subject_educator_attachments_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "educator_id" UUID, "id" UUID NOT NULL, "subject_id" UUID, CONSTRAINT "subject_educator_attachments_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-53
CREATE TABLE audit."subject_level_attachments_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "level_id" UUID, "subject_id" UUID, CONSTRAINT "subject_level_attachments_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-54
CREATE TABLE audit."subject_student_attachments_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "student_id" UUID, "subject_id" UUID, CONSTRAINT "subject_student_attachments_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-55
CREATE TABLE audit."subject_test_group_attachments_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "subject_id" UUID, "test_group_id" UUID, CONSTRAINT "subject_test_group_attachments_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-56
CREATE TABLE audit."subjects_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "name" VARCHAR(255), CONSTRAINT "subjects_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-57
CREATE TABLE audit."test_groups_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, CONSTRAINT "test_groups_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-58
CREATE TABLE audit."test_results_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "student_id" UUID, "subject_id" UUID, "test_id" UUID, CONSTRAINT "test_results_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-59
CREATE TABLE audit."tests_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "test_group_id" UUID, "name" VARCHAR(255), CONSTRAINT "tests_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-60
CREATE TABLE audit."topic_subject_attachments_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "subject_id" UUID, "topic_id" UUID, CONSTRAINT "topic_subject_attachments_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-61
CREATE TABLE audit."topics_aud" ("rev" INTEGER NOT NULL, "revtype" SMALLINT, "id" UUID NOT NULL, "name" VARCHAR(255), CONSTRAINT "topics_aud_pkey" PRIMARY KEY ("rev", "id"));

-- changeset kevin:1745159867096-62
ALTER TABLE audit."lesson_postponement_requests_aud" ADD CONSTRAINT "fk1tr6vg5jl5ve6hcd6ejxbo5bn" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-63
ALTER TABLE audit."student_course_registrations_aud" ADD CONSTRAINT "fk267pau4s1of7hf7fucvfwam4o" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-64
ALTER TABLE audit."outlets_aud" ADD CONSTRAINT "fk36v22eesrvh4vx0usq6mib9mt" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-65
ALTER TABLE audit."outlet_rooms_aud" ADD CONSTRAINT "fk3d5jo2rysuem2ma0hq263ovgt" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-66
ALTER TABLE audit."lesson_topic_proficiencies_aud" ADD CONSTRAINT "fk486q29emwthms9k7fwokgqr8m" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-67
ALTER TABLE audit."institutions_aud" ADD CONSTRAINT "fk4oir4hsqtgpk8d805mpj8i9ti" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-68
ALTER TABLE audit."educator_lesson_attachments_aud" ADD CONSTRAINT "fk4rw62gbvtg90w5nwb0p4vnjkp" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-69
ALTER TABLE audit."levels_aud" ADD CONSTRAINT "fk4ssb09udr8304fkrm2359521s" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-70
ALTER TABLE audit."lesson_plans_aud" ADD CONSTRAINT "fk51opmofy0qw594di86ohjy7hd" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-71
ALTER TABLE audit."outlet_admin_personas_outlets_aud" ADD CONSTRAINT "fk57snw5q0qa367140isbt3xf78" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-72
ALTER TABLE audit."student_lesson_attendances_aud" ADD CONSTRAINT "fk5yvo7e6mwu4rqq9ud8su0d6xk" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-73
ALTER TABLE audit."level_educator_attachments_aud" ADD CONSTRAINT "fk6b8phitbbi52xydsr11adpjgk" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-74
ALTER TABLE audit."lesson_objectives_aud" ADD CONSTRAINT "fk6ocm6gm4yp0yxjgba7igkduwr" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-75
ALTER TABLE audit."lesson_cancellation_requests_aud" ADD CONSTRAINT "fk84fw9993gykabg0pequulrp4r" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-76
ALTER TABLE audit."material_lesson_attachments_aud" ADD CONSTRAINT "fk8dpyu3ucrhk91yfjv29n2xpgc" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-77
ALTER TABLE audit."lesson_view_aud" ADD CONSTRAINT "fk8iupmo67o1cf0fjxx2euifqtx" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-78
ALTER TABLE audit."lesson_view_lesson_objective_aud" ADD CONSTRAINT "fk8vt43he8uxomm7pbmcpdfkax3" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-79
ALTER TABLE audit."lesson_homework_completions_aud" ADD CONSTRAINT "fk91ubqvu47dbq1i960d6aloxc5" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-80
ALTER TABLE audit."student_client_personas_aud" ADD CONSTRAINT "fk93vh9ujm7y3eixii0e3hbpj5o" FOREIGN KEY ("rev", "id") REFERENCES audit."personas_aud" ("rev", "id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-81
ALTER TABLE audit."student_lesson_registrations_aud" ADD CONSTRAINT "fk99hwp9vq90x8bm8twixuhlgu3" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-82
ALTER TABLE audit."institution_admin_personas_aud" ADD CONSTRAINT "fka75rr9ornaen5fuwf51dmkoc0" FOREIGN KEY ("rev", "id") REFERENCES audit."personas_aud" ("rev", "id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-83
ALTER TABLE audit."subjects_aud" ADD CONSTRAINT "fkal3wfn2pmr284otulkg6v7k0t" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-84
ALTER TABLE audit."lesson_quiz_student_lesson_registration_attachments_aud" ADD CONSTRAINT "fkbrx1tf9s64w93mg0fancdgpxe" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-85
ALTER TABLE audit."materials_aud" ADD CONSTRAINT "fkbxe3tk9a4903jh6yd3xfftuif" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-86
ALTER TABLE audit."educator_client_personas_aud" ADD CONSTRAINT "fkca6x66x54p6p480xg6b0m352g" FOREIGN KEY ("rev", "id") REFERENCES audit."personas_aud" ("rev", "id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-87
ALTER TABLE audit."educator_course_attachments_aud" ADD CONSTRAINT "fkchricqp1opvu0xj2138xh68q7" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-88
ALTER TABLE audit."price_records_aud" ADD CONSTRAINT "fkckx4bc9h1v2yl5qpwrxfkabgc" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-89
ALTER TABLE audit."test_results_aud" ADD CONSTRAINT "fkd4ktcnkekxl7eij86nrhhq4w" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-90
ALTER TABLE audit."accounts_aud" ADD CONSTRAINT "fkd4olimrdvgjr27d6gcjbqljic" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-91
ALTER TABLE audit."lesson_plan_materials_attachments_aud" ADD CONSTRAINT "fkdi8bi7hnc8u3l0d9wmsebpywi" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-92
ALTER TABLE audit."courses_aud" ADD CONSTRAINT "fkdn24rhy01k8t0rf868n9xuhos" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-93
ALTER TABLE audit."subject_course_attachments_aud" ADD CONSTRAINT "fkdr2v2arj7xvhxvc07gm6fcbb2" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-94
ALTER TABLE audit."lesson_objective_topic_attachments_aud" ADD CONSTRAINT "fke1pvndmara6yqtnuvna5ndklr" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-95
ALTER TABLE audit."lesson_quizzes_aud" ADD CONSTRAINT "fke8mxmlcose8orf3mkxxwrffy4" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-96
ALTER TABLE audit."lesson_view_lesson_postponement_request_aud" ADD CONSTRAINT "fkei12y3xqktcgbx21po0g22hh1" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-97
ALTER TABLE audit."tests_aud" ADD CONSTRAINT "fkesp7m8l2ni8555a98uc531qqo" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-98
ALTER TABLE audit."subject_level_attachments_aud" ADD CONSTRAINT "fkfffwojgtqkn99ryt6t8u4rea6" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-99
ALTER TABLE audit."material_topic_attachments_aud" ADD CONSTRAINT "fkfteu2rhypmlydxci87heebwyk" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-100
ALTER TABLE audit."outlet_admin_personas_aud" ADD CONSTRAINT "fkfw21r5rb8vxym4rjoqh5h5qw1" FOREIGN KEY ("rev", "id") REFERENCES audit."personas_aud" ("rev", "id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-101
ALTER TABLE audit."outlet_rooms_lesson_outlet_room_bookings_aud" ADD CONSTRAINT "fkg14c5a47f6cbacp4g8hvjs1dl" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-102
ALTER TABLE audit."educators_aud" ADD CONSTRAINT "fkgv3tydorrunmafg8p5ytpctys" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-103
ALTER TABLE audit."test_groups_aud" ADD CONSTRAINT "fkhk1vr2idqpiftqm0psjmy7x5f" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-104
ALTER TABLE audit."lesson_view_student_lesson_registration_aud" ADD CONSTRAINT "fki43rvvwxibp0yk72xafdh5g5h" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-105
ALTER TABLE audit."lesson_view_material_lesson_attachment_aud" ADD CONSTRAINT "fki5mxce97nswflh0xotkvdoktc" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-106
ALTER TABLE audit."lesson_view_educator_lesson_attachment_aud" ADD CONSTRAINT "fkievrvv75ys3bxdcg59v74e1yk" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-107
ALTER TABLE audit."students_aud" ADD CONSTRAINT "fkjdrcy8gujy8jxcrsmqrrg8ew2" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-108
ALTER TABLE audit."lesson_view_lesson_plan_aud" ADD CONSTRAINT "fkmbjhgpthl7vbx1mlc9b015d2t" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-109
ALTER TABLE audit."topics_aud" ADD CONSTRAINT "fkmsop6s61s75dytn3e3nb1kp9x" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-110
ALTER TABLE audit."student_outlet_registrations_aud" ADD CONSTRAINT "fknxb0infch3q0dhjowhrui9v2j" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-111
ALTER TABLE audit."subject_educator_attachments_aud" ADD CONSTRAINT "fkog7ucppetr7bvkup4a19i84ym" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-112
ALTER TABLE audit."lesson_outlet_room_bookings_aud" ADD CONSTRAINT "fkqa47s3t3klbw9ol3ct4fekus3" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-113
ALTER TABLE audit."lesson_participation_reviews_aud" ADD CONSTRAINT "fkqfpe74eumpnhk6l2r7r8ontp" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-114
ALTER TABLE audit."lessons_aud" ADD CONSTRAINT "fkqpldir3ptpq5nchxf5tytlj2h" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-115
ALTER TABLE audit."outlet_educator_attachments_aud" ADD CONSTRAINT "fkrcghv3p4d1ovev6y2hdbat23h" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-116
ALTER TABLE audit."subject_student_attachments_aud" ADD CONSTRAINT "fks3d8u8p500olqnkvofuo2agcl" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-117
ALTER TABLE audit."invoices_aud" ADD CONSTRAINT "fks9k8kou8kq40xqm3h98t6n9s1" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-118
ALTER TABLE audit."personas_aud" ADD CONSTRAINT "fksb8k9gr5wb2dv5uma6wemrxfb" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-119
ALTER TABLE audit."lesson_plan_topic_attachments_aud" ADD CONSTRAINT "fkso0utvkfe0o75q50rjuf1jain" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-120
ALTER TABLE audit."subject_test_group_attachments_aud" ADD CONSTRAINT "fksrcu1kqkjj6qun79nm08312wm" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset kevin:1745159867096-121
ALTER TABLE audit."topic_subject_attachments_aud" ADD CONSTRAINT "fksx8aok1e1ddp438yvveb6bnie" FOREIGN KEY ("rev") REFERENCES audit."revinfo" ("rev") ON UPDATE NO ACTION ON DELETE NO ACTION;