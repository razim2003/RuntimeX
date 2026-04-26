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
import com.runtimex.tecmis.services.interfaces.ExamMedicalService;
import com.runtimex.tecmis.services.interfaces.MarksService;
import javafx.scene.Parent;

public class TecmisDashboard {
    private final UiContext ctx;
    private final UiHelpers ui;
    private final LoginPage loginPage;
    private final SharedPages sharedPages;
    private final AttendanceMedicalPages attendanceMedicalPages;
    private final CourseMaterialPages courseMaterialPages;
    private final MarksPages marksPages;
    private final AdminDashboard adminDashboard;
    private final LecturerDashboard lecturerDashboard;
    private final TechnicalOfficerDashboard technicalOfficerDashboard;
    private final UndergraduateDashboard undergraduateDashboard;

    public TecmisDashboard(UserDao userDao, AttendanceDao attendanceDao, MedicalDao medicalDao,
                           MarksDao marksDao, MarksService marksService, ExamMedicalService examMedicalService,
                           CourseMaterialDao materialDao, TimetableDao timetableDao,
                           CourseUnitDAO courseUnitDAO, NoticeDao noticeDao, EnrollmentDao enrollmentDao) {
        this.ctx = new UiContext(userDao, attendanceDao, medicalDao, marksDao, marksService, examMedicalService,
                materialDao, timetableDao, courseUnitDAO, noticeDao, enrollmentDao);
        this.ui = new UiHelpers(ctx);
        this.sharedPages = new SharedPages(ctx, ui);
        this.attendanceMedicalPages = new AttendanceMedicalPages(ctx, ui);
        this.courseMaterialPages = new CourseMaterialPages(ctx, ui);
        this.marksPages = new MarksPages(ctx, ui);
        this.loginPage = new LoginPage(ctx, ui);

        Runnable logout = () -> {
            ctx.setCurrentUser(null);
            showLogin();
        };

        this.adminDashboard = new AdminDashboard(ctx, ui, sharedPages, logout);
        this.lecturerDashboard = new LecturerDashboard(ctx, ui, sharedPages, courseMaterialPages,
                attendanceMedicalPages, marksPages, logout);
        this.technicalOfficerDashboard = new TechnicalOfficerDashboard(ctx, ui, sharedPages,
                attendanceMedicalPages, logout);
        this.undergraduateDashboard = new UndergraduateDashboard(ctx, ui, sharedPages, courseMaterialPages,
                attendanceMedicalPages, marksPages, logout);
    }

    public Parent build() {
        showLogin();
        return ctx.getRoot();
    }

    private void showLogin() {
        loginPage.show(authUser -> {
            ctx.setCurrentUser(authUser);
            routeToRoleHome();
        });
    }

    private void routeToRoleHome() {
        if (ctx.getCurrentUser() == null) {
            showLogin();
            return;
        }
        String role = ctx.getCurrentUser().getUserType();
        if ("Admin".equals(role)) {
            adminDashboard.showHome();
        } else if ("Lecturer".equals(role)) {
            lecturerDashboard.showHome();
        } else if ("TechnicalOfficer".equals(role)) {
            technicalOfficerDashboard.showHome();
        } else {
            undergraduateDashboard.showHome();
        }
    }
}
