
CREATE TABLE "usertype" (
  usertype_id BIGSERIAL NOT NULL,
  type VARCHAR NOT NULL,
  PRIMARY KEY (usertype_id)
);

CREATE TABLE "dpuser" (
  dpuser_id BIGSERIAL NOT NULL,
  user_name VARCHAR(200) NOT NULL,
  password VARCHAR(200) NOT NULL,
  given_name VARCHAR(200),
  surname VARCHAR(200),
  email VARCHAR(200),
  roles  VARCHAR,
  usertype_id BIGSERIAL REFERENCES usertype(usertype_id),
  PRIMARY KEY (dpuser_id)
);

CREATE TABLE IF NOT EXISTS "project" (
  project_id BIGSERIAL NOT NULL,
  name VARCHAR(50) NOT NULL,
  type VARCHAR(50) NOT NULL,
  timeframe VARCHAR(50) NOT NULL,
  startdate DATE NOT NULL,
  enddate DATE NOT NULL,
  description VARCHAR(1000) NOT NULL,
  owner BIGSERIAL NOT NULL REFERENCES dpuser(dpuser_id),
  PRIMARY KEY (project_id)
);

CREATE TABLE "language" (
  language_id BIGSERIAL NOT NULL,
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY (language_id)
);

CREATE TABLE "experiment_def" (
  experiment_def_id BIGSERIAL NOT NULL,
  project_id BIGSERIAL NOT NULL REFERENCES project(project_id),
  name VARCHAR(50) NOT NULL,
  description VARCHAR(2000),
  PRIMARY KEY (experiment_def_id)
);

CREATE TABLE "consent_form" (
  consent_form_id BIGSERIAL NOT NULL,
  experiment_def_id BIGSERIAL NOT NULL REFERENCES experiment_def(experiment_def_id),
  name VARCHAR(50) NOT NULL,
  PRIMARY KEY (consent_form_id)
);

CREATE TABLE "consent_form_text" (
  consent_form_text_id BIGSERIAL NOT NULL,
  consent_form_id BIGSERIAL NOT NULL REFERENCES consent_form(consent_form_id),
  language_id BIGSERIAL NOT NULL REFERENCES language(language_id),
  text VARCHAR(2000) NOT NULL,
  PRIMARY KEY (consent_form_text_id)
);

CREATE TYPE gender AS ENUM ('MALE', 'FEMALE');

CREATE TABLE "data_type" (
  data_type_id BIGSERIAL NOT NULL,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(2000),
  PRIMARY KEY (data_type_id)
);

CREATE TABLE "role" (
  role_id BIGSERIAL NOT NULL,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(1000) NOT NULL,
  project_id INTEGER NOT NULL REFERENCES project(project_id),
  PRIMARY KEY (role_id)
);

CREATE TABLE "consent_option" (
  consent_option_id BIGSERIAL NOT NULL,
  consent_form_id BIGSERIAL NOT NULL REFERENCES consent_form(consent_form_id),
  name VARCHAR(50) NOT NULL,
  order_no INT NOT NULL,
  PRIMARY KEY (consent_option_id)
);

CREATE TABLE "consent_option_text" (
  consent_option_text_id BIGSERIAL NOT NULL,
  consent_option_id BIGSERIAL NOT NULL REFERENCES consent_option(consent_option_id),
  language_id BIGSERIAL NOT NULL REFERENCES language(language_id),
  text VARCHAR(2000) NOT NULL,
  PRIMARY KEY (consent_option_text_id)
);

CREATE TABLE "consent" (
  consent_id BIGSERIAL NOT NULL,
  consent_option_id BIGSERIAL NOT NULL REFERENCES consent_option(consent_option_id),
  dpuser_id BIGSERIAL NOT NULL REFERENCES dpuser(dpuser_id),
  given_consent BOOLEAN NOT NULL,
  PRIMARY KEY (consent_id)
);

CREATE TABLE "experiment" (
  experiment_id BIGSERIAL NOT NULL,
  experiment_def_id BIGSERIAL NOT NULL REFERENCES experiment_def(experiment_def_id),
  dpuser_id BIGSERIAL NOT NULL REFERENCES dpuser(dpuser_id),
  name VARCHAR(200) NOT NULL,
  PRIMARY KEY (experiment_id)
);

-- CREATE TABLE "data_set" (
--   data_set_id BIGSERIAL NOT NULL,
--   experiment_id BIGSERIAL NOT NULL REFERENCES experiment(experiment_id),
--   dpuser_id BIGSERIAL NOT NULL REFERENCES dpuser(dpuser_id),
--   PRIMARY KEY (data_set_id)
-- );

