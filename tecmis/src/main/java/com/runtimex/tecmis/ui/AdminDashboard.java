package com.runtimex.tecmis.ui;

import com.runtimex.tecmis.models.CourseUnit;
import com.runtimex.tecmis.models.EnrollmentRecord;
import com.runtimex.tecmis.models.Notice;
import com.runtimex.tecmis.models.Timetable;
import com.runtimex.tecmis.models.UserProfile;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class AdminDashboard {
    private static final java.nio.file.Path NOTICE_STORE =
            java.nio.file.Paths.get(System.getProperty("user.home"), "tecmis_notices");

    private final UiContext ctx;
    private final UiHelpers ui;
    private final SharedPages shared;
    private final Runnable onLogout;

    private final ObservableList<UserProfile> userRows = FXCollections.observableArrayList();
    private final ObservableList<CourseUnit> courseRows = FXCollections.observableArrayList();
    private final ObservableList<Notice> noticeRows = FXCollections.observableArrayList();
    private final ObservableList<EnrollmentRecord> enrollmentRows = FXCollections.observableArrayList();
    private final ObservableList<Timetable> timetableRows = FXCollections.observableArrayList();

    public AdminDashboard(UiContext ctx, UiHelpers ui, SharedPages shared, Runnable onLogout) {
        this.ctx = ctx;
        this.ui = ui;
        this.shared = shared;
        this.onLogout = onLogout;
    }

    public void showHome() {
        BorderPane page = ui.buildShell("Home", this::showHome, onLogout);
        GridPane cards = new GridPane();
        cards.setHgap(12);
        cards.setVgap(12);
        cards.setPadding(new Insets(16));

        List<FeatureCard> featureCards = new ArrayList<>();
        featureCards.add(new FeatureCard("\uD83D\uDE4D", "My Profile", "Update profile picture and contact details", () -> shared.openMyProfileEditor(this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83D\uDC65", "User Profiles", "Create and maintain user profiles", this::openUserManagement));
        featureCards.add(new FeatureCard("\uD83D\uDCDA", "Courses", "Create and maintain course details", this::openCourseManagement));
        featureCards.add(new FeatureCard("\uD83E\uDDFE", "Enrollments", "Enroll students to courses", this::openEnrollmentManagement));
        featureCards.add(new FeatureCard("\uD83D\uDCE2", "Notices", "Create and maintain notices", this::openNoticeManagement));
        featureCards.add(new FeatureCard("\uD83D\uDCF0", "Notice Board", "See notices", () -> shared.openNoticeBoard(this::showHome, onLogout)));
        featureCards.add(new FeatureCard("\uD83D\uDDD3", "Timetables", "Create and maintain timetables", this::openAdminTimetable));

        int col = 0, row = 0;
        for (FeatureCard fc : featureCards) {
            cards.add(ui.card(fc.icon + "  " + fc.title, fc.description, fc.action), col, row);
            if (++col == 3) { col = 0; row++; }
        }
        page.setCenter(cards);
        ctx.show(page);
    }

    private void openAdminTimetable() {
        BorderPane page = ui.buildShell("Timetable Management", this::showHome, onLogout);

        TextField idField = new TextField();
        idField.setPromptText("Timetable ID (auto)");
        idField.setEditable(false);
        idField.setText(ui.generateId("TT"));

        ComboBox<String> courseBox = new ComboBox<>();
        courseBox.setPromptText("Course");
        courseBox.setPrefWidth(220);
        try {
            List<CourseUnit> courses = ctx.getMarksService().getAllCourses();
            for (CourseUnit c : courses)
                courseBox.getItems().add(c.getCourseCode() + " - " + c.getTitle());
            if (!courseBox.getItems().isEmpty()) courseBox.getSelectionModel().select(0);
        } catch (Exception ex) { ui.showError(ex.getMessage()); }

        TextField lecField = new TextField();
        lecField.setPromptText("Lecturer ID");
        TextField locationField = new TextField();
        locationField.setPromptText("Location");
        TextField levelField = new TextField();
        levelField.setPromptText("Level (e.g. 1)");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Theory", "Practical");
        typeBox.setValue("Theory");

        TextField hoursField = new TextField();
        hoursField.setPromptText("Hours");

        ComboBox<String> dayBox = new ComboBox<>();
        dayBox.getItems().addAll("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday");
        dayBox.setValue("Monday");

        TextField timeField = new TextField();
        timeField.setPromptText("Start time (HH:mm)");
        timeField.setText("08:00");

        TableView<Timetable> table = new TableView<>(timetableRows);
        ui.addTimetableColumns(table);

        Button loadBtn = new Button("Load All");
        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");

        loadBtn.setOnAction(e -> {
            try { timetableRows.setAll(ctx.getTimetableDao().viewTimetable()); }
            catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        addBtn.setOnAction(e -> {
            Timetable t = buildTimetableFromForm(idField, courseBox, lecField, locationField,
                    levelField, typeBox, hoursField, dayBox, timeField);
            if (t == null) return;
            try {
                ctx.getTimetableDao().addSession(t, ctx.getCurrentUser().getId());
                idField.setText(ui.generateId("TT"));
                loadBtn.fire();
                ui.showInfo("Session added");
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, sel) -> {
            if (sel != null) populateTimetableForm(sel, idField, courseBox, lecField,
                    locationField, levelField, typeBox, hoursField, dayBox, timeField);
        });

        updateBtn.setOnAction(e -> {
            Timetable sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select a row first"); return; }
            Timetable t = buildTimetableFromForm(idField, courseBox, lecField, locationField,
                    levelField, typeBox, hoursField, dayBox, timeField);
            if (t == null) return;
            try {
                ctx.getTimetableDao().updateSession(t, ctx.getCurrentUser().getId());
                loadBtn.fire();
                ui.showInfo("Session updated");
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        deleteBtn.setOnAction(e -> {
            Timetable sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select a row first"); return; }
            try {
                ctx.getTimetableDao().deleteSession(sel.getTimetableId(), ctx.getCurrentUser().getId());
                loadBtn.fire();
                ui.showInfo("Session deleted");
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        HBox row1 = new HBox(8, new Label("ID:"), idField, new Label("Course:"), courseBox, new Label("Lec ID:"), lecField);
        HBox row2 = new HBox(8, new Label("Location:"), locationField, new Label("Level:"), levelField,
                new Label("Type:"), typeBox, new Label("Hours:"), hoursField);
        HBox row3 = new HBox(8, new Label("Day:"), dayBox, new Label("Start:"), timeField);
        HBox row4 = new HBox(8, addBtn, updateBtn, deleteBtn, loadBtn);

        VBox body = new VBox(10, row1, row2, row3, row4, table);
        body.setPadding(new Insets(16));
        page.setCenter(body);
        ctx.show(page);
        loadBtn.fire();
    }

    private Timetable buildTimetableFromForm(TextField idField, ComboBox<String> courseBox,
            TextField lecField, TextField locationField, TextField levelField,
            ComboBox<String> typeBox, TextField hoursField,
            ComboBox<String> dayBox, TextField timeField) {
        String code = ui.parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
        if (code.isBlank() || locationField.getText().isBlank()
                || levelField.getText().isBlank() || hoursField.getText().isBlank()) {
            ui.showError("Course, location, level and hours are required");
            return null;
        }
        int level, hours;
        try { level = Integer.parseInt(levelField.getText().trim()); }
        catch (NumberFormatException ex) { ui.showError("Level must be a number"); return null; }
        try { hours = Integer.parseInt(hoursField.getText().trim()); }
        catch (NumberFormatException ex) { ui.showError("Hours must be a number"); return null; }

        Timetable t = new Timetable();
        t.setTimetableId(idField.getText().trim());
        t.setCourseCode(code);
        t.setLecturerId(lecField.getText().isBlank() ? null : lecField.getText().trim());
        t.setLocation(locationField.getText().trim());
        t.setLevel(level);
        t.setType(typeBox.getValue());
        t.setHours(hours);
        t.setDayOfWeek(dayBox.getValue());
        t.setStartTime(timeField.getText().isBlank() ? "08:00" : timeField.getText().trim());
        return t;
    }

    private void populateTimetableForm(Timetable sel,
            TextField idField, ComboBox<String> courseBox, TextField lecField,
            TextField locationField, TextField levelField,
            ComboBox<String> typeBox, TextField hoursField,
            ComboBox<String> dayBox, TextField timeField) {
        idField.setText(sel.getTimetableId());
        for (String item : courseBox.getItems()) {
            if (item.startsWith(sel.getCourseCode())) { courseBox.setValue(item); break; }
        }
        lecField.setText(sel.getLecturerId() == null ? "" : sel.getLecturerId());
        locationField.setText(sel.getLocation());
        levelField.setText(String.valueOf(sel.getLevel()));
        typeBox.setValue(sel.getType());
        hoursField.setText(String.valueOf(sel.getHours()));
        if (sel.getDayOfWeek() != null) dayBox.setValue(sel.getDayOfWeek());
        if (sel.getStartTime() != null) timeField.setText(sel.getStartTime());
    }

    private void openUserManagement() {
        BorderPane page = ui.buildShell("User Profiles", this::showHome, onLogout);

        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.getItems().addAll("All","Admin","Lecturer","TechnicalOfficer","Undergraduate");
        typeFilter.setValue("All");
        TextField keywordField = new TextField();
        keywordField.setPromptText("Search by id / name / email");
        keywordField.setPrefWidth(240);
        Button loadBtn = new Button("\uD83D\uDD0D Search");

        TableView<UserProfile> table = new TableView<>(userRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<UserProfile,String> idCol = ui.col("ID", x -> x.getId());
        TableColumn<UserProfile,String> nameCol = ui.col("Name", x -> x.getFullName());
        TableColumn<UserProfile,String> emailCol = ui.col("Email", x -> x.getEmail());
        TableColumn<UserProfile,String> contactCol = ui.col("Contact", x -> x.getContactNo());
        TableColumn<UserProfile,String> typeCol = ui.col("Type", x -> x.getUserType());
        TableColumn<UserProfile,String> statusCol = ui.col("Status", x -> x.getStatus() == null ? "" : x.getStatus());
        idCol.setMinWidth(140); idCol.setPrefWidth(160);
        contactCol.setMinWidth(120); contactCol.setPrefWidth(150);
        table.getColumns().addAll(idCol, nameCol, emailCol, contactCol, typeCol, statusCol);
        table.setPrefHeight(280);

        TextField emailEdit = new TextField();
        emailEdit.setPromptText("Updated email");
        TextField contactEdit = new TextField();
        contactEdit.setPromptText("Updated contact");
        ComboBox<String> statusEdit = new ComboBox<>();
        statusEdit.getItems().addAll("Proper", "Repeat", "Suspended");
        statusEdit.setDisable(true);
        Button updateBtn = new Button("\uD83D\uDCBE Update Contact");
        Button updateStatusBtn = new Button("\uD83E\uDDFE Update Status");
        updateStatusBtn.setDisable(true);
        Button deleteBtn = new Button("\uD83D\uDDD1 Delete User");
        deleteBtn.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white;");

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                emailEdit.setText(sel.getEmail());
                contactEdit.setText(sel.getContactNo());
                boolean isUg = "Undergraduate".equals(sel.getUserType());
                statusEdit.setDisable(!isUg);
                updateStatusBtn.setDisable(!isUg);
                statusEdit.setValue(isUg ? (sel.getStatus() == null ? "Proper" : sel.getStatus()) : null);
            } else {
                statusEdit.setDisable(true);
                updateStatusBtn.setDisable(true);
                statusEdit.setValue(null);
            }
        });

        loadBtn.setOnAction(e -> {
            try { userRows.setAll(ctx.getUserDao().findUsers(typeFilter.getValue(), keywordField.getText().trim())); }
            catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        updateBtn.setOnAction(e -> {
            UserProfile sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select a user first"); return; }
            String emailValue = emailEdit.getText().trim();
            String contactValue = contactEdit.getText().trim();
            if (!ui.isValidEmail(emailValue)) { ui.showError("Enter a valid email address"); return; }
            if (!ui.isValidPhone(contactValue)) { ui.showError("Contact number must have at least 10 digits"); return; }
            try {
                ctx.getUserDao().updateUserContact(sel.getId(), emailValue, contactValue);
                ui.showInfo("Contact updated successfully");
                loadBtn.fire();
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        updateStatusBtn.setOnAction(e -> {
            UserProfile sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select a user first"); return; }
            if (!"Undergraduate".equals(sel.getUserType())) { ui.showError("Status applies only to undergraduates"); return; }
            String status = statusEdit.getValue();
            if (status == null || status.isBlank()) { ui.showError("Select a status first"); return; }
            try {
                ctx.getUserDao().updateUndergraduateStatus(sel.getId(), status);
                ui.showInfo("Undergraduate status updated");
                loadBtn.fire();
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        deleteBtn.setOnAction(e -> {
            UserProfile sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select a user first"); return; }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete user " + sel.getId() + " – " + sel.getFullName() + "?\nThis cannot be undone.",
                    ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Confirm Delete");
            confirm.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) {
                    try { ctx.getUserDao().deleteUser(sel.getId()); ui.showInfo("User deleted."); loadBtn.fire(); }
                    catch (Exception ex) { ui.showError(ex.getMessage()); }
                }
            });
        });

        Label createHeading = new Label("\u2795 Create New User");
        createHeading.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

        TextField newId = new TextField(); newId.setPromptText("User ID  (e.g. LEC005)");
        TextField newFirst = new TextField(); newFirst.setPromptText("First Name");
        TextField newLast = new TextField(); newLast.setPromptText("Last Name");
        TextField newEmail = new TextField(); newEmail.setPromptText("Email");
        TextField newContact = new TextField(); newContact.setPromptText("Contact No");
        PasswordField newPwd = new PasswordField(); newPwd.setPromptText("Password");
        ComboBox<String> newType = new ComboBox<>();
        newType.getItems().addAll("Admin","Lecturer","TechnicalOfficer","Undergraduate");
        newType.setPromptText("User Type");
        ComboBox<String> ugStatus = new ComboBox<>();
        ugStatus.getItems().addAll("Proper", "Repeat", "Suspended");
        ugStatus.setValue("Proper");
        ugStatus.setDisable(true);
        Button createBtn = new Button("\u2705 Create User");
        createBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");

        newType.setOnAction(e -> {
            boolean isUg = "Undergraduate".equals(newType.getValue());
            ugStatus.setDisable(!isUg);
            if (!isUg) { ugStatus.setValue("Proper"); }
        });

        GridPane createForm = new GridPane();
        createForm.setHgap(8);
        createForm.setVgap(6);
        createForm.setPadding(new Insets(10, 0, 0, 0));
        createForm.addRow(0, new Label("ID:"), newId, new Label("First Name:"), newFirst);
        createForm.addRow(1, new Label("Last Name:"), newLast, new Label("Email:"), newEmail);
        createForm.addRow(2, new Label("Contact:"), newContact, new Label("Password:"), newPwd);
        createForm.addRow(3, new Label("Type:"), newType, new Label("UG Status:"), ugStatus);
        createForm.addRow(4, new Label(""), createBtn);

        createBtn.setOnAction(e -> {
            String id = newId.getText().trim();
            String fn = newFirst.getText().trim();
            String ln = newLast.getText().trim();
            String em = newEmail.getText().trim();
            String cn = newContact.getText().trim();
            String pw = newPwd.getText();
            String tp = newType.getValue();
            if (id.isEmpty() || fn.isEmpty() || ln.isEmpty() || em.isEmpty() || cn.isEmpty() || pw.isEmpty() || tp == null) {
                ui.showError("All fields are required to create a user.");
                return;
            }
            if (!ui.isValidEmail(em)) { ui.showError("Enter a valid email address"); return; }
            if (!ui.isValidPhone(cn)) { ui.showError("Contact number must have at least 10 digits"); return; }
            try {
                ctx.getUserDao().createUser(id, fn, ln, em, cn, pw, tp);
                if ("Undergraduate".equals(tp)) {
                    ctx.getUserDao().updateUndergraduateStatus(id, ugStatus.getValue());
                }
                ui.showInfo("User " + id + " created successfully.");
                newId.clear(); newFirst.clear(); newLast.clear();
                newEmail.clear(); newContact.clear(); newPwd.clear(); newType.setValue(null);
                ugStatus.setValue("Proper"); ugStatus.setDisable(true);
                loadBtn.fire();
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        HBox filterBar = new HBox(8, new Label("Type:"), typeFilter, keywordField, loadBtn);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        HBox editBar = new HBox(8,
            new Label("Email:"), emailEdit,
            new Label("Contact:"), contactEdit, updateBtn,
            new Label("Status:"), statusEdit, updateStatusBtn,
            deleteBtn);
        editBar.setAlignment(Pos.CENTER_LEFT);

        Separator sep = new Separator();

        VBox layout = new VBox(10, filterBar, table, editBar, sep, createHeading, createForm);
        layout.setPadding(new Insets(14));
        page.setCenter(new ScrollPane(layout));
        ctx.show(page);
        loadBtn.fire();
    }

    private void openCourseManagement() {
        BorderPane page = ui.buildShell("Course Management", this::showHome, onLogout);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by code or title...");
        searchField.setPrefWidth(260);
        Button searchBtn = new Button("\uD83D\uDD0D Search");
        Button loadAllBtn = new Button("Load All");

        TableView<CourseUnit> table = new TableView<>(courseRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<CourseUnit,String> codeCol = ui.col("Course Code", c -> c.getCourseCode());
        TableColumn<CourseUnit,String> titleCol = ui.col("Title", c -> c.getTitle());
        TableColumn<CourseUnit,String> creditCol = ui.col("Credits", c -> String.valueOf(c.getCredit()));
        table.getColumns().addAll(codeCol, titleCol, creditCol);
        table.setPrefHeight(280);

        TextField editTitle = new TextField();
        editTitle.setPromptText("Title");
        TextField editCredit = new TextField();
        editCredit.setPromptText("Credits");
        Button updateBtn = new Button("\uD83D\uDCBE Update");
        Button deleteBtn = new Button("\uD83D\uDDD1 Delete");
        deleteBtn.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white;");

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                editTitle.setText(sel.getTitle());
                editCredit.setText(String.valueOf(sel.getCredit()));
            }
        });

        Runnable reloadAll = () -> {
            try { courseRows.setAll(ctx.getCourseUnitDAO().getAllCourses()); }
            catch (Exception ex) { ui.showError(ex.getMessage()); }
        };

        loadAllBtn.setOnAction(e -> reloadAll.run());
        searchBtn.setOnAction(e -> {
            String kw = searchField.getText().trim();
            try {
                if (kw.isEmpty()) reloadAll.run();
                else courseRows.setAll(ctx.getCourseUnitDAO().searchCourses(kw));
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        updateBtn.setOnAction(e -> {
            CourseUnit sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select a course first"); return; }
            String title = editTitle.getText().trim();
            String creditStr = editCredit.getText().trim();
            if (title.isEmpty() || creditStr.isEmpty()) { ui.showError("Title and credits are required"); return; }
            int credits;
            try { credits = Integer.parseInt(creditStr); } catch (NumberFormatException ex) { ui.showError("Credits must be a number"); return; }
            try {
                CourseUnit updated = new CourseUnit(sel.getCourseCode(), title, credits);
                boolean ok = ctx.getCourseUnitDAO().updateCourse(updated);
                if (ok) { ui.showInfo("Course updated."); reloadAll.run(); }
                else { ui.showError("Course not found for update."); }
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        deleteBtn.setOnAction(e -> {
            CourseUnit sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select a course first"); return; }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete course " + sel.getCourseCode() + " – " + sel.getTitle() + "?",
                    ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Confirm Delete");
            confirm.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) {
                    try { ctx.getCourseUnitDAO().deleteCourse(sel.getCourseCode()); ui.showInfo("Course deleted."); reloadAll.run(); }
                    catch (Exception ex) { ui.showError(ex.getMessage()); }
                }
            });
        });

        Label createHeading = new Label("\u2795 Add New Course");
        createHeading.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField newCode = new TextField(); newCode.setPromptText("Course Code (e.g. CS1234)");
        TextField newTitle = new TextField(); newTitle.setPromptText("Course Title");
        TextField newCredit = new TextField(); newCredit.setPromptText("Credits");
        Button addBtn = new Button("\u2705 Add Course");
        addBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");

        addBtn.setOnAction(e -> {
            String code = newCode.getText().trim();
            String title = newTitle.getText().trim();
            String credStr = newCredit.getText().trim();
            if (code.isEmpty() || title.isEmpty() || credStr.isEmpty()) { ui.showError("All fields required"); return; }
            int cred;
            try { cred = Integer.parseInt(credStr); } catch (NumberFormatException ex) { ui.showError("Credits must be a number"); return; }
            if (ctx.getCourseUnitDAO().courseExists(code)) { ui.showError("Course code already exists: " + code); return; }
            try {
                ctx.getCourseUnitDAO().addCourse(new CourseUnit(code, title, cred));
                ui.showInfo("Course added: " + code);
                newCode.clear(); newTitle.clear(); newCredit.clear();
                reloadAll.run();
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        HBox searchBar = new HBox(8, searchField, searchBtn, loadAllBtn);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        HBox editBar = new HBox(8, new Label("Title:"), editTitle, new Label("Credits:"), editCredit, updateBtn, deleteBtn);
        editBar.setAlignment(Pos.CENTER_LEFT);
        HBox addBar = new HBox(8, new Label("Code:"), newCode, new Label("Title:"), newTitle, new Label("Credits:"), newCredit, addBtn);
        addBar.setAlignment(Pos.CENTER_LEFT);

        VBox layout = new VBox(10, searchBar, table, editBar, new Separator(), createHeading, addBar);
        layout.setPadding(new Insets(14));
        page.setCenter(new ScrollPane(layout));
        ctx.show(page);
        reloadAll.run();
    }

    private void openEnrollmentManagement() {
        BorderPane page = ui.buildShell("Enrollment Management", this::showHome, onLogout);

        TextField filterField = new TextField();
        filterField.setPromptText("Filter by student id/name or course code/title");
        filterField.setPrefWidth(320);
        Button refreshBtn = new Button("\uD83D\uDD04 Refresh");

        TableView<EnrollmentRecord> table = new TableView<>(enrollmentRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<EnrollmentRecord,String> stuIdCol = ui.col("Student ID", EnrollmentRecord::getStudentId);
        TableColumn<EnrollmentRecord,String> stuNameCol = ui.col("Student Name", EnrollmentRecord::getStudentName);
        TableColumn<EnrollmentRecord,String> courseCol = ui.col("Course", EnrollmentRecord::getCourseCode);
        TableColumn<EnrollmentRecord,String> titleCol = ui.col("Course Title", EnrollmentRecord::getCourseTitle);
        table.getColumns().addAll(stuIdCol, stuNameCol, courseCol, titleCol);
        table.setPrefHeight(260);

        ComboBox<String> studentBox = new ComboBox<>();
        studentBox.setPromptText("Select student");
        studentBox.setPrefWidth(320);
        ComboBox<String> courseBox = new ComboBox<>();
        courseBox.setPromptText("Select course");
        courseBox.setPrefWidth(320);

        Button enrollBtn = new Button("\u2705 Enroll");
        enrollBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");
        Button removeBtn = new Button("\uD83D\uDDD1 Remove Selected");
        removeBtn.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white;");

        Runnable loadStudents = () -> {
            try {
                studentBox.getItems().clear();
                for (UserProfile u : ctx.getUserDao().findUndergraduates("")) {
                    studentBox.getItems().add(u.getId() + " - " + u.getFullName());
                }
                if (!studentBox.getItems().isEmpty()) studentBox.getSelectionModel().select(0);
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        };

        Runnable loadCourses = () -> {
            try {
                courseBox.getItems().clear();
                for (CourseUnit c : ctx.getCourseUnitDAO().getAllCourses()) {
                    courseBox.getItems().add(c.getCourseCode() + " - " + c.getTitle());
                }
                if (!courseBox.getItems().isEmpty()) courseBox.getSelectionModel().select(0);
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        };

        Runnable reloadAll = () -> {
            try { enrollmentRows.setAll(ctx.getEnrollmentDao().findEnrollments(filterField.getText().trim())); }
            catch (Exception ex) { ui.showError(ex.getMessage()); }
        };

        refreshBtn.setOnAction(e -> reloadAll.run());

        enrollBtn.setOnAction(e -> {
            String student = ui.parseStudentId(studentBox.getValue());
            String course = ui.parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            if (student.isBlank() || course.isBlank()) { ui.showError("Select both student and course"); return; }
            try {
                ctx.getEnrollmentDao().enrollStudent(student, course);
                ui.showInfo("Enrolled " + student + " to " + course);
                reloadAll.run();
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        removeBtn.setOnAction(e -> {
            EnrollmentRecord sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select an enrollment row first"); return; }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Remove enrollment for " + sel.getStudentId() + " in " + sel.getCourseCode() + "?",
                    ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Confirm Remove");
            confirm.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) {
                    try {
                        ctx.getEnrollmentDao().removeEnrollment(sel.getStudentId(), sel.getCourseCode());
                        ui.showInfo("Enrollment removed");
                        reloadAll.run();
                    } catch (Exception ex) { ui.showError(ex.getMessage()); }
                }
            });
        });

        HBox filterBar = new HBox(8, filterField, refreshBtn);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        HBox enrollBar = new HBox(8, studentBox, courseBox, enrollBtn, removeBtn);
        enrollBar.setAlignment(Pos.CENTER_LEFT);

        VBox layout = new VBox(10, filterBar, table, new Separator(), enrollBar);
        layout.setPadding(new Insets(14));
        page.setCenter(new ScrollPane(layout));
        ctx.show(page);
        loadStudents.run();
        loadCourses.run();
        reloadAll.run();
    }

    private void openNoticeManagement() {
        BorderPane page = ui.buildShell("Notice Management", this::showHome, onLogout);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by title...");
        searchField.setPrefWidth(260);
        Button searchBtn = new Button("\uD83D\uDD0D Search");
        Button loadAllBtn = new Button("Load All");
        Button viewBoardBtn = new Button("\uD83D\uDCF0 View Board");
        viewBoardBtn.setOnAction(e -> shared.openNoticeBoard(this::showHome, onLogout));

        TableView<Notice> table = new TableView<>(noticeRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(220);
        TableColumn<Notice,String> idCol = ui.col("Notice ID", n -> n.getNoticeId());
        TableColumn<Notice,String> titleCol = ui.col("Title", n -> n.getTitle());
        TableColumn<Notice,String> dateCol = ui.col("Date", n -> n.getDate() == null ? "" : n.getDate().toString());
        TableColumn<Notice,String> fileCol = ui.col("Attachment", n -> n.hasFile() ? "\uD83D\uDCCE " + n.getFileType().toUpperCase() : "—");
        table.getColumns().addAll(idCol, titleCol, dateCol, fileCol);

        Label editHeading = new Label("\u270F\uFE0F  Edit Selected Notice");
        editHeading.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        TextField editTitle = new TextField();
        editTitle.setPromptText("Title");
        ComboBox<String> editAudience = new ComboBox<>();
        editAudience.getItems().addAll("All", "Admin", "Lecturer", "TechnicalOfficer", "Undergraduate");
        editAudience.setValue("All");
        Label editFileLabel = new Label("No file attached");
        editFileLabel.setStyle("-fx-text-fill: #475569;");
        Button editBrowseBtn = new Button("\uD83D\uDCC2 Replace File...");
        Button editClearFile = new Button("\u2716 Remove File");
        final File[] editChosenFile = {null};
        final boolean[] editFileClear = {false};

        editBrowseBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Select attachment (PDF or PNG)");
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PDF files", "*.pdf"),
                    new FileChooser.ExtensionFilter("PNG images", "*.png"));
            File f = fc.showOpenDialog(ctx.getRoot().getScene() == null ? null : ctx.getRoot().getScene().getWindow());
            if (f != null) {
                editChosenFile[0] = f;
                editFileClear[0] = false;
                editFileLabel.setText("New: " + f.getName());
            }
        });
        editClearFile.setOnAction(e -> {
            editChosenFile[0] = null;
            editFileClear[0] = true;
            editFileLabel.setText("File will be removed on save");
        });

        Button updateBtn = new Button("\uD83D\uDCBE Save Changes");
        Button deleteBtn = new Button("\uD83D\uDDD1 Delete Notice");
        deleteBtn.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white;");

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            editChosenFile[0] = null;
            editFileClear[0] = false;
            if (sel != null) {
                editTitle.setText(sel.getTitle());
                editAudience.setValue(sel.getAudience() == null ? "All" : sel.getAudience());
                editFileLabel.setText(sel.hasFile()
                        ? "Current: " + new File(sel.getFilePath()).getName()
                        : "No file attached");
            } else {
                editTitle.clear();
                editAudience.setValue("All");
                editFileLabel.setText("No file attached");
            }
        });

        Runnable reloadAll = () -> {
            try { noticeRows.setAll(ctx.getNoticeDao().getNoticesForAudience(ctx.getCurrentUser().getUserType())); }
            catch (Exception ex) { ui.showError(ex.getMessage()); }
        };

        loadAllBtn.setOnAction(e -> reloadAll.run());
        searchBtn.setOnAction(e -> {
            String kw = searchField.getText().trim();
            try {
                noticeRows.setAll(kw.isEmpty()
                        ? ctx.getNoticeDao().getNoticesForAudience(ctx.getCurrentUser().getUserType())
                        : ctx.getNoticeDao().searchNoticesForAudience(kw, ctx.getCurrentUser().getUserType()));
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        updateBtn.setOnAction(e -> {
            Notice sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select a notice to edit."); return; }
            String title = editTitle.getText().trim();
            if (title.isEmpty()) { ui.showError("Title cannot be empty."); return; }
            sel.setTitle(title);
            sel.setAudience(editAudience.getValue());
            if (editFileClear[0]) {
                sel.setFilePath(null);
                sel.setFileType(null);
            } else if (editChosenFile[0] != null) {
                try {
                    String stored = copyNoticeFile(editChosenFile[0], sel.getNoticeId());
                    sel.setFilePath(stored);
                    sel.setFileType(ui.fileExt(editChosenFile[0].getName()));
                } catch (Exception ex) { ui.showError("File copy failed: " + ex.getMessage()); return; }
            }
            try {
                ctx.getNoticeDao().updateNotice(sel, ctx.getCurrentUser().getId());
                ui.showInfo("Notice updated.");
                reloadAll.run();
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        deleteBtn.setOnAction(e -> {
            Notice sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select a notice to delete."); return; }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete notice \"" + sel.getTitle() + "\"?\nThis cannot be undone.",
                    ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Confirm Delete");
            confirm.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) {
                    try { ctx.getNoticeDao().deleteNotice(sel.getNoticeId(), ctx.getCurrentUser().getId()); ui.showInfo("Notice deleted."); reloadAll.run(); }
                    catch (Exception ex) { ui.showError(ex.getMessage()); }
                }
            });
        });

        Label createHeading = new Label("\u2795 Create New Notice");
        createHeading.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

        TextField newTitle = new TextField();
        newTitle.setPromptText("Notice title...");
        newTitle.setPrefWidth(340);
        ComboBox<String> newAudience = new ComboBox<>();
        newAudience.getItems().addAll("All", "Admin", "Lecturer", "TechnicalOfficer", "Undergraduate");
        newAudience.setValue("All");
        Label newFileLabel = new Label("No file selected (optional)");
        newFileLabel.setStyle("-fx-text-fill: #475569;");
        Button newBrowseBtn = new Button("\uD83D\uDCC2 Attach File...");
        final File[] newChosenFile = {null};

        newBrowseBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Attach PDF or PNG");
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PDF files", "*.pdf"),
                    new FileChooser.ExtensionFilter("PNG images", "*.png"));
            File f = fc.showOpenDialog(ctx.getRoot().getScene() == null ? null : ctx.getRoot().getScene().getWindow());
            if (f != null) { newChosenFile[0] = f; newFileLabel.setText(f.getName()); }
        });

        Button addBtn = new Button("\u2705 Post Notice");
        addBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");

        addBtn.setOnAction(e -> {
            String title = newTitle.getText().trim();
            if (title.isEmpty()) { ui.showError("Title is required."); return; }
            String noticeId = "NOT" + String.format("%09d", Math.abs(System.nanoTime() % 1_000_000_000L));
            String storedPath = null;
            String storedType = null;
            if (newChosenFile[0] != null) {
                try {
                    storedPath = copyNoticeFile(newChosenFile[0], noticeId);
                    storedType = ui.fileExt(newChosenFile[0].getName());
                } catch (Exception ex) { ui.showError("File copy failed: " + ex.getMessage()); return; }
            }
            Notice n = new Notice(noticeId, ctx.getCurrentUser().getId(), title, null, storedPath, storedType, newAudience.getValue());
            try {
                ctx.getNoticeDao().createNotice(n, ctx.getCurrentUser().getId());
                ui.showInfo("Notice posted" + (storedPath != null ? " with attachment." : "."));
                newTitle.clear(); newAudience.setValue("All");
                newChosenFile[0] = null; newFileLabel.setText("No file selected (optional)");
                reloadAll.run();
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        HBox searchBar = new HBox(8, searchField, searchBtn, loadAllBtn, viewBoardBtn);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        HBox fileEditRow = new HBox(8, editBrowseBtn, editClearFile, editFileLabel);
        fileEditRow.setAlignment(Pos.CENTER_LEFT);
        GridPane editForm = new GridPane();
        editForm.setHgap(10); editForm.setVgap(6);
        editForm.addRow(0, new Label("Title:"), editTitle);
        editForm.addRow(1, new Label("Audience:"), editAudience);
        editForm.addRow(2, new Label("File:"), fileEditRow);
        HBox editBtnRow = new HBox(8, updateBtn, deleteBtn);
        VBox editSection = new VBox(6, editHeading, editForm, editBtnRow);
        editSection.setPadding(new Insets(8, 0, 0, 0));

        HBox newFileRow = new HBox(8, newBrowseBtn, newFileLabel);
        newFileRow.setAlignment(Pos.CENTER_LEFT);
        GridPane createForm = new GridPane();
        createForm.setHgap(10); createForm.setVgap(6);
        createForm.addRow(0, new Label("Title:"), newTitle);
        createForm.addRow(1, new Label("Audience:"), newAudience);
        createForm.addRow(2, new Label("File:"), newFileRow);
        createForm.addRow(3, new Label(), addBtn);
        VBox createSection = new VBox(6, createHeading, createForm);
        createSection.setPadding(new Insets(8, 0, 0, 0));

        VBox layout = new VBox(10, searchBar, table, new Separator(), editSection, new Separator(), createSection);
        layout.setPadding(new Insets(14));
        ScrollPane sp = new ScrollPane(layout);
        sp.setFitToWidth(true);
        page.setCenter(sp);
        ctx.show(page);
        reloadAll.run();
    }

    private java.nio.file.Path noticeStore() {
        try { java.nio.file.Files.createDirectories(NOTICE_STORE); } catch (Exception ignored) {}
        return NOTICE_STORE;
    }

    private String copyNoticeFile(File src, String noticeId) throws java.io.IOException {
        String ext = src.getName().contains(".") ? src.getName().substring(src.getName().lastIndexOf('.')) : "";
        String name = noticeId + ext;
        java.nio.file.Path dest = noticeStore().resolve(name);
        java.nio.file.Files.copy(src.toPath(), dest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        return dest.toString();
    }

    private record FeatureCard(String icon, String title, String description, Runnable action) {}
}
