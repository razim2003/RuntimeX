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

public class TechnicalOfficerDashboard {
    private final UiContext ctx;
    private final UiHelpers ui;
    private final SharedPages shared;
    private final AttendanceMedicalPages attendanceMedicalPages;
    private final Runnable onLogout;

    private final ObservableList<Timetable> timetableRows = FXCollections.observableArrayList();

    public TechnicalOfficerDashboard(UiContext ctx, UiHelpers ui, SharedPages shared,
            AttendanceMedicalPages attendanceMedicalPages, Runnable onLogout) {
        this.ctx = ctx;
        this.ui = ui;
        this.shared = shared;
        this.attendanceMedicalPages = attendanceMedicalPages;
        this.onLogout = onLogout;
    }

    public void showHome() {
        BorderPane page = ui.buildShell("Home", this::showHome, onLogout);
        GridPane cards = new GridPane();
        cards.setHgap(12);
        cards.setVgap(12);
        cards.setPadding(new Insets(16));

        List<FeatureCard> featureCards = new ArrayList<>();
        featureCards.add(new FeatureCard("\uD83D\uDE4D", "My Profile", "Update profile except username/password", () -> shared.openMyProfileEditor(this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83D\uDDC2", "Attendance", "Add and maintain attendance details", () -> attendanceMedicalPages.openAttendance(true, false, this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83E\uDE79", "Medical", "Add and maintain medical details", () -> attendanceMedicalPages.openMedical(true, false, this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83E\uDDFE", "Exam Medical", "Approve or reject exam medical requests", () -> attendanceMedicalPages.openExamMedical(true, false, this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83D\uDCE2", "Notices", "See notices", () -> shared.openNoticeBoard(this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83D\uDDD3", "Department Timetable", "See timetables for your department", this::openTOTimetable));

        int col = 0, row = 0;
        for (FeatureCard fc : featureCards) {
            cards.add(ui.card(fc.icon + "  " + fc.title, fc.description, fc.action), col, row);
            if (++col == 3) { col = 0; row++; }
        }
        page.setCenter(cards);
        ctx.show(page);
    }

    private void openTOTimetable() {
        BorderPane page = ui.buildShell("Department Timetable", this::showHome, onLogout);

        String dept = resolveTODepartment(ctx.getCurrentUser().getId());

        TableView<Timetable> table = new TableView<>(timetableRows);
        ui.addTimetableColumns(table);

        Label deptLabel = new Label("Department filter: " + (dept == null || dept.isBlank() ? "(all)" : dept));
        deptLabel.setStyle("-fx-text-fill: #334155;");

        Button loadBtn = new Button("Load");
        loadBtn.setOnAction(e -> {
            try { timetableRows.setAll(ctx.getTimetableDao().viewTimetableByDepartment(dept)); }
            catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        HBox bar = new HBox(10, deptLabel, loadBtn);
        VBox body = new VBox(10, bar, table);
        body.setPadding(new Insets(16));
        page.setCenter(body);
        ctx.show(page);
        loadBtn.fire();
    }

    private String resolveTODepartment(String toId) {
        try {
            java.sql.Connection conn = com.runtimex.tecmis.utils.DatabaseConnection.getConnection();
            String sql = "SELECT department FROM technical_officer WHERE to_id = ?";
            try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, toId);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getString("department");
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    private record FeatureCard(String icon, String title, String description, Runnable action) {}
}
