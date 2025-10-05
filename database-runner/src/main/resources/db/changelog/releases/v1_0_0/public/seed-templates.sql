-- liquibase formatted sql


-- changeset kevin:insert-school-templates
-- Primary Schools - Government
INSERT INTO public.school_templates (id, name, school_category, created_at, updated_at, version, created_by, updated_by)
VALUES
(gen_random_uuid(), 'Admiralty Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Ahmad Ibrahim Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Anchor Green Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Anderson Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Ang Mo Kio Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Angsana Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Balestier Hill Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Beacon Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Bedok Green Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Bendemeer Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Blangah Rise Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Boon Lay Garden Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Bukit Panjang Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Bukit Timah Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Bukit View Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Canberra Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Cantonment Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Casuarina Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Cedar Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Chongfu Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),

-- Primary Schools - Government-Aided
(gen_random_uuid(), 'Al-Irsyad Islamic School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Anglo-Chinese School (Junior)', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Anglo-Chinese School (Primary)', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Canossa Catholic Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'CHIJ Our Lady of Good Counsel', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'CHIJ Our Lady of the Nativity', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'CHIJ Primary (Toa Payoh)', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'CHIJ St. Nicholas Girls'' Primary', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Holy Innocents'' Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Kong Hwa School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Maha Bodhi School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Methodist Girls'' Primary', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Nanyang Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Paya Lebar Methodist Girls'' Primary', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Raffles Girls'' Primary School', 'PRIMARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),

-- Secondary Schools - Government
(gen_random_uuid(), 'Admiralty Secondary School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Ahmad Ibrahim Secondary School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Anderson Secondary School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Ang Mo Kio Secondary School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Bartley Secondary School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Beatty Secondary School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Bedok Green Secondary School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Bedok South Secondary School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Bedok View Secondary School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Bendemeer Secondary School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Bishan Park Secondary School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Bowen Secondary School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Bukit Batok Secondary School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Bukit Merah Secondary School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Bukit Panjang Government High School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),

-- Secondary Schools - Government-Aided
(gen_random_uuid(), 'Anglican High School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Anglo-Chinese School (Barker Road)', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Catholic High School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'CHIJ Katong Convent', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'CHIJ Secondary (Toa Payoh)', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'CHIJ St. Joseph''s Convent', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'CHIJ St. Nicholas Girls'' Secondary', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'CHIJ St. Theresa''s Convent', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Christ Church Secondary School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Holy Innocents'' High School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Kuo Chuan Presbyterian Secondary', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Maris Stella High School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Methodist Girls'' Secondary', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Nan Chiau High School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Nan Hua High School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),

-- Secondary Schools - Independent
(gen_random_uuid(), 'Anglo-Chinese School (Independent)', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Hwa Chong Institution (Secondary)', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Raffles Girls'' School (Secondary)', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Raffles Institution (Secondary)', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'School of Science and Technology', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Singapore Chinese Girls'' School (Secondary)', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Singapore Sports School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'NUS High School', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'School of the Arts', 'SECONDARY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),

-- Junior Colleges - Government
(gen_random_uuid(), 'Anderson Serangoon Junior College', 'JUNIOR_COLLEGE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Anglo-Chinese Junior College', 'JUNIOR_COLLEGE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Catholic Junior College', 'JUNIOR_COLLEGE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Eunoia Junior College', 'JUNIOR_COLLEGE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Jurong Pioneer Junior College', 'JUNIOR_COLLEGE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'National Junior College', 'JUNIOR_COLLEGE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Tampines Meridian Junior College', 'JUNIOR_COLLEGE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Temasek Junior College', 'JUNIOR_COLLEGE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Victoria Junior College', 'JUNIOR_COLLEGE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Yishun Innova Junior College', 'JUNIOR_COLLEGE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),

-- Junior Colleges - Independent
(gen_random_uuid(), 'Hwa Chong Institution (JC)', 'JUNIOR_COLLEGE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Raffles Institution (JC)', 'JUNIOR_COLLEGE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Anglo-Chinese School (Independent) (JC)', 'JUNIOR_COLLEGE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'St. Joseph''s Institution (JC)', 'JUNIOR_COLLEGE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),

-- Polytechnics
(gen_random_uuid(), 'Singapore Polytechnic', 'POLYTECHNIC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Ngee Ann Polytechnic', 'POLYTECHNIC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Temasek Polytechnic', 'POLYTECHNIC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Nanyang Polytechnic', 'POLYTECHNIC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Republic Polytechnic', 'POLYTECHNIC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),

-- International Schools
(gen_random_uuid(), 'Anglo-Chinese School (International)', 'INTERNATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Australian International School', 'INTERNATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Brighton College Singapore', 'INTERNATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Canadian International School', 'INTERNATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Chatsworth International School', 'INTERNATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Dulwich College Singapore', 'INTERNATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Dover Court International School', 'INTERNATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'EtonHouse International School', 'INTERNATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'German European School Singapore', 'INTERNATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'GESS International School', 'INTERNATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Global Indian International School', 'INTERNATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Holland International School', 'INTERNATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'INSWORLD Institute', 'INTERNATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'International Community School', 'INTERNATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'International French School Singapore', 'INTERNATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),

-- Technical Education
(gen_random_uuid(), 'ITE College Central', 'TECHNICAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'ITE College East', 'TECHNICAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'ITE College West', 'TECHNICAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),

-- Arts Institutions
(gen_random_uuid(), 'LASALLE College of the Arts', 'ARTS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Nanyang Academy of Fine Arts', 'ARTS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'University of the Arts Singapore', 'ARTS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),

-- Universities
(gen_random_uuid(), 'National University of Singapore', 'UNIVERSITY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Nanyang Technological University', 'UNIVERSITY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Singapore Management University', 'UNIVERSITY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Singapore University of Technology and Design', 'UNIVERSITY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Singapore Institute of Technology', 'UNIVERSITY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM'),
(gen_random_uuid(), 'Singapore University of Social Sciences', 'UNIVERSITY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SYSTEM', 'SYSTEM');



--changeset system:insert-level-templates
INSERT INTO public.level_templates (id, created_at, updated_at, version, created_by, updated_by, name, school_category)
VALUES
-- Primary School
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Primary 1', 'PRIMARY'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Primary 2', 'PRIMARY'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Primary 3', 'PRIMARY'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Primary 4', 'PRIMARY'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Primary 5', 'PRIMARY'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Primary 6', 'PRIMARY'),

-- Secondary School
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Secondary 1', 'SECONDARY'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Secondary 2', 'SECONDARY'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Secondary 3', 'SECONDARY'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Secondary 4', 'SECONDARY'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Secondary 5', 'SECONDARY'),

-- Junior College
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Junior College 1', 'JUNIOR_COLLEGE'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Junior College 2', 'JUNIOR_COLLEGE'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Junior College 3', 'JUNIOR_COLLEGE'),

-- Polytechnic
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Polytechnic Year 1', 'POLYTECHNIC'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Polytechnic Year 2', 'POLYTECHNIC'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Polytechnic Year 3', 'POLYTECHNIC'),

-- ITE/Technical
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Nitec Year 1', 'TECHNICAL'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Nitec Year 2', 'TECHNICAL'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Higher Nitec Year 1', 'TECHNICAL'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Higher Nitec Year 2', 'TECHNICAL'),

-- University
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'University Year 1', 'UNIVERSITY'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'University Year 2', 'UNIVERSITY'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'University Year 3', 'UNIVERSITY'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'University Year 4', 'UNIVERSITY'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Honours Year', 'UNIVERSITY'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Masters', 'UNIVERSITY'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Doctor of Philosophy', 'UNIVERSITY'),

-- International School
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Kindergarten 1', 'INTERNATIONAL'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Kindergarten 2', 'INTERNATIONAL'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Grade 1', 'INTERNATIONAL'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Grade 2', 'INTERNATIONAL'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Grade 3', 'INTERNATIONAL'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Grade 4', 'INTERNATIONAL'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Grade 5', 'INTERNATIONAL'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Grade 6', 'INTERNATIONAL'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Grade 7', 'INTERNATIONAL'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Grade 8', 'INTERNATIONAL'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Grade 9', 'INTERNATIONAL'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Grade 10', 'INTERNATIONAL'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Grade 11', 'INTERNATIONAL'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Grade 12', 'INTERNATIONAL');

--rollback DELETE FROM level_templates WHERE created_by = 'SYSTEM';


--changeset system:insert-subject-templates
INSERT INTO public.subject_templates (id, created_at, updated_at, version, created_by, updated_by, name)
VALUES
-- Primary School Subjects
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'English Language'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Mathematics'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Science'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Mother Tongue'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Chinese'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Malay'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Tamil'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Social Studies'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Art'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Music'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Physical Education'),

-- Secondary School Core Subjects
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Additional Mathematics'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Elementary Mathematics'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Physics'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Chemistry'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Biology'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Combined Science'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Higher Chinese'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Higher Malay'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Higher Tamil'),

-- Secondary School Humanities
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'History'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Geography'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Literature in English'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Literature in Chinese'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Literature in Malay'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Literature in Tamil'),

-- Secondary School Arts and Technology
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Design and Technology'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Food and Nutrition'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Computer Applications'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Computing'),

-- Business and Commerce
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Principles of Accounts'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Business Studies'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Economics'),

-- Junior College H1 Subjects
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'General Paper'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Project Work'),

-- Junior College H2 Subjects
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H2 Mathematics'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H2 Physics'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H2 Chemistry'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H2 Biology'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H2 Economics'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H2 History'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H2 Geography'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H2 Literature in English'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H2 Art'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H2 Music'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H2 Computing'),

-- Junior College H1 Subjects
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H1 Mathematics'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H1 Physics'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H1 Chemistry'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H1 Biology'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H1 Economics'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H1 History'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H1 Geography'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H1 Literature in English'),

-- Junior College H3 Subjects
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H3 Mathematics'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H3 Physics'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H3 Chemistry'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H3 Biology'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'H3 Economics'),

