INSERT INTO users (id, f_name, l_name, email, contact_no, hash_pwd, user_type)
VALUES
    ('AD001', 'Ravindra', 'Perera', 'ravindra@ruh.ac.lk', '0771234567', 'd4c3b2a1f6e5d4c3b2a1f0e9d8c7b6a5', 'Admin'),
    ('LEC001', 'Nishantha', 'Fernando', 'nishantha@ruh.ac.lk', '0771111111', 'a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6', 'Lecturer'),
    ('TO001', 'Ameer', 'Faisal', 'ameer@ruh.ac.lk', '0711111111', 'f1e2d3c4b5a6f7e8d9c0b1a2f3e4d5c6', 'TechnicalOfficer'),
    ('TG/2023/1780', 'Saman', 'Perera', 'saman.perera@gmail.com', '0750000001', 'd4c3b2a1f6e5d4c3b2a1f0e9d8c7b6a5', 'Undergraduate'),
    ('TG/2023/1781', 'Kamal', 'Silva', 'kamal.silva@gmail.com', '0750000002', 'a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0', 'Undergraduate'),
    ('TG/2022/1785', 'Dinuka', 'Fernando', 'dinuka.fernando@gmail.com', '0750000006', 'e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0', 'Undergraduate');





INSERT INTO admin (admin_id)
VALUES ('AD001'); 


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

('TG/2023/1780','ICT2142'),
('TG/2023/1780','ICT2122'),
('TG/2023/1780','ENG2122'),

('TG/2023/1781','ICT2142'),
('TG/2023/1781','ICT2132'),
('TG/2023/1781','ICT2113'),

('TG/2023/1782','ICT2122'),
('TG/2023/1782','ICT2152'),
('TG/2023/1782','TCS2112'),

('TG/2023/1783','ENG2122'),
('TG/2023/1783','ICT2113'),
('TG/2023/1783','TCS2122'),

('TG/2023/1784','ICT2142'),
('TG/2023/1784','ICT2122'),
('TG/2023/1784','ICT2113'),


('TG/2022/1785','ICT2142'),
('TG/2022/1785','ICT2122'),

('TG/2022/1786','ICT2142'),
('TG/2022/1786','ICT2132'),

('TG/2022/1787','ICT2113'),
('TG/2022/1787','ICT2152'),

('TG/2022/1788','ICT2142'),
('TG/2022/1788','ENG2122'),

('TG/2022/1789','TCS2112'),
('TG/2022/1789','TCS2122'),


('TG/2021/1790','ICT2122'),
('TG/2021/1790','ENG2122'),

('TG/2021/1791','ICT2113'),
('TG/2021/1791','ICT2142'),

('TG/2021/1792','TCS2112'),
('TG/2021/1792','TCS2122'),

('TG/2021/1793','ICT2132'),
('TG/2021/1793','ICT2122'),

('TG/2021/1794','ICT2152'),
('TG/2021/1794','ENG2122'); 





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




INSERT INTO attendance (attendance_id, stu_id, course_code, session_date, status)
VALUES

('ATT001','TG/2023/1780','ICT2142','2026-04-01','Present'),
('ATT002','TG/2023/1780','ICT2142','2026-04-08','Present'),
('ATT003','TG/2023/1780','ICT2142','2026-04-15','Absent'),
('ATT004','TG/2023/1780','ICT2142','2026-04-22','Present'),

('ATT005','TG/2023/1781','ICT2142','2026-04-01','Present'),
('ATT006','TG/2023/1781','ICT2142','2026-04-08','Absent'),
('ATT007','TG/2023/1781','ICT2142','2026-04-15','Present'),
('ATT008','TG/2023/1781','ICT2142','2026-04-22','Present'),

('ATT009','TG/2022/1785','ICT2142','2026-04-01','Absent'),
('ATT010','TG/2022/1785','ICT2142','2026-04-08','Absent'),
('ATT011','TG/2022/1785','ICT2142','2026-04-15','Present'),

('ATT012','TG/2021/1791','ICT2142','2026-04-01','Present'),
('ATT013','TG/2021/1791','ICT2142','2026-04-08','Absent'),


('ATT014','TG/2023/1780','ICT2122','2026-04-02','Present'),
('ATT015','TG/2023/1780','ICT2122','2026-04-09','Present'),
('ATT016','TG/2023/1780','ICT2122','2026-04-16','Present'),

('ATT017','TG/2023/1782','ICT2122','2026-04-02','Absent'),
('ATT018','TG/2023/1782','ICT2122','2026-04-09','Present'),
('ATT019','TG/2023/1782','ICT2122','2026-04-16','Present'),

('ATT020','TG/2022/1786','ICT2122','2026-04-02','Present'),
('ATT021','TG/2022/1786','ICT2122','2026-04-09','Absent'),


('ATT022','TG/2023/1781','ICT2113','2026-04-03','Present'),
('ATT023','TG/2023/1781','ICT2113','2026-04-10','Present'),
('ATT024','TG/2023/1781','ICT2113','2026-04-17','Absent'),

