INSERT INTO users VALUES


('ADM001','Kamal','Perera','kamal@gmail.com','0711111111',
 SHA2(CONCAT('Adm!n@123','🔥SecureSalt'),256),'Admin'),


('LEC001','Nimal','Fernando','nimal@gmail.com','0721111111',
 SHA2(CONCAT('Lec#2026','🔥SecureSalt'),256),'Lecturer'),

('LEC002','Sunil','Silva','sunil@gmail.com','0722222222',
 SHA2(CONCAT('Lec#2026','🔥SecureSalt'),256),'Lecturer'),

('LEC003','Amara','Perera','amara@gmail.com','0723333333',
 SHA2(CONCAT('Lec#2026','🔥SecureSalt'),256),'Lecturer'),

('LEC004','Saman','Dias','saman@gmail.com','0724444444',
 SHA2(CONCAT('Lec#2026','🔥SecureSalt'),256),'Lecturer'),

('LEC005','Kumari','Peris','kumari@gmail.com','0725555555',
 SHA2(CONCAT('Lec#2026','🔥SecureSalt'),256),'Lecturer'),  

('LEC006','Rashmi','Gunawardena','rashmi@gmail.com','0726666666',
 SHA2(CONCAT('Lec#2026','🔥SecureSalt'),256),'Lecturer'),

('LEC007','Dinesh','Wijesinghe','dinesh@gmail.com','0727777777',
 SHA2(CONCAT('Lec#2026','🔥SecureSalt'),256),'Lecturer'),

('LEC008','Shalini','Perera','shalini@gmail.com','0728888888',
 SHA2(CONCAT('Lec#2026','🔥SecureSalt'),256),'Lecturer');

('TO001','Ruwan','De Silva','ruwan@gmail.com','0731111111',
 SHA2(CONCAT('Tech@456','🔥SecureSalt'),256),'TechnicalOfficer'),

('TO002','Ajith','Fernando','ajith@gmail.com','0732222222',
 SHA2(CONCAT('Tech@456','🔥SecureSalt'),256),'TechnicalOfficer'),

('TO003','Mahesh','Perera','mahesh@gmail.com','0733333333',
 SHA2(CONCAT('Tech@456','🔥SecureSalt'),256),'TechnicalOfficer'),

('TO004','Nadeesha','Silva','nadeesha@gmail.com','0734444444',
 SHA2(CONCAT('Tech@456','🔥SecureSalt'),256),'TechnicalOfficer'),

('TG/2023/0001','Asha','Fernando','asha1@gmail.com','0740000001',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2023/0002','Bimal','Perera','bimal@gmail.com','0740000002',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2023/0003','Chathura','Silva','chathura@gmail.com','0740000003',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2023/0004','Dilani','Fernando','dilani@gmail.com','0740000004',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2023/0005','Eshan','Perera','eshan@gmail.com','0740000005',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2023/0006','Fathima','Rizvi','fathima@gmail.com','0740000006',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2023/0007','Gayan','Silva','gayan@gmail.com','0740000007',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2023/0008','Hansi','Perera','hansi@gmail.com','0740000008',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2023/0009','Ishara','Fernando','ishara@gmail.com','0740000009',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2023/0010','Janith','Silva','janith@gmail.com','0740000010',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2022/0001','Kasun','Perera','kasun@gmail.com','0741000001',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2022/0002','Lahiru','Fernando','lahiru@gmail.com','0741000002',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2022/0003','Madhavi','Silva','madhavi@gmail.com','0741000003',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2022/0004','Nirosha','Perera','nirosha@gmail.com','0741000004',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2022/0005','Oshada','Fernando','oshada@gmail.com','0741000005',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2023/0011','Piumi','Silva','piumi@gmail.com','0740000011',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2023/0012','Ravindu','Perera','ravindu@gmail.com','0740000012',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2023/0013','Sahan','Fernando','sahan@gmail.com','0740000013',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2023/0014','Thilini','Silva','thilini@gmail.com','0740000014',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'),

('TG/2023/0015','Umesh','Perera','umesh@gmail.com','0740000015',
 SHA2(CONCAT('Stu$789','🔥SecureSalt'),256),'Undergraduate'); 






INSERT INTO admin VALUES
('ADM001');


INSERT INTO lecturer VALUES
('LEC001','Senior Lecturer'),
('LEC002','Lecturer'),
('LEC003','Assistant Lecturer'),
('LEC004','Senior Lecturer'),
('LEC005','Lecturer'), 
('LEC006','Senior Lecturer'),
('LEC007','Lecturer'),
('LEC008','Assistant Lecturer');


INSERT INTO technical_officer VALUES
('TO001'),
('TO002'),
('TO003'),
('TO004'); 


INSERT INTO undergraduate VALUES
('TG/2023/0001','Proper','LEC001'),
('TG/2023/0002','Proper','LEC002'),
('TG/2023/0003','Proper','LEC003'),
('TG/2023/0004','Proper','LEC001'),
('TG/2023/0005','Proper','LEC002'),
('TG/2023/0006','Proper','LEC003'),
('TG/2023/0007','Proper','LEC004'),
('TG/2023/0008','Proper','LEC005'),
('TG/2023/0009','Proper','LEC001'),
('TG/2023/0010','Proper','LEC002'),
  
('TG/2022/0001','Repeat','LEC003'),
('TG/2022/0002','Repeat','LEC004'),
('TG/2022/0003','Repeat','LEC005'),
('TG/2022/0004','Repeat','LEC001'),
('TG/2022/0005','Repeat','LEC002'),

('TG/2023/0011','Suspended','LEC003'),
('TG/2023/0012','Proper','LEC004'),
('TG/2023/0013','Proper','LEC005'),
('TG/2023/0014','Proper','LEC001'),
('TG/2023/0015','Proper','LEC002');  






