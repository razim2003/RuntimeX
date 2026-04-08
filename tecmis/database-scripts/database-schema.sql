CREATE DATABASE IF NOT EXISTS tecmis_java
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE tecmis_java;

CREATE TABLE users (
                      id CHAR(12) PRIMARY KEY,
                      f_name VARCHAR(50) NOT NULL,
                      l_name VARCHAR(50) NOT NULL,
                      email VARCHAR(100) UNIQUE NOT NULL,
                      contact_no VARCHAR(15) NOT NULL,
                      hash_pwd VARCHAR(255) NOT NULL,
                      user_type ENUM('Admin','Lecturer','TechnicalOfficer','Undergraduate') NOT NULL
);


CREATE TABLE admin (
                       admin_id CHAR(12) PRIMARY KEY,
                       FOREIGN KEY (admin_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE lecturer (
                          lec_id CHAR(12) PRIMARY KEY,
                          designation VARCHAR(50),
                          FOREIGN KEY (lec_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE technical_officer (
                                   to_id CHAR(12) PRIMARY KEY,
                                   FOREIGN KEY (to_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE undergraduate (
                               stu_id CHAR(12) PRIMARY KEY,
                               status ENUM('Proper','Repeat','Suspended') DEFAULT 'Proper',
                               mentor_id CHAR(12),
                               FOREIGN KEY (stu_id) REFERENCES users(id) ON DELETE CASCADE,
                               FOREIGN KEY (mentor_id) REFERENCES lecturer(lec_id) ON DELETE SET NULL
);


CREATE TABLE course_unit (
                             course_code CHAR(7) PRIMARY KEY,
                             title VARCHAR(100),
                             credit INT NOT NULL

);


CREATE TABLE enrollment (
                            stu_id CHAR(12),
                            course_code CHAR(7),
                            PRIMARY KEY (stu_id, course_code),
                            FOREIGN KEY (stu_id) REFERENCES undergraduate(stu_id) ON DELETE CASCADE,
                            FOREIGN KEY (course_code) REFERENCES course_unit(course_code) ON DELETE CASCADE
);


CREATE TABLE exam_type (
                           type_id CHAR(4) PRIMARY KEY,
                           type_name VARCHAR(50),
                           weight DECIMAL(3,2) NOT NULL CHECK (weight > 0 AND weight <= 1)-- e.g. 0.30, 0.70
);


CREATE TABLE marks (
                       mark_id CHAR(12) PRIMARY KEY,
                       stu_id CHAR(12) NOT NULL,
                       course_code CHAR(7) NOT NULL,
                       type_id CHAR(4) NOT NULL,
                       mark DECIMAL(5,2) CHECK (mark >= 0 AND mark <= 100),


                       FOREIGN KEY (stu_id, course_code)
                           REFERENCES enrollment(stu_id, course_code) ON DELETE CASCADE,

                       FOREIGN KEY (type_id) REFERENCES exam_type(type_id),

                       UNIQUE (stu_id, course_code, type_id) -- prevent duplicates
);


CREATE TABLE attendance (
                            attendance_id CHAR(12) PRIMARY KEY,
                            stu_id CHAR(12) NOT NULL,
                            course_code CHAR(7) NOT NULL,
                            session_date DATE NOT NULL,
                            status ENUM('Present','Absent') DEFAULT 'Present',

                            UNIQUE (stu_id, course_code, session_date),

                            FOREIGN KEY (stu_id, course_code)
                            REFERENCES enrollment(stu_id, course_code)
);

CREATE TABLE medical (
                         ref_no CHAR(6) PRIMARY KEY,
                         stu_id CHAR(12) NOT NULL,
                         reason TEXT,
                         status ENUM('Approved','Pending','Rejected') DEFAULT 'Pending',
                         start_date DATE,
                         end_date DATE,

                         FOREIGN KEY (stu_id) REFERENCES undergraduate(stu_id) ON DELETE CASCADE
);

CREATE TABLE medical_attendance (
                                    attendance_id CHAR(12),
                                    ref_no CHAR(6),

                                    PRIMARY KEY (attendance_id, ref_no),

                                    FOREIGN KEY (attendance_id)
                                        REFERENCES attendance(attendance_id) ON DELETE CASCADE,

                                    FOREIGN KEY (ref_no)
                                        REFERENCES medical(ref_no) ON DELETE CASCADE
);


CREATE TABLE notice (
                        notice_id CHAR(12) PRIMARY KEY,
                        admin_id CHAR(12) NOT NULL,
                        title VARCHAR(255) NOT NULL,
                        date DATE NOT NULL,
                        FOREIGN KEY (admin_id) REFERENCES admin(admin_id)
);


CREATE TABLE blog (
                      blog_id CHAR(12) PRIMARY KEY,
                      user_id CHAR(12),
                      title VARCHAR(255) NOT NULL,
                      date DATE NOT NULL,
                      FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE TABLE notification (
                              notification_id CHAR(12) PRIMARY KEY,
                              admin_id CHAR(12) NOT NULL,
                              message VARCHAR(255) NOT NULL,
                              date DATE NOT NULL,
                              status ENUM('Active','Inactive') DEFAULT 'Active',

                              FOREIGN KEY (admin_id) REFERENCES admin(admin_id) ON DELETE CASCADE
);


CREATE TABLE timetable (
                           timetable_id CHAR(12) PRIMARY KEY,
                           admin_id CHAR(12),
                           lec_id CHAR(12),
                           course_code CHAR(7) NOT NULL,
                           location VARCHAR(100) NOT NULL,
                           level INT NOT NULL,
                           type ENUM('Theory','Practical') NOT NULL,
                           hours INT NOT NULL,
                           FOREIGN KEY (admin_id) REFERENCES admin(admin_id) ON DELETE CASCADE,
                           FOREIGN KEY (lec_id) REFERENCES lecturer(lec_id) ON DELETE SET NULL,
                           FOREIGN KEY (course_code) REFERENCES course_unit(course_code) ON DELETE CASCADE
);


CREATE TABLE event_cal (
                           event_id CHAR(12) PRIMARY KEY,
                           user_id CHAR(12) NOT NULL,
                           title VARCHAR(100) NOT NULL,
                           description TEXT,
                           date DATE NOT NULL,
                           time TIME NOT NULL,

                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE lecturer_course (
                                 lec_id CHAR(12),
                                 course_code CHAR(7),
                                 PRIMARY KEY (lec_id, course_code),

                                 FOREIGN KEY (lec_id) REFERENCES lecturer(lec_id) ON DELETE CASCADE,
                                 FOREIGN KEY (course_code) REFERENCES course_unit(course_code) ON DELETE CASCADE
);


CREATE INDEX idx_enrollment_stu ON enrollment(stu_id);
CREATE INDEX idx_marks_course ON marks(course_code);