-- International School Subjects
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Mathematics Intl'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Science Intl'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Physics Intl'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Chemistry Intl'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Biology Intl'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'Social Studies Intl'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'World History Intl'),
(gen_random_uuid(), NOW(), NOW(), 0, 'SYSTEM', 'SYSTEM', 'World Geography Intl');

--rollback DELETE FROM public."subject_templates";

-- SubjectLevelLinkTemplates INSERT statements

-- English Language
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'English Language', 'Primary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'English Language', 'Primary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'English Language', 'Primary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'English Language', 'Primary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'English Language', 'Primary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'English Language', 'Primary 6', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'English Language', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'English Language', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'English Language', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'English Language', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'English Language', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Mathematics
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Mathematics', 'Primary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics', 'Primary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics', 'Primary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics', 'Primary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics', 'Primary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics', 'Primary 6', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Science
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Science', 'Primary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Science', 'Primary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Science', 'Primary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Science', 'Primary 6', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Science', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Science', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Mother Tongue
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Mother Tongue', 'Primary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mother Tongue', 'Primary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mother Tongue', 'Primary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mother Tongue', 'Primary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mother Tongue', 'Primary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mother Tongue', 'Primary 6', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mother Tongue', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mother Tongue', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mother Tongue', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mother Tongue', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mother Tongue', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mother Tongue', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mother Tongue', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mother Tongue', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Chinese
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Chinese', 'Primary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese', 'Primary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese', 'Primary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese', 'Primary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese', 'Primary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese', 'Primary 6', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Malay
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Malay', 'Primary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay', 'Primary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay', 'Primary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay', 'Primary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay', 'Primary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay', 'Primary 6', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Tamil
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Tamil', 'Primary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil', 'Primary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil', 'Primary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil', 'Primary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil', 'Primary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil', 'Primary 6', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Social Studies
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Social Studies', 'Primary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies', 'Primary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies', 'Primary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies', 'Primary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies', 'Primary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies', 'Primary 6', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Art
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Art', 'Primary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Art', 'Primary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Art', 'Primary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Art', 'Primary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Art', 'Primary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Art', 'Primary 6', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Art', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Art', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Art', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Art', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Art', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Music
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Music', 'Primary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music', 'Primary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music', 'Primary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music', 'Primary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music', 'Primary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music', 'Primary 6', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Physical Education
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Physical Education', 'Primary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physical Education', 'Primary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physical Education', 'Primary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physical Education', 'Primary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physical Education', 'Primary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physical Education', 'Primary 6', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physical Education', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physical Education', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physical Education', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physical Education', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physical Education', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Additional Mathematics
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Additional Mathematics', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Additional Mathematics', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Additional Mathematics', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Elementary Mathematics
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Elementary Mathematics', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Elementary Mathematics', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Elementary Mathematics', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Physics
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Physics', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physics', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physics', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physics', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physics', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physics', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Chemistry
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Chemistry', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chemistry', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chemistry', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chemistry', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chemistry', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chemistry', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Biology
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Biology', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Biology', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Biology', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Biology', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Biology', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Biology', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Combined Science
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Combined Science', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Combined Science', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Combined Science', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Higher Chinese
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Higher Chinese', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Higher Chinese', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Higher Chinese', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Higher Chinese', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Higher Malay
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Higher Malay', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Higher Malay', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Higher Malay', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Higher Malay', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Higher Tamil
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Higher Tamil', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Higher Tamil', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Higher Tamil', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Higher Tamil', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- History
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'History', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'History', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'History', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'History', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'History', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'History', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'History', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'History', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Geography
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Geography', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Geography', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Geography', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Geography', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Geography', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Geography', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Geography', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Geography', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Literature in English
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Literature in English', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Literature in English', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Literature in English', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Literature in English', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Literature in English', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Literature in English', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Literature in English', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Literature in English', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Literature in Chinese
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Literature in Chinese', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Literature in Chinese', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Literature in Chinese', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Literature in Malay
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Literature in Malay', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Literature in Malay', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Literature in Malay', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Literature in Tamil
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Literature in Tamil', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Literature in Tamil', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Literature in Tamil', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Design and Technology
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Design and Technology', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Design and Technology', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Design and Technology', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Design and Technology', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Design and Technology', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Food and Nutrition
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Food and Nutrition', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Food and Nutrition', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Food and Nutrition', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Food and Nutrition', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Food and Nutrition', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Computer Applications
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Computer Applications', 'Secondary 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Computer Applications', 'Secondary 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Computer Applications', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Computer Applications', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Computer Applications', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Computing
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Computing', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Computing', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Computing', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Principles of Accounts
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Principles of Accounts', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Principles of Accounts', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Principles of Accounts', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Business Studies
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Business Studies', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Business Studies', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Business Studies', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Economics
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Economics', 'Secondary 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Economics', 'Secondary 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Economics', 'Secondary 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Economics', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Economics', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Economics', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- General Paper
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'General Paper', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'General Paper', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'General Paper', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Project Work
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Project Work', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H2 Mathematics
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H2 Mathematics', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Mathematics', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Mathematics', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H2 Physics
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H2 Physics', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Physics', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Physics', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H2 Chemistry
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H2 Chemistry', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Chemistry', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Chemistry', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H2 Biology
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H2 Biology', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Biology', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Biology', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H2 Economics
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H2 Economics', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Economics', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Economics', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H2 History
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H2 History', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 History', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 History', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H2 Geography
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H2 Geography', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Geography', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Geography', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H2 Literature in English
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H2 Literature in English', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Literature in English', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Literature in English', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H2 Art
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H2 Art', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Art', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Art', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H2 Music
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H2 Music', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Music', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Music', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H2 Computing
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H2 Computing', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Computing', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H2 Computing', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H1 Mathematics
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H1 Mathematics', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H1 Mathematics', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H1 Mathematics', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H1 Physics
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H1 Physics', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H1 Physics', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H1 Physics', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H1 Chemistry
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H1 Chemistry', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H1 Chemistry', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H1 Chemistry', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H1 Biology
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H1 Biology', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H1 Biology', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H1 Biology', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H1 Economics
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H1 Economics', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H1 Economics', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H1 Economics', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H1 History
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H1 History', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H1 History', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H1 History', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H1 Geography
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H1 Geography', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H1 Geography', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H1 Geography', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H1 Literature in English
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H1 Literature in English', 'Junior College 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H1 Literature in English', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H1 Literature in English', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H3 Mathematics
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H3 Mathematics', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H3 Mathematics', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H3 Physics
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H3 Physics', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H3 Physics', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H3 Chemistry
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H3 Chemistry', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H3 Chemistry', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H3 Biology
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H3 Biology', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H3 Biology', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- H3 Economics
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'H3 Economics', 'Junior College 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'H3 Economics', 'Junior College 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Mathematics Intl
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Mathematics Intl', 'Kindergarten 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics Intl', 'Kindergarten 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics Intl', 'Grade 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics Intl', 'Grade 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics Intl', 'Grade 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics Intl', 'Grade 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics Intl', 'Grade 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics Intl', 'Grade 6', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics Intl', 'Grade 7', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics Intl', 'Grade 8', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics Intl', 'Grade 9', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics Intl', 'Grade 10', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics Intl', 'Grade 11', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mathematics Intl', 'Grade 12', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Science Intl
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Science Intl', 'Grade 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Science Intl', 'Grade 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Science Intl', 'Grade 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Science Intl', 'Grade 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Science Intl', 'Grade 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Science Intl', 'Grade 6', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Science Intl', 'Grade 7', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Science Intl', 'Grade 8', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Physics Intl
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Physics Intl', 'Grade 9', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physics Intl', 'Grade 10', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physics Intl', 'Grade 11', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physics Intl', 'Grade 12', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Chemistry Intl
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Chemistry Intl', 'Grade 9', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chemistry Intl', 'Grade 10', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chemistry Intl', 'Grade 11', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chemistry Intl', 'Grade 12', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Biology Intl
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Biology Intl', 'Grade 9', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Biology Intl', 'Grade 10', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Biology Intl', 'Grade 11', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Biology Intl', 'Grade 12', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Social Studies Intl
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Social Studies Intl', 'Grade 1', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies Intl', 'Grade 2', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies Intl', 'Grade 3', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies Intl', 'Grade 4', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies Intl', 'Grade 5', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies Intl', 'Grade 6', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies Intl', 'Grade 7', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Social Studies Intl', 'Grade 8', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- World History Intl
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'World History Intl', 'Grade 9', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'World History Intl', 'Grade 10', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'World History Intl', 'Grade 11', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'World History Intl', 'Grade 12', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- World Geography Intl
INSERT INTO public.subject_level_link_templates (id, subject_name, level_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'World Geography Intl', 'Grade 9', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'World Geography Intl', 'Grade 10', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'World Geography Intl', 'Grade 11', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'World Geography Intl', 'Grade 12', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- TopicsTemplates INSERT statements

INSERT INTO public.topics_templates (id, name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Grammar Basics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Comprehension', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Composition Writing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Vocabulary Building', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Oral Communication', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Listening Comprehension', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Poetry Analysis', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Narrative Writing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Argumentative Writing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Numbers and Operations', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Addition and Subtraction', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Multiplication and Division', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Fractions', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Decimals', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Geometry and Shapes', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Measurement', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Data Handling', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Money Concepts', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Time Concepts', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Algebra Basics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Linear Equations', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Simultaneous Equations', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Quadratic Equations', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Indices and Surds', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Polynomials', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Coordinate Geometry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Trigonometry Basics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Geometry of Circles', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Similarity and Congruency', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Statistics and Probability', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mensuration', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Functions', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Differentiation', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Integration', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Exponential and Logarithmic Functions', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Trigonometric Functions', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Trigonometric Identities', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Binomial Theorem', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Partial Fractions', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Sequences and Series', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Advanced Coordinate Geometry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), '2D Vectors', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Measurement in Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mechanics: Kinematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mechanics: Dynamics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Forces and Motion', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Work, Energy and Power', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Momentum', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Circular Motion', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Gravitational Field', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Oscillations', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Waves', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Superposition', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Thermal Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Electricity and Magnetism', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Electromagnetic Induction', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Alternating Current', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Quantum Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Atomic and Nuclear Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Atomic Structure', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chemical Bonding', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Stoichiometry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'States of Matter', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Acids, Bases and Salts', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Redox Reactions', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Organic Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chemical Kinetics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chemical Equilibrium', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Thermochemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Electrochemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Transition Elements', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Isomerism', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Polymers', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Cell Structure and Function', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Biological Molecules', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Enzymes', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Cell Membrane and Transport', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Cellular Respiration', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Photosynthesis', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mitosis and Meiosis', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Genetics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Molecular Genetics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Homeostasis', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Coordination and Response', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Nutrition', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Transport in Organisms', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Reproduction', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Ecology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Evolution', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Applications of Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Living and Non-Living Things', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Plants and Animals', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Human Body Systems', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Materials and Properties', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Magnets', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Light and Shadows', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Heat and Temperature', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Water Cycle', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Ancient Civilizations', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Colonial Singapore', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'World War Two', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Singapore Independence', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Cold War', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Southeast Asian History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Nationalism in Southeast Asia', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physical Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Weather and Climate', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Plate Tectonics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Population Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Settlement Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Economic Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Environmental Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Development Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tourism Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Basic Economic Problem', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Demand and Supply', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Market Structures', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Market Failure', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Macroeconomics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'International Trade', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Economic Development', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Monetary Policy', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Fiscal Policy', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), '3D Vectors', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Complex Numbers', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Calculus: Differentiation', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Calculus: Integration', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Differential Equations', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Maclaurin Series', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Probability and Statistics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Normal Distribution', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Hypothesis Testing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Correlation and Regression', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese Composition', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese Comprehension', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese Oral', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese Listening', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese Grammar', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese Vocabulary', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese Poetry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese Classical Texts', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay Composition', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay Comprehension', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay Oral', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay Listening', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay Grammar', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay Literature', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil Composition', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil Comprehension', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil Oral', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil Listening', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil Grammar', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil Literature', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Prose Analysis', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Drama Analysis', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Literary Devices', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Themes and Motifs', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Character Analysis', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Comparative Literature', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Creative Writing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Singapore Society', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Cultural Diversity', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Governance in Singapore', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Citizenship', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Globalisation', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tourism in Singapore', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Housing in Singapore', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Transport in Singapore', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Design Process', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Materials and Technology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Engineering Drawing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Woodworking', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Metalworking', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Electronics Basics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mechanisms', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Sustainable Design', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Nutritional Science', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Food Safety and Hygiene', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Food Preparation', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Meal Planning', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Dietary Requirements', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Food Preservation', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Culinary Skills', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Computational Thinking', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Programming Fundamentals', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Algorithms and Data Structures', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Software Engineering', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Database Systems', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Computer Networks', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Cybersecurity', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Artificial Intelligence', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Web Development', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Spreadsheet Applications', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Presentation Software', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Drawing Techniques', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Painting Techniques', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Sculpture', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Printmaking', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Digital Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Art History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Art Appreciation', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mixed Media', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music Theory', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music Composition', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music Performance', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music Appreciation', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Instrumental Techniques', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Vocal Techniques', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music Technology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Fundamental Movement', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Team Sports', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Individual Sports', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Fitness and Conditioning', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Outdoor Education', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Sports Science', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Healthy Lifestyle', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Safety and Risk Management', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Business Environment', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Business Planning', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Marketing Fundamentals', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Human Resources', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Operations Management', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Business Finance', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Business Ethics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Entrepreneurship', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Double Entry Bookkeeping', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Financial Statements', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Profit and Loss Account', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Balance Sheet', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Cash Flow Statement', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Ratio Analysis', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Depreciation', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Bad Debts and Provisions', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Global Issues', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Science and Technology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Media and Society', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Education and Youth', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Environment and Sustainability', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Politics and Governance', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Arts and Culture', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Economics and Society', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'World Languages', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'STEM Integration', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Critical Thinking', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Research Skills', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Presentation Skills', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Collaborative Learning', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Environmental Science', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Cultural Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Digital Citizenship', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Grammar Basics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Grammar Basics', 'English Language', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Comprehension
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Comprehension', 'English Language', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Composition Writing
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Composition Writing', 'English Language', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Vocabulary Building
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Vocabulary Building', 'English Language', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Oral Communication
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Oral Communication', 'English Language', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Listening Comprehension
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Listening Comprehension', 'English Language', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Poetry Analysis
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Poetry Analysis', 'English Language', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Poetry Analysis', 'Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Poetry Analysis', 'H1 Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Poetry Analysis', 'H2 Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Narrative Writing
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Narrative Writing', 'English Language', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Argumentative Writing
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Argumentative Writing', 'English Language', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Numbers and Operations
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Numbers and Operations', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Addition and Subtraction
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Addition and Subtraction', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Multiplication and Division
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Multiplication and Division', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Fractions
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Fractions', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Decimals
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Decimals', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Geometry and Shapes
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Geometry and Shapes', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Measurement
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Measurement', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Data Handling
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Data Handling', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Money Concepts
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Money Concepts', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Time Concepts
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Time Concepts', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Algebra Basics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Algebra Basics', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Linear Equations
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Linear Equations', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Simultaneous Equations
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Simultaneous Equations', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Quadratic Equations
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Quadratic Equations', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Indices and Surds
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Indices and Surds', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Polynomials
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Polynomials', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Coordinate Geometry
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Coordinate Geometry', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Trigonometry Basics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Trigonometry Basics', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Geometry of Circles
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Geometry of Circles', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Similarity and Congruency
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Similarity and Congruency', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Statistics and Probability
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Statistics and Probability', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Mensuration
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Mensuration', 'Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Functions
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Functions', 'Additional Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Functions', 'H2 Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Differentiation
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Differentiation', 'Additional Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Integration
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Integration', 'Additional Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Exponential and Logarithmic Functions
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Exponential and Logarithmic Functions', 'Additional Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Trigonometric Functions
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Trigonometric Functions', 'Additional Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Trigonometric Identities
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Trigonometric Identities', 'Additional Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Binomial Theorem
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Binomial Theorem', 'Additional Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Partial Fractions
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Partial Fractions', 'Additional Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Sequences and Series
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Sequences and Series', 'Additional Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Sequences and Series', 'H2 Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Advanced Coordinate Geometry
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Advanced Coordinate Geometry', 'Additional Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- 2D Vectors
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), '2D Vectors', 'Additional Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Measurement in Physics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Measurement in Physics', 'Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Measurement in Physics', 'H1 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Measurement in Physics', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Mechanics: Kinematics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Mechanics: Kinematics', 'Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mechanics: Kinematics', 'H1 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mechanics: Kinematics', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Mechanics: Dynamics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Mechanics: Dynamics', 'Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mechanics: Dynamics', 'H1 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mechanics: Dynamics', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Forces and Motion
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Forces and Motion', 'Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Forces and Motion', 'H1 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Forces and Motion', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Work, Energy and Power
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Work, Energy and Power', 'Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Work, Energy and Power', 'H1 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Work, Energy and Power', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Momentum
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Momentum', 'Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Momentum', 'H1 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Momentum', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Circular Motion
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Circular Motion', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Gravitational Field
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Gravitational Field', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Oscillations
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Oscillations', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Waves
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Waves', 'Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Waves', 'H1 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Waves', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Superposition
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Superposition', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Thermal Physics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Thermal Physics', 'Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Thermal Physics', 'H1 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Thermal Physics', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Electricity and Magnetism
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Electricity and Magnetism', 'Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Electricity and Magnetism', 'H1 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Electricity and Magnetism', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Electromagnetic Induction
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Electromagnetic Induction', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Alternating Current
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Alternating Current', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Quantum Physics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Quantum Physics', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Atomic and Nuclear Physics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Atomic and Nuclear Physics', 'H2 Physics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Atomic Structure
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Atomic Structure', 'Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Atomic Structure', 'H1 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Atomic Structure', 'H2 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Chemical Bonding
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Chemical Bonding', 'Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chemical Bonding', 'H1 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chemical Bonding', 'H2 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Stoichiometry
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Stoichiometry', 'Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Stoichiometry', 'H1 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Stoichiometry', 'H2 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- States of Matter
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'States of Matter', 'Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'States of Matter', 'H1 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'States of Matter', 'H2 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Acids, Bases and Salts
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Acids, Bases and Salts', 'Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Acids, Bases and Salts', 'H1 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Acids, Bases and Salts', 'H2 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Redox Reactions
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Redox Reactions', 'Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Redox Reactions', 'H1 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Redox Reactions', 'H2 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Organic Chemistry
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Organic Chemistry', 'Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Organic Chemistry', 'H1 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Organic Chemistry', 'H2 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Chemical Kinetics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Chemical Kinetics', 'H2 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Chemical Equilibrium
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Chemical Equilibrium', 'H2 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Thermochemistry
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Thermochemistry', 'H2 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Electrochemistry
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Electrochemistry', 'H2 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Transition Elements
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Transition Elements', 'H2 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Isomerism
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Isomerism', 'H2 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Polymers
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Polymers', 'H2 Chemistry', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Cell Structure and Function
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Cell Structure and Function', 'Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Cell Structure and Function', 'H1 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Cell Structure and Function', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Biological Molecules
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Biological Molecules', 'Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Biological Molecules', 'H1 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Biological Molecules', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Enzymes
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Enzymes', 'Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Enzymes', 'H1 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Enzymes', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Cell Membrane and Transport
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Cell Membrane and Transport', 'Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Cell Membrane and Transport', 'H1 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Cell Membrane and Transport', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Cellular Respiration
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Cellular Respiration', 'Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Cellular Respiration', 'H1 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Cellular Respiration', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Photosynthesis
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Photosynthesis', 'Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Photosynthesis', 'H1 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Photosynthesis', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Mitosis and Meiosis
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Mitosis and Meiosis', 'Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mitosis and Meiosis', 'H1 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mitosis and Meiosis', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Genetics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Genetics', 'Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Genetics', 'H1 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Genetics', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Molecular Genetics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Molecular Genetics', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Homeostasis
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Homeostasis', 'Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Homeostasis', 'H1 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Homeostasis', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Coordination and Response
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Coordination and Response', 'Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Coordination and Response', 'H1 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Coordination and Response', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Nutrition
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Nutrition', 'Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Nutrition', 'H1 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Nutrition', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Transport in Organisms
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Transport in Organisms', 'Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Transport in Organisms', 'H1 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Transport in Organisms', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Reproduction
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Reproduction', 'Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Reproduction', 'H1 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Reproduction', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Ecology
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Ecology', 'Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Ecology', 'H1 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Ecology', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Evolution
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Evolution', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Applications of Biology
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Applications of Biology', 'H2 Biology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Living and Non-Living Things
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Living and Non-Living Things', 'Science', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Plants and Animals
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Plants and Animals', 'Science', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Human Body Systems
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Human Body Systems', 'Science', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Materials and Properties
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Materials and Properties', 'Science', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Magnets
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Magnets', 'Science', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Light and Shadows
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Light and Shadows', 'Science', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Heat and Temperature
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Heat and Temperature', 'Science', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Water Cycle
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Water Cycle', 'Science', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Ancient Civilizations
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Ancient Civilizations', 'History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Ancient Civilizations', 'H1 History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Ancient Civilizations', 'H2 History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Colonial Singapore
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Colonial Singapore', 'History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Colonial Singapore', 'H1 History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Colonial Singapore', 'H2 History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- World War Two
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'World War Two', 'History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'World War Two', 'H1 History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'World War Two', 'H2 History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Singapore Independence
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Singapore Independence', 'History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Singapore Independence', 'H1 History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Singapore Independence', 'H2 History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Cold War
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Cold War', 'H2 History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Southeast Asian History
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Southeast Asian History', 'History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Southeast Asian History', 'H1 History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Southeast Asian History', 'H2 History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Nationalism in Southeast Asia
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Nationalism in Southeast Asia', 'H2 History', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Physical Geography
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Physical Geography', 'Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physical Geography', 'H1 Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Physical Geography', 'H2 Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Weather and Climate
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Weather and Climate', 'Science', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Weather and Climate', 'Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Weather and Climate', 'H1 Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Weather and Climate', 'H2 Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Plate Tectonics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Plate Tectonics', 'Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Plate Tectonics', 'H1 Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Plate Tectonics', 'H2 Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Population Studies
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Population Studies', 'Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Population Studies', 'H1 Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Population Studies', 'H2 Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Settlement Geography
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Settlement Geography', 'Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Settlement Geography', 'H1 Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Settlement Geography', 'H2 Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Economic Geography
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Economic Geography', 'Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Economic Geography', 'H1 Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Economic Geography', 'H2 Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Environmental Geography
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Environmental Geography', 'Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Environmental Geography', 'H1 Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Environmental Geography', 'H2 Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Development Geography
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Development Geography', 'H2 Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Tourism Geography
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Tourism Geography', 'H2 Geography', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Basic Economic Problem
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Basic Economic Problem', 'Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Basic Economic Problem', 'H1 Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Basic Economic Problem', 'H2 Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Demand and Supply
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Demand and Supply', 'Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Demand and Supply', 'H1 Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Demand and Supply', 'H2 Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Market Structures
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Market Structures', 'Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Market Structures', 'H1 Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Market Structures', 'H2 Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Market Failure
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Market Failure', 'Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Market Failure', 'H1 Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Market Failure', 'H2 Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Macroeconomics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Macroeconomics', 'Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Macroeconomics', 'H1 Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Macroeconomics', 'H2 Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- International Trade
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'International Trade', 'Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'International Trade', 'H1 Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'International Trade', 'H2 Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Economic Development
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Economic Development', 'H2 Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Monetary Policy
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Monetary Policy', 'H2 Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Fiscal Policy
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Fiscal Policy', 'H2 Economics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- 3D Vectors
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), '3D Vectors', 'H2 Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Complex Numbers
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Complex Numbers', 'H2 Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Calculus: Differentiation
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Calculus: Differentiation', 'H2 Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Calculus: Integration
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Calculus: Integration', 'H2 Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Differential Equations
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Differential Equations', 'H2 Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Maclaurin Series
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Maclaurin Series', 'H2 Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Probability and Statistics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Probability and Statistics', 'H2 Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Normal Distribution
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Normal Distribution', 'H2 Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Hypothesis Testing
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Hypothesis Testing', 'H2 Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Correlation and Regression
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Correlation and Regression', 'H2 Mathematics', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Chinese Composition
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Chinese Composition', 'Chinese', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese Composition', 'Higher Chinese', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Chinese Comprehension
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Chinese Comprehension', 'Chinese', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese Comprehension', 'Higher Chinese', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Chinese Oral
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Chinese Oral', 'Chinese', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese Oral', 'Higher Chinese', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Chinese Listening
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Chinese Listening', 'Chinese', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese Listening', 'Higher Chinese', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Chinese Grammar
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Chinese Grammar', 'Chinese', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese Grammar', 'Higher Chinese', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Chinese Vocabulary
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Chinese Vocabulary', 'Chinese', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese Vocabulary', 'Higher Chinese', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Chinese Poetry
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Chinese Poetry', 'Higher Chinese', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese Poetry', 'Literature in Chinese', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Chinese Classical Texts
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Chinese Classical Texts', 'Higher Chinese', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Chinese Classical Texts', 'Literature in Chinese', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Malay Composition
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Malay Composition', 'Malay', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay Composition', 'Higher Malay', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Malay Comprehension
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Malay Comprehension', 'Malay', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay Comprehension', 'Higher Malay', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Malay Oral
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Malay Oral', 'Malay', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay Oral', 'Higher Malay', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Malay Listening
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Malay Listening', 'Malay', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay Listening', 'Higher Malay', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Malay Grammar
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Malay Grammar', 'Malay', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay Grammar', 'Higher Malay', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Malay Literature
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Malay Literature', 'Higher Malay', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Malay Literature', 'Literature in Malay', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Tamil Composition
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Tamil Composition', 'Tamil', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil Composition', 'Higher Tamil', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Tamil Comprehension
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Tamil Comprehension', 'Tamil', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil Comprehension', 'Higher Tamil', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Tamil Oral
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Tamil Oral', 'Tamil', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil Oral', 'Higher Tamil', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Tamil Listening
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Tamil Listening', 'Tamil', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil Listening', 'Higher Tamil', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Tamil Grammar
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Tamil Grammar', 'Tamil', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil Grammar', 'Higher Tamil', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Tamil Literature
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Tamil Literature', 'Higher Tamil', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Tamil Literature', 'Literature in Tamil', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Prose Analysis
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Prose Analysis', 'Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Prose Analysis', 'H1 Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Prose Analysis', 'H2 Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Drama Analysis
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Drama Analysis', 'Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Drama Analysis', 'H1 Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Drama Analysis', 'H2 Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Literary Devices
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Literary Devices', 'Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Literary Devices', 'H1 Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Literary Devices', 'H2 Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Themes and Motifs
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Themes and Motifs', 'Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Themes and Motifs', 'H1 Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Themes and Motifs', 'H2 Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Character Analysis
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Character Analysis', 'Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Character Analysis', 'H1 Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Character Analysis', 'H2 Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Comparative Literature
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Comparative Literature', 'H2 Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Creative Writing
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Creative Writing', 'H2 Literature in English', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Singapore Society
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Singapore Society', 'Social Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Cultural Diversity
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Cultural Diversity', 'Social Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Governance in Singapore
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Governance in Singapore', 'Social Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Citizenship
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Citizenship', 'Social Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Globalisation
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Globalisation', 'Social Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Tourism in Singapore
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Tourism in Singapore', 'Social Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Housing in Singapore
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Housing in Singapore', 'Social Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Transport in Singapore
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Transport in Singapore', 'Social Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Design Process
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Design Process', 'Design and Technology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Materials and Technology
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Materials and Technology', 'Design and Technology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Engineering Drawing
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Engineering Drawing', 'Design and Technology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Woodworking
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Woodworking', 'Design and Technology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Metalworking
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Metalworking', 'Design and Technology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Electronics Basics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Electronics Basics', 'Design and Technology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Mechanisms
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Mechanisms', 'Design and Technology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Sustainable Design
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Sustainable Design', 'Design and Technology', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Nutritional Science
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Nutritional Science', 'Food and Nutrition', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Food Safety and Hygiene
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Food Safety and Hygiene', 'Food and Nutrition', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Food Preparation
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Food Preparation', 'Food and Nutrition', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Meal Planning
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Meal Planning', 'Food and Nutrition', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Dietary Requirements
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Dietary Requirements', 'Food and Nutrition', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Food Preservation
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Food Preservation', 'Food and Nutrition', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Culinary Skills
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Culinary Skills', 'Food and Nutrition', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Computational Thinking
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Computational Thinking', 'Computing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Computational Thinking', 'H2 Computing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Computational Thinking', 'Computer Applications', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Programming Fundamentals
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Programming Fundamentals', 'Computing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Programming Fundamentals', 'H2 Computing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Algorithms and Data Structures
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Algorithms and Data Structures', 'Computing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Algorithms and Data Structures', 'H2 Computing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Software Engineering
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Software Engineering', 'H2 Computing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Database Systems
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Database Systems', 'H2 Computing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Computer Networks
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Computer Networks', 'H2 Computing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Cybersecurity
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Cybersecurity', 'H2 Computing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Artificial Intelligence
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Artificial Intelligence', 'H2 Computing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Web Development
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Web Development', 'Computer Applications', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Web Development', 'Computing', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Spreadsheet Applications
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Spreadsheet Applications', 'Computer Applications', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Presentation Software
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Presentation Software', 'Computer Applications', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Drawing Techniques
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Drawing Techniques', 'Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Drawing Techniques', 'H2 Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Painting Techniques
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Painting Techniques', 'Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Painting Techniques', 'H2 Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Sculpture
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Sculpture', 'Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Sculpture', 'H2 Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Printmaking
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Printmaking', 'Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Printmaking', 'H2 Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Digital Art
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Digital Art', 'Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Digital Art', 'H2 Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Art History
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Art History', 'Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Art History', 'H2 Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Art Appreciation
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Art Appreciation', 'Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Art Appreciation', 'H2 Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Mixed Media
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Mixed Media', 'Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Mixed Media', 'H2 Art', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Music Theory
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Music Theory', 'Music', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music Theory', 'H2 Music', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Music Composition
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Music Composition', 'Music', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music Composition', 'H2 Music', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Music Performance
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Music Performance', 'Music', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music Performance', 'H2 Music', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Music History
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Music History', 'Music', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music History', 'H2 Music', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Music Appreciation
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Music Appreciation', 'Music', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Music Appreciation', 'H2 Music', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Instrumental Techniques
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Instrumental Techniques', 'Music', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Instrumental Techniques', 'H2 Music', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Vocal Techniques
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Vocal Techniques', 'Music', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Vocal Techniques', 'H2 Music', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Music Technology
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Music Technology', 'H2 Music', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Fundamental Movement
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Fundamental Movement', 'Physical Education', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Team Sports
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Team Sports', 'Physical Education', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Individual Sports
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Individual Sports', 'Physical Education', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Fitness and Conditioning
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Fitness and Conditioning', 'Physical Education', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Outdoor Education
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Outdoor Education', 'Physical Education', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Sports Science
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Sports Science', 'Physical Education', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Healthy Lifestyle
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Healthy Lifestyle', 'Physical Education', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Safety and Risk Management
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Safety and Risk Management', 'Physical Education', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Business Environment
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Business Environment', 'Business Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Business Planning
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Business Planning', 'Business Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Marketing Fundamentals
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Marketing Fundamentals', 'Business Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Human Resources
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Human Resources', 'Business Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Operations Management
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Operations Management', 'Business Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Business Finance
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Business Finance', 'Business Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Business Ethics
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Business Ethics', 'Business Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Entrepreneurship
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Entrepreneurship', 'Business Studies', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Double Entry Bookkeeping
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Double Entry Bookkeeping', 'Principles of Accounts', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Financial Statements
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Financial Statements', 'Principles of Accounts', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Profit and Loss Account
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Profit and Loss Account', 'Principles of Accounts', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Balance Sheet
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Balance Sheet', 'Principles of Accounts', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Cash Flow Statement
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Cash Flow Statement', 'Principles of Accounts', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Ratio Analysis
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Ratio Analysis', 'Principles of Accounts', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Depreciation
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Depreciation', 'Principles of Accounts', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Bad Debts and Provisions
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Bad Debts and Provisions', 'Principles of Accounts', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Global Issues
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Global Issues', 'General Paper', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Science and Technology
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Science and Technology', 'General Paper', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Media and Society
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Media and Society', 'General Paper', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Education and Youth
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Education and Youth', 'General Paper', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Environment and Sustainability
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Environment and Sustainability', 'General Paper', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Politics and Governance
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Politics and Governance', 'General Paper', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Arts and Culture
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Arts and Culture', 'General Paper', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Economics and Society
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Economics and Society', 'General Paper', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- World Languages
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'World Languages', 'English Language', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- STEM Integration
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'STEM Integration', 'Science Intl', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'STEM Integration', 'Mathematics Intl', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Critical Thinking
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Critical Thinking', 'English Language', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Critical Thinking', 'Social Studies Intl', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Research Skills
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Research Skills', 'English Language', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Research Skills', 'Social Studies Intl', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Presentation Skills
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Presentation Skills', 'English Language', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Collaborative Learning
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES (gen_random_uuid(), 'Collaborative Learning', 'Social Studies Intl', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Environmental Science
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Environmental Science', 'Science Intl', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Environmental Science', 'Biology Intl', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Cultural Studies
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Cultural Studies', 'Social Studies Intl', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Cultural Studies', 'World History Intl', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);

-- Digital Citizenship
INSERT INTO public.topic_subject_link_templates (id, topic_name, subject_name, created_at, created_by, updated_at, updated_by, version)
VALUES
(gen_random_uuid(), 'Digital Citizenship', 'English Language', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0),
(gen_random_uuid(), 'Digital Citizenship', 'Social Studies Intl', NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0);