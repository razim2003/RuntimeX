import com.runtimex.tecmis.dao.impl.MarksDaoImpl;
import com.runtimex.tecmis.models.Mark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class MarksDaoTest {
    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/your_db", "root", "1234"
        );

        MarksDaoImpl dao = new MarksDaoImpl(conn);

        // Test addMark
        Mark m = new Mark("MK001", "STU001", "CS101", "QU01", 85.0);
        dao.addMark(m);
        System.out.println("Added mark");

        // Test getMarksByStudentAndCourse
        List<Mark> marks = dao.getMarksByStudentAndCourse("STU001", "CS101");
        marks.forEach(mk -> System.out.println(mk.getMarkId() + " - " + mk.getMark()));

        // Test updateMark
        m = new Mark("MK001", "STU001", "CS101", "QU01", 90.0);
        dao.updateMark(m);
        System.out.println("Updated mark");

        // Test deleteMark
        dao.deleteMark("MK001");
        System.out.println("Deleted mark");

        conn.close();
    }
}