('ATT025','TG/2023/1783','ICT2113','2026-04-03','Absent'),
('ATT026','TG/2023/1783','ICT2113','2026-04-10','Present'),
('ATT027','TG/2023/1783','ICT2113','2026-04-17','Present'),


('ATT028','TG/2023/1783','ENG2122','2026-04-05','Present'),
('ATT029','TG/2023/1783','ENG2122','2026-04-12','Present'),
('ATT030','TG/2023/1784','ENG2122','2026-04-05','Absent'),
('ATT031','TG/2023/1784','ENG2122','2026-04-12','Present'),

('ATT032','TG/2022/1785','ENG2122','2026-04-05','Absent'),
('ATT033','TG/2022/1785','ENG2122','2026-04-12','Absent'),


('ATT034','TG/2023/1782','TCS2112','2026-04-06','Present'),
('ATT035','TG/2023/1782','TCS2112','2026-04-13','Present'),

('ATT036','TG/2022/1789','TCS2112','2026-04-06','Absent'),
('ATT037','TG/2022/1789','TCS2112','2026-04-13','Present'),

('ATT038','TG/2021/1792','TCS2112','2026-04-06','Present'),
('ATT039','TG/2021/1792','TCS2112','2026-04-13','Absent'),


('ATT040','TG/2023/1783','TCS2122','2026-04-07','Present'),
('ATT041','TG/2023/1783','TCS2122','2026-04-14','Present'),

('ATT042','TG/2022/1789','TCS2122','2026-04-07','Absent'),
('ATT043','TG/2022/1789','TCS2122','2026-04-14','Absent'),

('ATT044','TG/2021/1793','TCS2122','2026-04-07','Present'),
('ATT045','TG/2021/1793','TCS2122','2026-04-14','Absent');  




INSERT INTO medical (ref_no, stu_id, reason, status, start_date, end_date)
VALUES

('REF001','TG/2023/1780','Flu and fever','Approved','2026-04-02','2026-04-04'),
('REF002','TG/2023/1781','Migraine headache','Pending','2026-04-05','2026-04-06'),
('REF003','TG/2023/1782','Food poisoning','Approved','2026-04-10','2026-04-12'),
('REF004','TG/2023/1783','Viral fever','Rejected','2026-04-03','2026-04-05'),
('REF005','TG/2023/1784','Sprained ankle','Approved','2026-04-08','2026-04-10'),


('REF006','TG/2022/1785','Dengue suspicion','Approved','2026-04-01','2026-04-07'),
('REF007','TG/2022/1786','Severe cold','Pending','2026-04-06','2026-04-08'),
('REF008','TG/2022/1787','Hospital admission','Approved','2026-04-09','2026-04-15'),
('REF009','TG/2022/1788','Eye infection','Rejected','2026-04-02','2026-04-03'),
('REF010','TG/2022/1789','Back pain','Approved','2026-04-11','2026-04-13'),


('REF011','TG/2021/1790','Chronic illness checkup','Approved','2026-04-01','2026-04-10'),
('REF012','TG/2021/1791','Surgery recovery','Approved','2026-04-03','2026-04-14'),
('REF013','TG/2021/1792','Migraine','Pending','2026-04-05','2026-04-06'),
('REF014','TG/2021/1793','Accident recovery','Approved','2026-04-07','2026-04-12'),
('REF015','TG/2021/1794','Fever and fatigue','Rejected','2026-04-08','2026-04-09'); 




INSERT INTO medical_attendance (attendance_id, ref_no)
VALUES

('ATT003','REF001'),  
('ATT006','REF002'),  
('ATT011','REF003'),  
('ATT016','REF004'),  
('ATT030','REF005'),  


('ATT009','REF006'),  
('ATT010','REF006'),  

('ATT021','REF007'), 
('ATT023','REF007'),

('ATT032','REF010'),  
('ATT033','REF010'),


('ATT012','REF011'),  
('ATT013','REF011'),

('ATT024','REF012'),  
('ATT025','REF012'),

('ATT036','REF013'), 
('ATT037','REF013'),

('ATT044','REF014'),  
('ATT045','REF014'); 



INSERT INTO notice (notice_id, admin_id, title, date)
VALUES
('NTC001','AD001','Semester Registration Started','2026-04-01'),
('NTC002','AD001','Library Closed on Public Holiday','2026-04-03'),
('NTC003','AD001','Mid Exam Timetable Released','2026-04-05'),
('NTC004','AD001','Attendance Warning Notice Issued','2026-04-07'),
('NTC005','AD001','Final Project Submission Guidelines','2026-04-10'); 



INSERT INTO blog (blog_id, user_id, title, date)
VALUES

('BLG001','LEC001','Introduction to Object Oriented Programming','2026-03-25'),
('BLG002','LEC002','Database Normalization Explained','2026-03-26'),
('BLG003','LEC003','Tips for Data Structures Exam','2026-03-27'),
('BLG004','LEC004','How to Improve Coding Skills','2026-03-28'),
('BLG005','LEC005','Software Engineering Best Practices','2026-03-29'),

