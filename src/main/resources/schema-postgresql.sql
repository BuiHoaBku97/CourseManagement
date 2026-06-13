-- PostgreSQL schema for Course Management

CREATE TABLE admin (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE student (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    sex BIT(1) NOT NULL,
    phone VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    create_at DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE course (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    duration INT NOT NULL,
    instructor VARCHAR(100) NOT NULL,
    create_at DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE topic (
    topic_code VARCHAR(20) PRIMARY KEY
);

CREATE TABLE course_topic (
    course_id INT NOT NULL,
    topic_code VARCHAR(20) NOT NULL,
    level INT NOT NULL,
    CONSTRAINT pk_course_topic
        PRIMARY KEY (course_id, topic_code),
    CONSTRAINT fk_course_topic_course
        FOREIGN KEY (course_id) REFERENCES course (id) ON DELETE CASCADE,
    CONSTRAINT fk_course_topic_topic
        FOREIGN KEY (topic_code) REFERENCES topic (topic_code) ON DELETE CASCADE,
    CONSTRAINT chk_course_topic_level
        CHECK (level > 0)
);

CREATE INDEX idx_course_topic_topic_code
    ON course_topic (topic_code);

CREATE INDEX idx_course_topic_topic_code_level
    ON course_topic (topic_code, level);

CREATE TYPE enrollment_status AS ENUM ('WAITING', 'DENIED', 'CANCEL', 'CONFIRM');

CREATE TABLE enrollment (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    registered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status enrollment_status NOT NULL DEFAULT 'WAITING',
    CONSTRAINT fk_enrollment_student
        FOREIGN KEY (student_id) REFERENCES student (id),
    CONSTRAINT fk_enrollment_course
        FOREIGN KEY (course_id) REFERENCES course (id)
);