CREATE TABLE "data_subject_experiment"(
  experiment_id BIGSERIAL NOT NULL REFERENCES experiment(experiment_id),
  data_subject_id BIGSERIAL REFERENCES dpuser(dpuser_id)
);

CREATE TABLE "project_member_project"(
  project_id BIGSERIAL REFERENCES project(project_id),
  project_member_id BIGSERIAL REFERENCES dpuser(dpuser_id)
);

CREATE TABLE "device_type" (
  device_type_id BIGSERIAL NOT NULL,
  producer VARCHAR(50) NOT NULL,
  model VARCHAR(50) NOT NULL,
  type VARCHAR(100) NOT NULL,
  PRIMARY KEY (device_type_id)
);

CREATE TABLE "device" (
  device_id BIGSERIAL NOT NULL,
  name VARCHAR(50),
  muid VARCHAR(50) NOT NULL UNIQUE,
  batterylevel VARCHAR(50),
  lastsynctime VARCHAR(50),
  devicetype_id INTEGER REFERENCES device_type (device_type_id),
  data_subject_id BIGSERIAL REFERENCES dpuser (dpuser_id),
  PRIMARY KEY (device_id)
);

CREATE TABLE "phase" (
  phase_id BIGSERIAL NOT NULL,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(1000) NOT NULL,
  startdate DATE NOT NULL,
  enddate DATE NOT NULL,
  project_id INTEGER NOT NULL REFERENCES project(project_id),
  PRIMARY KEY (phase_id)
);

CREATE TABLE "devicetype_datatype" (
    devicetype_id BIGSERIAL REFERENCES device_type(device_type_id),
    datatype_id BIGSERIAL REFERENCES data_type(data_type_id)

);

CREATE TABLE "role_dpuser" (
   role_id BIGSERIAL REFERENCES role (role_id),
   dpuser_id BIGSERIAL REFERENCES dpuser (dpuser_id)
);

CREATE TYPE "data_sensitivity" AS ENUM ('NON_PERSONAL', 'PERSONAL', 'SENSITIVE_PERSONAL');

CREATE TABLE "data_def" (
  data_def_id BIGSERIAL NOT NULL,
  experiment_def_id BIGSERIAL NOT NULL REFERENCES experiment_def(experiment_def_id),
  name VARCHAR(50) NOT NULL,
  sensitivity data_sensitivity NOT NULL,
  data_type_id BIGSERIAL NOT NULL REFERENCES data_type(data_type_id),
  lines_to_skip BIGINT,
  column_delimiter VARCHAR(50),
  cell_delimiter VARCHAR(50),
  timestamp_column BIGINT,
  timestamp_format VARCHAR(50),
  timestamp_unit VARCHAR(50),
  PRIMARY KEY (data_def_id)
);

CREATE TABLE "file" (
  file_id BIGSERIAL NOT NULL,
  data_def_id BIGSERIAL NOT NULL REFERENCES data_def(data_def_id),
  experiment_id BIGSERIAL NOT NULL REFERENCES experiment(experiment_id),
--   name VARCHAR(2000) NOT NULL, -- To be removed
  upload_date_time TIMESTAMP NOT NULL,
  version INTEGER NOT NULL,
  file_path VARCHAR(2000) NOT NULL,
  primary key (file_id)
);

CREATE TABLE "questionnaire_def" (
  questionnaire_def_id BIGSERIAL NOT NULL,
  experiment_def_id BIGSERIAL NOT NULL REFERENCES experiment_def(experiment_def_id),
  title VARCHAR(2000) NOT NULL,
  description VARCHAR(2000),
  PRIMARY KEY (questionnaire_def_id)
);

-- CREATE TYPE question_type AS ENUM ('SINGLE_CHOICE', 'MULTIPLE_CHOICE');

CREATE TABLE "question" (
  question_id BIGSERIAL NOT NULL,
  questionnaire_def_id BIGSERIAL NOT NULL REFERENCES  questionnaire_def(questionnaire_def_id),
  text VARCHAR(2000) NOT NULL,
  order_no INT NOT NULL,
--   type question_type NOT NULL,
  PRIMARY KEY (question_id)
);

-- CREATE TABLE "question_text" (
--   question_text_id BIGSERIAL NOT NULL,
--   question_id BIGSERIAL NOT NULL REFERENCES question(question_id),
--   language_id BIGSERIAL NOT NULL REFERENCES language(language_id),
--   text VARCHAR(2000) NOT NULL,
--   PRIMARY KEY (question_text_id)
-- );

