package com.runtimex.tecmis.ui;

import com.runtimex.tecmis.models.Timetable;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UndergraduateDashboard {
    private final UiContext ctx;
    private final UiHelpers ui;
    private final SharedPages shared;
    private final CourseMaterialPages materialPages;
    private final AttendanceMedicalPages attendanceMedicalPages;
    private final MarksPages marksPages;
    private final Runnable onLogout;

    private final ObservableList<Timetable> timetableRows = FXCollections.observableArrayList();

    public UndergraduateDashboard(UiContext ctx, UiHelpers ui, SharedPages shared,
            CourseMaterialPages materialPages, AttendanceMedicalPages attendanceMedicalPages,
            MarksPages marksPages, Runnable onLogout) {
        this.ctx = ctx;
        this.ui = ui;
        this.shared = shared;
        this.materialPages = materialPages;
        this.attendanceMedicalPages = attendanceMedicalPages;
        this.marksPages = marksPages;
        this.onLogout = onLogout;
    }

    public void showHome() {
        BorderPane page = ui.buildShell("Home", this::showHome, onLogout);
        GridPane cards = new GridPane();
        cards.setHgap(12);
        cards.setVgap(12);
        cards.setPadding(new Insets(16));

        List<FeatureCard> featureCards = new ArrayList<>();
        featureCards.add(new FeatureCard("\uD83D\uDE4D", "My Profile", "Update only contact details and profile picture", () -> shared.openMyProfileEditor(this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83D\uDCCB", "My Attendance", "See your attendance details", () -> attendanceMedicalPages.openAttendance(false, true, this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83E\uDE7A", "My Medical", "See your medical details", () -> attendanceMedicalPages.openMedical(false, true, this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83D\uDCDD", "Exam Medical", "Submit exam medical requests", () -> attendanceMedicalPages.openExamMedical(false, true, this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83D\uDCD8", "Course Materials", "View materials for your enrolled courses", () -> materialPages.openUndergraduateMaterials(this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83D\uDCC8", "My Grades & GPA", "See your grades and GPA", () -> marksPages.openMarksOverviewForUndergraduate(this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83D\uDDD3", "My Timetable", "See your class timetable", this::openUndergraduateTimetable));
        featureCards.add(new FeatureCard("\uD83D\uDCE2", "Notices", "See notices", () -> shared.openNoticeBoard(this::showHome, onLogout)));

        int col = 0, row = 0;
        for (FeatureCard fc : featureCards) {
            cards.add(ui.card(fc.icon + "  " + fc.title, fc.description, fc.action), col, row);
            if (++col == 3) { col = 0; row++; }
        }
        page.setCenter(cards);
        ctx.show(page);
    }

    private void openUndergraduateTimetable() {
        BorderPane page = ui.buildShell("My Timetable", this::showHome, onLogout);

        int level = ctx.getTimetableDao().getLevelForStudent(ctx.getCurrentUser().getId());
        Label info = new Label("Showing timetable for Level " + level);
        info.setStyle("-fx-text-fill: #334155;");

        TableView<Timetable> table = new TableView<>(timetableRows);
        ui.addTimetableColumns(table);

        Button loadBtn = new Button("Refresh");
        loadBtn.setOnAction(e -> {
            try { timetableRows.setAll(ctx.getTimetableDao().viewTimetableByLevel(level)); }
            catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        HBox bar = new HBox(10, info, loadBtn);
        VBox body = new VBox(10, bar, table);
        body.setPadding(new Insets(16));
        page.setCenter(body);
        ctx.show(page);
        loadBtn.fire();
    }

    private record FeatureCard(String icon, String title, String description, Runnable action) {}
}
