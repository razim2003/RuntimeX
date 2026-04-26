USE tecmis_java;
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;
SET collation_connection = 'utf8mb4_unicode_ci';
INSERT INTO users (
        id,
        f_name,
        l_name,
        email,
        contact_no,
        hash_pwd,
        user_type
    )
VALUES -- Admin
    (
        'AD001',
        'Ravindra',
        'Perera',
        'ravindra@ruh.ac.lk',
        '0771234567',
        'd4c3b2a1f6e5d4c3b2a1f0e9d8c7b6a5',
        'Admin'
    ),
    -- Lecturers
    (
        'LEC001',
        'Nishantha',
        'Fernando',
        'nishantha@ruh.ac.lk',
        '0771111111',
        'a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6',
        'Lecturer'
    ),
    (
        'LEC002',
        'Chamila',
        'Wijesinghe',
        'chamila@ruh.ac.lk',
        '0772222222',
        'b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6',
        'Lecturer'
    ),
    (
        'LEC003',
        'Kavindu',
        'Jayasinghe',
        'kavindu@ruh.ac.lk',
        '0773333333',
        'c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6',
        'Lecturer'
    ),
    (
        'LEC004',
        'Dilani',
        'Senanayake',
        'dilani@ruh.ac.lk',
        '0774444444',
        'd1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6',
        'Lecturer'
    ),
    (
        'LEC005',
        'Roshan',
        'Kumarasinghe',
        'roshan@ruh.ac.lk',
        '0775555555',
        'e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6',
        'Lecturer'
    ),
    (
        'LEC006',
        'Nadeesha',
        'De Silva',
        'nadeesha@ruh.ac.lk',
        '0776666666',
        'f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6',
        'Lecturer'
    ),
    (
        'LEC007',
        'Chamika',
        'Jayawardena',
        'chamika@ruh.ac.lk',
        '0777777777',
        'a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7',
        'Lecturer'
    ),
    (
        'LEC008',
        'Kasun',
        'Rajapaksha',
        'kasun@ruh.ac.lk',
        '0778888888',
        'b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7',
        'Lecturer'
    ),
    -- Technical Officers
    (
        'TO001',
        'Ameer',
        'Faisal',
        'ameer@ruh.ac.lk',
        '0711111111',
        'f1e2d3c4b5a6f7e8d9c0b1a2f3e4d5c6',
        'TechnicalOfficer'
    ),
    (
        'TO002',
        'Tharindu',
        'Gunawardena',
        'tharindu@ruh.ac.lk',
        '0722222222',
        'a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7',
        'TechnicalOfficer'
    ),
    (
        'TO003',
        'Sanduni',
        'Abeysekara',
        'sanduni@ruh.ac.lk',
        '0733333333',
        'b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7',
        'TechnicalOfficer'
    ),
    (
        'TO004',
        'Nimal',
        'Perera',
        'nimal@ruh.ac.lk',
        '0744444444',
        'c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7',
        'TechnicalOfficer'
    );
INSERT INTO users (
        id,
        f_name,
        l_name,
        email,
        contact_no,
        hash_pwd,
        user_type
    )
VALUES -- Current students (2023)
    (
        'TG/2023/1780',
        'Nimal',
        'Perera',
        'nimal.perera@students.tecmis.lk',
        '0712345678',
        'hashed_password_1',
        'Undergraduate'
    ),
    (
        'TG/2023/1781',
        'Kamal',
        'Fernando',
        'kamal.fernando@students.tecmis.lk',
        '0712345679',
        'hashed_password_2',
        'Undergraduate'
    ),
    (
        'TG/2023/1782',
        'Sunil',
        'Silva',
        'sunil.silva@students.tecmis.lk',
        '0712345680',
        'hashed_password_3',
        'Undergraduate'
    ),
    (
        'TG/2023/1783',
        'Amara',
        'Jayawardena',
        'amara.j@students.tecmis.lk',
        '0712345681',
        'hashed_password_4',
        'Undergraduate'
    ),
    (
        'TG/2023/1784',
        'Ruwan',
        'Rathnayake',
        'ruwan.r@students.tecmis.lk',
        '0712345682',
        'hashed_password_5',
        'Undergraduate'
    ),
    -- Repeat students (2022)
    (
        'TG/2022/1785',
        'Dinuka',
        'Fernando',
        'dinuka.fernando@gmail.com',
        '0750000006',
        'e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0',
        'Undergraduate'
    ),
    (
        'TG/2022/1786',
        'Kasun',
        'Perera',
        'kasun.perera@gmail.com',
        '0750000007',
        'f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0',
        'Undergraduate'
    ),
    (
        'TG/2022/1787',
        'Malithi',
        'Silva',
        'malithi.silva@gmail.com',
        '0750000008',
        'a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1',
        'Undergraduate'
    ),
    (
        'TG/2022/1788',
        'Ravindu',
        'Fernando',
        'ravindu.fernando@gmail.com',
        '0750000009',
        'b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1',
        'Undergraduate'
    ),
    (
        'TG/2022/1789',
        'Piumi',
        'Perera',
        'piumi.perera@gmail.com',
        '0750000010',
        'c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1',
        'Undergraduate'
    ),
    -- Suspended/Batch-missed students (2021)
    (
        'TG/2021/1790',
        'Supun',
        'Silva',
        'supun.silva@gmail.com',
        '0750000011',
        'd6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1',
        'Undergraduate'
    ),
    (
        'TG/2021/1791',
        'Sachini',
        'Fernando',
        'sachini.fernando@gmail.com',
        '0750000012',
        'e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1',
        'Undergraduate'
    ),
    (
        'TG/2021/1792',
        'Amila',
        'Perera',
        'amila.perera@gmail.com',
        '0750000013',
        'f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1',
        'Undergraduate'
    ),
    (
        'TG/2021/1793',
        'Nimali',
        'Silva',
        'nimali.silva@gmail.com',
        '0750000014',
        'a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2',
        'Undergraduate'
    ),
    (
        'TG/2021/1794',
        'Chathura',
        'Fernando',
        'chathura.fernando@gmail.com',
        '0750000015',
        'b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2',
        'Undergraduate'
    );
