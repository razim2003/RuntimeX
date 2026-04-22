-- ============================================================
-- Migration: Course Materials + Timetable Day/Time columns
-- Apply this on top of the existing database-schema.sql
-- ============================================================

USE tecmis_java;

-- ----------------------------------------------------------------
-- 1.  Add day_of_week and start_time columns to the timetable table
--     (the table already exists – we only ADD missing columns)
-- ----------------------------------------------------------------
ALTER TABLE timetable
    ADD COLUMN IF NOT EXISTS day_of_week ENUM(
        'Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday'
    ) NOT NULL DEFAULT 'Monday' AFTER hours,
    ADD COLUMN IF NOT EXISTS start_time TIME NOT NULL DEFAULT '08:00:00' AFTER day_of_week;

-- Add a department column to technical_officer so that
-- TOs can be filtered to see only their department's timetable.
ALTER TABLE technical_officer
    ADD COLUMN IF NOT EXISTS department VARCHAR(100) NULL AFTER to_id;

-- ----------------------------------------------------------------
-- 2.  course_material table
--     One row = one material item (file / link / text) per course.
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS course_material (
    material_id   CHAR(12)     PRIMARY KEY,
    course_code   CHAR(7)      NOT NULL,
    lec_id        CHAR(12)     NOT NULL,                 -- uploader / owner
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    material_type ENUM('File','Link','Text') NOT NULL DEFAULT 'Text',
    content       TEXT,                                   -- file path OR URL OR raw text
    uploaded_date DATE         NOT NULL,
    FOREIGN KEY (course_code) REFERENCES course_unit(course_code) ON DELETE CASCADE,
    FOREIGN KEY (lec_id)      REFERENCES lecturer(lec_id)         ON DELETE CASCADE
);