('BLG006','TG/2023/1780','My First Programming Project','2026-03-30'),
('BLG007','TG/2023/1781','How I Study for Exams','2026-03-31'),
('BLG008','TG/2023/1782','Campus Life Experience','2026-04-01'),
('BLG009','TG/2022/1785','Challenges in Final Year Projects','2026-04-02'),

('BLG010','AD001','System Maintenance Update','2026-04-03'); 




INSERT INTO notification (notification_id, admin_id, message, date, status)
VALUES
('NOT001','AD001','System maintenance will be held on 12th April', '2026-04-01','Active'),
('NOT002','AD001','New semester registration is now open', '2026-04-02','Active'),
('NOT003','AD001','Lecture cancellation due to public holiday', '2026-04-03','Inactive'),
('NOT004','AD001','Mid exam timetable has been published', '2026-04-05','Active'),
('NOT005','AD001','Attendance below 75% will be restricted', '2026-04-06','Active'),
('NOT006','AD001','Library will be closed for renovation', '2026-04-07','Inactive'),
('NOT007','AD001','Final project submission deadline extended', '2026-04-08','Active'); 




INSERT INTO timetable (timetable_id, admin_id, lec_id, course_code, location, level, type, hours)
VALUES

('TT001','AD001','LEC001','ICT1212','Lab 1',1,'Theory',2),
('TT002','AD001','LEC002','ICT1222','Lecture Hall A',1,'Theory',2),
('TT003','AD001','LEC003','ICT1232','Lab 2',1,'Practical',3),
('TT004','AD001','LEC004','ICT1242','Lecture Hall B',1,'Theory',2),


('TT005','AD001','LEC001','ICT2142','Lab 1',2,'Theory',2),
('TT006','AD001','LEC002','ICT2122','Lecture Hall A',2,'Theory',2),
('TT007','AD001','LEC003','ICT2132','Lab 2',2,'Practical',3),
('TT008','AD001','LEC004','ICT2152','Lecture Hall B',2,'Theory',2),
('TT009','AD001','LEC005','ENG2122','Hall C',2,'Theory',2),
('TT010','AD001','LEC006','ICT2113','Lab 1',2,'Practical',3),
('TT011','AD001','LEC007','TCS2112','Lecture Hall A',2,'Theory',2),
('TT012','AD001','LEC008','TCS2122','Lab 3',2,'Practical',3),


('TT013','AD001','LEC009','TCS3121','Lecture Hall A',3,'Theory',2),
('TT014','AD001','LEC010','ICT3122','Lab 1',3,'Practical',3),
('TT015','AD001','LEC011','ICT3132','Lecture Hall B',3,'Theory',2),
('TT016','AD001','LEC012','TCS3142','Lab 2',3,'Practical',3),
('TT017','AD001','LEC013','ICT3152','Lecture Hall C',3,'Theory',2),
('TT018','AD001','LEC014','TCS3162','Lab 3',3,'Practical',3); 




INSERT INTO event_cal (event_id, user_id, title, description, date, time)
VALUES

('EVT001','AD001','System Maintenance','Server maintenance downtime','2026-04-12','10:00:00'),
('EVT002','AD001','Semester Opening','Welcome session for new semester','2026-04-15','09:00:00'),


('EVT003','LEC001','Extra OOP Lecture','Revision session before mid exam','2026-04-10','14:00:00'),
('EVT004','LEC002','DBMS Workshop','Hands-on database session','2026-04-11','11:00:00'),
('EVT005','LEC003','Practical Lab Session','Extra coding practice','2026-04-13','13:00:00'),


('EVT006','TG/2023/1780','Group Study','Study with friends for exams','2026-04-09','16:00:00'),
('EVT007','TG/2023/1781','Assignment Deadline','Submit DBMS assignment','2026-04-10','23:59:00'),
('EVT008','TG/2023/1782','Presentation Day','Project presentation','2026-04-14','10:00:00'),
('EVT009','TG/2022/1785','Repeat Exam Prep','Prepare for repeat exams','2026-04-08','15:00:00'),
('EVT010','TG/2021/1791','Medical Review','Doctor follow-up appointment','2026-04-07','09:30:00');



INSERT INTO lecturer_course (lec_id, course_code)
VALUES


('LEC001','ICT2142'),
('LEC002','ICT2122'),
('LEC003','ICT2132'),
('LEC004','ICT2152'),
('LEC005','ENG2122'),
('LEC006','ICT2113'),
('LEC007','TCS2112'),
('LEC008','TCS2122'),

('LEC009','ICT1212'),
('LEC010','ICT1222'),
('LEC011','ICT1232'),

('LEC012','TCS3121'),
('LEC013','ICT3122'),
('LEC014','ICT3132'),
('LEC015','TCS3142'),
('LEC016','ICT3152');