INSERT INTO admin (admin_id)
VALUES ('AD001');
INSERT INTO lecturer (lec_id, designation)
VALUES ('LEC001', 'Lecturer');
INSERT INTO technical_officer (to_id)
VALUES ('TO001'),
    ('TO002'),
    ('TO003'),
    ('TO004');
INSERT INTO undergraduate (stu_id, status)
VALUES ('TG/2023/1780', 'Proper'),
    ('TG/2023/1781', 'Proper'),
    ('TG/2022/1785', 'Repeat'),
    ('TG/2021/1790', 'Suspended');
INSERT INTO course_unit (course_code, title, credit)
VALUES (
        'ICT2142',
        'Object Oriented Analysis and Design',
        2
    ),
    ('ICT2122', 'Object Oriented Programming', 2),
    (
        'ICT2132',
        'Object Oriented Programming Practicum',
        2
    ),
    (
        'ICT2152',
        'E-Commerce Implementation, Management and Security',
        2
    ),
    ('ENG2122', 'English III', 2),
    ('ICT2113', 'Data Structures and Algorithms', 3),
    ('TCS2112', 'Business Economics', 2),
    ('TCS2122', 'Soft Skills', 1);
INSERT INTO enrollment (stu_id, course_code)
VALUES -- TG/2023/1780 (Proper student - enrolled in ALL 8 courses)
    ('TG/2023/1780', 'ICT2142'),
    ('TG/2023/1780', 'ICT2122'),
    ('TG/2023/1780', 'ICT2132'),
    ('TG/2023/1780', 'ICT2152'),
    ('TG/2023/1780', 'ENG2122'),
    ('TG/2023/1780', 'ICT2113'),
    ('TG/2023/1780', 'TCS2112'),
    ('TG/2023/1780', 'TCS2122'),
    -- TG/2023/1781 (Proper student - enrolled in ALL 8 courses, but will miss some exams)
    ('TG/2023/1781', 'ICT2142'),
    ('TG/2023/1781', 'ICT2122'),
    ('TG/2023/1781', 'ICT2132'),
    ('TG/2023/1781', 'ICT2152'),
    ('TG/2023/1781', 'ENG2122'),
    ('TG/2023/1781', 'ICT2113'),
    ('TG/2023/1781', 'TCS2112'),
    ('TG/2023/1781', 'TCS2122'),
    -- TG/2022/1785 (Repeat student - taking 2 courses)
    ('TG/2022/1785', 'ICT2142'),
    ('TG/2022/1785', 'TCS2122'),
    -- TG/2021/1790 (Suspended student - previous enrollments)
    ('TG/2021/1790', 'ICT2122'),
    ('TG/2021/1790', 'ENG2122'),
    ('TG/2021/1790', 'ICT2142');
INSERT INTO exam_type (type_id, type_name)
VALUES ('ASST', 'Assignment'),
    ('MID', 'Midterm'),
    ('FIN', 'Final'),
    ('QU01', 'Quiz 01'),
    ('QU02', 'Quiz 02'),
    ('QU03', 'Quiz 03');
-- ICT2142 - Object Oriented Analysis and Design
INSERT INTO course_exam (
        course_code,
        type_id,
        weight,
        exam_date,
        exam_name
    )
VALUES (
        'ICT2142',
        'ASST',
        0.20,
        '2026-03-15',
        'Assignment'
    ),
    (
        'ICT2142',
        'FIN',
        0.70,
        '2026-06-10',
        'Final Exam'
    ),
    ('ICT2142', 'QU01', 0.05, '2026-02-10', 'Quiz 1'),
    ('ICT2142', 'QU02', 0.05, '2026-03-05', 'Quiz 2'),
    ('ICT2142', 'QU03', 0.05, '2026-04-20', 'Quiz 3');
-- ICT2122 - Object Oriented Programming
INSERT INTO course_exam (
        course_code,
        type_id,
        weight,
        exam_date,
        exam_name
    )
VALUES (
        'ICT2122',
        'ASST',
        0.10,
        '2026-03-20',
        'Assignment'
    ),
    (
        'ICT2122',
        'MID',
        0.10,
        '2026-04-05',
        'Midterm'
    ),
    (
        'ICT2122',
        'FIN',
        0.70,
        '2026-06-15',
        'Final Exam'
    ),
    ('ICT2122', 'QU01', 0.05, '2026-02-20', 'Quiz 1'),
    ('ICT2122', 'QU02', 0.05, '2026-03-25', 'Quiz 2'),
    ('ICT2122', 'QU03', 0.05, '2026-04-15', 'Quiz 3');
-- ICT2132 - Object Oriented Programming Practicum
INSERT INTO course_exam (
        course_code,
        type_id,
        weight,
        exam_date,
        exam_name
    )