CREATE TABLE "fixed_answer" (
  fixed_answer_id BIGSERIAL NOT NULL,
  question_id BIGSERIAL NOT NULL REFERENCES question(question_id),
  text VARCHAR(2000) NOT NULL,
  order_no INT NOT NULL,
  PRIMARY KEY (fixed_answer_id)
);

-- CREATE TABLE "fixed_answer_text" (
--   fixed_answer_text_id BIGSERIAL NOT NULL,
--   fixed_answer_id BIGSERIAL NOT NULL REFERENCES fixed_answer(fixed_answer_id),
--   language_id BIGSERIAL NOT NULL REFERENCES language(language_id),
--   text VARCHAR(2000),
--   PRIMARY KEY (fixed_answer_text_id)
-- );

CREATE TABLE "questionnaire" (
  questionnaire_id BIGSERIAL NOT NULL,
  data_def_id BIGSERIAL NOT NULL REFERENCES data_def(data_def_id),
  experiment_id BIGSERIAL NOT NULL REFERENCES experiment(experiment_id),
  questionnaire_def_id BIGSERIAL NOT NULL REFERENCES questionnaire_def(questionnaire_def_id),
  timestamp TIMESTAMP NOT NULL,
  PRIMARY KEY (questionnaire_id)
);

CREATE TABLE "answer" (
  answer_id BIGSERIAL NOT NULL,
  questionnaire_id BIGSERIAL NOT NULL REFERENCES questionnaire(questionnaire_id),
  question_id BIGSERIAL NOT NULL REFERENCES question(question_id),
  text VARCHAR NOT NULL,
--   fixed_answer_id BIGSERIAL NOT NULL REFERENCES fixed_answer(fixed_answer_id),
  PRIMARY KEY (answer_id)
);

CREATE TABLE "data_points" (
  data_points_id BIGSERIAL NOT NULL,
  data_def_id BIGSERIAL NOT NULL REFERENCES data_def(data_def_id),
  experiment_id BIGSERIAL NOT NULL REFERENCES experiment(experiment_id),
  measurement_name VARCHAR(2000) NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  PRIMARY KEY (data_points_id)
);

-- The two insertions are necessary before a user can be created.

INSERT INTO usertype (usertype_id, type) VALUES (1, 'Project member');

INSERT INTO usertype (usertype_id, type) VALUES (2, 'Data subject');

INSERT INTO language (name) VALUES ('English');

-- Creating some users

-- Password: testuser
INSERT INTO dpuser (user_name, password, given_name, surname, email, usertype_id) VALUES
  ('testuser', '$2a$10$24.capCv2pQpF54PyZPgsOkYZqVnxdgkGHzpYGWRlA8wOI1/.X4Q.', 'Test', 'User', 'tu@email.com', 1);

-- The three users below cannot log in as the passwords need to hashed like the one above

INSERT INTO dpuser (user_name, password, given_name, surname, email, usertype_id) VALUES
  ('tessa', 't', 'Tessa', 'Dickson', 'td@email.com', 2);

INSERT INTO dpuser (user_name, password, given_name, surname, email, usertype_id) VALUES
  ('tim', 'tim', 'Tim', 'Ho', 'th@email.com', 2);

INSERT INTO dpuser (user_name, password, given_name, surname, email, usertype_id) VALUES
  ('chris', 'c', 'Chris', 'Thompson', 'ct@email.com', 2);

-- The three insertions are necessary before data definitions can be created.

INSERT INTO data_type (data_type_id, name, description) VALUES
  (1, 'Time Series', 'Data that can be identified with a timestamp');

INSERT INTO data_type (data_type_id, name, description) VALUES
  (2, 'Questionnaire', 'Exported CSV file from Google Forms');

INSERT INTO data_type (data_type_id, name, description) VALUES
  (3, 'File', 'Any data that does not fall in any other category');

-- Creating views for easier retrievement of certain data

-- This view is used to fetch an overview of the data definitions with data in the current experiment
CREATE VIEW dataset_file_overview AS (SELECT gf.file_id, gf.experiment_id, dd.experiment_def_id, dd.data_def_id,
                                         dd.name as data_def_name, gf.version_count, gf.date_uploaded
              FROM (SELECT max(file_id) as file_id ,data_def_id, experiment_id, count(file_id) as version_count,
                           max(upload_date_time) as date_uploaded
                    FROM file
                    GROUP BY data_def_id, experiment_id) gf
                     INNER JOIN data_def dd ON gf.data_def_id = dd.data_def_id ORDER BY data_def_name);