CREATE TABLE user (
                      id CHAR(12) PRIMARY KEY,
                      f_name VARCHAR(50) NOT NULL,
                      l_name VARCHAR(50) NOT NULL,
                      email VARCHAR(100) UNIQUE,
                      contact_no VARCHAR(15),
                      profile_picture VARCHAR(255),
                      user_type ENUM('Admin','Lecturer','TechnicalOfficer','Undergraduate') NOT NULL
);

CREATE TABLE admin (
                       admin_id CHAR(12) PRIMARY KEY,
                       FOREIGN KEY (admin_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE lecturer (
                          lec_id CHAR(12) PRIMARY KEY,
                          designation VARCHAR(50),
                          department_id CHAR(7),
                          FOREIGN KEY (lec_id) REFERENCES user(id) ON DELETE CASCADE,
                          FOREIGN KEY (department_id) REFERENCES department(dep_id) ON DELETE SET NULL
);

CREATE TABLE technical_officer (
                                   to_id CHAR(12) PRIMARY KEY,
                                   FOREIGN KEY (to_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE undergraduate (
                               stu_id CHAR(12) PRIMARY KEY,
                               status ENUM('Proper','Repeat','Suspended') DEFAULT 'Proper',
                               department_id CHAR(7),
                               mentor_id CHAR(12),
                               FOREIGN KEY (stu_id) REFERENCES user(id) ON DELETE CASCADE,
                               FOREIGN KEY (department_id) REFERENCES department(dep_id) ON DELETE SET NULL,
                               FOREIGN KEY (mentor_id) REFERENCES lecturer(lec_id) ON DELETE SET NULL
);

CREATE TABLE marks (
                       mark_id CHAR(12) PRIMARY KEY,
                       stu_id CHAR(12) NOT NULL,
                       course_code CHAR(7) NOT NULL,
                       type_id CHAR(4) NOT NULL,
                       mark DECIMAL(5,2) NOT NULL,
                       FOREIGN KEY (stu_id) REFERENCES undergraduate(stu_id) ON DELETE CASCADE,
                       FOREIGN KEY (course_code) REFERENCES course_unit(course_code),
                       FOREIGN KEY (type_id) REFERENCES exam_type(type_id)
);

CREATE TABLE attendance (
                            attendance_id CHAR(12) PRIMARY KEY,
                            stu_id CHAR(12),
                            course_code CHAR(7),
                            session_no INT,
                            session_date DATE,
                            session_type ENUM('Theory','Practical'),
                            status ENUM('Present','Absent','Medical'),
                            FOREIGN KEY (stu_id) REFERENCES undergraduate(stu_id),
                            FOREIGN KEY (course_code) REFERENCES course_unit(course_code)
);

CREATE TABLE medical (
                         ref_no CHAR(6) PRIMARY KEY,
                         stu_id CHAR(12),
                         reason TEXT,
                         status ENUM('Approved','Pending','Rejected') DEFAULT 'Pending',
                         start_date DATE,
                         end_date DATE,
                         FOREIGN KEY (stu_id) REFERENCES undergraduate(stu_id)
);

CREATE TABLE notice (
                        notice_id CHAR(12) PRIMARY KEY,
                        admin_id CHAR(12),
                        title VARCHAR(255),
                        date DATE,
                        pdf_file VARCHAR(255),
                        FOREIGN KEY (admin_id) REFERENCES admin(admin_id)
);

CREATE TABLE blog (
                      blog_id CHAR(12) PRIMARY KEY,
                      id CHAR(12),
                      title VARCHAR(255),
                      date DATE,
                      FOREIGN KEY (id) REFERENCES user(id)
);