VALUES ('ICT2132', 'ASST', 0.20, '2026-04-10', 'Project'),
    (
        'ICT2132',
        'MID',
        0.10,
        '2026-04-20',
        'Midterm'
    ),
    (
        'ICT2132',
        'FIN',
        0.60,
        '2026-06-20',
        'Final Exam'
    ),
    ('ICT2132', 'QU01', 0.05, '2026-02-15', 'Quiz 1'),
    ('ICT2132', 'QU02', 0.05, '2026-03-15', 'Quiz 2'),
    ('ICT2132', 'QU03', 0.05, '2026-04-25', 'Quiz 3');
-- ICT2152 - E-Commerce Implementation
INSERT INTO course_exam (
        course_code,
        type_id,
        weight,
        exam_date,
        exam_name
    )
VALUES (
        'ICT2152',
        'ASST',
        0.20,
        '2026-03-25',
        'E-Commerce Project'
    ),
    (
        'ICT2152',
        'MID',
        0.10,
        '2026-04-15',
        'Midterm'
    ),
    (
        'ICT2152',
        'FIN',
        0.60,
        '2026-06-18',
        'Final Exam'
    ),
    ('ICT2152', 'QU01', 0.05, '2026-02-12', 'Quiz 1'),
    ('ICT2152', 'QU02', 0.05, '2026-03-12', 'Quiz 2'),
    ('ICT2152', 'QU03', 0.05, '2026-04-22', 'Quiz 3');
-- ENG2122 - English III
INSERT INTO course_exam (
        course_code,
        type_id,
        weight,
        exam_date,
        exam_name
    )
VALUES (
        'ENG2122',
        'ASST',
        0.20,
        '2026-03-30',
        'English Assignment'
    ),
    (
        'ENG2122',
        'MID',
        0.10,
        '2026-04-12',
        'Midterm'
    ),
    (
        'ENG2122',
        'FIN',
        0.60,
        '2026-06-14',
        'Final Exam'
    ),
    ('ENG2122', 'QU01', 0.05, '2026-02-18', 'Quiz 1'),
    ('ENG2122', 'QU02', 0.05, '2026-03-18', 'Quiz 2'),
    ('ENG2122', 'QU03', 0.05, '2026-04-28', 'Quiz 3');
-- ICT2113 - Data Structures and Algorithms
INSERT INTO course_exam (
        course_code,
        type_id,
        weight,
        exam_date,
        exam_name
    )
VALUES (
        'ICT2113',
        'ASST',
        0.10,
        '2026-03-18',
        'Assignment'
    ),
    (
        'ICT2113',
        'MID',
        0.20,
        '2026-04-10',
        'Midterm'
    ),
    (
        'ICT2113',
        'FIN',
        0.60,
        '2026-06-22',
        'Final Exam'
    ),
    ('ICT2113', 'QU01', 0.05, '2026-02-25', 'Quiz 1'),
    ('ICT2113', 'QU02', 0.05, '2026-03-20', 'Quiz 2'),
    ('ICT2113', 'QU03', 0.05, '2026-04-18', 'Quiz 3');
-- TCS2112 - Business Economics
INSERT INTO course_exam (
        course_code,
        type_id,
        weight,
        exam_date,
        exam_name
    )
VALUES (
        'TCS2112',
        'ASST',
        0.10,
        '2026-03-22',
        'Economics Assignment'
    ),
    (
        'TCS2112',
        'MID',
        0.10,
        '2026-04-18',
        'Midterm'
    ),
    (
        'TCS2112',
        'FIN',
        0.70,
        '2026-06-20',
        'Final Exam'
    ),
    ('TCS2112', 'QU01', 0.05, '2026-02-22', 'Quiz 1'),
    ('TCS2112', 'QU02', 0.05, '2026-03-22', 'Quiz 2'),
    ('TCS2112', 'QU03', 0.05, '2026-04-30', 'Quiz 3');
-- TCS2122 - Soft Skills
INSERT INTO course_exam (
        course_code,
        type_id,
        weight,
        exam_date,
        exam_name
    )
VALUES (
        'TCS2122',
        'ASST',
        0.10,
        '2026-03-28',
        'Soft Skills Assignment'
    ),
    (
        'TCS2122',
        'MID',
        0.20,
        '2026-04-22',
        'Midterm'
    ),
    (
        'TCS2122',
        'FIN',
        0.60,
        '2026-06-23',
        'Final Exam'
    ),
    ('TCS2122', 'QU01', 0.05, '2026-02-28', 'Quiz 1'),
    ('TCS2122', 'QU02', 0.05, '2026-03-28', 'Quiz 2'),
    ('TCS2122', 'QU03', 0.05, '2026-05-01', 'Quiz 3');
