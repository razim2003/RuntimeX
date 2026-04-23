

USE tecmis_java;

ALTER TABLE notice
    ADD COLUMN file_path VARCHAR(500) NULL AFTER date,
    ADD COLUMN file_type VARCHAR(10)  NULL AFTER file_path,
    ADD COLUMN audience  ENUM('All','Admin','Lecturer','TechnicalOfficer','Undergraduate')
        NOT NULL DEFAULT 'All' AFTER file_type;

UPDATE notice
SET audience = 'All'
WHERE audience IS NULL;
