package com.runtimex.tecmis.ui;

import com.runtimex.tecmis.dao.AttendanceDao;
import com.runtimex.tecmis.dao.MedicalDao;
import com.runtimex.tecmis.dao.UserDao;
import com.runtimex.tecmis.models.AttendanceRecord;
import com.runtimex.tecmis.models.AttendanceSummary;
import com.runtimex.tecmis.models.AuthUser;
import com.runtimex.tecmis.models.CourseUnit;
import com.runtimex.tecmis.models.MedicalRecord;
import com.runtimex.tecmis.models.UserProfile;
import com.runtimex.tecmis.services.impl.AttendanceServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TecmisDashboard {

    private final UserDao userDao;
    private final AttendanceDao attendanceDao;
    private final MedicalDao medicalDao;
    private final AttendanceServiceImpl attendanceService;

    private final ObservableList<UserProfile> userRows = FXCollections.observableArrayList();
    private final ObservableList<AttendanceRecord> attendanceRows = FXCollections.observableArrayList();
    private final ObservableList<MedicalRecord> medicalRows = FXCollections.observableArrayList();
    private final ObservableList<AttendanceSummary> summaryRows = FXCollections.observableArrayList();

    private final StackPane root = new StackPane();
    private AuthUser currentUser;

    public TecmisDashboard(UserDao userDao, AttendanceDao attendanceDao, MedicalDao medicalDao,
            AttendanceServiceImpl attendanceService) {
        this.userDao = userDao;
        this.attendanceDao = attendanceDao;
        this.medicalDao = medicalDao;
        this.attendanceService = attendanceService;
    }

    public Parent build() {
        showLogin();
        return root;
    }

    private void showLogin() {
        BorderPane page = new BorderPane();
        page.setStyle("-fx-background-color: linear-gradient(to bottom right, #0f172a, #1f2937);");

        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(28));
        card.setMaxWidth(420);
        card.setStyle("-fx-background-color: #111827; -fx-background-radius: 16; -fx-border-radius: 16;"
                + "-fx-border-color: #334155; -fx-border-width: 1;");

        Label heading = new Label("TecMIS Login");
        heading.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");

        Label sub = new Label("Role-based access for User, Attendance, and Medical modules");
        sub.setStyle("-fx-text-fill: #cbd5e1;");

        TextField userIdField = new TextField();
        userIdField.setPromptText("User ID (e.g. AD001, LEC001, TO001, TG/2023/1780)");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Label note = new Label("Demo tip: use password 1234 for seeded users");
        note.setStyle("-fx-text-fill: #94a3b8;");

        Button loginBtn = new Button("🔐 Login");
        loginBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");

        loginBtn.setOnAction(e -> {
            String userId = userIdField.getText().trim();
            String password = passwordField.getText().trim();

            if (userId.isEmpty() || password.isEmpty()) {
                showError("Enter both User ID and password");
                return;
            }

            try {
                AuthUser authUser = userDao.authenticate(userId, password);
                if (authUser == null) {
                    showError("Invalid credentials");
                    return;
                }

                currentUser = authUser;
                showHome();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        card.getChildren().addAll(heading, sub, userIdField, passwordField, loginBtn, note);

        StackPane center = new StackPane(card);
        center.setPadding(new Insets(24));
        page.setCenter(center);

        root.getChildren().setAll(page);
    }

    private void showHome() {
        BorderPane page = buildShell("Home");

        GridPane cards = new GridPane();
        cards.setHgap(12);
        cards.setVgap(12);
        cards.setPadding(new Insets(16));

        List<FeatureCard> featureCards = getFeatureCardsForRole(currentUser.getUserType());

        int col = 0;
        int row = 0;
        for (FeatureCard feature : featureCards) {
            VBox card = card(feature.icon + "  " + feature.title, feature.description, feature.action);
            cards.add(card, col, row);
            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }

        page.setCenter(cards);
        root.getChildren().setAll(page);
    }

    private BorderPane buildShell(String section) {
        BorderPane page = new BorderPane();
        page.setStyle("-fx-background-color: linear-gradient(to bottom right, #e2e8f0, #cbd5e1);");

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(12));
        topBar.setStyle("-fx-background-color: #0f172a;");

        Label title = new Label("TecMIS");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");

        Label sec = new Label("| " + section);
        sec.setStyle("-fx-font-size: 15px; -fx-text-fill: #bfdbfe;");

        Label userInfo = new Label(currentUser.getFullName() + "  (" + currentUser.getUserType() + ")");
        userInfo.setStyle("-fx-text-fill: #cbd5e1;");

        StackPane avatar = buildTopBarAvatar();

        Region gap = new Region();
        HBox.setHgrow(gap, Priority.ALWAYS);

        Button homeBtn = new Button("🏠 Home");
        homeBtn.setOnAction(e -> showHome());

        Button logoutBtn = new Button("⏻ Logout");
        logoutBtn.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> {
            currentUser = null;
            showLogin();
        });

        topBar.getChildren().addAll(title, sec, gap, avatar, userInfo, homeBtn, logoutBtn);
        page.setTop(topBar);
        return page;
    }

    private StackPane buildTopBarAvatar() {
        StackPane avatar = new StackPane();
        avatar.setPrefSize(34, 34);
        avatar.setMinSize(34, 34);
        avatar.setMaxSize(34, 34);
        avatar.setStyle("-fx-background-color: #1e293b; -fx-background-radius: 17; -fx-border-color: #64748b;"
                + "-fx-border-radius: 17; -fx-border-width: 1;");

        try {
            UserProfile profile = userDao.findById(currentUser.getId());
            String imagePath = profile == null ? null : profile.getProfileImagePath();

            if (imagePath != null && !imagePath.isBlank()) {
                File file = new File(imagePath);
                if (file.exists() && file.isFile()) {
                    Image image = new Image(file.toURI().toString(), 34, 34, true, true);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(34);
                    imageView.setFitHeight(34);
                    imageView.setPreserveRatio(false);
                    imageView.setClip(new Circle(17, 17, 17));
                    avatar.getChildren().add(imageView);
                    return avatar;
                }
            }
        } catch (Exception ignored) {
            // Fallback icon is shown below if profile/photo cannot be loaded.
        }

        Label fallbackIcon = new Label("👤");
        fallbackIcon.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 14px;");
        avatar.getChildren().add(fallbackIcon);
        return avatar;
    }

    private VBox card(String title, String description, Runnable action) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(14));
        box.setMinHeight(130);
        box.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 14;"
                + "-fx-border-color: #94a3b8; -fx-border-radius: 14; -fx-border-width: 1;");

        Label t = new Label(title);
        t.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

        Label d = new Label(description);
        d.setWrapText(true);
        d.setStyle("-fx-text-fill: #334155;");

        Button open = new Button("Open");
        open.setStyle("-fx-background-color: #0ea5e9; -fx-text-fill: white;");
        open.setOnAction(e -> action.run());

        box.getChildren().addAll(t, d, open);
        return box;
    }

    private List<FeatureCard> getFeatureCardsForRole(String role) {
        List<FeatureCard> list = new ArrayList<>();

        if ("Admin".equals(role)) {
            list.add(new FeatureCard("🙍", "My Profile", "Update profile picture and contact details",
                this::openMyProfileEditor));
            list.add(new FeatureCard("👥", "User Profiles", "Create and maintain user profiles",
                    this::openUserManagement));
            list.add(new FeatureCard("📚", "Courses", "Create and maintain course details",
                    () -> openPlaceholder("Course management is planned in next phase.")));
            list.add(new FeatureCard("📢", "Notices", "Create and maintain notices",
                    () -> openPlaceholder("Notice management is planned in next phase.")));
            list.add(new FeatureCard("🗓", "Timetables", "Create and maintain timetables",
                    () -> openPlaceholder("Timetable management is planned in next phase.")));
            return list;
        }

        if ("Lecturer".equals(role)) {
            list.add(new FeatureCard("🙍", "My Profile", "Update profile except username/password",
                    this::openMyProfileEditor));
            list.add(new FeatureCard("📘", "Course Materials", "Modify and add materials to courses",
                    () -> openPlaceholder("Course material management is next phase.")));
            list.add(new FeatureCard("📋", "Attendance View", "See undergraduate attendance and summary",
                    () -> openAttendance(false, false)));
            list.add(new FeatureCard("🩺", "Medical View", "See undergraduate medical records",
                    () -> openMedical(false, false)));
            list.add(new FeatureCard("🎓", "Undergraduate Details", "See undergraduate details",
                    () -> openPlaceholder("Undergraduate details module is next phase.")));
            list.add(new FeatureCard("🧮", "Eligibility", "See undergraduate eligibility", this::openSummary));
            list.add(new FeatureCard("📝", "Upload Marks", "Upload marks for all kinds of exams",
                    () -> openPlaceholder("Marks upload module is next phase.")));
            list.add(new FeatureCard("📈", "Marks, Grades, GPA", "See undergraduate marks, grades and GPA",
                    () -> openPlaceholder("Marks, grades and GPA view is next phase.")));
            list.add(new FeatureCard("📢", "Notices", "See notices",
                    () -> openPlaceholder("Notice board is next phase.")));
            return list;
        }

        if ("TechnicalOfficer".equals(role)) {
            list.add(new FeatureCard("🙍", "My Profile", "Update profile except username/password",
                    this::openMyProfileEditor));
            list.add(new FeatureCard("🗂", "Attendance", "Add and maintain attendance details",
                    () -> openAttendance(true, false)));
            list.add(new FeatureCard("🩹", "Medical", "Add and maintain medical details",
                    () -> openMedical(true, false)));
            list.add(new FeatureCard("📢", "Notices", "See notices",
                    () -> openPlaceholder("Notice board is next phase.")));
            list.add(new FeatureCard("🗓", "Department Timetable", "See timetables",
                    () -> openPlaceholder("Timetable view is next phase.")));
            return list;
        }

        list.add(new FeatureCard("🙍", "My Profile", "Update only contact details and profile picture",
                this::openMyProfileEditor));
        list.add(new FeatureCard("📋", "My Attendance", "See your attendance details",
                () -> openAttendance(false, true)));
        list.add(new FeatureCard("🩺", "My Medical", "See your medical details", () -> openMedical(false, true)));
        list.add(new FeatureCard("📚", "My Courses", "See your course details",
                () -> openPlaceholder("Course details view is next phase.")));
        list.add(new FeatureCard("📈", "My Grades & GPA", "See your grades and GPA",
                () -> openPlaceholder("Grades/GPA view is next phase.")));
        list.add(new FeatureCard("🗓", "My Timetable", "See your timetable",
                () -> openPlaceholder("Timetable view is next phase.")));
        list.add(new FeatureCard("📢", "Notices", "See notices", () -> openPlaceholder("Notice board is next phase.")));
        return list;
    }

    private void openUserManagement() {
        BorderPane page = buildShell("User Profiles");

        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.getItems().addAll("All", "Admin", "Lecturer", "TechnicalOfficer", "Undergraduate");
        typeFilter.setValue("All");

        TextField keywordField = new TextField();
        keywordField.setPromptText("Search by id/name/email");
        keywordField.setPrefWidth(260);

        Button loadBtn = new Button("Load");

        TableView<UserProfile> table = new TableView<>(userRows);

        TableColumn<UserProfile, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getId()));

        TableColumn<UserProfile, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getFullName()));

        TableColumn<UserProfile, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getEmail()));

        TableColumn<UserProfile, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getContactNo()));

        TableColumn<UserProfile, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getUserType()));

        table.getColumns().add(idCol);
        table.getColumns().add(nameCol);
        table.getColumns().add(emailCol);
        table.getColumns().add(contactCol);
        table.getColumns().add(typeCol);

        TextField emailEdit = new TextField();
        emailEdit.setPromptText("Updated email");
        TextField contactEdit = new TextField();
        contactEdit.setPromptText("Updated contact");
        Button updateBtn = new Button("Update Contact");

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, selected) -> {
            if (selected != null) {
                emailEdit.setText(selected.getEmail());
                contactEdit.setText(selected.getContactNo());
            }
        });

        loadBtn.setOnAction(e -> {
            try {
                userRows.setAll(userDao.findUsers(typeFilter.getValue(), keywordField.getText().trim()));
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        updateBtn.setOnAction(e -> {
            UserProfile selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Select a user first");
                return;
            }
            try {
                userDao.updateUserContact(selected.getId(), emailEdit.getText().trim(), contactEdit.getText().trim());
                loadBtn.fire();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        HBox filters = new HBox(8, new Label("Type:"), typeFilter, keywordField, loadBtn);
        HBox updateBar = new HBox(8, new Label("Email:"), emailEdit, new Label("Contact:"), contactEdit, updateBtn);
        VBox layout = new VBox(10, filters, table, updateBar);
        layout.setPadding(new Insets(14));
        page.setCenter(layout);

        root.getChildren().setAll(page);
        loadBtn.fire();
    }

    private void openMyProfileEditor() {
        BorderPane page = buildShell("My Profile");

        UserProfile me = userDao.findById(currentUser.getId());
        if (me == null) {
            showError("Unable to load your profile");
            return;
        }

        Label roleNote = new Label(getProfileRuleText(me.getUserType()));
        roleNote.setWrapText(true);
        roleNote.setStyle("-fx-text-fill: #1e293b;");

        TextField id = new TextField(me.getId());
        id.setEditable(false);

        TextField name = new TextField(me.getFullName());
        name.setEditable(false);

        TextField email = new TextField(me.getEmail());
        TextField contact = new TextField(me.getContactNo());

        TextField profileImagePath = new TextField(me.getProfileImagePath() == null ? "" : me.getProfileImagePath());
        profileImagePath.setPromptText("Profile image path");
        profileImagePath.setEditable(false);

        Button uploadBtn = new Button("Upload Photo");
        uploadBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select profile image");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.webp"));
            java.io.File file = chooser.showOpenDialog(root.getScene() == null ? null : root.getScene().getWindow());
            if (file != null) {
                profileImagePath.setText(file.getAbsolutePath());
            }
        });

        Button clearBtn = new Button("Remove Photo");
        clearBtn.setOnAction(e -> profileImagePath.clear());

        Button save = new Button("Save");
        save.setOnAction(e -> saveMyProfile(email.getText().trim(), contact.getText().trim(),
                profileImagePath.getText().trim()));

        HBox photoBar = new HBox(8, profileImagePath, uploadBtn, clearBtn);

        VBox box = new VBox(10,
                new Label("User ID"), id,
                new Label("Name"), name,
                new Label("Email"), email,
                new Label("Contact"), contact,
                new Label("Profile Picture"), photoBar,
                roleNote,
                save);
        box.setPadding(new Insets(16));
        page.setCenter(box);
        root.getChildren().setAll(page);
    }

    private void saveMyProfile(String email, String contact, String profileImagePath) {
        try {
            userDao.updateMyProfile(currentUser.getId(), email, contact, profileImagePath);
            showInfo("Profile updated");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private String getProfileRuleText(String role) {
        if ("Lecturer".equals(role) || "TechnicalOfficer".equals(role)) {
            return "You can update your profile except username and password.";
        }
        if ("Undergraduate".equals(role)) {
            return "You can update contact details and profile picture.";
        }
        return "You can update your profile picture and contact details here.";
    }

    private void openAttendance(boolean canManage, boolean selfOnly) {
        BorderPane page = buildShell("Attendance");

        TextField attendanceIdField = new TextField();
        attendanceIdField.setPromptText("Attendance ID");

        TextField studentIdField = new TextField(selfOnly ? currentUser.getId() : "");
        studentIdField.setPromptText("Student ID");
        studentIdField.setEditable(!selfOnly);

        TextField courseField = new TextField();
        courseField.setPromptText("Course Code");

        DatePicker datePicker = new DatePicker(LocalDate.now());

        ComboBox<String> componentBox = new ComboBox<>();
        componentBox.getItems().addAll("Theory", "Practical");
        componentBox.setValue("Theory");

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Present", "Absent");
        statusBox.setValue("Present");

        Button addBtn = new Button("Add Attendance");
        addBtn.setDisable(!canManage);

        TextField filterStudent = new TextField(selfOnly ? currentUser.getId() : "");
        filterStudent.setPromptText("Filter Student ID");
        filterStudent.setEditable(!selfOnly);

        TextField filterCourse = new TextField();
        filterCourse.setPromptText("Filter Course");

        ComboBox<String> filterComponent = new ComboBox<>();
        filterComponent.getItems().addAll("Combined", "Theory", "Practical");
        filterComponent.setValue("Combined");

        Button refreshBtn = new Button("Refresh");

        ComboBox<String> ugCourseSelector = new ComboBox<>();
        ugCourseSelector.setPrefWidth(420);

        ProgressBar ugHoursBar = new ProgressBar(0);
        ugHoursBar.setPrefWidth(360);
        Label ugHoursLabel = new Label("Presented Hours: 0 / 0");

        TableView<AttendanceRecord> table = new TableView<>(attendanceRows);

        TableColumn<AttendanceRecord, String> idCol = new TableColumn<>("Attendance ID");
        idCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getAttendanceId()));
        TableColumn<AttendanceRecord, String> stuCol = new TableColumn<>("Student");
        stuCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getStudentId()));
        TableColumn<AttendanceRecord, String> courseCol = new TableColumn<>("Course");
        courseCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getCourseCode()));
        TableColumn<AttendanceRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getSessionDate()));
        TableColumn<AttendanceRecord, String> compCol = new TableColumn<>("Component");
        compCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getComponent()));
        TableColumn<AttendanceRecord, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getDisplayStatus()));

        if (!selfOnly) {
            table.getColumns().add(idCol);
            table.getColumns().add(stuCol);
        }
        table.getColumns().add(courseCol);
        table.getColumns().add(dateCol);
        table.getColumns().add(compCol);
        table.getColumns().add(statusCol);

        ComboBox<String> updateStatus = new ComboBox<>();
        updateStatus.getItems().addAll("Present", "Absent");
        updateStatus.setValue("Present");
        Button updateBtn = new Button("Update Selected Status");
        updateBtn.setDisable(!canManage || selfOnly);

        Button editBtn = new Button("Edit Selected Attendance");
        editBtn.setDisable(!canManage || selfOnly);

        addBtn.setOnAction(e -> {
            try {
                AttendanceRecord record = new AttendanceRecord(
                        attendanceIdField.getText().trim(),
                        studentIdField.getText().trim(),
                        courseField.getText().trim(),
                        datePicker.getValue().toString(),
                        componentBox.getValue(),
                        statusBox.getValue());
                attendanceDao.addAttendance(record);
                refreshBtn.fire();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        refreshBtn.setOnAction(e -> {
            try {
                attendanceRows.setAll(attendanceDao.findAttendance(
                        filterStudent.getText().trim(),
                        filterCourse.getText().trim(),
                        filterComponent.getValue()));

                if (selfOnly) {
                    int totalHours = attendanceRows.size() * 2;
                    int presentHours = 0;
                    for (AttendanceRecord r : attendanceRows) {
                        if ("Present".equals(r.getStatus())) {
                            presentHours += 2;
                        }
                    }
                    double ratio = totalHours == 0 ? 0.0 : (double) presentHours / totalHours;
                    ugHoursBar.setProgress(ratio);
                    ugHoursLabel.setText("Presented Hours: " + presentHours + " / " + totalHours);
                }
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        updateBtn.setOnAction(e -> {
            AttendanceRecord selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Select an attendance row first");
                return;
            }
            try {
                attendanceDao.updateAttendanceStatus(selected.getAttendanceId(), updateStatus.getValue());
                refreshBtn.fire();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        editBtn.setOnAction(e -> {
            AttendanceRecord selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Select an attendance row first");
                return;
            }

            try {
                AttendanceRecord record = new AttendanceRecord(
                        selected.getAttendanceId(),
                        studentIdField.getText().trim(),
                        courseField.getText().trim(),
                        datePicker.getValue().toString(),
                        componentBox.getValue(),
                        statusBox.getValue());
                attendanceDao.updateAttendance(record);
                refreshBtn.fire();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, selected) -> {
            if (selected != null) {
                attendanceIdField.setText(selected.getAttendanceId());
                studentIdField.setText(selected.getStudentId());
                courseField.setText(selected.getCourseCode());
                if (selected.getSessionDate() != null && !selected.getSessionDate().isBlank()) {
                    datePicker.setValue(LocalDate.parse(selected.getSessionDate()));
                }
                componentBox.setValue(selected.getComponent());
                statusBox.setValue(selected.getStatus());
                updateStatus.setValue(selected.getStatus());
            }
        });

        HBox addBar = new HBox(8, attendanceIdField, studentIdField, courseField, datePicker, componentBox, statusBox,
                addBtn, editBtn);
        HBox filterBar = new HBox(8, new Label("Filter:"), filterStudent, filterCourse, filterComponent, refreshBtn);
        HBox updateBar = new HBox(8, new Label("New Status:"), updateStatus, updateBtn);

        VBox body;
        if (selfOnly) {
            List<CourseUnit> courses = attendanceDao.getCoursesByStudent(currentUser.getId());
            for (CourseUnit c : courses) {
                ugCourseSelector.getItems().add(c.getCourseCode() + " - " + c.getTitle());
            }

            if (!ugCourseSelector.getItems().isEmpty()) {
                ugCourseSelector.setValue(ugCourseSelector.getItems().get(0));
                filterCourse.setText(parseCourseCode(ugCourseSelector.getValue()));
            }

            ugCourseSelector.setOnAction(e -> {
                String selected = ugCourseSelector.getValue();
                if (selected != null) {
                    filterCourse.setText(parseCourseCode(selected));
                    refreshBtn.fire();
                }
            });

            HBox courseBar = new HBox(10, new Label("Course:"), ugCourseSelector, filterComponent, refreshBtn);
            HBox hoursBar = new HBox(10, ugHoursLabel, ugHoursBar);
            body = new VBox(10, courseBar, hoursBar, table);
        } else if (canManage) {
            body = new VBox(10, addBar, filterBar, table, updateBar);
        } else {
            body = new VBox(10, filterBar, table);
        }
        body.setPadding(new Insets(16));
        page.setCenter(body);

        root.getChildren().setAll(page);
        refreshBtn.fire();
    }

    private void openMedical(boolean canManage, boolean selfOnly) {
        BorderPane page = buildShell("Medical");

        boolean canSubmitMedical = selfOnly;

        TextField refField = new TextField();
        refField.setPromptText("Ref No");
        refField.setText(generateMedicalRefNo());
        refField.setEditable(false);

        TextField studentId = new TextField(selfOnly ? currentUser.getId() : "");
        studentId.setPromptText("Student ID");
        studentId.setEditable(false);

        TextArea reason = new TextArea();
        reason.setPromptText("Reason");
        reason.setPrefRowCount(2);

        TextField proofPath = new TextField();
        proofPath.setPromptText("Medical image path");
        proofPath.setEditable(false);

        Button uploadBtn = new Button("Upload Photo");
        uploadBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select medical image");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.webp"));
            java.io.File file = chooser.showOpenDialog(root.getScene() == null ? null : root.getScene().getWindow());
            if (file != null) {
                proofPath.setText(file.getAbsolutePath());
            }
        });

        DatePicker startDate = new DatePicker(LocalDate.now());
        DatePicker endDate = new DatePicker(LocalDate.now());

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Pending", "Approved", "Rejected");
        statusBox.setValue("Pending");
        statusBox.setDisable(true);

        Button addBtn = new Button("Submit Medical");
        addBtn.setDisable(!canSubmitMedical);

        TextField filterStudent = new TextField(selfOnly ? currentUser.getId() : "");
        filterStudent.setPromptText("Filter by Student ID");
        filterStudent.setEditable(canManage);

        Button refreshBtn = new Button("Refresh");

        TableView<MedicalRecord> table = new TableView<>(medicalRows);

        TableColumn<MedicalRecord, String> refCol = new TableColumn<>("Ref No");
        refCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getRefNo()));
        TableColumn<MedicalRecord, String> stuCol = new TableColumn<>("Student");
        stuCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getStudentId()));
        TableColumn<MedicalRecord, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getStatus()));
        TableColumn<MedicalRecord, String> startCol = new TableColumn<>("Start");
        startCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getStartDate()));
        TableColumn<MedicalRecord, String> endCol = new TableColumn<>("End");
        endCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getEndDate()));
        TableColumn<MedicalRecord, String> proofCol = new TableColumn<>("Photo");
        proofCol.setCellValueFactory(x -> new SimpleStringProperty(
                x.getValue().getProofImagePath() == null ? "-" : x.getValue().getProofImagePath()));

        table.getColumns().add(refCol);
        table.getColumns().add(stuCol);
        table.getColumns().add(statusCol);
        table.getColumns().add(startCol);
        table.getColumns().add(endCol);
        table.getColumns().add(proofCol);

        ComboBox<String> newStatus = new ComboBox<>();
        newStatus.getItems().addAll("Pending", "Approved", "Rejected");
        newStatus.setValue("Pending");
        Button updateStatusBtn = new Button("Update Selected Status");
        updateStatusBtn.setDisable(!canManage);

        Button editBtn = new Button("Edit Selected Medical");
        editBtn.setDisable(!canManage);

        Label selectedPhoto = new Label("Selected Photo: -");
        selectedPhoto.setWrapText(true);

        addBtn.setOnAction(e -> {
            try {
                MedicalRecord record = new MedicalRecord(
                        refField.getText().trim(),
                        studentId.getText().trim(),
                        reason.getText().trim(),
                        "Pending",
                        startDate.getValue().toString(),
                        endDate.getValue().toString(),
                        proofPath.getText().trim());
                medicalDao.addMedical(record);
                reason.clear();
                proofPath.clear();
                startDate.setValue(LocalDate.now());
                endDate.setValue(LocalDate.now());
                refField.setText(generateMedicalRefNo());
                refreshBtn.fire();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, selected) -> {
            if (selected != null) {
                refField.setText(selected.getRefNo());
                studentId.setText(selected.getStudentId());
                reason.setText(selected.getReason());
                statusBox.setValue(selected.getStatus());
                if (selected.getStartDate() != null) {
                    startDate.setValue(LocalDate.parse(selected.getStartDate()));
                }
                if (selected.getEndDate() != null) {
                    endDate.setValue(LocalDate.parse(selected.getEndDate()));
                }
                proofPath.setText(selected.getProofImagePath() == null ? "" : selected.getProofImagePath());
                selectedPhoto.setText("Selected Photo: "
                        + (selected.getProofImagePath() == null || selected.getProofImagePath().isBlank()
                                ? "-"
                                : selected.getProofImagePath()));
            }
        });

        refreshBtn.setOnAction(e -> {
            try {
                medicalRows.setAll(medicalDao.findMedicalByStudent(filterStudent.getText().trim()));
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        updateStatusBtn.setOnAction(e -> {
            MedicalRecord selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Select a medical record first");
                return;
            }
            try {
                medicalDao.updateMedicalStatus(selected.getRefNo(), newStatus.getValue());
                refreshBtn.fire();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        editBtn.setOnAction(e -> {
            MedicalRecord selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Select a medical record first");
                return;
            }

            if (selfOnly && !currentUser.getId().equals(selected.getStudentId())) {
                showError("You can edit only your own medical records");
                return;
            }

            if (!canManage) {
                showError("Only Technical Officer can edit medical records");
                return;
            }

            try {
                MedicalRecord updated = new MedicalRecord(
                        selected.getRefNo(),
                        selected.getStudentId(),
                        reason.getText().trim(),
                        statusBox.getValue(),
                        startDate.getValue().toString(),
                        endDate.getValue().toString(),
                        proofPath.getText().trim());
                medicalDao.updateMedical(updated);
                refreshBtn.fire();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        HBox addBar1 = new HBox(8, refField, studentId, statusBox, startDate, endDate, addBtn);
        HBox addBar2 = new HBox(8, new Label("Reason:"), reason);
        HBox addBar3 = new HBox(8, new Label("Photo:"), proofPath, uploadBtn, editBtn);
        HBox filterBar = new HBox(8, new Label("Filter:"), filterStudent, refreshBtn);
        HBox updateBar = new HBox(8, new Label("New Status:"), newStatus, updateStatusBtn);

        VBox body;
        if (canManage) {
            body = new VBox(10, filterBar, table, updateBar, selectedPhoto);
        } else if (canSubmitMedical) {
            body = new VBox(10, addBar1, addBar2, addBar3, filterBar, table);
        } else {
            body = new VBox(10, filterBar, table);
        }
        body.setPadding(new Insets(16));
        page.setCenter(body);

        root.getChildren().setAll(page);
        refreshBtn.fire();
    }

    private String parseCourseCode(String courseDisplay) {
        int idx = courseDisplay.indexOf(" - ");
        if (idx <= 0) {
            return courseDisplay;
        }
        return courseDisplay.substring(0, idx).trim();
    }

    private String generateMedicalRefNo() {
        long value = Math.abs(System.currentTimeMillis() % 1_000_000_000L);
        return String.format("REF%09d", value);
    }

    private void openSummary() {
        BorderPane page = buildShell("Attendance Eligibility");

        TextField courseField = new TextField("ICT2132");
        courseField.setPromptText("Course Code");

        ComboBox<String> componentBox = new ComboBox<>();
        componentBox.getItems().addAll("Combined", "Theory", "Practical");
        componentBox.setValue("Combined");

        CheckBox includeMedical = new CheckBox("Count approved medical absences as present");
        includeMedical.setSelected(true);

        Button loadBtn = new Button("Generate Summary");

        TableView<AttendanceSummary> table = new TableView<>(summaryRows);

        TableColumn<AttendanceSummary, String> stuCol = new TableColumn<>("Student ID");
        stuCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getStudentId()));
        TableColumn<AttendanceSummary, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getStudentName()));
        TableColumn<AttendanceSummary, String> pctCol = new TableColumn<>("Attendance %");
        pctCol.setCellValueFactory(x -> new SimpleStringProperty(String.format("%.2f", x.getValue().getPercentage())));
        TableColumn<AttendanceSummary, String> eligCol = new TableColumn<>("Eligibility");
        eligCol.setCellValueFactory(
                x -> new SimpleStringProperty(x.getValue().isEligible() ? "Eligible" : "Not Eligible"));

        table.getColumns().add(stuCol);
        table.getColumns().add(nameCol);
        table.getColumns().add(pctCol);
        table.getColumns().add(eligCol);

        loadBtn.setOnAction(e -> {
            try {
                summaryRows.setAll(attendanceService.getSummaryForCourse(
                        courseField.getText().trim(),
                        componentBox.getValue(),
                        includeMedical.isSelected()));
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        HBox controls = new HBox(8,
                new Label("Course:"), courseField,
                new Label("Component:"), componentBox,
                includeMedical,
                loadBtn);

        VBox body = new VBox(10, controls, table);
        body.setPadding(new Insets(16));
        page.setCenter(body);

        root.getChildren().setAll(page);
        loadBtn.fire();
    }

    private void openPlaceholder(String message) {
        BorderPane page = buildShell("Module");

        VBox box = new VBox(10);
        box.setPadding(new Insets(30));
        box.setAlignment(Pos.CENTER_LEFT);

        Label t = new Label("Feature Placeholder");
        t.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label d = new Label(message);
        d.setWrapText(true);

        Label p = new Label("Your part is fully available now: User + Attendance + Medical.");

        box.getChildren().addAll(t, d, p);
        page.setCenter(box);

        root.getChildren().setAll(page);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Operation Failed");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Completed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private record FeatureCard(String icon, String title, String description, Runnable action) {
    }
}
