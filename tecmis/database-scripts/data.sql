
INSERT INTO users (id, f_name, l_name, email, contact_no, hash_pwd, user_type)
VALUES
-- Admin
('AD001', 'Ravindra', 'Perera', 'ravindra@ruh.ac.lk', '0771234567', 'd4c3b2a1f6e5d4c3b2a1f0e9d8c7b6a5', 'Admin'),

-- Lecturers
('LEC001', 'Nishantha', 'Fernando', 'nishantha@ruh.ac.lk', '0771111111', 'a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6', 'Lecturer'),
('LEC002', 'Chamila', 'Wijesinghe', 'chamila@ruh.ac.lk', '0772222222', 'b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6', 'Lecturer'),
('LEC003', 'Kavindu', 'Jayasinghe', 'kavindu@ruh.ac.lk', '0773333333', 'c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6', 'Lecturer'),
('LEC004', 'Dilani', 'Senanayake', 'dilani@ruh.ac.lk', '0774444444', 'd1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6', 'Lecturer'),
('LEC005', 'Roshan', 'Kumarasinghe', 'roshan@ruh.ac.lk', '0775555555', 'e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6', 'Lecturer'),
('LEC006', 'Nadeesha', 'De Silva', 'nadeesha@ruh.ac.lk', '0776666666', 'f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6', 'Lecturer'),
('LEC007', 'Chamika', 'Jayawardena', 'chamika@ruh.ac.lk', '0777777777', 'a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7', 'Lecturer'),
('LEC008', 'Kasun', 'Rajapaksha', 'kasun@ruh.ac.lk', '0778888888', 'b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7', 'Lecturer'),

-- Technical Officers
('TO001', 'Ameer', 'Faisal', 'ameer@ruh.ac.lk', '0711111111', 'f1e2d3c4b5a6f7e8d9c0b1a2f3e4d5c6', 'TechnicalOfficer'),
('TO002', 'Tharindu', 'Gunawardena', 'tharindu@ruh.ac.lk', '0722222222', 'a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7', 'TechnicalOfficer'),
('TO003', 'Sanduni', 'Abeysekara', 'sanduni@ruh.ac.lk', '0733333333', 'b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7', 'TechnicalOfficer'),
('TO004', 'Nimal', 'Perera', 'nimal@ruh.ac.lk', '0744444444', 'c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7', 'TechnicalOfficer'),


-- Current batch
('TG/2023/1780', 'Saman', 'Perera', 'saman.perera@gmail.com', '0750000001', 'd4c3b2a1f6e5d4c3b2a1f0e9d8c7b6a5', 'Undergraduate'),
('TG/2023/1781', 'Kamal', 'Silva', 'kamal.silva@gmail.com', '0750000002', 'a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0', 'Undergraduate'),
('TG/2023/1782', 'Nadeesha', 'Fernando', 'nadeesha.fernando@gmail.com', '0750000003', 'b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0', 'Undergraduate'),
('TG/2023/1783', 'Ishara', 'Perera', 'ishara.perera@gmail.com', '0750000004', 'c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0', 'Undergraduate'),
('TG/2023/1784', 'Tharushi', 'Silva', 'tharushi.silva@gmail.com', '0750000005', 'd5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0', 'Undergraduate'),
('TG/2023/1795', 'Sanduni', 'Perera', 'sanduni.perera@gmail.com', '0750000016', 'c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2', 'Undergraduate'),
('TG/2023/1796', 'Lahiru', 'Silva', 'lahiru.silva@gmail.com', '0750000017', 'd7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2', 'Undergraduate'),
('TG/2023/1797', 'Thilini', 'Fernando', 'thilini.fernando@gmail.com', '0750000018', 'e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2', 'Undergraduate'),
('TG/2023/1798', 'Nuwan', 'Perera', 'nuwan.perera@gmail.com', '0750000019', 'f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2', 'Undergraduate'),
('TG/2023/1799', 'Gayani', 'Silva', 'gayani.silva@gmail.com', '0750000020', 'a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3', 'Undergraduate');

 

