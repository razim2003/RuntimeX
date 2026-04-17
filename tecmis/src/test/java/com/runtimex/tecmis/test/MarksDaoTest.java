package com.runtimex.tecmis.test;

import com.runtimex.tecmis.dao.MarksDao;
import com.runtimex.tecmis.dao.impl.MarksDaoImpl;
import com.runtimex.tecmis.models.Mark;
import com.runtimex.tecmis.models.CourseExam;

import java.sql.*;

public class MarksDaoTest {

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tecmis_java",
                    "root",
                    "1234"
            );
            System.out.println("DB Connected ✔");

            Statement stmt = conn.createStatement();
            // Disable FK checks for this session only
            //stmt.execute("SET FOREIGN_KEY_CHECKS = 0");

            MarksDao dao = new MarksDaoImpl(conn);

            CourseExam ce = new CourseExam();
            ce.setCourseCode("CS101");
            ce.setExamTypeId("QU01");

            Mark mark = new Mark("M2", "TG/2023/1755", ce, 85.0);
            dao.addMark(mark);
            System.out.println("Mark inserted ✔");

            // Verify
            ResultSet rs = stmt.executeQuery("SELECT * FROM marks WHERE mark_id = 'M1'");
            if (rs.next()) {
                System.out.println("Verified ✔ | "
                        + rs.getString("mark_id") + " | "
                        + rs.getString("stu_id") + " | "
                        + rs.getString("course_code") + " | "
                        + rs.getString("type_id") + " | "
                        + rs.getDouble("mark"));
            } else {
                System.out.println("Not found ❌");
            }

            // Re-enable foreign key checks
            //stmt.execute("SET FOREIGN_KEY_CHECKS = 1");

            stmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error ❌");
            e.printStackTrace();
        }
    }
}