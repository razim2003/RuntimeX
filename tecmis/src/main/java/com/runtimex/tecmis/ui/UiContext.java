package com.runtimex.tecmis.ui;

import com.runtimex.tecmis.dao.AttendanceDao;
import com.runtimex.tecmis.dao.CourseMaterialDao;
import com.runtimex.tecmis.dao.CourseUnitDAO;
import com.runtimex.tecmis.dao.EnrollmentDao;
import com.runtimex.tecmis.dao.MarksDao;
import com.runtimex.tecmis.dao.MedicalDao;
import com.runtimex.tecmis.dao.NoticeDao;
import com.runtimex.tecmis.dao.TimetableDao;
import com.runtimex.tecmis.dao.UserDao;
import com.runtimex.tecmis.models.AuthUser;
import com.runtimex.tecmis.services.interfaces.ExamMedicalService;
import com.runtimex.tecmis.services.interfaces.MarksService;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class UiContext {
    private final StackPane root = new StackPane();
    private final UserDao userDao;
    private final AttendanceDao attendanceDao;
    private final MedicalDao medicalDao;
    private final MarksDao marksDao;
    private final MarksService marksService;
    private final ExamMedicalService examMedicalService;
    private final CourseMaterialDao materialDao;
    private final TimetableDao timetableDao;
    private final CourseUnitDAO courseUnitDAO;
    private final NoticeDao noticeDao;
    private final EnrollmentDao enrollmentDao;
    private AuthUser currentUser;

    public UiContext(UserDao userDao, AttendanceDao attendanceDao, MedicalDao medicalDao,
            MarksDao marksDao, MarksService marksService, ExamMedicalService examMedicalService,
            CourseMaterialDao materialDao, TimetableDao timetableDao,
            CourseUnitDAO courseUnitDAO, NoticeDao noticeDao, EnrollmentDao enrollmentDao) {
        this.userDao = userDao;
        this.attendanceDao = attendanceDao;
        this.medicalDao = medicalDao;
        this.marksDao = marksDao;
        this.marksService = marksService;
        this.examMedicalService = examMedicalService;
        this.materialDao = materialDao;
        this.timetableDao = timetableDao;
        this.courseUnitDAO = courseUnitDAO;
        this.noticeDao = noticeDao;
        this.enrollmentDao = enrollmentDao;
    }

    public StackPane getRoot() {
        return root;
    }

    public void show(Parent page) {
        root.getChildren().setAll(page);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public AttendanceDao getAttendanceDao() {
        return attendanceDao;
    }

    public MedicalDao getMedicalDao() {
        return medicalDao;
    }

    public MarksDao getMarksDao() {
        return marksDao;
    }

    public MarksService getMarksService() {
        return marksService;
    }

    public ExamMedicalService getExamMedicalService() {
        return examMedicalService;
    }

    public CourseMaterialDao getMaterialDao() {
        return materialDao;
    }

    public TimetableDao getTimetableDao() {
        return timetableDao;
    }

    public CourseUnitDAO getCourseUnitDAO() {
        return courseUnitDAO;
    }

    public NoticeDao getNoticeDao() {
        return noticeDao;
    }

    public EnrollmentDao getEnrollmentDao() {
        return enrollmentDao;
    }

    public AuthUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(AuthUser currentUser) {
        this.currentUser = currentUser;
    }
}
