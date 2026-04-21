USE tecmis_java;
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;
SET collation_connection = 'utf8mb4_unicode_ci';
SET @course := _utf8mb4 'ICT2132' COLLATE utf8mb4_unicode_ci;
INSERT IGNORE INTO medical (
        ref_no,
        stu_id,
        reason,
        status,
        start_date,
        end_date
    )
VALUES (
        'MD1001',
        'TG/2023/1780',
        'Flu with medical certificate',
        'Approved',
        '2026-03-20',
        '2026-03-21'
    ),
    (
        'MD1002',
        'TG/2023/1781',
        'Clinic treatment',
        'Approved',
        '2026-04-10',
        '2026-04-11'
    ),
    (
        'MD1003',
        'TG/2023/1781',
        'Outpatient checkup',
        'Pending',
        '2026-04-17',
        '2026-04-18'
    );
DROP TEMPORARY TABLE IF EXISTS seq15;
CREATE TEMPORARY TABLE seq15 (n INT PRIMARY KEY);
INSERT INTO seq15 (n)
VALUES (1),
    (2),
    (3),
    (4),
    (5),
    (6),
    (7),
    (8),
    (9),
    (10),
    (11),
    (12),
    (13),
    (14),
    (15);
INSERT INTO attendance (
        attendance_id,
        stu_id,
        course_code,
        session_date,
        component,
        status
    )
SELECT CONCAT(
        'AT',
        UPPER(
            SUBSTRING(MD5(CONCAT(s.stu_id, 'T', seq15.n)), 1, 10)
        )
    ) AS attendance_id,
    s.stu_id,
    @course,
    DATE_ADD('2026-02-06', INTERVAL (seq15.n - 1) * 7 DAY) AS session_date,
    'Theory' AS component,
    CASE
        WHEN s.stu_id = 'TG/2023/1780' THEN IF(seq15.n IN (6, 12), 'Absent', 'Present')
        WHEN s.stu_id = 'TG/2023/1781' THEN IF(seq15.n IN (5, 9, 13), 'Absent', 'Present')
        ELSE 'Present'
    END AS status
FROM (
        SELECT e.stu_id
        FROM enrollment e
        WHERE e.course_code = @course
    ) s
    JOIN seq15 ON TRUE ON DUPLICATE KEY
UPDATE status =
VALUES(status);
INSERT INTO attendance (
        attendance_id,
        stu_id,
        course_code,
        session_date,
        component,
        status
    )
SELECT CONCAT(
        'AT',
        UPPER(
            SUBSTRING(MD5(CONCAT(s.stu_id, 'P', seq15.n)), 1, 10)
        )
    ) AS attendance_id,
    s.stu_id,
    @course,
    DATE_ADD('2026-02-04', INTERVAL (seq15.n - 1) * 7 DAY) AS session_date,
    'Practical' AS component,
    CASE
        WHEN s.stu_id = 'TG/2023/1780' THEN IF(seq15.n IN (5, 11), 'Absent', 'Present')
        WHEN s.stu_id = 'TG/2023/1781' THEN IF(seq15.n IN (4, 8, 12), 'Absent', 'Present')
        ELSE 'Present'
    END AS status
FROM (
        SELECT e.stu_id
        FROM enrollment e
        WHERE e.course_code = @course
    ) s
    JOIN seq15 ON TRUE ON DUPLICATE KEY
UPDATE status =
VALUES(status);
INSERT IGNORE INTO medical_attendance (attendance_id, ref_no)
SELECT a.attendance_id,
    'MD1001'
FROM attendance a
WHERE a.stu_id = 'TG/2023/1780'
    AND a.course_code = @course
    AND a.component = 'Theory'
    AND a.session_date = DATE_ADD('2026-02-06', INTERVAL (6 - 1) * 7 DAY);
INSERT IGNORE INTO medical_attendance (attendance_id, ref_no)
SELECT a.attendance_id,
    'MD1002'
FROM attendance a
WHERE a.stu_id = 'TG/2023/1781'
    AND a.course_code = @course
    AND a.component = 'Practical'
    AND a.session_date = DATE_ADD('2026-02-04', INTERVAL (4 - 1) * 7 DAY);
INSERT IGNORE INTO medical_attendance (attendance_id, ref_no)
SELECT a.attendance_id,
    'MD1003'
FROM attendance a
WHERE a.stu_id = 'TG/2023/1781'
    AND a.course_code = @course
    AND a.component = 'Theory'
    AND a.session_date = DATE_ADD('2026-02-06', INTERVAL (9 - 1) * 7 DAY);
DROP TEMPORARY TABLE IF EXISTS seq15;