INSERT INTO marks (mark_id, stu_id, course_code, type_id, mark)
VALUES -- ICT2142
    (
        'MK000000001',
        'TG/2023/1780',
        'ICT2142',
        'ASST',
        82.00
    ),
    (
        'MK000000002',
        'TG/2023/1780',
        'ICT2142',
        'FIN',
        75.00
    ),
    (
        'MK000000003',
        'TG/2023/1780',
        'ICT2142',
        'QU01',
        80.00
    ),
    (
        'MK000000004',
        'TG/2023/1780',
        'ICT2142',
        'QU02',
        78.00
    ),
    (
        'MK000000005',
        'TG/2023/1780',
        'ICT2142',
        'QU03',
        85.00
    ),
    -- ICT2122
    (
        'MK000000006',
        'TG/2023/1780',
        'ICT2122',
        'ASST',
        88.00
    ),
    (
        'MK000000007',
        'TG/2023/1780',
        'ICT2122',
        'MID',
        72.00
    ),
    (
        'MK000000008',
        'TG/2023/1780',
        'ICT2122',
        'FIN',
        79.00
    ),
    (
        'MK000000009',
        'TG/2023/1780',
        'ICT2122',
        'QU01',
        85.00
    ),
    (
        'MK000000010',
        'TG/2023/1780',
        'ICT2122',
        'QU02',
        82.00
    ),
    (
        'MK000000011',
        'TG/2023/1780',
        'ICT2122',
        'QU03',
        80.00
    ),
    -- ICT2132
    (
        'MK000000012',
        'TG/2023/1780',
        'ICT2132',
        'ASST',
        90.00
    ),
    (
        'MK000000013',
        'TG/2023/1780',
        'ICT2132',
        'MID',
        84.00
    ),
    (
        'MK000000014',
        'TG/2023/1780',
        'ICT2132',
        'FIN',
        86.00
    ),
    (
        'MK000000015',
        'TG/2023/1780',
        'ICT2132',
        'QU01',
        88.00
    ),
    (
        'MK000000016',
        'TG/2023/1780',
        'ICT2132',
        'QU02',
        85.00
    ),
    (
        'MK000000017',
        'TG/2023/1780',
        'ICT2132',
        'QU03',
        87.00
    ),
    -- ICT2152
    (
        'MK000000018',
        'TG/2023/1780',
        'ICT2152',
        'ASST',
        76.00
    ),
    (
        'MK000000019',
        'TG/2023/1780',
        'ICT2152',
        'MID',
        68.00
    ),
    (
        'MK000000020',
        'TG/2023/1780',
        'ICT2152',
        'FIN',
        74.00
    ),
    (
        'MK000000021',
        'TG/2023/1780',
        'ICT2152',
        'QU01',
        72.00
    ),
    (
        'MK000000022',
        'TG/2023/1780',
        'ICT2152',
        'QU02',
        70.00
    ),
    (
        'MK000000023',
        'TG/2023/1780',
        'ICT2152',
        'QU03',
        75.00
    ),
    -- ENG2122
    (
        'MK000000024',
        'TG/2023/1780',
        'ENG2122',
        'ASST',
        85.00
    ),
    (
        'MK000000025',
        'TG/2023/1780',
        'ENG2122',
        'MID',
        78.00
    ),
    (
        'MK000000026',
        'TG/2023/1780',
        'ENG2122',
        'FIN',
        80.00
    ),
    (
        'MK000000027',
        'TG/2023/1780',
        'ENG2122',
        'QU01',
        82.00
    ),
    (
        'MK000000028',
        'TG/2023/1780',
        'ENG2122',
        'QU02',
        79.00
    ),
    (
        'MK000000029',
        'TG/2023/1780',
        'ENG2122',
        'QU03',
        83.00
    ),
    -- ICT2113
    (
        'MK000000030',
        'TG/2023/1780',
        'ICT2113',
        'ASST',
        77.00
    ),
    (
        'MK000000031',
        'TG/2023/1780',
        'ICT2113',
        'MID',
        71.00
    ),
    (
        'MK000000032',
        'TG/2023/1780',
        'ICT2113',
        'FIN',
        73.00
    ),
    (
        'MK000000033',
        'TG/2023/1780',
        'ICT2113',
        'QU01',
        75.00
    ),
    (
        'MK000000034',
        'TG/2023/1780',
        'ICT2113',
        'QU02',
        74.00
    ),
    (
        'MK000000035',
        'TG/2023/1780',
        'ICT2113',
        'QU03',
        76.00
    ),
    -- TCS2112
    (
        'MK000000036',
        'TG/2023/1780',
        'TCS2112',
        'ASST',
        80.00
    ),
    (
        'MK000000037',
        'TG/2023/1780',
        'TCS2112',
        'MID',
        75.00
    ),
    (
        'MK000000038',
        'TG/2023/1780',
        'TCS2112',
        'FIN',
        78.00
    ),
    (
        'MK000000039',
        'TG/2023/1780',
        'TCS2112',
        'QU01',
        77.00
    ),
    (
        'MK000000040',
        'TG/2023/1780',
        'TCS2112',
        'QU02',
        79.00
    ),
    (
        'MK000000041',
        'TG/2023/1780',
        'TCS2112',
        'QU03',
        81.00
    ),
    -- TCS2122
    (
        'MK000000042',
        'TG/2023/1780',
        'TCS2122',
        'ASST',
        83.00
    ),
    (
        'MK000000043',
        'TG/2023/1780',
        'TCS2122',
        'MID',
        76.00
    ),
    (
        'MK000000044',
        'TG/2023/1780',
        'TCS2122',
        'FIN',
        79.00
    ),
    (
        'MK000000045',
        'TG/2023/1780',
        'TCS2122',
        'QU01',
        80.00
    ),
    (
        'MK000000046',
        'TG/2023/1780',
        'TCS2122',
        'QU02',
        78.00
    ),
    (
        'MK000000047',
        'TG/2023/1780',
        'TCS2122',
        'QU03',
        82.00
    ),
    -- ============================================================
    -- TG/2023/1781 (Proper - Kamal Fernando) - misses some exams (NULL marks)
    -- ============================================================
    -- ICT2142
    (
        'MK000000048',
        'TG/2023/1781',
        'ICT2142',
        'ASST',
        65.00
    ),
    (
        'MK000000049',
        'TG/2023/1781',
        'ICT2142',
        'FIN',
        NULL
    ),
    -- missed final
    (
        'MK000000050',
        'TG/2023/1781',
        'ICT2142',
        'QU01',
        70.00
    ),
    (
        'MK000000051',
        'TG/2023/1781',
        'ICT2142',
        'QU02',
        NULL
    ),
    -- missed quiz
    (
        'MK000000052',
        'TG/2023/1781',
        'ICT2142',
        'QU03',
        68.00
    ),
    -- ICT2122
    (
        'MK000000053',
        'TG/2023/1781',
        'ICT2122',
        'ASST',
        60.00
    ),
    (
        'MK000000054',
        'TG/2023/1781',
        'ICT2122',
        'MID',
        55.00
    ),
    (
        'MK000000055',
        'TG/2023/1781',
        'ICT2122',
        'FIN',
        62.00
    ),
    (
        'MK000000056',
        'TG/2023/1781',
        'ICT2122',
        'QU01',
        58.00
    ),
    (
        'MK000000057',
        'TG/2023/1781',
        'ICT2122',
        'QU02',
        63.00
    ),
    (
        'MK000000058',
        'TG/2023/1781',
        'ICT2122',
        'QU03',
        NULL
    ),
    -- missed
    -- ICT2132
    (
        'MK000000059',
        'TG/2023/1781',
        'ICT2132',
        'ASST',
        70.00
    ),
    (
        'MK000000060',
        'TG/2023/1781',
        'ICT2132',
        'MID',
        65.00
    ),
    (
        'MK000000061',
        'TG/2023/1781',
        'ICT2132',
        'FIN',
        NULL
    ),
    -- missed final practical
    (
        'MK000000062',
        'TG/2023/1781',
        'ICT2132',
        'QU01',
        72.00
    ),
    (
        'MK000000063',
        'TG/2023/1781',
        'ICT2132',
        'QU02',
        68.00
    ),
    (
        'MK000000064',
        'TG/2023/1781',
        'ICT2132',
        'QU03',
        71.00
    ),
    -- ICT2152
    (
        'MK000000065',
        'TG/2023/1781',
        'ICT2152',
        'ASST',
        58.00
    ),
    (
        'MK000000066',
        'TG/2023/1781',
        'ICT2152',
        'MID',
        52.00
    ),
    (
        'MK000000067',
        'TG/2023/1781',
        'ICT2152',
        'FIN',
        60.00
    ),
    (
        'MK000000068',
        'TG/2023/1781',
        'ICT2152',
        'QU01',
        55.00
    ),
    (
        'MK000000069',
        'TG/2023/1781',
        'ICT2152',
        'QU02',
        57.00
    ),
    (
        'MK000000070',
        'TG/2023/1781',
        'ICT2152',
        'QU03',
        59.00
    ),
    -- ENG2122
    (
        'MK000000071',
        'TG/2023/1781',
        'ENG2122',
        'ASST',
        72.00
    ),
    (
        'MK000000072',
        'TG/2023/1781',
        'ENG2122',
        'MID',
        68.00
    ),
    (
        'MK000000073',
        'TG/2023/1781',
        'ENG2122',
        'FIN',
        70.00
    ),
    (
        'MK000000074',
        'TG/2023/1781',
        'ENG2122',
        'QU01',
        65.00
    ),
    (
        'MK000000075',
        'TG/2023/1781',
        'ENG2122',
        'QU02',
        67.00
    ),
    (
        'MK000000076',
        'TG/2023/1781',
        'ENG2122',
        'QU03',
        69.00
    ),
    -- ICT2113
    (
        'MK000000077',
        'TG/2023/1781',
        'ICT2113',
        'ASST',
        55.00
    ),
    (
        'MK000000078',
        'TG/2023/1781',
        'ICT2113',
        'MID',
        NULL
    ),
    -- missed
    (
        'MK000000079',
        'TG/2023/1781',
        'ICT2113',
        'FIN',
        58.00
    ),
    (
        'MK000000080',
        'TG/2023/1781',
        'ICT2113',
        'QU01',
        60.00
    ),
    (
        'MK000000081',
        'TG/2023/1781',
        'ICT2113',
        'QU02',
        56.00
    ),
    (
        'MK000000082',
        'TG/2023/1781',
        'ICT2113',
        'QU03',
        62.00
    ),
    -- TCS2112
    (
        'MK000000083',
        'TG/2023/1781',
        'TCS2112',
        'ASST',
        63.00
    ),
    (
        'MK000000084',
        'TG/2023/1781',
        'TCS2112',
        'MID',
        59.00
    ),
    (
        'MK000000085',
        'TG/2023/1781',
        'TCS2112',
        'FIN',
        65.00
    ),
    (
        'MK000000086',
        'TG/2023/1781',
        'TCS2112',
        'QU01',
        61.00
    ),
    (
        'MK000000087',
        'TG/2023/1781',
        'TCS2112',
        'QU02',
        64.00
    ),
    (
        'MK000000088',
        'TG/2023/1781',
        'TCS2112',
        'QU03',
        62.00
    ),
    -- TCS2122
    (
        'MK000000089',
        'TG/2023/1781',
        'TCS2122',
        'ASST',
        68.00
    ),
    (
        'MK000000090',
        'TG/2023/1781',
        'TCS2122',
        'MID',
        64.00
    ),
    (
        'MK000000091',
        'TG/2023/1781',
        'TCS2122',
        'FIN',
        66.00
    ),
    (
        'MK000000092',
        'TG/2023/1781',
        'TCS2122',
        'QU01',
        62.00
    ),
    (
        'MK000000093',
        'TG/2023/1781',
        'TCS2122',
        'QU02',
        NULL
    ),
    -- missed
    (
        'MK000000094',
        'TG/2023/1781',
        'TCS2122',
        'QU03',
        67.00
    ),
    -- ============================================================
    -- TG/2022/1785 (Repeat - Dinuka Fernando) - 2 courses, low marks (repeat reason)
    -- ============================================================
    -- ICT2142
    (
        'MK000000095',
        'TG/2022/1785',
        'ICT2142',
        'ASST',
        45.00
    ),
    (
        'MK000000096',
        'TG/2022/1785',
        'ICT2142',
        'FIN',
        38.00
    ),
    (
        'MK000000097',
        'TG/2022/1785',
        'ICT2142',
        'QU01',
        42.00
    ),
    (
        'MK000000098',
        'TG/2022/1785',
        'ICT2142',
        'QU02',
        40.00
    ),
    (
        'MK000000099',
        'TG/2022/1785',
        'ICT2142',
        'QU03',
        44.00
    ),
    -- TCS2122
    (
        'MK000000100',
        'TG/2022/1785',
        'TCS2122',
        'ASST',
        50.00
    ),
    (
        'MK000000101',
        'TG/2022/1785',
        'TCS2122',
        'MID',
        43.00
    ),
    (
        'MK000000102',
        'TG/2022/1785',
        'TCS2122',
        'FIN',
        41.00
    ),
    (
        'MK000000103',
        'TG/2022/1785',
        'TCS2122',
        'QU01',
        48.00
    ),
    (
        'MK000000104',
        'TG/2022/1785',
        'TCS2122',
        'QU02',
        46.00
    ),
    (
        'MK000000105',
        'TG/2022/1785',
        'TCS2122',
        'QU03',
        44.00
    ),
    -- ============================================================
    -- TG/2021/1790 (Suspended - Supun Silva) - 3 courses, very poor/incomplete marks
    -- ============================================================
    -- ICT2122
    (
        'MK000000106',
        'TG/2021/1790',
        'ICT2122',
        'ASST',
        30.00
    ),
    (
        'MK000000107',
        'TG/2021/1790',
        'ICT2122',
        'MID',
        NULL
    ),
    -- missed
    (
        'MK000000108',
        'TG/2021/1790',
        'ICT2122',
        'FIN',
        25.00
    ),
    (
        'MK000000109',
        'TG/2021/1790',
        'ICT2122',
        'QU01',
        35.00
    ),
    (
        'MK000000110',
        'TG/2021/1790',
        'ICT2122',
        'QU02',
        NULL
    ),
    -- missed
    (
        'MK000000111',
        'TG/2021/1790',
        'ICT2122',
        'QU03',
        28.00
    ),
    -- ENG2122
    (
        'MK000000112',
        'TG/2021/1790',
        'ENG2122',
        'ASST',
        40.00
    ),
    (
        'MK000000113',
        'TG/2021/1790',
        'ENG2122',
        'MID',
        32.00
    ),
    (
        'MK000000114',
        'TG/2021/1790',
        'ENG2122',
        'FIN',
        NULL
    ),
    -- missed final
    (
        'MK000000115',
        'TG/2021/1790',
        'ENG2122',
        'QU01',
        38.00
    ),
    (
        'MK000000116',
        'TG/2021/1790',
        'ENG2122',
        'QU02',
        35.00
    ),
    (
        'MK000000117',
        'TG/2021/1790',
        'ENG2122',
        'QU03',
        NULL
    ),
    -- missed
    -- ICT2142
    (
        'MK000000118',
        'TG/2021/1790',
        'ICT2142',
        'ASST',
        28.00
    ),
    (
        'MK000000119',
        'TG/2021/1790',
        'ICT2142',
        'FIN',
        NULL
    ),
    -- missed final
    (
        'MK000000120',
        'TG/2021/1790',
        'ICT2142',
        'QU01',
        33.00
    ),
    (
        'MK000000121',
        'TG/2021/1790',
        'ICT2142',
        'QU02',
        NULL
    ),
    -- missed
    (
        'MK000000122',
        'TG/2021/1790',
        'ICT2142',
        'QU03',
        30.00
    );
