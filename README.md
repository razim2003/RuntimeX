# RuntimeX

Java OOP Semester Project

## ICT2132 Mini Project - User, Attendance, Medical Module (JavaFX)

This repository now includes a JavaFX dashboard for the module parts:

- User profile viewing, contact update, and profile picture path update
- Attendance management (Theory / Practical)
- Medical submission and approval workflow
- Attendance summary with 80% eligibility check
- Role-based login and privilege-based home page
- Stylish card-based UI with icon-driven main functions

### Database setup

1. Run schema script:
   - `tecmis/database-scripts/database-schema.sql`

- Optional one-time collation standardization on existing DB:
  - `tecmis/database-scripts/collation-standardize.sql`

2. Run main data seed:
   - `tecmis/database-scripts/data.sql`
3. Run attendance + medical scenario seed:
   - `tecmis/database-scripts/attendance-medical-seed.sql`

If your database already has the old `attendance` structure, apply this migration first:

```sql
ALTER TABLE attendance
ADD COLUMN component ENUM('Theory','Practical') NOT NULL DEFAULT 'Theory' AFTER session_date;

-- Replace old unique key (stu_id, course_code, session_date)
-- with this one after dropping the previous unique index name from SHOW INDEX.
ALTER TABLE attendance
ADD UNIQUE KEY uq_attendance_session (stu_id, course_code, session_date, component);

ALTER TABLE medical
ADD COLUMN proof_image_path VARCHAR(300) NULL AFTER end_date;

ALTER TABLE users
ADD COLUMN profile_image_path VARCHAR(300) NULL AFTER contact_no;
```

### Run application

1. Update DB credentials in `tecmis/src/main/java/com/runtimex/tecmis/utils/DatabaseConnection.java`
2. From `tecmis` folder run:

```bash
mvn clean compile
mvn javafx:run
```

Main JavaFX entry:

- `tecmis/src/main/java/com/runtimex/tecmis/Main.java`

Main dashboard UI:

- `tecmis/src/main/java/com/runtimex/tecmis/ui/TecmisDashboard.java`

### Login and role privileges

The app now starts from a login page.

- Login with User ID + password
- For seeded data demo, password `1234` is accepted for all users

Role behavior currently implemented:

- Admin:
  - User profile maintenance (fully implemented)
  - Course/Notice/Timetable cards visible as placeholders for next phase
- Lecturer:
  - Update own profile (except username/password)
  - View-only attendance and medical records of undergraduates
  - View undergraduate eligibility
  - Materials/marks/undergraduate details/notices cards available as placeholders for next phase
- Technical Officer:
  - Update own profile (except username/password)
  - Add and fully maintain attendance details (TO-only edit controls)
  - Add and maintain medical details
  - Notice/timetable placeholders visible
- Undergraduate:
  - Update own contact details and profile picture
  - View own attendance
  - View own medical details (view-only)
  - Other student features shown as placeholders for next phase
