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