INSERT INTO attendance (
        attendance_id,
        stu_id,
        course_code,
        session_date,
        status
    )
VALUES (
        'ATT001',
        'TG/2023/1780',
        'ICT2142',
        '2026-04-01',
        'Present'
    ),
    (
        'ATT002',
        'TG/2023/1780',
        'ICT2142',
        '2026-04-08',
        'Present'
    ),
    (
        'ATT003',
        'TG/2023/1780',
        'ICT2142',
        '2026-04-15',
        'Absent'
    ),
    (
        'ATT004',
        'TG/2023/1780',
        'ICT2142',
        '2026-04-22',
        'Present'
    ),
    (
        'ATT005',
        'TG/2023/1781',
        'ICT2142',
        '2026-04-01',
        'Present'
    ),
    (
        'ATT006',
        'TG/2023/1781',
        'ICT2142',
        '2026-04-08',
        'Absent'
    ),
    (
        'ATT007',
        'TG/2023/1781',
        'ICT2142',
        '2026-04-15',
        'Present'
    ),
    (
        'ATT008',
        'TG/2023/1781',
        'ICT2142',
        '2026-04-22',
        'Present'
    ),
    (
        'ATT009',
        'TG/2022/1785',
        'ICT2142',
        '2026-04-01',
        'Absent'
    ),
    (
        'ATT010',
        'TG/2022/1785',
        'ICT2142',
        '2026-04-08',
        'Absent'
    ),
    (
        'ATT011',
        'TG/2022/1785',
        'ICT2142',
        '2026-04-15',
        'Present'
    ),
    (
        'ATT012',
        'TG/2021/1790',
        'ICT2142',
        '2026-04-01',
        'Present'
    ),
    (
        'ATT013',
        'TG/2021/1790',
        'ICT2142',
        '2026-04-08',
        'Absent'
    );
