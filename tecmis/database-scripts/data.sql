INSERT INTO users (id, f_name, l_name, email, contact_no, hash_pwd, user_type)
VALUES
    ('AD001', 'Ravindra', 'Perera', 'ravindra@ruh.ac.lk', '0771234567', 'd4c3b2a1f6e5d4c3b2a1f0e9d8c7b6a5', 'Admin'),
    ('LEC001', 'Nishantha', 'Fernando', 'nishantha@ruh.ac.lk', '0771111111', 'a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6', 'Lecturer'),
    ('TO001', 'Ameer', 'Faisal', 'ameer@ruh.ac.lk', '0711111111', 'f1e2d3c4b5a6f7e8d9c0b1a2f3e4d5c6', 'TechnicalOfficer'),
    ('TG/2023/1780', 'Saman', 'Perera', 'saman.perera@gmail.com', '0750000001', 'd4c3b2a1f6e5d4c3b2a1f0e9d8c7b6a5', 'Undergraduate'),
    ('TG/2023/1781', 'Kamal', 'Silva', 'kamal.silva@gmail.com', '0750000002', 'a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0', 'Undergraduate'),
    ('TG/2022/1785', 'Dinuka', 'Fernando', 'dinuka.fernando@gmail.com', '0750000006', 'e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0', 'Undergraduate');





INSERT INTO admin (admin_id) VALUES ('AD001'); 


INSERT INTO lecturer (lec_id, designation)
VALUES
('LEC001','Lecturer');


INSERT INTO technical_officer (to_id)
VALUES ('TO001'), ('TO002'), ('TO003'), ('TO004');


INSERT INTO undergraduate (stu_id, status, mentor_id)
VALUES
    ('TG/2023/1780', 'Proper', 'LEC001'),
    ('TG/2023/1781', 'Proper', 'LEC001'),
    ('TG/2022/1785', 'Repeat', 'LEC001');




INSERT INTO course_unit (course_code, title, credit)
VALUES
('ICT2142','Object Oriented Analysis and Design',2),
('ICT2122','Object Oriented Programming',2),
('ICT2132','Object Oriented Programming Practicum',2),
('ICT2152','E-Commerce Implementation, Management and Security',2),
('ENG2122','English III',2),
('ICT2113','Data Structures and Algorithms',3),
('TCS2112','Business Economics',2),
('TCS2122','Soft Skills',1);

INSERT INTO enrollment (stu_id, course_code)
VALUES
    ('TG/2023/1780', 'ICT2142'),
    ('TG/2023/1780', 'ICT2122'),
    ('TG/2023/1780', 'ICT2132'),
    ('TG/2023/1780', 'ICT2152'),
    ('TG/2023/1780', 'ENG2122'),
    ('TG/2023/1780', 'ICT2113'),
    ('TG/2023/1780', 'TCS2112'),
    ('TG/2023/1780', 'TCS2122'),
    ('TG/2023/1781', 'ICT2142'),
    ('TG/2023/1781', 'ICT2122'),
    ('TG/2023/1781', 'ICT2132'),
    ('TG/2023/1781', 'ICT2152'),
    ('TG/2023/1781', 'ENG2122'),
    ('TG/2023/1781', 'ICT2113'),
    ('TG/2023/1781', 'TCS2112'),
    ('TG/2023/1781', 'TCS2122'),
    ('TG/2022/1785', 'ICT2142'),
    ('TG/2022/1785', 'TCS2122');


INSERT INTO exam_type (type_id, type_name, weight)
VALUES
    ('ASST', 'Assessment', 0.05),
    ('FINP', 'Final Practical', 0.60),
    ('FINT', 'Final Theory', 0.10),
    ('MIDP', 'Mid Practical', 0.10),
    ('MIDT', 'Mid Theory', 0.10),
    ('QU01', 'Quiz 01', 0.05),
    ('QU02', 'Quiz 02', 0.05),
    ('QU03', 'Quiz 03', 0.05);


