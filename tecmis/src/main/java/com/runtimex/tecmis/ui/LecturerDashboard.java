package com.runtimex.tecmis.ui;

import com.runtimex.tecmis.models.UserProfile;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LecturerDashboard {
    private final UiContext ctx;
    private final UiHelpers ui;
    private final SharedPages shared;
    private final CourseMaterialPages materialPages;
    private final AttendanceMedicalPages attendanceMedicalPages;
    private final MarksPages marksPages;
    private final Runnable onLogout;

    private final ObservableList<UserProfile> ugRows = FXCollections.observableArrayList();

    public LecturerDashboard(UiContext ctx, UiHelpers ui, SharedPages shared,
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
        featureCards.add(new FeatureCard("\uD83D\uDE4D", "My Profile", "Update profile except username/password", () -> shared.openMyProfileEditor(this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83D\uDCD8", "Course Materials", "Create and modify materials for your courses", () -> materialPages.openLecturerMaterials(this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83D\uDCCB", "Attendance View", "See undergraduate attendance and summary", () -> attendanceMedicalPages.openAttendance(false, false, this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83E\uDE7A", "Medical View", "See undergraduate medical records", () -> attendanceMedicalPages.openMedical(false, false, this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83C\uDF93", "Undergraduate Details", "See undergraduate details", this::openUndergraduateDetails));
        featureCards.add(new FeatureCard("\uD83E\uDDEE", "Eligibility", "See undergraduate eligibility", () -> marksPages.openSummary(this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83D\uDCDD", "Upload Marks", "Upload marks for all kinds of exams", () -> marksPages.openMarksUpload(this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83D\uDCC8", "Marks, Grades, GPA", "See undergraduate marks, grades and GPA", () -> marksPages.openMarksOverviewForLecturer(this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83D\uDCE2", "Notices", "See notices", () -> shared.openNoticeBoard(this::showHome, onLogout)));

        int col = 0, row = 0;
        for (FeatureCard fc : featureCards) {
            cards.add(ui.card(fc.icon + "  " + fc.title, fc.description, fc.action), col, row);
            if (++col == 3) { col = 0; row++; }
        }
        page.setCenter(cards);
        ctx.show(page);
    }

    private void openUndergraduateDetails() {
        BorderPane page = ui.buildShell("Undergraduate Details", this::showHome, onLogout);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by ID or name...");
        searchField.setPrefWidth(240);
        Button searchBtn = new Button("\uD83D\uDD0D Search");
        Button loadAllBtn = new Button("Load All");

        TableView<UserProfile> table = new TableView<>(ugRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<UserProfile,String> idCol = ui.col("Student ID", u -> u.getId());
        TableColumn<UserProfile,String> nameCol = ui.col("Full Name", u -> u.getFullName());
        TableColumn<UserProfile,String> emailCol = ui.col("Email", u -> u.getEmail());
        TableColumn<UserProfile,String> contactCol = ui.col("Contact", u -> u.getContactNo());
        TableColumn<UserProfile,String> statusCol = ui.col("Status", u -> u.getStatus() == null ? "" : u.getStatus());
        idCol.setMinWidth(140); idCol.setPrefWidth(160);
        nameCol.setMinWidth(180); nameCol.setPrefWidth(220);
        emailCol.setMinWidth(200); emailCol.setPrefWidth(240);
        contactCol.setMinWidth(120); contactCol.setPrefWidth(150);
        statusCol.setMinWidth(100); statusCol.setPrefWidth(120);
        table.getColumns().addAll(idCol, nameCol, emailCol, contactCol, statusCol);
        table.setPrefHeight(260);
        table.setPlaceholder(new Label("No undergraduates found."));

        Label detailId = new Label();
        detailId.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label detailName = new Label();
        Label detailEmail = new Label();
        Label detailContact = new Label();
        Label detailStatus = new Label();

        VBox detailCard = new VBox(6,
                new Label("── Selected Undergraduate ──"),
                ui.hRow("Student ID:", detailId),
                ui.hRow("Name:", detailName),
                ui.hRow("Email:", detailEmail),
                ui.hRow("Contact:", detailContact),
                ui.hRow("Status:", detailStatus));
        detailCard.setPadding(new Insets(12));
        detailCard.setStyle("-fx-background-color: #f1f5f9; -fx-background-radius: 8; -fx-border-color: #cbd5e1; -fx-border-radius: 8;");
        detailCard.setVisible(false);

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                detailId.setText(sel.getId());
                detailName.setText(sel.getFullName());
                detailEmail.setText(sel.getEmail());
                detailContact.setText(sel.getContactNo());
                detailStatus.setText(sel.getStatus() == null ? "N/A" : sel.getStatus());
                detailCard.setVisible(true);
            } else {
                detailCard.setVisible(false);
            }
        });

        Runnable reload = () -> {
            try { ugRows.setAll(ctx.getUserDao().findUndergraduates("")); }
            catch (Exception ex) { ui.showError(ex.getMessage()); }
        };

        loadAllBtn.setOnAction(e -> reload.run());
        searchBtn.setOnAction(e -> {
            String kw = searchField.getText().trim();
            try { ugRows.setAll(ctx.getUserDao().findUndergraduates(kw)); }
            catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        HBox filterBar = new HBox(8, searchField, searchBtn, loadAllBtn);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        VBox layout = new VBox(12, filterBar, table, detailCard);
        layout.setPadding(new Insets(14));
        page.setCenter(new ScrollPane(layout));
        ctx.show(page);
        reload.run();
    }

    private record FeatureCard(String icon, String title, String description, Runnable action) {}
}