INSERT INTO attendance (
        attendance_id,
        stu_id,
        course_code,
        session_date,
        status
    )
VALUES (
        'ATT014',
        'TG/2023/1780',
        'ICT2122',
        '2026-04-02',
        'Present'
    ),
    (
        'ATT015',
        'TG/2023/1780',
        'ICT2122',
        '2026-04-09',
        'Present'
    ),
    (
        'ATT016',
        'TG/2023/1780',
        'ICT2122',
        '2026-04-16',
        'Present'
    ),
    (
        'ATT017',
        'TG/2023/1781',
        'ICT2122',
        '2026-04-02',
        'Absent'
    ),
    (
        'ATT018',
        'TG/2023/1781',
        'ICT2122',
        '2026-04-09',
        'Present'
    ),
    (
        'ATT019',
        'TG/2023/1781',
        'ICT2122',
        '2026-04-16',
        'Present'
    ),
    (
        'ATT020',
        'TG/2021/1790',
        'ICT2122',
        '2026-04-02',
        'Present'
    ),
    (
        'ATT021',
        'TG/2021/1790',
        'ICT2122',
        '2026-04-09',
        'Absent'
    );
INSERT INTO attendance (
        attendance_id,
        stu_id,
        course_code,
        session_date,
        status
    )
VALUES (
        'ATT022',
        'TG/2023/1781',
        'ICT2113',
        '2026-04-03',
        'Present'
    ),
    (
        'ATT023',
        'TG/2023/1781',
        'ICT2113',
        '2026-04-10',
        'Present'
    ),
    (
        'ATT024',
        'TG/2023/1781',
        'ICT2113',
        '2026-04-17',
        'Absent'
    ),
    (
        'ATT025',
        'TG/2023/1780',
        'ICT2113',
        '2026-04-03',
        'Absent'
    ),
    (
        'ATT026',
        'TG/2023/1780',
        'ICT2113',
        '2026-04-10',
        'Present'
    ),
    (
        'ATT027',
        'TG/2023/1780',
        'ICT2113',
        '2026-04-17',
        'Present'
    );