INSERT INTO marks (mark_id, stu_id, course_code, type_id, mark)
VALUES
    ('M001', 'TG/2023/1780', 'ICT2142', 'ASST', 85.50),
    ('M002', 'TG/2023/1780', 'ICT2142', 'MIDT', 78.00),
    ('M003', 'TG/2023/1780', 'ICT2142', 'MIDP', 82.00),
    ('M004', 'TG/2023/1780', 'ICT2142', 'FINT', 75.50),
    ('M005', 'TG/2023/1780', 'ICT2142', 'FINP', 80.00),
    ('M006', 'TG/2023/1780', 'ICT2142', 'QU01', 90.00),
    ('M007', 'TG/2023/1780', 'ICT2142', 'QU02', 88.00),
    ('M008', 'TG/2023/1780', 'ICT2142', 'QU03', 85.00),

    ('M009', 'TG/2023/1780', 'ICT2122', 'ASST', 75.00),
    ('M010', 'TG/2023/1780', 'ICT2122', 'MIDT', 70.00),
    ('M011', 'TG/2023/1780', 'ICT2122', 'MIDP', 72.00),
    ('M012', 'TG/2023/1780', 'ICT2122', 'FINT', 68.00),
    ('M013', 'TG/2023/1780', 'ICT2122', 'FINP', 74.00),
    ('M014', 'TG/2023/1780', 'ICT2122', 'QU01', 80.00),
    ('M015', 'TG/2023/1780', 'ICT2122', 'QU02', 78.00),

    ('M016', 'TG/2023/1780', 'ICT2132', 'ASST', 88.00),
    ('M017', 'TG/2023/1780', 'ICT2132', 'MIDP', 85.00),
    ('M018', 'TG/2023/1780', 'ICT2132', 'FINP', 90.00),

    ('M019', 'TG/2023/1780', 'ICT2152', 'ASST', 82.00),
    ('M020', 'TG/2023/1780', 'ICT2152', 'MIDT', 76.00),
    ('M021', 'TG/2023/1780', 'ICT2152', 'FINT', 79.00),

    ('M022', 'TG/2023/1780', 'ENG2122', 'ASST', 78.00),
    ('M023', 'TG/2023/1780', 'ENG2122', 'MIDT', 74.00),
    ('M024', 'TG/2023/1780', 'ENG2122', 'FINT', 72.00),

    ('M025', 'TG/2023/1780', 'ICT2113', 'ASST', 70.00),
    ('M026', 'TG/2023/1780', 'ICT2113', 'MIDT', 65.00),
    ('M027', 'TG/2023/1780', 'ICT2113', 'MIDP', 68.00),
    ('M028', 'TG/2023/1780', 'ICT2113', 'FINT', 60.00),
    ('M029', 'TG/2023/1780', 'ICT2113', 'QU01', 75.00),

    ('M030', 'TG/2023/1780', 'TCS2112', 'ASST', 85.00),
    ('M031', 'TG/2023/1780', 'TCS2112', 'MIDT', 82.00),
    ('M032', 'TG/2023/1780', 'TCS2112', 'FINT', 80.00),

    ('M033', 'TG/2023/1780', 'TCS2122', 'ASST', 90.00),
    ('M034', 'TG/2023/1780', 'TCS2122', 'MIDT', 88.00),
    ('M035', 'TG/2023/1780', 'TCS2122', 'FINT', 92.00),

    ('M036', 'TG/2023/1781', 'ICT2142', 'ASST', 65.00),
    ('M037', 'TG/2023/1781', 'ICT2142', 'MIDT', 60.00),
    ('M038', 'TG/2023/1781', 'ICT2142', 'FINT', 58.00),
    ('M039', 'TG/2023/1781', 'ICT2142', 'QU01', 70.00),

    ('M040', 'TG/2023/1781', 'ICT2122', 'ASST', 72.00),
    ('M041', 'TG/2023/1781', 'ICT2122', 'MIDT', 68.00),
    ('M042', 'TG/2023/1781', 'ICT2122', 'FINT', 70.00),

    ('M043', 'TG/2023/1781', 'ICT2132', 'ASST', 60.00),
    ('M044', 'TG/2023/1781', 'ICT2132', 'MIDP', 55.00),

    ('M045', 'TG/2023/1781', 'ICT2152', 'ASST', 68.00),
    ('M046', 'TG/2023/1781', 'ICT2152', 'MIDT', 62.00),

    ('M047', 'TG/2023/1781', 'ENG2122', 'ASST', 70.00),

    ('M048', 'TG/2023/1781', 'ICT2113', 'ASST', 55.00),
    ('M049', 'TG/2023/1781', 'ICT2113', 'MIDT', 50.00),

    ('M050', 'TG/2023/1781', 'TCS2112', 'ASST', 75.00),
    ('M051', 'TG/2023/1781', 'TCS2112', 'MIDT', 70.00),

    ('M052', 'TG/2023/1781', 'TCS2122', 'ASST', 80.00),

    ('M053', 'TG/2022/1785', 'ICT2142', 'ASST', 45.00),
    ('M054', 'TG/2022/1785', 'ICT2142', 'MIDT', 40.00),
    ('M055', 'TG/2022/1785', 'ICT2142', 'FINT', 35.00),

    ('M056', 'TG/2022/1785', 'TCS2122', 'ASST', 50.00),
    ('M057', 'TG/2022/1785', 'TCS2122', 'MIDT', 48.00);









