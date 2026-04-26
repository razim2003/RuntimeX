package com.runtimex.tecmis;

import com.runtimex.tecmis.dao.AttendanceDao;
import com.runtimex.tecmis.dao.CourseMaterialDao;
import com.runtimex.tecmis.dao.CourseUnitDAO;
import com.runtimex.tecmis.dao.ExamMedicalDao;
import com.runtimex.tecmis.dao.EnrollmentDao;
import com.runtimex.tecmis.dao.MedicalDao;
import com.runtimex.tecmis.dao.MarksDao;
import com.runtimex.tecmis.dao.NoticeDao;
import com.runtimex.tecmis.dao.TimetableDao;
import com.runtimex.tecmis.dao.UserDao;
import com.runtimex.tecmis.dao.impl.AttendanceDaoImpl;
import com.runtimex.tecmis.dao.impl.ExamMedicalDaoImpl;
import com.runtimex.tecmis.dao.impl.MedicalDaoImpl;
import com.runtimex.tecmis.dao.impl.MarksDaoImpl;
import com.runtimex.tecmis.dao.impl.UserDaoImpl;
import com.runtimex.tecmis.services.impl.AttendanceServiceImpl;
import com.runtimex.tecmis.services.impl.ExamMedicalServiceImpl;
import com.runtimex.tecmis.services.impl.MarksServiceImpl;
import com.runtimex.tecmis.ui.TecmisDashboard;
import com.runtimex.tecmis.utils.DatabaseConnection;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.Connection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Connection connection = DatabaseConnection.getConnection();

        Parent root;
        if (connection == null) {
            BorderPane fallback = new BorderPane(
                    new Label("Database connection failed. Check DB credentials in DatabaseConnection.java"));
            root = fallback;
        } else {
            UserDao            userDao         = new UserDaoImpl(connection);
            AttendanceDao      attendanceDao   = new AttendanceDaoImpl(connection);
            MedicalDao         medicalDao      = new MedicalDaoImpl(connection);
            MarksDao           marksDao        = new MarksDaoImpl(connection);
            ExamMedicalDao     examMedicalDao  = new ExamMedicalDaoImpl(connection);
            CourseMaterialDao  materialDao     = new CourseMaterialDao(connection);
            TimetableDao       timetableDao    = new TimetableDao(connection);
            CourseUnitDAO      courseUnitDAO   = new CourseUnitDAO(connection);
            NoticeDao          noticeDao       = new NoticeDao(connection);
            EnrollmentDao      enrollmentDao   = new EnrollmentDao(connection);

            AttendanceServiceImpl attendanceService = new AttendanceServiceImpl(attendanceDao);
            MarksServiceImpl      marksService      = new MarksServiceImpl(marksDao, attendanceDao);
            ExamMedicalServiceImpl examMedicalService = new ExamMedicalServiceImpl(examMedicalDao, marksDao);

            root = new TecmisDashboard(
                    userDao, attendanceDao, medicalDao, marksDao, marksService, examMedicalService,
                    materialDao, timetableDao, courseUnitDAO, noticeDao, enrollmentDao
            ).build();
        }

        Scene scene = new Scene(root, 1300, 760);
        java.net.URL stylesheet = getClass().getResource("/com/runtimex/tecmis/ui/tecmis.css");
        if (stylesheet != null) {
            scene.getStylesheets().add(stylesheet.toExternalForm());
        }
        primaryStage.setTitle("TecMIS - RuntimeX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