INSERT INTO attendance (
        attendance_id,
        stu_id,
        course_code,
        session_date,
        status
    )
VALUES (
        'ATT028',
        'TG/2023/1780',
        'ENG2122',
        '2026-04-05',
        'Present'
    ),
    (
        'ATT029',
        'TG/2023/1780',
        'ENG2122',
        '2026-04-12',
        'Present'
    ),
    (
        'ATT030',
        'TG/2023/1781',
        'ENG2122',
        '2026-04-05',
        'Absent'
    ),
    (
        'ATT031',
        'TG/2023/1781',
        'ENG2122',
        '2026-04-12',
        'Present'
    ),
    (
        'ATT032',
        'TG/2021/1790',
        'ENG2122',
        '2026-04-05',
        'Absent'
    ),
    (
        'ATT033',
        'TG/2021/1790',
        'ENG2122',
        '2026-04-12',
        'Absent'
    );
INSERT INTO attendance (
        attendance_id,
        stu_id,
        course_code,
        session_date,
        status
    )
VALUES (
        'ATT034',
        'TG/2023/1780',
        'TCS2112',
        '2026-04-06',
        'Present'
    ),
    (
        'ATT035',
        'TG/2023/1780',
        'TCS2112',
        '2026-04-13',
        'Present'
    ),
    (
        'ATT036',
        'TG/2023/1781',
        'TCS2112',
        '2026-04-06',
        'Absent'
    ),
    (
        'ATT037',
        'TG/2023/1781',
        'TCS2112',
        '2026-04-13',
        'Present'
    );
INSERT INTO attendance (
        attendance_id,
        stu_id,
        course_code,
        session_date,
        status
    )
VALUES (
        'ATT040',
        'TG/2023/1780',
        'TCS2122',
        '2026-04-07',
        'Present'
    ),
    (
        'ATT041',
        'TG/2023/1780',
        'TCS2122',
        '2026-04-14',
        'Present'
    ),
    (
        'ATT042',
        'TG/2023/1781',
        'TCS2122',
        '2026-04-07',
        'Absent'
    ),
    (
        'ATT043',
        'TG/2023/1781',
        'TCS2122',
        '2026-04-14',
        'Absent'
    ),
    (
        'ATT044',
        'TG/2022/1785',
        'TCS2122',
        '2026-04-07',
        'Present'
    ),
    (
        'ATT045',
        'TG/2022/1785',
        'TCS2122',
        '2026-04-14',
        'Absent'
    );
INSERT INTO medical (
        ref_no,
        stu_id,
        reason,
        status,
        start_date,
        end_date
    )
VALUES (
        'REF001',
        'TG/2023/1780',
        'Flu and fever',
        'Approved',
        '2026-04-02',
        '2026-04-04'
    ),
    (
        'REF002',
        'TG/2023/1781',
        'Migraine headache',
        'Pending',
        '2026-04-05',
        '2026-04-06'
    ),
    (
        'REF003',
        'TG/2022/1785',
        'Dengue suspicion',
        'Approved',
        '2026-04-01',
        '2026-04-07'
    ),
    (
        'REF004',
        'TG/2021/1790',
        'Chronic illness checkup',
        'Approved',
        '2026-04-01',
        '2026-04-10'
    ),
    (
        'REF005',
        'TG/2023/1781',
        'Viral fever',
        'Rejected',
        '2026-04-08',
        '2026-04-10'
    ),
    (
        'REF006',
        'TG/2022/1785',
        'Back pain',
        'Approved',
        '2026-04-11',
        '2026-04-13'
    );
