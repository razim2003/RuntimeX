
USE tecmis_java;

ALTER TABLE timetable
    ADD COLUMN day_of_week ENUM(
        'Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday'
    ) NOT NULL DEFAULT 'Monday' AFTER hours,
    ADD COLUMN start_time TIME NOT NULL DEFAULT '08:00:00' AFTER day_of_week;


ALTER TABLE technical_officer
    ADD COLUMN department VARCHAR(100) NULL AFTER to_id;

CREATE TABLE course_material (
    material_id   CHAR(12)     PRIMARY KEY,
    course_code   CHAR(7)      NOT NULL,
    lec_id        CHAR(12)     NOT NULL,                 
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    material_type ENUM('File','Link','Text') NOT NULL DEFAULT 'Text',
    content       TEXT,                                  
    uploaded_date DATE         NOT NULL,
    FOREIGN KEY (course_code) REFERENCES course_unit(course_code) ON DELETE CASCADE,
    FOREIGN KEY (lec_id)      REFERENCES lecturer(lec_id)         ON DELETE CASCADE
);