-- Repeat students from 2022
('TG/2022/1785', 'Dinuka', 'Fernando', 'dinuka.fernando@gmail.com', '0750000006', 'e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0', 'Undergraduate'),
('TG/2022/1786', 'Kasun', 'Perera', 'kasun.perera@gmail.com', '0750000007', 'f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0', 'Undergraduate'),
('TG/2022/1787', 'Malithi', 'Silva', 'malithi.silva@gmail.com', '0750000008', 'a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1', 'Undergraduate'),
('TG/2022/1788', 'Ravindu', 'Fernando', 'ravindu.fernando@gmail.com', '0750000009', 'b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1', 'Undergraduate'),
('TG/2022/1789', 'Piumi', 'Perera', 'piumi.perera@gmail.com', '0750000010', 'c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1', 'Undergraduate'),

-- Batch-missed / Suspended 2021
('TG/2021/1790', 'Supun', 'Silva', 'supun.silva@gmail.com', '0750000011', 'd6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1', 'Undergraduate'),
('TG/2021/1791', 'Sachini', 'Fernando', 'sachini.fernando@gmail.com', '0750000012', 'e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1', 'Undergraduate'),
('TG/2021/1792', 'Amila', 'Perera', 'amila.perera@gmail.com', '0750000013', 'f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1', 'Undergraduate'),
('TG/2021/1793', 'Nimali', 'Silva', 'nimali.silva@gmail.com', '0750000014', 'a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2', 'Undergraduate'),
('TG/2021/1794', 'Chathura', 'Fernando', 'chathura.fernando@gmail.com', '0750000015', 'b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2', 'Undergraduate');





INSERT INTO admin (admin_id) VALUES ('AD001'); 


INSERT INTO lecturer (lec_id, designation)
VALUES
('LEC001','Lecturer'),
('LEC002','Senior Lecturer'),
('LEC003','Lecturer'),
('LEC004','Assistant Lecturer'),
('LEC005','Lecturer'),
('LEC006','Senior Lecturer'),
('LEC007','Lecturer'),
('LEC008','Lecturer'); 


INSERT INTO technical_officer (to_id)
VALUES ('TO001'), ('TO002'), ('TO003'), ('TO004');


INSERT INTO undergraduate (stu_id, status, mentor_id)
VALUES
-- Proper 2023
('TG/2023/1780','Proper','LEC001'),
('TG/2023/1781','Proper','LEC003'),
('TG/2023/1782','Proper','LEC006'),
('TG/2023/1783','Proper','LEC008'),
('TG/2023/1784','Proper','LEC002'),
('TG/2023/1795','Proper','LEC004'),
('TG/2023/1796','Proper','LEC005'),
('TG/2023/1797','Proper','LEC007'),
('TG/2023/1798','Proper','LEC001'),
('TG/2023/1799','Proper','LEC002'),

-- Repeat 2022
('TG/2022/1785','Repeat','LEC006'),
('TG/2022/1786','Repeat','LEC001'),
('TG/2022/1787','Repeat','LEC004'),
('TG/2022/1788','Repeat','LEC003'),
('TG/2022/1789','Repeat','LEC002'),

-- Suspended 
('TG/2022/1790','Suspended','LEC005'),
('TG/2023/1791','Suspended','LEC008'),
('TG/2023/1792','Suspended','LEC007'),
('TG/2023/1793','Suspended','LEC006'),
('TG/2023/1794','Suspended','LEC004');




INSERT INTO course_unit (course_code, title, credit)
VALUES
('ICT2142','Object Oriented Analysis and Design',2),
('ICT2122','Object Oriented Programming',2),
('ICT2132','Object Oriented Programming Practicum',2),
('ICT2152','E-Commerce Implementation, Management and Security',2),
('ENG2122','English III',0),
('ICT2113','Data Structures and Algorithms',3),
('TCS2112','Business Economics',2),
('TCS2122','Soft Skills',1);