INSERT INTO medical_attendance (attendance_id, ref_no)
VALUES ('ATT003', 'REF001'),
    ('ATT006', 'REF002'),
    ('ATT011', 'REF003'),
    ('ATT013', 'REF004'),
    ('ATT030', 'REF005'),
    ('ATT009', 'REF006'),
    ('ATT010', 'REF006');
INSERT INTO notice (notice_id, admin_id, title, date)
VALUES (
        'NTC001',
        'AD001',
        'Semester Registration Started',
        '2026-04-01'
    ),
    (
        'NTC002',
        'AD001',
        'Library Closed on Public Holiday',
        '2026-04-03'
    ),
    (
        'NTC003',
        'AD001',
        'Mid Exam Timetable Released',
        '2026-04-05'
    ),
    (
        'NTC004',
        'AD001',
        'Attendance Warning Notice Issued',
        '2026-04-07'
    ),
    (
        'NTC005',
        'AD001',
        'Final Project Submission Guidelines',
        '2026-04-10'
    );
-- Guard data for safe partial reruns (prevents FK errors in timetable/lecturer_course).
INSERT IGNORE INTO lecturer (lec_id, designation)
SELECT id,
    'Lecturer'
FROM users
WHERE user_type = 'Lecturer';
INSERT IGNORE INTO course_unit (course_code, title, credit)
VALUES (
        'ICT2142',
        'Object Oriented Analysis and Design',
        2
    ),
    ('ICT2122', 'Object Oriented Programming', 2),
    (
        'ICT2132',
        'Object Oriented Programming Practicum',
        2
    ),
    (
        'ICT2152',
        'E-Commerce Implementation, Management and Security',
        2
    ),
    ('ENG2122', 'English III', 2),
    ('ICT2113', 'Data Structures and Algorithms', 3),
    ('TCS2112', 'Business Economics', 2),
    ('TCS2122', 'Soft Skills', 1);
INSERT INTO timetable (
        timetable_id,
        admin_id,
        lec_id,
        course_code,
        location,
        level,
        type,
        hours
    )
VALUES (
        'TT001',
        'AD001',
        'LEC001',
        'ICT2142',
        'Lab 1',
        1,
        'Theory',
        2
    ),
    (
        'TT002',
        'AD001',
        'LEC001',
        'ICT2122',
        'Lecture Hall A',
        1,
        'Theory',
        2
    ),
    (
        'TT003',
        'AD001',
        'LEC001',
        'ICT2132',
        'Lab 2',
        1,
        'Practical',
        3
    ),
    (
        'TT004',
        'AD001',
        'LEC001',
        'ICT2152',
        'Lecture Hall B',
        1,
        'Theory',
        2
    ),
    (
        'TT005',
        'AD001',
        'LEC001',
        'ENG2122',
        'Hall C',
        2,
        'Theory',
        2
    ),
    (
        'TT006',
        'AD001',
        'LEC001',
        'ICT2113',
        'Lab 1',
        2,
        'Practical',
        3
    ),
    (
        'TT007',
        'AD001',
        'LEC001',
        'TCS2112',
        'Lecture Hall A',
        2,
        'Theory',
        2
    ),
    (
        'TT008',
        'AD001',
        'LEC001',
        'TCS2122',
        'Lab 3',
        2,
        'Practical',
        3
    ),
    (
        'TT009',
        'AD001',
        'LEC001',
        'ICT2142',
        'Lab 2',
        2,
        'Theory',
        2
    ),
    (
        'TT010',
        'AD001',
        'LEC001',
        'ICT2122',
        'Lecture Hall B',
        2,
        'Theory',
        2
    ),
    (
        'TT011',
        'AD001',
        'LEC001',
        'ICT2132',
        'Lab 1',
        2,
        'Practical',
        3
    ),
    (
        'TT012',
        'AD001',
        'LEC001',
        'ICT2152',
        'Hall C',
        2,
        'Theory',
        2
    ),
    (
        'TT013',
        'AD001',
        'LEC001',
        'ENG2122',
        'Lecture Hall A',
        3,
        'Theory',
        2
    ),
    (
        'TT014',
        'AD001',
        'LEC001',
        'ICT2113',
        'Lab 1',
        3,
        'Practical',
        3
    ),
    (
        'TT015',
        'AD001',
        'LEC001',
        'TCS2112',
        'Lecture Hall B',
        3,
        'Theory',
        2
    ),
    (
        'TT016',
        'AD001',
        'LEC001',
        'TCS2122',
        'Lab 2',
        3,
        'Practical',
        3
    ),
    (
        'TT017',
        'AD001',
        'LEC001',
        'ICT2142',
        'Lecture Hall C',
        3,
        'Theory',
        2
    ),
    (
        'TT018',
        'AD001',
        'LEC001',
        'ICT2132',
        'Lab 3',
        3,
        'Practical',
        3
    );
INSERT INTO lecturer_course (lec_id, course_code)
VALUES ('LEC001', 'ICT2142'),
    ('LEC001', 'ICT2122'),
    ('LEC001', 'ICT2132'),
    ('LEC001', 'ICT2152'),
    ('LEC001', 'ENG2122'),
    ('LEC001', 'ICT2113'),
    ('LEC001', 'TCS2112'),
    ('LEC001', 'TCS2122');