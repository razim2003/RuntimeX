package com.runtimex.tecmis.ui;

import com.runtimex.tecmis.dao.AttendanceDao;
import com.runtimex.tecmis.dao.CourseMaterialDao;
import com.runtimex.tecmis.dao.MedicalDao;
import com.runtimex.tecmis.dao.MarksDao;
import com.runtimex.tecmis.dao.TimetableDao;
import com.runtimex.tecmis.dao.UserDao;
import com.runtimex.tecmis.models.*;
import com.runtimex.tecmis.services.impl.AttendanceServiceImpl;
import java.awt.Desktop;
import com.runtimex.tecmis.services.interfaces.MarksService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
    private final MarksDao marksDao;
    private final MarksService marksService;
    private final CourseMaterialDao materialDao;
    private final TimetableDao timetableDao;
    private final com.runtimex.tecmis.dao.CourseUnitDAO courseUnitDAO;
    private final com.runtimex.tecmis.dao.NoticeDao noticeDao;

    private final ObservableList<UserProfile>         userRows         = FXCollections.observableArrayList();
    private final ObservableList<AttendanceRecord>    attendanceRows   = FXCollections.observableArrayList();
    private final ObservableList<MedicalRecord>       medicalRows      = FXCollections.observableArrayList();
    private final ObservableList<CourseResultSummary> courseResultRows = FXCollections.observableArrayList();
    private final ObservableList<StudentGpaSummary>   gpaRows          = FXCollections.observableArrayList();
    private final ObservableList<CourseMaterial>      materialRows     = FXCollections.observableArrayList();
    private final ObservableList<Timetable>           timetableRows    = FXCollections.observableArrayList();
    private final ObservableList<CourseUnit>          courseRows       = FXCollections.observableArrayList();
    private final ObservableList<Notice>              noticeRows       = FXCollections.observableArrayList();
    private final ObservableList<UserProfile>         ugRows           = FXCollections.observableArrayList();

    private final StackPane root = new StackPane();
    private AuthUser currentUser;

    // ─────────────────────────────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────────────────────────────

    public TecmisDashboard(UserDao userDao, AttendanceDao attendanceDao, MedicalDao medicalDao,
                           MarksDao marksDao, MarksService marksService,
                           CourseMaterialDao materialDao, TimetableDao timetableDao,
                           com.runtimex.tecmis.dao.CourseUnitDAO courseUnitDAO,
                           com.runtimex.tecmis.dao.NoticeDao noticeDao) {
        this.userDao       = userDao;
        this.attendanceDao = attendanceDao;
        this.medicalDao    = medicalDao;
        this.marksDao      = marksDao;
        this.marksService  = marksService;
        this.materialDao   = materialDao;
        this.timetableDao  = timetableDao;
        this.courseUnitDAO = courseUnitDAO;
        this.noticeDao     = noticeDao;
    }

    public Parent build() {
        showLogin();
        return root;
    }

    // ─────────────────────────────────────────────────────────────────────
    // Login
    // ─────────────────────────────────────────────────────────────────────

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

        Label sub = new Label("Role-based access for User, Attendance, Medical, Course Materials and Timetable");
        sub.setWrapText(true);
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
            String userId   = userIdField.getText().trim();
            String password = passwordField.getText().trim();
            if (userId.isEmpty() || password.isEmpty()) { showError("Enter both User ID and password"); return; }
            try {
                AuthUser authUser = userDao.authenticate(userId, password);
                if (authUser == null) { showError("Invalid credentials"); return; }
                currentUser = authUser;
                showHome();
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        card.getChildren().addAll(heading, sub, userIdField, passwordField, loginBtn, note);
        StackPane center = new StackPane(card);
        center.setPadding(new Insets(24));
        page.setCenter(center);
        root.getChildren().setAll(page);
    }

    // ─────────────────────────────────────────────────────────────────────
    // Home / Shell
    // ─────────────────────────────────────────────────────────────────────

    private void showHome() {
        BorderPane page = buildShell("Home");
        GridPane cards  = new GridPane();
        cards.setHgap(12); cards.setVgap(12); cards.setPadding(new Insets(16));

        List<FeatureCard> featureCards = getFeatureCardsForRole(currentUser.getUserType());
        int col = 0, row = 0;
        for (FeatureCard fc : featureCards) {
            cards.add(card(fc.icon + "  " + fc.title, fc.description, fc.action), col, row);
            if (++col == 3) { col = 0; row++; }
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

        Label title   = new Label("TecMIS");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");
        Label sec     = new Label("| " + section);
        sec.setStyle("-fx-font-size: 15px; -fx-text-fill: #bfdbfe;");
        Label userInfo = new Label(currentUser.getFullName() + "  (" + currentUser.getUserType() + ")");
        userInfo.setStyle("-fx-text-fill: #cbd5e1;");

        Region gap = new Region(); HBox.setHgrow(gap, Priority.ALWAYS);

        Button homeBtn   = new Button("🏠 Home");
        homeBtn.setOnAction(e -> showHome());
        Button logoutBtn = new Button("⏻ Logout");
        logoutBtn.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> { currentUser = null; showLogin(); });

        topBar.getChildren().addAll(title, sec, gap, buildTopBarAvatar(), userInfo, homeBtn, logoutBtn);
        page.setTop(topBar);
        return page;
    }

    private StackPane buildTopBarAvatar() {
        StackPane avatar = new StackPane();
        avatar.setPrefSize(34, 34); avatar.setMinSize(34, 34); avatar.setMaxSize(34, 34);
        avatar.setStyle("-fx-background-color: #1e293b; -fx-background-radius: 17;"
                + "-fx-border-color: #64748b; -fx-border-radius: 17; -fx-border-width: 1;");
        try {
            UserProfile profile = userDao.findById(currentUser.getId());
            String imagePath = profile == null ? null : profile.getProfileImagePath();
            if (imagePath != null && !imagePath.isBlank()) {
                File file = new File(imagePath);
                if (file.exists() && file.isFile()) {
                    Image image = new Image(file.toURI().toString(), 34, 34, true, true);
                    ImageView iv = new ImageView(image);
                    iv.setFitWidth(34); iv.setFitHeight(34); iv.setPreserveRatio(false);
                    iv.setClip(new Circle(17, 17, 17));
                    avatar.getChildren().add(iv);
                    return avatar;
                }
            }
        } catch (Exception ignored) {}
        Label fallback = new Label("👤");
        fallback.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 14px;");
        avatar.getChildren().add(fallback);
        return avatar;
    }

    private VBox card(String title, String description, Runnable action) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(14)); box.setMinHeight(130);
        box.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 14;"
                + "-fx-border-color: #94a3b8; -fx-border-radius: 14; -fx-border-width: 1;");
        Label t = new Label(title);
        t.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");
        Label d = new Label(description); d.setWrapText(true);
        d.setStyle("-fx-text-fill: #334155;");
        Button open = new Button("Open");
        open.setStyle("-fx-background-color: #0ea5e9; -fx-text-fill: white;");
        open.setOnAction(e -> action.run());
        box.getChildren().addAll(t, d, open);
        return box;
    }

    // ─────────────────────────────────────────────────────────────────────
    // Role → feature cards
    // ─────────────────────────────────────────────────────────────────────

    private List<FeatureCard> getFeatureCardsForRole(String role) {
        List<FeatureCard> list = new ArrayList<>();

        if ("Admin".equals(role)) {
            list.add(new FeatureCard("🙍", "My Profile",    "Update profile picture and contact details", this::openMyProfileEditor));
            list.add(new FeatureCard("👥", "User Profiles", "Create and maintain user profiles",           this::openUserManagement));
            list.add(new FeatureCard("📚", "Courses",       "Create and maintain course details",          this::openCourseManagement));
            list.add(new FeatureCard("📢", "Notices",       "Create and maintain notices",                 this::openNoticeManagement));
            list.add(new FeatureCard("📰", "Notice Board",  "See notices",                                  this::openNoticeBoard));
            list.add(new FeatureCard("🗓", "Timetables",    "Create and maintain timetables",              this::openAdminTimetable));
            return list;
        }

        if ("Lecturer".equals(role)) {
            list.add(new FeatureCard("🙍", "My Profile",       "Update profile except username/password",      this::openMyProfileEditor));
            list.add(new FeatureCard("📘", "Course Materials", "Create and modify materials for your courses", this::openLecturerMaterials));
            list.add(new FeatureCard("📋", "Attendance View",  "See undergraduate attendance and summary",     () -> openAttendance(false, false)));
            list.add(new FeatureCard("🩺", "Medical View",     "See undergraduate medical records",            () -> openMedical(false, false)));
            list.add(new FeatureCard("🎓", "Undergraduate Details", "See undergraduate details",               this::openUndergraduateDetails));
            list.add(new FeatureCard("🧮", "Eligibility",      "See undergraduate eligibility",                this::openSummary));
            list.add(new FeatureCard("📝", "Upload Marks",     "Upload marks for all kinds of exams",          this::openMarksUpload));
            list.add(new FeatureCard("📈", "Marks, Grades, GPA", "See undergraduate marks, grades and GPA",   this::openMarksOverview));
            list.add(new FeatureCard("📢", "Notices",          "See notices",                                  this::openNoticeBoard));
            return list;
        }

        if ("TechnicalOfficer".equals(role)) {
            list.add(new FeatureCard("🙍", "My Profile",          "Update profile except username/password", this::openMyProfileEditor));
            list.add(new FeatureCard("🗂", "Attendance",          "Add and maintain attendance details",     () -> openAttendance(true, false)));
            list.add(new FeatureCard("🩹", "Medical",             "Add and maintain medical details",        () -> openMedical(true, false)));
            list.add(new FeatureCard("📢", "Notices",             "See notices",                             this::openNoticeBoard));
            list.add(new FeatureCard("🗓", "Department Timetable","See timetables for your department",      this::openTOTimetable));
            return list;
        }

        // Undergraduate (default)
        list.add(new FeatureCard("🙍", "My Profile",      "Update only contact details and profile picture", this::openMyProfileEditor));
        list.add(new FeatureCard("📋", "My Attendance",   "See your attendance details",                     () -> openAttendance(false, true)));
        list.add(new FeatureCard("🩺", "My Medical",      "See your medical details",                        () -> openMedical(false, true)));
        list.add(new FeatureCard("📘", "Course Materials","View materials for your enrolled courses",         this::openUndergraduateMaterials));
        list.add(new FeatureCard("📈", "My Grades & GPA", "See your grades and GPA",                         this::openMarksOverview));
        list.add(new FeatureCard("🗓", "My Timetable",    "See your class timetable",                        this::openUndergraduateTimetable));
        list.add(new FeatureCard("📢", "Notices",         "See notices",                                     this::openNoticeBoard));
        return list;
    }

    // ═════════════════════════════════════════════════════════════════════
    // ── COURSE MATERIALS ─────────────────────────────────────────────────
    // ═════════════════════════════════════════════════════════════════════

    /**
     * Lecturer panel: create / edit / delete materials for their assigned courses.
     */
    // ─────────────────────────────────────────────────────────────────────
    // Shared material storage root: <user.home>/tecmis_materials/
    // Files are copied here on upload so the stored path is always stable
    // and accessible to every user on the same machine.
    // ─────────────────────────────────────────────────────────────────────
    private static final java.nio.file.Path MATERIAL_STORE =
            java.nio.file.Paths.get(System.getProperty("user.home"), "tecmis_materials");

    /** Ensures the material store directory exists and returns it. */
    private java.nio.file.Path materialStore() {
        try { java.nio.file.Files.createDirectories(MATERIAL_STORE); }
        catch (Exception ignored) {}
        return MATERIAL_STORE;
    }

    /**
     * Copies the source file into the material store and returns the stored path string.
     * The filename is prefixed with the material ID to avoid collisions.
     */
    private String copyToStore(File source, String materialId) throws java.io.IOException {
        String safeName = materialId + "_" + source.getName()
                .replaceAll("[^a-zA-Z0-9._\\-]", "_");
        java.nio.file.Path dest = materialStore().resolve(safeName);
        java.nio.file.Files.copy(source.toPath(), dest,
                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        return dest.toString();
    }

    // ─────────────────────────────────────────────────────────────────────
    // Lecturer: create / edit / delete course materials
    // ─────────────────────────────────────────────────────────────────────
    private void openLecturerMaterials() {
        BorderPane page = buildShell("Course Materials");

        // ── course selector ──
        ComboBox<String> courseBox = new ComboBox<>();
        courseBox.setPromptText("Select course"); courseBox.setPrefWidth(300);
        try {
            List<CourseUnit> courses = marksService.getAllCourses();
            for (CourseUnit c : courses)
                courseBox.getItems().add(c.getCourseCode() + " - " + c.getTitle());
            if (!courseBox.getItems().isEmpty()) courseBox.getSelectionModel().select(0);
        } catch (Exception ex) { showError(ex.getMessage()); }

        // ── form fields ──
        TextField    titleField  = new TextField();  titleField.setPromptText("Title");
        TextArea     descArea    = new TextArea();    descArea.setPromptText("Description (optional)"); descArea.setPrefRowCount(2);
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("File", "Link", "Text"); typeBox.setValue("File");

        // File row – shown only when type == "File"
        Label    fileLabel  = new Label("No file selected");
        fileLabel.setStyle("-fx-text-fill: #475569;");
        fileLabel.setMaxWidth(340); fileLabel.setWrapText(true);
        Button   browseBtn  = new Button("📂 Browse…");
        // We keep a reference to the chosen file so we can copy it on Add/Update
        final File[] chosenFile = {null};
        browseBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Select material file");
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Documents", "*.pdf","*.docx","*.doc","*.pptx","*.xlsx","*.txt"),
                    new FileChooser.ExtensionFilter("Images",    "*.png","*.jpg","*.jpeg","*.gif","*.webp"),
                    new FileChooser.ExtensionFilter("All files", "*.*"));
            File f = fc.showOpenDialog(root.getScene() == null ? null : root.getScene().getWindow());
            if (f != null) {
                chosenFile[0] = f;
                fileLabel.setText(f.getName() + "  (" + humanSize(f.length()) + ")");
                typeBox.setValue("File");
            }
        });
        HBox fileRow = new HBox(8, browseBtn, fileLabel);
        fileRow.setAlignment(Pos.CENTER_LEFT);

        // Link / Text row – shown for non-File types
        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Paste URL (Link) or write text content (Text)");
        contentArea.setPrefRowCount(3);

        // Toggle visibility based on type
        typeBox.setOnAction(e -> {
            boolean isFile = "File".equals(typeBox.getValue());
            fileRow.setVisible(isFile); fileRow.setManaged(isFile);
            contentArea.setVisible(!isFile); contentArea.setManaged(!isFile);
        });
        // initial state
        contentArea.setVisible(false); contentArea.setManaged(false);

        // ── table ──
        TableView<CourseMaterial> table = new TableView<>(materialRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        addMaterialColumns(table, true);

        Button loadBtn   = new Button("Load");
        Button addBtn    = new Button("✚ Add");
        Button updateBtn = new Button("✎ Update");
        Button deleteBtn = new Button("✖ Delete");
        addBtn.setStyle   ("-fx-background-color: #16a34a; -fx-text-fill: white;");
        updateBtn.setStyle("-fx-background-color: #0284c7; -fx-text-fill: white;");
        deleteBtn.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white;");

        // Load
        loadBtn.setOnAction(e -> {
            String code = parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            if (code.isBlank()) { showError("Select a course first"); return; }
            try { materialRows.setAll(materialDao.getMaterialsByLecturerAndCourse(currentUser.getId(), code)); }
            catch (Exception ex) { showError(ex.getMessage()); }
        });

        // Add
        addBtn.setOnAction(e -> {
            String code = parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            if (code.isBlank() || titleField.getText().isBlank()) {
                showError("Course and title are required"); return;
            }
            String type    = typeBox.getValue();
            String content = "";
            if ("File".equals(type)) {
                if (chosenFile[0] == null) { showError("Please select a file to upload"); return; }
                try { content = copyToStore(chosenFile[0], generateId("CM")); }
                catch (Exception ex) { showError("File copy failed: " + ex.getMessage()); return; }
            } else {
                content = contentArea.getText().trim();
            }
            CourseMaterial m = new CourseMaterial(
                    generateId("CM"), code, currentUser.getId(),
                    titleField.getText().trim(), descArea.getText().trim(),
                    type, content, LocalDate.now().toString());
            try {
                materialDao.addMaterial(m);
                clearLecturerMaterialForm(titleField, descArea, contentArea, fileLabel, chosenFile);
                loadBtn.fire(); showInfo("Material added successfully");
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        // Populate form when a row is selected
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, sel) -> {
            if (sel == null) return;
            titleField.setText(sel.getTitle());
            descArea.setText(sel.getDescription() == null ? "" : sel.getDescription());
            typeBox.setValue(sel.getMaterialType());
            boolean isFile = "File".equals(sel.getMaterialType());
            fileRow.setVisible(isFile); fileRow.setManaged(isFile);
            contentArea.setVisible(!isFile); contentArea.setManaged(!isFile);
            if (isFile) {
                chosenFile[0] = null; // clear staged file; existing path is already in DB
                String stored = sel.getContent();
                fileLabel.setText(stored == null || stored.isBlank() ? "No file stored"
                        : new File(stored).getName());
            } else {
                contentArea.setText(sel.getContent() == null ? "" : sel.getContent());
            }
        });

        // Update
        updateBtn.setOnAction(e -> {
            CourseMaterial sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Select a material first"); return; }
            sel.setTitle(titleField.getText().trim());
            sel.setDescription(descArea.getText().trim());
            sel.setMaterialType(typeBox.getValue());
            sel.setUploadedDate(LocalDate.now().toString());
            if ("File".equals(typeBox.getValue())) {
                // Only replace the stored file if a new one was chosen
                if (chosenFile[0] != null) {
                    try { sel.setContent(copyToStore(chosenFile[0], sel.getMaterialId())); }
                    catch (Exception ex) { showError("File copy failed: " + ex.getMessage()); return; }
                }
                // else keep existing stored path (already in sel.getContent())
            } else {
                sel.setContent(contentArea.getText().trim());
            }
            try {
                boolean ok = materialDao.updateMaterial(sel, currentUser.getId());
                if (ok) { loadBtn.fire(); showInfo("Material updated"); }
                else     showError("Update failed – you may not own this material");
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        // Delete
        deleteBtn.setOnAction(e -> {
            CourseMaterial sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Select a material first"); return; }
            try {
                boolean ok = materialDao.deleteMaterial(sel.getMaterialId(), currentUser.getId());
                if (ok) { loadBtn.fire(); showInfo("Material deleted"); }
                else     showError("Delete failed – you may not own this material");
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        HBox courseBar = new HBox(8, new Label("Course:"), courseBox, loadBtn);
        HBox typeBar   = new HBox(8, new Label("Type:"), typeBox);
        HBox actionBar = new HBox(8, addBtn, updateBtn, deleteBtn);
        VBox form = new VBox(8,
                new Label("Title:"), titleField,
                new Label("Description:"), descArea,
                typeBar, fileRow, contentArea,
                actionBar);
        form.setPadding(new Insets(0, 0, 8, 0));

        VBox body = new VBox(10, courseBar, form, table);
        body.setPadding(new Insets(16));
        page.setCenter(body);
        root.getChildren().setAll(page);
        loadBtn.fire();
    }

    // ─────────────────────────────────────────────────────────────────────
    // Undergraduate: view, open and download course materials
    // ─────────────────────────────────────────────────────────────────────
    private void openUndergraduateMaterials() {
        BorderPane page = buildShell("Course Materials");

        ComboBox<String> courseBox = new ComboBox<>();
        courseBox.setPromptText("Select course"); courseBox.setPrefWidth(300);
        try {
            List<CourseUnit> courses = marksService.getCoursesByStudent(currentUser.getId());
            for (CourseUnit c : courses)
                courseBox.getItems().add(c.getCourseCode() + " - " + c.getTitle());
            if (!courseBox.getItems().isEmpty()) courseBox.getSelectionModel().select(0);
        } catch (Exception ex) { showError(ex.getMessage()); }

        // ── table ──
        TableView<CourseMaterial> table = new TableView<>(materialRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        addMaterialColumns(table, false);

        // ── detail / action panel (shown below table on selection) ──
        Label typeLabel = new Label();
        typeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #0f172a;");

        Label titleLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1e40af;");

        Label descLabel = new Label();
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-text-fill: #334155;");

        // For Text/Link – show content inline
        TextArea contentView = new TextArea();
        contentView.setEditable(false);
        contentView.setPrefRowCount(4);
        contentView.setPromptText("Content will appear here for Text/Link materials");

        // For File materials – Open and Download buttons
        Button openFileBtn     = new Button("📂 Open File");
        Button downloadFileBtn = new Button("⬇ Download / Save As…");
        openFileBtn.setStyle    ("-fx-background-color: #0284c7; -fx-text-fill: white; -fx-font-weight: bold;");
        downloadFileBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");
        openFileBtn.setVisible(false);     openFileBtn.setManaged(false);
        downloadFileBtn.setVisible(false); downloadFileBtn.setManaged(false);

        Label fileInfoLabel = new Label();
        fileInfoLabel.setStyle("-fx-text-fill: #475569;");

        HBox fileActionBar = new HBox(10, openFileBtn, downloadFileBtn, fileInfoLabel);
        fileActionBar.setAlignment(Pos.CENTER_LEFT);

        // Wire up selection
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, sel) -> {
            if (sel == null) return;
            typeLabel.setText("[" + sel.getMaterialType() + "]");
            titleLabel.setText(sel.getTitle());
            descLabel.setText(sel.getDescription() == null ? "" : sel.getDescription());

            boolean isFile = "File".equals(sel.getMaterialType());
            openFileBtn.setVisible(isFile);     openFileBtn.setManaged(isFile);
            downloadFileBtn.setVisible(isFile); downloadFileBtn.setManaged(isFile);
            contentView.setVisible(!isFile);    contentView.setManaged(!isFile);

            if (isFile) {
                String storedPath = sel.getContent();
                File f = storedPath == null ? null : new File(storedPath);
                boolean exists = f != null && f.exists() && f.isFile();
                fileInfoLabel.setText(exists
                        ? f.getName() + "  (" + humanSize(f.length()) + ")"
                        : "⚠ File not found on disk: " + storedPath);
                openFileBtn.setDisable(!exists);
                downloadFileBtn.setDisable(!exists);

                // Open in system default app
                openFileBtn.setOnAction(ev -> {
                    try { java.awt.Desktop.getDesktop().open(f); }
                    catch (Exception ex) { showError("Cannot open file: " + ex.getMessage()); }
                });

                // Copy to user-chosen location
                downloadFileBtn.setOnAction(ev -> {
                    FileChooser fc = new FileChooser();
                    fc.setTitle("Save file as…");
                    fc.setInitialFileName(f.getName());
                    // Pre-set extension filter based on file extension
                    String ext = fileExtension(f.getName());
                    if (!ext.isEmpty())
                        fc.getExtensionFilters().add(
                                new FileChooser.ExtensionFilter(ext.toUpperCase() + " file", "*." + ext));
                    fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files","*.*"));
                    File dest = fc.showSaveDialog(root.getScene() == null ? null : root.getScene().getWindow());
                    if (dest == null) return;
                    try {
                        java.nio.file.Files.copy(f.toPath(), dest.toPath(),
                                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        showInfo("File saved to:\n" + dest.getAbsolutePath());
                    } catch (Exception ex) { showError("Download failed: " + ex.getMessage()); }
                });
            } else {
                contentView.setText(sel.getContent() == null ? "" : sel.getContent());
            }
        });

        Button loadBtn = new Button("Load");
        loadBtn.setOnAction(e -> {
            String code = parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            if (code.isBlank()) { showError("Select a course first"); return; }
            try { materialRows.setAll(materialDao.getMaterialsByCourse(code)); }
            catch (Exception ex) { showError(ex.getMessage()); }
        });
        courseBox.setOnAction(e -> loadBtn.fire());

        VBox detailPane = new VBox(6, typeLabel, titleLabel, descLabel, fileActionBar, contentView);
        detailPane.setPadding(new Insets(10, 0, 0, 0));

        Separator sep = new Separator();

        HBox courseBar = new HBox(8, new Label("Course:"), courseBox, loadBtn);
        VBox body = new VBox(10, courseBar, table, sep, detailPane);
        body.setPadding(new Insets(16));
        page.setCenter(body);
        root.getChildren().setAll(page);
        loadBtn.fire();
    }

    // ── Shared helpers ────────────────────────────────────────────────────

    /** Adds standard columns to a CourseMaterial TableView. */
    private void addMaterialColumns(TableView<CourseMaterial> table, boolean includeLec) {
        TableColumn<CourseMaterial, String> ttCol = col("Title",       x -> x.getTitle());
        TableColumn<CourseMaterial, String> tpCol = col("Type",        x -> x.getMaterialType());
        TableColumn<CourseMaterial, String> dtCol = col("Uploaded",    x -> x.getUploadedDate());
        TableColumn<CourseMaterial, String> dsCol = col("Description", x -> x.getDescription() == null ? "" : x.getDescription());
        // File column: show filename only (not full path) for cleanliness
        TableColumn<CourseMaterial, String> fnCol = col("File / Content",
                x -> "File".equals(x.getMaterialType())
                        ? (x.getContent() == null ? "-" : new File(x.getContent()).getName())
                        : (x.getContent() == null ? "" : x.getContent().length() > 60
                                ? x.getContent().substring(0, 60) + "…" : x.getContent()));
        table.getColumns().addAll(ttCol, tpCol, dtCol, dsCol, fnCol);
        if (includeLec) table.getColumns().add(col("Lecturer", x -> x.getLecId()));
    }

    private void clearLecturerMaterialForm(TextField titleField, TextArea descArea,
            TextArea contentArea, Label fileLabel, File[] chosenFile) {
        titleField.clear(); descArea.clear(); contentArea.clear();
        fileLabel.setText("No file selected"); chosenFile[0] = null;
    }

    /** Returns a human-readable file size string. */
    private String humanSize(long bytes) {
        if (bytes < 1024)           return bytes + " B";
        if (bytes < 1024 * 1024)    return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024));
    }

    /** Extracts the lowercase extension from a filename (without the dot). */
    private String fileExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        return idx < 0 ? "" : filename.substring(idx + 1).toLowerCase();
    }

    // ═════════════════════════════════════════════════════════════════════
    // ── TIMETABLE ────────────────────────────────────────────────────────
    // ═════════════════════════════════════════════════════════════════════

    /**
     * Admin panel: full CRUD for timetable.
     */
    private void openAdminTimetable() {
        BorderPane page = buildShell("Timetable Management");

        // ── form ──
        TextField idField     = new TextField(); idField.setPromptText("Timetable ID (auto)"); idField.setEditable(false);
        idField.setText(generateId("TT"));

        ComboBox<String> courseBox = new ComboBox<>(); courseBox.setPromptText("Course"); courseBox.setPrefWidth(220);
        try {
            List<CourseUnit> courses = marksService.getAllCourses();
            for (CourseUnit c : courses)
                courseBox.getItems().add(c.getCourseCode() + " - " + c.getTitle());
            if (!courseBox.getItems().isEmpty()) courseBox.getSelectionModel().select(0);
        } catch (Exception ex) { showError(ex.getMessage()); }

        TextField lecField    = new TextField(); lecField.setPromptText("Lecturer ID");
        TextField locationField = new TextField(); locationField.setPromptText("Location");
        TextField levelField  = new TextField(); levelField.setPromptText("Level (e.g. 1)");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Theory", "Practical"); typeBox.setValue("Theory");

        TextField hoursField  = new TextField(); hoursField.setPromptText("Hours");

        ComboBox<String> dayBox = new ComboBox<>();
        dayBox.getItems().addAll("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday");
        dayBox.setValue("Monday");

        TextField timeField   = new TextField(); timeField.setPromptText("Start time (HH:mm)"); timeField.setText("08:00");

        // ── table ──
        TableView<Timetable> table = new TableView<>(timetableRows);
        addTimetableColumns(table);

        Button loadBtn   = new Button("Load All");
        Button addBtn    = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");

        loadBtn.setOnAction(e -> {
            try { timetableRows.setAll(timetableDao.viewTimetable()); }
            catch (Exception ex) { showError(ex.getMessage()); }
        });

        addBtn.setOnAction(e -> {
            Timetable t = buildTimetableFromForm(idField, courseBox, lecField, locationField,
                    levelField, typeBox, hoursField, dayBox, timeField);
            if (t == null) return;
            try {
                timetableDao.addSession(t, currentUser.getId());
                idField.setText(generateId("TT"));
                loadBtn.fire(); showInfo("Session added");
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, sel) -> {
            if (sel != null) populateTimetableForm(sel, idField, courseBox, lecField,
                    locationField, levelField, typeBox, hoursField, dayBox, timeField);
        });

        updateBtn.setOnAction(e -> {
            Timetable sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Select a row first"); return; }
            Timetable t = buildTimetableFromForm(idField, courseBox, lecField, locationField,
                    levelField, typeBox, hoursField, dayBox, timeField);
            if (t == null) return;
            try { timetableDao.updateSession(t, currentUser.getId()); loadBtn.fire(); showInfo("Session updated"); }
            catch (Exception ex) { showError(ex.getMessage()); }
        });

        deleteBtn.setOnAction(e -> {
            Timetable sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Select a row first"); return; }
            try { timetableDao.deleteSession(sel.getTimetableId(), currentUser.getId()); loadBtn.fire(); showInfo("Session deleted"); }
            catch (Exception ex) { showError(ex.getMessage()); }
        });

        HBox row1 = new HBox(8, new Label("ID:"), idField, new Label("Course:"), courseBox, new Label("Lec ID:"), lecField);
        HBox row2 = new HBox(8, new Label("Location:"), locationField, new Label("Level:"), levelField,
                new Label("Type:"), typeBox, new Label("Hours:"), hoursField);
        HBox row3 = new HBox(8, new Label("Day:"), dayBox, new Label("Start:"), timeField);
        HBox row4 = new HBox(8, addBtn, updateBtn, deleteBtn, loadBtn);

        VBox body = new VBox(10, row1, row2, row3, row4, table);
        body.setPadding(new Insets(16));
        page.setCenter(body);
        root.getChildren().setAll(page);
        loadBtn.fire();
    }

    /**
     * Technical Officer panel: read-only, filtered by their department.
     * The TO's department is stored in technical_officer.department.
     * Here we load it via a simple query; fall back to full view if null.
     */
    private void openTOTimetable() {
        BorderPane page = buildShell("Department Timetable");

        // Resolve TO's department
        String dept = resolveTODepartment(currentUser.getId());

        TableView<Timetable> table = new TableView<>(timetableRows);
        addTimetableColumns(table);

        Label deptLabel = new Label("Department filter: " + (dept == null || dept.isBlank() ? "(all)" : dept));
        deptLabel.setStyle("-fx-text-fill: #334155;");

        Button loadBtn = new Button("Load");
        loadBtn.setOnAction(e -> {
            try { timetableRows.setAll(timetableDao.viewTimetableByDepartment(dept)); }
            catch (Exception ex) { showError(ex.getMessage()); }
        });

        HBox bar  = new HBox(10, deptLabel, loadBtn);
        VBox body = new VBox(10, bar, table);
        body.setPadding(new Insets(16));
        page.setCenter(body);
        root.getChildren().setAll(page);
        loadBtn.fire();
    }

    /**
     * Undergraduate panel: read-only view filtered to their study level.
     */
    private void openUndergraduateTimetable() {
        BorderPane page = buildShell("My Timetable");

        int level = timetableDao.getLevelForStudent(currentUser.getId());
        Label info = new Label("Showing timetable for Level " + level);
        info.setStyle("-fx-text-fill: #334155;");

        TableView<Timetable> table = new TableView<>(timetableRows);
        addTimetableColumns(table);

        Button loadBtn = new Button("Refresh");
        loadBtn.setOnAction(e -> {
            try { timetableRows.setAll(timetableDao.viewTimetableByLevel(level)); }
            catch (Exception ex) { showError(ex.getMessage()); }
        });

        HBox bar  = new HBox(10, info, loadBtn);
        VBox body = new VBox(10, bar, table);
        body.setPadding(new Insets(16));
        page.setCenter(body);
        root.getChildren().setAll(page);
        loadBtn.fire();
    }

    private void addTimetableColumns(TableView<Timetable> table) {
        table.getColumns().addAll(
                col("ID",       x -> x.getTimetableId()),
                col("Course",   x -> x.getCourseCode()),
                col("Lecturer", x -> x.getLecturerId() == null ? "-" : x.getLecturerId()),
                col("Level",    x -> String.valueOf(x.getLevel())),
                col("Type",     x -> x.getType()),
                col("Day",      x -> x.getDayOfWeek() == null ? "-" : x.getDayOfWeek()),
                col("Start",    x -> x.getStartTime() == null ? "-" : x.getStartTime()),
                col("Hours",    x -> String.valueOf(x.getHours())),
                col("Location", x -> x.getLocation())
        );
    }

    private Timetable buildTimetableFromForm(TextField idField, ComboBox<String> courseBox,
            TextField lecField, TextField locationField, TextField levelField,
            ComboBox<String> typeBox, TextField hoursField,
            ComboBox<String> dayBox, TextField timeField) {
        String code = parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
        if (code.isBlank() || locationField.getText().isBlank()
                || levelField.getText().isBlank() || hoursField.getText().isBlank()) {
            showError("Course, location, level and hours are required"); return null;
        }
        int level, hours;
        try { level = Integer.parseInt(levelField.getText().trim()); }
        catch (NumberFormatException ex) { showError("Level must be a number"); return null; }
        try { hours = Integer.parseInt(hoursField.getText().trim()); }
        catch (NumberFormatException ex) { showError("Hours must be a number"); return null; }

        Timetable t = new Timetable();
        t.setTimetableId(idField.getText().trim());
        t.setCourseCode (code);
        t.setLecturerId (lecField.getText().isBlank() ? null : lecField.getText().trim());
        t.setLocation   (locationField.getText().trim());
        t.setLevel      (level);
        t.setType       (typeBox.getValue());
        t.setHours      (hours);
        t.setDayOfWeek  (dayBox.getValue());
        t.setStartTime  (timeField.getText().isBlank() ? "08:00" : timeField.getText().trim());
        return t;
    }

    private void populateTimetableForm(Timetable sel,
            TextField idField, ComboBox<String> courseBox, TextField lecField,
            TextField locationField, TextField levelField,
            ComboBox<String> typeBox, TextField hoursField,
            ComboBox<String> dayBox, TextField timeField) {
        idField.setText(sel.getTimetableId());
        // find matching entry in courseBox
        for (String item : courseBox.getItems()) {
            if (item.startsWith(sel.getCourseCode())) { courseBox.setValue(item); break; }
        }
        lecField.setText      (sel.getLecturerId() == null ? "" : sel.getLecturerId());
        locationField.setText (sel.getLocation());
        levelField.setText    (String.valueOf(sel.getLevel()));
        typeBox.setValue      (sel.getType());
        hoursField.setText    (String.valueOf(sel.getHours()));
        if (sel.getDayOfWeek() != null) dayBox.setValue(sel.getDayOfWeek());
        if (sel.getStartTime() != null) timeField.setText(sel.getStartTime());
    }

    /** Reads department from technical_officer table for the logged-in TO. */
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

    // ═════════════════════════════════════════════════════════════════════
    // ── EXISTING PANELS (unchanged logic, kept intact) ───────────────────
    // ═════════════════════════════════════════════════════════════════════

    private void openUserManagement() {
        BorderPane page = buildShell("User Profiles");

        // ── Filter bar ──
        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.getItems().addAll("All","Admin","Lecturer","TechnicalOfficer","Undergraduate");
        typeFilter.setValue("All");
        TextField keywordField = new TextField();
        keywordField.setPromptText("Search by id / name / email");
        keywordField.setPrefWidth(240);
        Button loadBtn = new Button("🔍 Search");

        // ── Table ──
        TableView<UserProfile> table = new TableView<>(userRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<UserProfile,String> idCol      = col("ID",      x -> x.getId());
        TableColumn<UserProfile,String> nameCol    = col("Name",    x -> x.getFullName());
        TableColumn<UserProfile,String> emailCol   = col("Email",   x -> x.getEmail());
        TableColumn<UserProfile,String> contactCol = col("Contact", x -> x.getContactNo());
        TableColumn<UserProfile,String> typeCol    = col("Type",    x -> x.getUserType());
        TableColumn<UserProfile,String> statusCol  = col("Status",  x -> x.getStatus() == null ? "" : x.getStatus());
        idCol.setMinWidth(140); idCol.setPrefWidth(160);
        contactCol.setMinWidth(120); contactCol.setPrefWidth(150);
        table.getColumns().addAll(idCol, nameCol, emailCol, contactCol, typeCol, statusCol);
        table.setPrefHeight(280);

        // ── Edit contact bar ──
        TextField emailEdit   = new TextField(); emailEdit.setPromptText("Updated email");
        TextField contactEdit = new TextField(); contactEdit.setPromptText("Updated contact");
        ComboBox<String> statusEdit = new ComboBox<>();
        statusEdit.getItems().addAll("Proper", "Repeat", "Suspended");
        statusEdit.setDisable(true);
        Button    updateBtn   = new Button("💾 Update Contact");
        Button    updateStatusBtn = new Button("🧾 Update Status");
        updateStatusBtn.setDisable(true);
        Button    deleteBtn   = new Button("🗑 Delete User");
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
            try { userRows.setAll(userDao.findUsers(typeFilter.getValue(), keywordField.getText().trim())); }
            catch (Exception ex) { showError(ex.getMessage()); }
        });

        updateBtn.setOnAction(e -> {
            UserProfile sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Select a user first"); return; }
            try {
                userDao.updateUserContact(sel.getId(), emailEdit.getText().trim(), contactEdit.getText().trim());
                showInfo("Contact updated successfully");
                loadBtn.fire();
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        updateStatusBtn.setOnAction(e -> {
            UserProfile sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Select a user first"); return; }
            if (!"Undergraduate".equals(sel.getUserType())) { showError("Status applies only to undergraduates"); return; }
            String status = statusEdit.getValue();
            if (status == null || status.isBlank()) { showError("Select a status first"); return; }
            try {
                userDao.updateUndergraduateStatus(sel.getId(), status);
                showInfo("Undergraduate status updated");
                loadBtn.fire();
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        deleteBtn.setOnAction(e -> {
            UserProfile sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Select a user first"); return; }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete user " + sel.getId() + " – " + sel.getFullName() + "?\nThis cannot be undone.",
                    ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Confirm Delete");
            confirm.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) {
                    try { userDao.deleteUser(sel.getId()); showInfo("User deleted."); loadBtn.fire(); }
                    catch (Exception ex) { showError(ex.getMessage()); }
                }
            });
        });

        // ── Create User form ──
        Label createHeading = new Label("➕ Create New User");
        createHeading.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

        TextField newId        = new TextField(); newId.setPromptText("User ID  (e.g. LEC005)");
        TextField newFirst     = new TextField(); newFirst.setPromptText("First Name");
        TextField newLast      = new TextField(); newLast.setPromptText("Last Name");
        TextField newEmail     = new TextField(); newEmail.setPromptText("Email");
        TextField newContact   = new TextField(); newContact.setPromptText("Contact No");
        PasswordField newPwd   = new PasswordField(); newPwd.setPromptText("Password");
        ComboBox<String> newType = new ComboBox<>();
        newType.getItems().addAll("Admin","Lecturer","TechnicalOfficer","Undergraduate");
        newType.setPromptText("User Type");
        ComboBox<String> ugStatus = new ComboBox<>();
        ugStatus.getItems().addAll("Proper", "Repeat", "Suspended");
        ugStatus.setValue("Proper");
        ugStatus.setDisable(true);
        Button createBtn = new Button("✅ Create User");
        createBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");

        newType.setOnAction(e -> {
            boolean isUg = "Undergraduate".equals(newType.getValue());
            ugStatus.setDisable(!isUg);
            if (!isUg) { ugStatus.setValue("Proper"); }
        });

        GridPane createForm = new GridPane();
        createForm.setHgap(8); createForm.setVgap(6);
        createForm.setPadding(new Insets(10, 0, 0, 0));
        createForm.addRow(0, new Label("ID:"), newId, new Label("First Name:"), newFirst);
        createForm.addRow(1, new Label("Last Name:"), newLast, new Label("Email:"), newEmail);
        createForm.addRow(2, new Label("Contact:"), newContact, new Label("Password:"), newPwd);
        createForm.addRow(3, new Label("Type:"), newType, new Label("UG Status:"), ugStatus);
        createForm.addRow(4, new Label(""), createBtn);

        createBtn.setOnAction(e -> {
            String id   = newId.getText().trim();
            String fn   = newFirst.getText().trim();
            String ln   = newLast.getText().trim();
            String em   = newEmail.getText().trim();
            String cn   = newContact.getText().trim();
            String pw   = newPwd.getText();
            String tp   = newType.getValue();
            if (id.isEmpty() || fn.isEmpty() || ln.isEmpty() || em.isEmpty() || cn.isEmpty() || pw.isEmpty() || tp == null) {
                showError("All fields are required to create a user.");
                return;
            }
            try {
                userDao.createUser(id, fn, ln, em, cn, pw, tp);
                if ("Undergraduate".equals(tp)) {
                    userDao.updateUndergraduateStatus(id, ugStatus.getValue());
                }
                showInfo("User " + id + " created successfully.");
                newId.clear(); newFirst.clear(); newLast.clear();
                newEmail.clear(); newContact.clear(); newPwd.clear(); newType.setValue(null);
                ugStatus.setValue("Proper"); ugStatus.setDisable(true);
                loadBtn.fire();
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        // ── Layout ──
        HBox filterBar  = new HBox(8, new Label("Type:"), typeFilter, keywordField, loadBtn);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        HBox editBar    = new HBox(8,
            new Label("Email:"), emailEdit,
            new Label("Contact:"), contactEdit, updateBtn,
            new Label("Status:"), statusEdit, updateStatusBtn,
            deleteBtn);
        editBar.setAlignment(Pos.CENTER_LEFT);

        Separator sep = new Separator();

        VBox layout = new VBox(10, filterBar, table, editBar, sep, createHeading, createForm);
        layout.setPadding(new Insets(14));
        page.setCenter(new ScrollPane(layout));
        root.getChildren().setAll(page);
        loadBtn.fire();
    }

    private void openMyProfileEditor() {
        BorderPane page = buildShell("My Profile");
        UserProfile me = userDao.findById(currentUser.getId());
        if (me == null) { showError("Unable to load your profile"); return; }
        Label roleNote = new Label(getProfileRuleText(me.getUserType())); roleNote.setWrapText(true);
        roleNote.setStyle("-fx-text-fill: #1e293b;");
        TextField id = new TextField(me.getId()); id.setEditable(false);
        TextField name = new TextField(me.getFullName()); name.setEditable(false);
        TextField email = new TextField(me.getEmail());
        TextField contact = new TextField(me.getContactNo());
        TextField profileImagePath = new TextField(me.getProfileImagePath() == null ? "" : me.getProfileImagePath());
        profileImagePath.setPromptText("Profile image path"); profileImagePath.setEditable(false);
        Button uploadBtn = new Button("Upload Photo");
        uploadBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser(); fc.setTitle("Select profile image");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.jpeg","*.webp"));
            File file = fc.showOpenDialog(root.getScene() == null ? null : root.getScene().getWindow());
            if (file != null) profileImagePath.setText(file.getAbsolutePath());
        });
        Button clearBtn = new Button("Remove Photo"); clearBtn.setOnAction(e -> profileImagePath.clear());
        Button save = new Button("Save");
        save.setOnAction(e -> saveMyProfile(email.getText().trim(), contact.getText().trim(), profileImagePath.getText().trim()));
        HBox photoBar = new HBox(8, profileImagePath, uploadBtn, clearBtn);
        VBox box = new VBox(10, new Label("User ID"), id, new Label("Name"), name,
                new Label("Email"), email, new Label("Contact"), contact,
                new Label("Profile Picture"), photoBar, roleNote, save);
        box.setPadding(new Insets(16));
        page.setCenter(box);
        root.getChildren().setAll(page);
    }

    private void saveMyProfile(String email, String contact, String profileImagePath) {
        try { userDao.updateMyProfile(currentUser.getId(), email, contact, profileImagePath); showInfo("Profile updated"); }
        catch (Exception ex) { showError(ex.getMessage()); }
    }

    private String getProfileRuleText(String role) {
        if ("Lecturer".equals(role) || "TechnicalOfficer".equals(role))
            return "You can update your profile except username and password.";
        if ("Undergraduate".equals(role)) return "You can update contact details and profile picture.";
        return "You can update your profile picture and contact details here.";
    }

    private void openAttendance(boolean canManage, boolean selfOnly) {
        BorderPane page = buildShell("Attendance");
        TextField attendanceIdField = new TextField(); attendanceIdField.setPromptText("Attendance ID");
        TextField studentIdField = new TextField(selfOnly ? currentUser.getId() : "");
        studentIdField.setPromptText("Student ID"); studentIdField.setEditable(!selfOnly);
        TextField courseField = new TextField(); courseField.setPromptText("Course Code");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        ComboBox<String> componentBox = new ComboBox<>(); componentBox.getItems().addAll("Theory","Practical"); componentBox.setValue("Theory");
        ComboBox<String> statusBox    = new ComboBox<>(); statusBox.getItems().addAll("Present","Absent"); statusBox.setValue("Present");
        Button addBtn = new Button("Add Attendance"); addBtn.setDisable(!canManage);
        TextField filterStudent = new TextField(selfOnly ? currentUser.getId() : "");
        filterStudent.setPromptText("Filter Student ID"); filterStudent.setEditable(!selfOnly);
        ComboBox<String> filterCourse = new ComboBox<>();
        filterCourse.setPromptText("Filter Course");
        filterCourse.setPrefWidth(220);
        ComboBox<String> filterComponent = new ComboBox<>(); filterComponent.getItems().addAll("Combined","Theory","Practical"); filterComponent.setValue("Combined");
        Button refreshBtn = new Button("Refresh");
        ComboBox<String> ugCourseSelector = new ComboBox<>(); ugCourseSelector.setPrefWidth(420);
        ProgressBar ugHoursBar = new ProgressBar(0); ugHoursBar.setPrefWidth(360);
        Label ugHoursLabel = new Label("Presented Hours: 0 / 0");
        TableView<AttendanceRecord> table = new TableView<>(attendanceRows);
        TableColumn<AttendanceRecord,String> idCol2    = col("Attendance ID", x -> x.getAttendanceId());
        TableColumn<AttendanceRecord,String> stuCol    = col("Student",       x -> x.getStudentId());
        TableColumn<AttendanceRecord,String> courseCol = col("Course",        x -> x.getCourseCode());
        TableColumn<AttendanceRecord,String> dateCol   = col("Date",          x -> x.getSessionDate());
        TableColumn<AttendanceRecord,String> compCol   = col("Component",     x -> x.getComponent());
        TableColumn<AttendanceRecord,String> statusCol = col("Status",        x -> x.getDisplayStatus());
        if (!selfOnly) { table.getColumns().add(idCol2); table.getColumns().add(stuCol); }
        table.getColumns().addAll(courseCol, dateCol, compCol, statusCol);
        ComboBox<String> updateStatus = new ComboBox<>(); updateStatus.getItems().addAll("Present","Absent"); updateStatus.setValue("Present");
        Button updateBtn = new Button("Update Selected Status"); updateBtn.setDisable(!canManage || selfOnly);
        Button editBtn   = new Button("Edit Selected Attendance"); editBtn.setDisable(!canManage || selfOnly);
        addBtn.setOnAction(e -> {
            try {
                AttendanceRecord record = new AttendanceRecord(attendanceIdField.getText().trim(),
                        studentIdField.getText().trim(), courseField.getText().trim(),
                        datePicker.getValue().toString(), componentBox.getValue(), statusBox.getValue());
                attendanceDao.addAttendance(record); refreshBtn.fire();
            } catch (Exception ex) { showError(ex.getMessage()); }
        });
        refreshBtn.setOnAction(e -> {
            try {
                String courseFilter = "";
                if (filterCourse.getValue() != null && !filterCourse.getValue().isBlank()) {
                    courseFilter = parseCourseCode(filterCourse.getValue());
                }
                attendanceRows.setAll(attendanceDao.findAttendance(
                        filterStudent.getText().trim(), courseFilter, filterComponent.getValue()));
                if (selfOnly) {
                    int totalHours = attendanceRows.size() * 2, presentHours = 0;
                    for (AttendanceRecord r : attendanceRows) if ("Present".equals(r.getStatus())) presentHours += 2;
                    double ratio = totalHours == 0 ? 0.0 : (double) presentHours / totalHours;
                    ugHoursBar.setProgress(ratio);
                    ugHoursLabel.setText("Presented Hours: " + presentHours + " / " + totalHours);
                }
            } catch (Exception ex) { showError(ex.getMessage()); }
        });
        updateBtn.setOnAction(e -> {
            AttendanceRecord sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Select an attendance row first"); return; }
            try { attendanceDao.updateAttendanceStatus(sel.getAttendanceId(), updateStatus.getValue()); refreshBtn.fire(); }
            catch (Exception ex) { showError(ex.getMessage()); }
        });
        editBtn.setOnAction(e -> {
            AttendanceRecord sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Select an attendance row first"); return; }
            try {
                AttendanceRecord record = new AttendanceRecord(sel.getAttendanceId(),
                        studentIdField.getText().trim(), courseField.getText().trim(),
                        datePicker.getValue().toString(), componentBox.getValue(), statusBox.getValue());
                attendanceDao.updateAttendance(record); refreshBtn.fire();
            } catch (Exception ex) { showError(ex.getMessage()); }
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs,oldV,sel) -> {
            if (sel != null) {
                attendanceIdField.setText(sel.getAttendanceId()); studentIdField.setText(sel.getStudentId());
                courseField.setText(sel.getCourseCode());
                if (sel.getSessionDate() != null && !sel.getSessionDate().isBlank())
                    datePicker.setValue(LocalDate.parse(sel.getSessionDate()));
                componentBox.setValue(sel.getComponent()); statusBox.setValue(sel.getStatus());
                updateStatus.setValue(sel.getStatus());
            }
        });
        HBox addBar    = new HBox(8, attendanceIdField, studentIdField, courseField, datePicker, componentBox, statusBox, addBtn, editBtn);
        HBox filterBar = new HBox(8, new Label("Filter:"), filterStudent, filterCourse, filterComponent, refreshBtn);
        HBox updateBar = new HBox(8, new Label("New Status:"), updateStatus, updateBtn);
        VBox body;
        if (selfOnly) {
            List<CourseUnit> courses = attendanceDao.getCoursesByStudent(currentUser.getId());
            for (CourseUnit c : courses) {
                String item = c.getCourseCode() + " - " + c.getTitle();
                ugCourseSelector.getItems().add(item);
                filterCourse.getItems().add(item);
            }
            if (!ugCourseSelector.getItems().isEmpty()) {
                ugCourseSelector.setValue(ugCourseSelector.getItems().get(0));
                filterCourse.setValue(ugCourseSelector.getValue());
            }
            ugCourseSelector.setOnAction(e -> {
                String s = ugCourseSelector.getValue();
                if (s != null) { filterCourse.setValue(s); refreshBtn.fire(); }
            });
            HBox courseBar2 = new HBox(10, new Label("Course:"), ugCourseSelector, filterComponent, refreshBtn);
            HBox hoursBar   = new HBox(10, ugHoursLabel, ugHoursBar);
            body = new VBox(10, courseBar2, hoursBar, table);
        } else if (canManage) {
            try {
                for (CourseUnit c : marksService.getAllCourses()) {
                    filterCourse.getItems().add(c.getCourseCode() + " - " + c.getTitle());
                }
            } catch (Exception ex) { showError(ex.getMessage()); }
            body = new VBox(10, addBar, filterBar, table, updateBar);
        } else {
            try {
                for (CourseUnit c : marksService.getAllCourses()) {
                    filterCourse.getItems().add(c.getCourseCode() + " - " + c.getTitle());
                }
            } catch (Exception ex) { showError(ex.getMessage()); }
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
        TextField refField  = new TextField(generateMedicalRefNo()); refField.setPromptText("Ref No"); refField.setEditable(false);
        TextField studentId = new TextField(selfOnly ? currentUser.getId() : ""); studentId.setPromptText("Student ID"); studentId.setEditable(false);
        TextArea reason     = new TextArea(); reason.setPromptText("Reason"); reason.setPrefRowCount(2);
        TextField proofPath = new TextField(); proofPath.setPromptText("Medical image path"); proofPath.setEditable(false);
        Button uploadBtn = new Button("Upload Photo");
        uploadBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser(); fc.setTitle("Select medical image");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.jpeg","*.webp"));
            File file = fc.showOpenDialog(root.getScene() == null ? null : root.getScene().getWindow());
            if (file != null) proofPath.setText(file.getAbsolutePath());
        });
        DatePicker startDate = new DatePicker(LocalDate.now()), endDate = new DatePicker(LocalDate.now());
        ComboBox<String> statusBox = new ComboBox<>(); statusBox.getItems().addAll("Pending","Approved","Rejected"); statusBox.setValue("Pending"); statusBox.setDisable(true);
        Button addBtn = new Button("Submit Medical"); addBtn.setDisable(!canSubmitMedical);
        TextField filterStudent = new TextField(selfOnly ? currentUser.getId() : "");
        filterStudent.setPromptText("Filter by Student ID");
        filterStudent.setEditable(!selfOnly);
        Button refreshBtn = new Button("Filter");
        TableView<MedicalRecord> table = new TableView<>(medicalRows);
        table.getColumns().addAll(col("Ref No",  x -> x.getRefNo()), col("Student", x -> x.getStudentId()),
                col("Status", x -> x.getStatus()), col("Start",x -> x.getStartDate()),
                col("End",    x -> x.getEndDate()), col("Photo", x -> x.getProofImagePath() == null ? "-" : x.getProofImagePath()));
        ComboBox<String> newStatus = new ComboBox<>(); newStatus.getItems().addAll("Pending","Approved","Rejected"); newStatus.setValue("Pending");
        Button updateStatusBtn = new Button("Update Selected Status"); updateStatusBtn.setDisable(!canManage);
        Button editBtn = new Button("Edit Selected Medical"); editBtn.setDisable(!(canManage || canSubmitMedical));
        Button viewPhotoBtn = new Button("View Selected Photo");
        Label selectedPhoto = new Label("Selected Photo: -"); selectedPhoto.setWrapText(true);
        addBtn.setOnAction(e -> {
            try {
                medicalDao.addMedical(new MedicalRecord(refField.getText().trim(), studentId.getText().trim(),
                        reason.getText().trim(), "Pending", startDate.getValue().toString(),
                        endDate.getValue().toString(), proofPath.getText().trim()));
                reason.clear(); proofPath.clear(); startDate.setValue(LocalDate.now()); endDate.setValue(LocalDate.now());
                refField.setText(generateMedicalRefNo()); refreshBtn.fire();
            } catch (Exception ex) { showError(ex.getMessage()); }
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs,oldV,sel) -> {
            if (sel != null) {
                refField.setText(sel.getRefNo()); studentId.setText(sel.getStudentId());
                reason.setText(sel.getReason()); statusBox.setValue(sel.getStatus());
                if (sel.getStartDate() != null) startDate.setValue(LocalDate.parse(sel.getStartDate()));
                if (sel.getEndDate()   != null) endDate.setValue(LocalDate.parse(sel.getEndDate()));
                proofPath.setText(sel.getProofImagePath() == null ? "" : sel.getProofImagePath());
                selectedPhoto.setText("Selected Photo: " + (sel.getProofImagePath() == null || sel.getProofImagePath().isBlank() ? "-" : sel.getProofImagePath()));
            }
        });
        refreshBtn.setOnAction(e -> {
            try { medicalRows.setAll(medicalDao.findMedicalByStudent(filterStudent.getText().trim())); }
            catch (Exception ex) { showError(ex.getMessage()); }
        });
        updateStatusBtn.setOnAction(e -> {
            MedicalRecord sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Select a medical record first"); return; }
            try { medicalDao.updateMedicalStatus(sel.getRefNo(), newStatus.getValue()); refreshBtn.fire(); }
            catch (Exception ex) { showError(ex.getMessage()); }
        });
        viewPhotoBtn.setOnAction(e -> {
            MedicalRecord sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Select a medical record first"); return; }
            String path = sel.getProofImagePath();
            if (path == null || path.isBlank()) { showError("No photo uploaded for selected medical record"); return; }
            showMedicalPhoto(path);
        });
        editBtn.setOnAction(e -> {
            MedicalRecord sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Select a medical record first"); return; }
            if (selfOnly && !currentUser.getId().equals(sel.getStudentId())) { showError("You can edit only your own medical records"); return; }
            try {
                medicalDao.updateMedical(new MedicalRecord(sel.getRefNo(), sel.getStudentId(),
                        reason.getText().trim(), statusBox.getValue(), startDate.getValue().toString(),
                        endDate.getValue().toString(), proofPath.getText().trim()));
                refreshBtn.fire();
            } catch (Exception ex) { showError(ex.getMessage()); }
        });
        HBox addBar1   = new HBox(8, refField, studentId, statusBox, startDate, endDate, addBtn);
        HBox addBar2   = new HBox(8, new Label("Reason:"), reason);
        HBox addBar3   = new HBox(8, new Label("Photo:"), proofPath, uploadBtn, editBtn);
        HBox filterBar = new HBox(8, new Label("Filter:"), filterStudent, refreshBtn);
        HBox updateBar = new HBox(8, new Label("New Status:"), newStatus, updateStatusBtn);
        VBox body;
        if (canManage) body = new VBox(10, filterBar, table, updateBar, viewPhotoBtn, selectedPhoto);
        else if (canSubmitMedical) body = new VBox(10, addBar1, addBar2, addBar3, filterBar, table, viewPhotoBtn, selectedPhoto);
        else body = new VBox(10, filterBar, table, viewPhotoBtn, selectedPhoto);
        body.setPadding(new Insets(16)); page.setCenter(body);
        root.getChildren().setAll(page); refreshBtn.fire();
    }

    private void openMarksUpload() {
        BorderPane page = buildShell("Marks Upload");
        ComboBox<String> courseBox = new ComboBox<>(); courseBox.setPrefWidth(320); courseBox.setEditable(true);
        loadCourseOptions(courseBox);
        ComboBox<CourseExam> examBox = new ComboBox<>(); examBox.setPrefWidth(320);
        examBox.setCellFactory(list -> new ListCell<>() {
            protected void updateItem(CourseExam item, boolean empty) { super.updateItem(item, empty); setText(empty || item == null ? null : formatExamLabel(item)); }
        });
        examBox.setButtonCell(new ListCell<>() {
            protected void updateItem(CourseExam item, boolean empty) { super.updateItem(item, empty); setText(empty || item == null ? null : formatExamLabel(item)); }
        });
        ComboBox<StudentInfo> studentBox = new ComboBox<>(); studentBox.setPrefWidth(320);
        studentBox.setCellFactory(list -> new ListCell<>() {
            protected void updateItem(StudentInfo item, boolean empty) { super.updateItem(item, empty); setText(empty || item == null ? null : formatStudentLabel(item)); }
        });
        studentBox.setButtonCell(new ListCell<>() {
            protected void updateItem(StudentInfo item, boolean empty) { super.updateItem(item, empty); setText(empty || item == null ? null : formatStudentLabel(item)); }
        });
        TextField markField = new TextField(); markField.setPromptText("Mark (0-100)");
        Button loadBtn = new Button("Load"), saveBtn = new Button("Save Mark");
        loadBtn.setOnAction(e -> {
            String code = parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            if (code.isBlank()) { showError("Select a course first"); return; }
            try {
                List<CourseExam> exams = marksService.getCourseExams(code); examBox.getItems().setAll(exams);
                if (!exams.isEmpty()) examBox.getSelectionModel().select(0);
                List<StudentInfo> students = marksService.getStudentsByCourse(code); studentBox.getItems().setAll(students);
                if (!students.isEmpty()) studentBox.getSelectionModel().select(0);
            } catch (Exception ex) { showError(ex.getMessage()); }
        });
        saveBtn.setOnAction(e -> {
            String code = parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            CourseExam exam = examBox.getSelectionModel().getSelectedItem();
            StudentInfo student = studentBox.getSelectionModel().getSelectedItem();
            if (code.isBlank() || exam == null || student == null) { showError("Select course, exam, and student"); return; }
            double value;
            try { value = Double.parseDouble(markField.getText().trim()); }
            catch (NumberFormatException ex) { showError("Enter a valid numeric mark"); return; }
            if (value < 0 || value > 100) { showError("Mark should be between 0 and 100"); return; }
            try {
                MarkEntry existing = marksDao.getMarkEntry(student.getStudentId(), code, exam.getExamTypeId());
                CourseExam ce = new CourseExam(); ce.setCourseCode(code); ce.setExamTypeId(exam.getExamTypeId());
                if (existing == null) { marksDao.addMark(new Mark(generateMarkId(), student.getStudentId(), ce, value)); showInfo("Mark added for " + student.getStudentId()); }
                else { marksDao.updateMark(new Mark(existing.getMarkId(), student.getStudentId(), ce, value)); showInfo("Mark updated for " + student.getStudentId()); }
                gpaRows.setAll(marksService.getStudentGpaSummariesForCourse(code, true));
            } catch (Exception ex) { showError(ex.getMessage()); }
        });
        HBox filters  = new HBox(8, new Label("Course:"), courseBox, loadBtn);
        HBox entryRow = new HBox(8, new Label("Exam:"), examBox, new Label("Student:"), studentBox, new Label("Mark:"), markField, saveBtn);
        VBox body = new VBox(12, filters, entryRow); body.setPadding(new Insets(16));
        page.setCenter(body); root.getChildren().setAll(page); loadBtn.fire();
    }

    private void openMarksOverview() {
        if ("Undergraduate".equals(currentUser.getUserType())) openUndergraduateMarks();
        else openLecturerMarks();
    }

    private void openUndergraduateMarks() {
        BorderPane page = buildShell("My Grades & GPA");
        TableView<CourseResultSummary> table = new TableView<>(courseResultRows);
        table.getColumns().addAll(
                col("Course",     x -> x.getCourseCode()),
                col("CA Status",  x -> x.getCaStatus()),
                col("End Status", x -> formatEndStatusForStudent(x)),
                col("Grade",      x -> formatGradeForStudent(x)));
        Label sgpaLabel = new Label("SGPA: -"), cgpaLabel = new Label("CGPA: -");
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> {
            try {
                List<CourseUnit> courses = marksService.getCoursesByStudent(currentUser.getId());
                List<CourseResultSummary> rows = new ArrayList<>();
                for (CourseUnit course : courses)
                    rows.add(marksService.getCourseResult(currentUser.getId(), course.getCourseCode(), true));
                courseResultRows.setAll(rows);
                StudentGpaSummary gpa = marksService.getStudentGpaSummary(currentUser.getId(), true);
                sgpaLabel.setText(String.format("SGPA: %.2f", gpa.getSgpa()));
                cgpaLabel.setText(String.format("CGPA: %.2f", gpa.getCgpa()));
            } catch (Exception ex) { showError(ex.getMessage()); }
        });
        HBox gpaBar = new HBox(16, sgpaLabel, cgpaLabel, refreshBtn);
        VBox body   = new VBox(12, table, gpaBar); body.setPadding(new Insets(16));
        page.setCenter(body); root.getChildren().setAll(page); refreshBtn.fire();
    }

    private void openLecturerMarks() {
        BorderPane page = buildShell("Marks, Grades & GPA");
        ComboBox<String> courseBox = new ComboBox<>(); courseBox.setPrefWidth(320); courseBox.setEditable(true);
        loadCourseOptions(courseBox);
        TextField studentFilter = new TextField(); studentFilter.setPromptText("Filter Student ID");
        CheckBox includeMedical = new CheckBox("Count approved medical for attendance"); includeMedical.setSelected(true);
        Button loadBtn = new Button("Load Results"), gpaBtn = new Button("Load GPA");
        TableView<CourseResultSummary> resultsTable = new TableView<>(courseResultRows);
        resultsTable.getColumns().addAll(
                col("Student ID",  x -> x.getStudentId()),
                col("Name",        x -> x.getStudentName()),
                col("Attendance%", x -> String.format("%.2f", x.getAttendancePercentage())),
                col("CA%",         x -> String.format("%.2f", x.getCaPercentage())),
                col("CA Status",   x -> x.getCaStatus()),
                col("Eligibility", x -> x.getEligibilityStatus()),
                col("End%",        x -> String.format("%.2f", x.getEndPercentage())),
                col("End Status",  x -> formatEndStatusForStudent(x)),
                col("Total",       x -> formatTotalForStudent(x)),
                col("Grade",       x -> formatGradeForStudent(x)));
        TableView<StudentGpaSummary> gpaTable = new TableView<>(gpaRows);
        gpaTable.getColumns().addAll(
                col("Student ID", x -> x.getStudentId()),
                col("Name",       x -> x.getStudentName()),
                col("SGPA",       x -> String.format("%.2f", x.getSgpa())),
                col("CGPA",       x -> String.format("%.2f", x.getCgpa())));
        loadBtn.setOnAction(e -> {
            String code = parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            if (code.isBlank()) { showError("Select a course first"); return; }
            try {
                List<CourseResultSummary> results = marksService.getCourseResultsForBatch(code, includeMedical.isSelected());
                String filter = studentFilter.getText().trim();
                if (!filter.isBlank()) results.removeIf(r -> !r.getStudentId().contains(filter));
                courseResultRows.setAll(results);
            } catch (Exception ex) { showError(ex.getMessage()); }
        });
        gpaBtn.setOnAction(e -> {
            String code = parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            if (code.isBlank()) { showError("Select a course first"); return; }
            try {
                List<StudentGpaSummary> results = marksService.getStudentGpaSummariesForCourse(code, includeMedical.isSelected());
                String filter = studentFilter.getText().trim();
                if (!filter.isBlank()) results.removeIf(r -> !r.getStudentId().contains(filter));
                gpaRows.setAll(results);
            } catch (Exception ex) { showError(ex.getMessage()); }
        });
        HBox controls = new HBox(10, new Label("Course:"), courseBox, studentFilter, includeMedical, loadBtn, gpaBtn);
        VBox body     = new VBox(12, controls, resultsTable, new Label("GPA Summary"), gpaTable);
        body.setPadding(new Insets(16)); page.setCenter(body);
        root.getChildren().setAll(page); loadBtn.fire(); gpaBtn.fire();
    }

    private void openSummary() {
        BorderPane page = buildShell("Eligibility (Attendance + CA)");
        ComboBox<String> courseBox = new ComboBox<>(); courseBox.setPrefWidth(320); courseBox.setEditable(true);
        loadCourseOptions(courseBox);
        TextField studentFilter = new TextField(); studentFilter.setPromptText("Filter Student ID");
        CheckBox includeMedical = new CheckBox("Count approved medical for attendance"); includeMedical.setSelected(true);
        Button loadBtn = new Button("Generate Summary");
        TableView<CourseResultSummary> table = new TableView<>(courseResultRows);
        table.getColumns().addAll(
                col("Student ID",  x -> x.getStudentId()),
                col("Name",        x -> x.getStudentName()),
                col("Attendance%", x -> String.format("%.2f", x.getAttendancePercentage())),
                col("CA%",         x -> String.format("%.2f", x.getCaPercentage())),
                col("CA Status",   x -> x.getCaStatus()),
                col("Eligibility", x -> x.getEligibilityStatus()));
        loadBtn.setOnAction(e -> {
            String code = parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            if (code.isBlank()) { showError("Select a course first"); return; }
            try {
                List<CourseResultSummary> results = marksService.getCourseResultsForBatch(code, includeMedical.isSelected());
                String filter = studentFilter.getText().trim();
                if (!filter.isBlank()) results.removeIf(r -> !r.getStudentId().contains(filter));
                courseResultRows.setAll(results);
            } catch (Exception ex) { showError(ex.getMessage()); }
        });
        HBox controls = new HBox(8, new Label("Course:"), courseBox, studentFilter, includeMedical, loadBtn);
        VBox body     = new VBox(10, controls, table); body.setPadding(new Insets(16));
        page.setCenter(body); root.getChildren().setAll(page); loadBtn.fire();
    }

    // ═════════════════════════════════════════════════════════════════════
    // ── COURSE MANAGEMENT (Admin) ─────────────────────────────────────────
    // ═════════════════════════════════════════════════════════════════════

    private void openCourseManagement() {
        BorderPane page = buildShell("Course Management");

        // ── Search bar ──
        TextField searchField = new TextField();
        searchField.setPromptText("Search by code or title…");
        searchField.setPrefWidth(260);
        Button searchBtn = new Button("🔍 Search");
        Button loadAllBtn = new Button("Load All");

        // ── Table ──
        TableView<CourseUnit> table = new TableView<>(courseRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<CourseUnit,String> codeCol   = col("Course Code", c -> c.getCourseCode());
        TableColumn<CourseUnit,String> titleCol  = col("Title",       c -> c.getTitle());
        TableColumn<CourseUnit,String> creditCol = col("Credits",     c -> String.valueOf(c.getCredit()));
        table.getColumns().addAll(codeCol, titleCol, creditCol);
        table.setPrefHeight(280);

        // ── Edit bar ──
        TextField editTitle  = new TextField(); editTitle.setPromptText("Title");
        TextField editCredit = new TextField(); editCredit.setPromptText("Credits");
        Button    updateBtn  = new Button("💾 Update");
        Button    deleteBtn  = new Button("🗑 Delete");
        deleteBtn.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white;");

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                editTitle.setText(sel.getTitle());
                editCredit.setText(String.valueOf(sel.getCredit()));
            }
        });

        Runnable reloadAll = () -> {
            try { courseRows.setAll(courseUnitDAO.getAllCourses()); }
            catch (Exception ex) { showError(ex.getMessage()); }
        };

        loadAllBtn.setOnAction(e -> reloadAll.run());
        searchBtn.setOnAction(e -> {
            String kw = searchField.getText().trim();
            try {
                if (kw.isEmpty()) reloadAll.run();
                else courseRows.setAll(courseUnitDAO.searchCourses(kw));
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        updateBtn.setOnAction(e -> {
            CourseUnit sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Select a course first"); return; }
            String title = editTitle.getText().trim();
            String creditStr = editCredit.getText().trim();
            if (title.isEmpty() || creditStr.isEmpty()) { showError("Title and credits are required"); return; }
            int credits;
            try { credits = Integer.parseInt(creditStr); } catch (NumberFormatException ex) { showError("Credits must be a number"); return; }
            try {
                CourseUnit updated = new CourseUnit(sel.getCourseCode(), title, credits);
                boolean ok = courseUnitDAO.updateCourse(updated);
                if (ok) { showInfo("Course updated."); reloadAll.run(); }
                else { showError("Course not found for update."); }
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        deleteBtn.setOnAction(e -> {
            CourseUnit sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Select a course first"); return; }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete course " + sel.getCourseCode() + " – " + sel.getTitle() + "?",
                    ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Confirm Delete");
            confirm.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) {
                    try { courseUnitDAO.deleteCourse(sel.getCourseCode()); showInfo("Course deleted."); reloadAll.run(); }
                    catch (Exception ex) { showError(ex.getMessage()); }
                }
            });
        });

        // ── Create form ──
        Label createHeading = new Label("➕ Add New Course");
        createHeading.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField newCode   = new TextField(); newCode.setPromptText("Course Code (e.g. CS1234)");
        TextField newTitle  = new TextField(); newTitle.setPromptText("Course Title");
        TextField newCredit = new TextField(); newCredit.setPromptText("Credits");
        Button    addBtn    = new Button("✅ Add Course");
        addBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");

        addBtn.setOnAction(e -> {
            String code   = newCode.getText().trim();
            String title  = newTitle.getText().trim();
            String credStr = newCredit.getText().trim();
            if (code.isEmpty() || title.isEmpty() || credStr.isEmpty()) { showError("All fields required"); return; }
            int cred;
            try { cred = Integer.parseInt(credStr); } catch (NumberFormatException ex) { showError("Credits must be a number"); return; }
            if (courseUnitDAO.courseExists(code)) { showError("Course code already exists: " + code); return; }
            try {
                courseUnitDAO.addCourse(new CourseUnit(code, title, cred));
                showInfo("Course added: " + code);
                newCode.clear(); newTitle.clear(); newCredit.clear();
                reloadAll.run();
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        HBox searchBar = new HBox(8, searchField, searchBtn, loadAllBtn);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        HBox editBar   = new HBox(8, new Label("Title:"), editTitle, new Label("Credits:"), editCredit, updateBtn, deleteBtn);
        editBar.setAlignment(Pos.CENTER_LEFT);
        HBox addBar    = new HBox(8, new Label("Code:"), newCode, new Label("Title:"), newTitle, new Label("Credits:"), newCredit, addBtn);
        addBar.setAlignment(Pos.CENTER_LEFT);

        VBox layout = new VBox(10, searchBar, table, editBar, new Separator(), createHeading, addBar);
        layout.setPadding(new Insets(14));
        page.setCenter(new ScrollPane(layout));
        root.getChildren().setAll(page);
        reloadAll.run();
    }

    // ═════════════════════════════════════════════════════════════════════
    // ── NOTICE MANAGEMENT (Admin) ─────────────────────────────────────────
    // ═════════════════════════════════════════════════════════════════════

    /** Shared folder where notice attachments are stored (same machine). */
    private static final java.nio.file.Path NOTICE_STORE =
            java.nio.file.Paths.get(System.getProperty("user.home"), "tecmis_notices");

    private java.nio.file.Path noticeStore() {
        try { java.nio.file.Files.createDirectories(NOTICE_STORE); } catch (Exception ignored) {}
        return NOTICE_STORE;
    }

    /** Copies the chosen file into the notice store and returns the stored path. */
    private String copyNoticeFile(java.io.File src, String noticeId) throws java.io.IOException {
        String ext   = src.getName().contains(".") ? src.getName().substring(src.getName().lastIndexOf('.')) : "";
        String name  = noticeId + ext;
        java.nio.file.Path dest = noticeStore().resolve(name);
        java.nio.file.Files.copy(src.toPath(), dest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        return dest.toString();
    }

    private void openNoticeManagement() {
        BorderPane page = buildShell("Notice Management");

        // ── search bar ──
        TextField searchField = new TextField();
        searchField.setPromptText("Search by title…"); searchField.setPrefWidth(260);
        Button searchBtn  = new Button("🔍 Search");
        Button loadAllBtn = new Button("Load All");
        Button viewBoardBtn = new Button("📰 View Board");
        viewBoardBtn.setOnAction(e -> openNoticeBoard());

        // ── table ──
        TableView<Notice> table = new TableView<>(noticeRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(220);
        TableColumn<Notice,String> idCol    = col("Notice ID", n -> n.getNoticeId());
        TableColumn<Notice,String> titleCol = col("Title",     n -> n.getTitle());
        TableColumn<Notice,String> dateCol  = col("Date",      n -> n.getDate() == null ? "" : n.getDate().toString());
        TableColumn<Notice,String> fileCol  = col("Attachment", n -> n.hasFile() ? "📎 " + n.getFileType().toUpperCase() : "—");
        table.getColumns().addAll(idCol, titleCol, dateCol, fileCol);

        // ── edit section ──
        Label editHeading = new Label("✏️  Edit Selected Notice");
        editHeading.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        TextField editTitle      = new TextField(); editTitle.setPromptText("Title");
        ComboBox<String> editAudience = new ComboBox<>();
        editAudience.getItems().addAll("All", "Admin", "Lecturer", "TechnicalOfficer", "Undergraduate");
        editAudience.setValue("All");
        Label     editFileLabel  = new Label("No file attached");
        editFileLabel.setStyle("-fx-text-fill: #475569;");
        Button    editBrowseBtn  = new Button("📂 Replace File…");
        Button    editClearFile  = new Button("✖ Remove File");
        final java.io.File[] editChosenFile = {null};
        final boolean[]      editFileClear  = {false};

        editBrowseBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Select attachment (PDF or PNG)");
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PDF files", "*.pdf"),
                    new FileChooser.ExtensionFilter("PNG images", "*.png"));
            java.io.File f = fc.showOpenDialog(root.getScene() == null ? null : root.getScene().getWindow());
            if (f != null) {
                editChosenFile[0] = f;
                editFileClear[0]  = false;
                editFileLabel.setText("New: " + f.getName());
            }
        });
        editClearFile.setOnAction(e -> {
            editChosenFile[0] = null;
            editFileClear[0]  = true;
            editFileLabel.setText("File will be removed on save");
        });

        Button updateBtn = new Button("💾 Save Changes");
        Button deleteBtn = new Button("🗑 Delete Notice");
        deleteBtn.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white;");

        // populate edit fields when row selected
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            editChosenFile[0] = null;
            editFileClear[0]  = false;
            if (sel != null) {
                editTitle.setText(sel.getTitle());
                editAudience.setValue(sel.getAudience() == null ? "All" : sel.getAudience());
                editFileLabel.setText(sel.hasFile()
                        ? "Current: " + new java.io.File(sel.getFilePath()).getName()
                        : "No file attached");
            } else {
                editTitle.clear();
                editAudience.setValue("All");
                editFileLabel.setText("No file attached");
            }
        });

        Runnable reloadAll = () -> {
            try { noticeRows.setAll(noticeDao.getNoticesForAudience(currentUser.getUserType())); }
            catch (Exception ex) { showError(ex.getMessage()); }
        };

        loadAllBtn.setOnAction(e -> reloadAll.run());
        searchBtn.setOnAction(e -> {
            String kw = searchField.getText().trim();
            try {
                noticeRows.setAll(kw.isEmpty()
                        ? noticeDao.getNoticesForAudience(currentUser.getUserType())
                        : noticeDao.searchNoticesForAudience(kw, currentUser.getUserType()));
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        updateBtn.setOnAction(e -> {
            Notice sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Select a notice to edit."); return; }
            String title = editTitle.getText().trim();
            if (title.isEmpty()) { showError("Title cannot be empty."); return; }
            sel.setTitle(title);
            sel.setAudience(editAudience.getValue());
            // handle file changes
            if (editFileClear[0]) {
                sel.setFilePath(null); sel.setFileType(null);
            } else if (editChosenFile[0] != null) {
                try {
                    String stored = copyNoticeFile(editChosenFile[0], sel.getNoticeId());
                    sel.setFilePath(stored);
                    sel.setFileType(fileExt(editChosenFile[0].getName()));
                } catch (Exception ex) { showError("File copy failed: " + ex.getMessage()); return; }
            }
            try { noticeDao.updateNotice(sel, currentUser.getId()); showInfo("Notice updated."); reloadAll.run(); }
            catch (Exception ex) { showError(ex.getMessage()); }
        });

        deleteBtn.setOnAction(e -> {
            Notice sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Select a notice to delete."); return; }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete notice \"" + sel.getTitle() + "\"?\nThis cannot be undone.",
                    ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Confirm Delete");
            confirm.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) {
                    try { noticeDao.deleteNotice(sel.getNoticeId(), currentUser.getId()); showInfo("Notice deleted."); reloadAll.run(); }
                    catch (Exception ex) { showError(ex.getMessage()); }
                }
            });
        });

        // ── create notice form ──
        Label createHeading = new Label("➕ Create New Notice");
        createHeading.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

        TextField newTitle     = new TextField(); newTitle.setPromptText("Notice title…"); newTitle.setPrefWidth(340);
        ComboBox<String> newAudience = new ComboBox<>();
        newAudience.getItems().addAll("All", "Admin", "Lecturer", "TechnicalOfficer", "Undergraduate");
        newAudience.setValue("All");
        Label     newFileLabel = new Label("No file selected (optional)");
        newFileLabel.setStyle("-fx-text-fill: #475569;");
        Button    newBrowseBtn = new Button("📂 Attach File…");
        final java.io.File[] newChosenFile = {null};

        newBrowseBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Attach PDF or PNG");
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PDF files", "*.pdf"),
                    new FileChooser.ExtensionFilter("PNG images", "*.png"));
            java.io.File f = fc.showOpenDialog(root.getScene() == null ? null : root.getScene().getWindow());
            if (f != null) { newChosenFile[0] = f; newFileLabel.setText(f.getName()); }
        });

        Button addBtn = new Button("✅ Post Notice");
        addBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");

        addBtn.setOnAction(e -> {
            String title = newTitle.getText().trim();
            if (title.isEmpty()) { showError("Title is required."); return; }
            String noticeId = "NOT" + String.format("%09d", Math.abs(System.nanoTime() % 1_000_000_000L));
            String storedPath = null;
            String storedType = null;
            if (newChosenFile[0] != null) {
                try {
                    storedPath = copyNoticeFile(newChosenFile[0], noticeId);
                    storedType = fileExt(newChosenFile[0].getName());
                } catch (Exception ex) { showError("File copy failed: " + ex.getMessage()); return; }
            }
            Notice n = new Notice(noticeId, currentUser.getId(), title, null, storedPath, storedType, newAudience.getValue());
            try {
                noticeDao.createNotice(n, currentUser.getId());
                showInfo("Notice posted" + (storedPath != null ? " with attachment." : "."));
                newTitle.clear(); newAudience.setValue("All");
                newChosenFile[0] = null; newFileLabel.setText("No file selected (optional)");
                reloadAll.run();
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        // ── layout ──
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
        root.getChildren().setAll(page);
        reloadAll.run();
    }

    // ═════════════════════════════════════════════════════════════════════
    // ── NOTICE BOARD (Lecturer / TO / Undergraduate – read + download) ────
    // ═════════════════════════════════════════════════════════════════════

    private void openNoticeBoard() {
        BorderPane page = buildShell("Notice Board");

        TextField searchField = new TextField();
        searchField.setPromptText("Search notices…"); searchField.setPrefWidth(240);
        Button searchBtn  = new Button("🔍 Search");
        Button refreshBtn = new Button("🔄 Refresh");

        // ── table ──
        TableView<Notice> table = new TableView<>(noticeRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setPlaceholder(new Label("No notices available."));
        TableColumn<Notice,String> idCol    = col("Notice ID", n -> n.getNoticeId());
        TableColumn<Notice,String> titleCol = col("Title",     n -> n.getTitle());
        TableColumn<Notice,String> dateCol  = col("Date",      n -> n.getDate() == null ? "" : n.getDate().toString());
        TableColumn<Notice,String> fileCol  = col("Attachment", n -> n.hasFile() ? "📎 " + n.getFileType().toUpperCase() : "—");
        table.getColumns().addAll(idCol, titleCol, dateCol, fileCol);

        // ── action buttons ──
        Button viewBtn     = new Button("👁 View / Open");
        Button downloadBtn = new Button("⬇ Download");
        viewBtn.setDisable(true); downloadBtn.setDisable(true);

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            boolean hasFile = sel != null && sel.hasFile();
            viewBtn.setDisable(!hasFile);
            downloadBtn.setDisable(!hasFile);
        });

        // Open the file using the system default viewer
        viewBtn.setOnAction(e -> {
            Notice sel = table.getSelectionModel().getSelectedItem();
            if (sel == null || !sel.hasFile()) return;
            try {
                java.io.File f = new java.io.File(sel.getFilePath());
                if (!f.exists()) { showError("Attachment not found on disk:\n" + sel.getFilePath()); return; }
                if (Desktop.isDesktopSupported()) Desktop.getDesktop().open(f);
                else showError("Desktop open is not supported on this system.");
            } catch (Exception ex) { showError("Cannot open file: " + ex.getMessage()); }
        });

        // Save a copy to a user-chosen location
        downloadBtn.setOnAction(e -> {
            Notice sel = table.getSelectionModel().getSelectedItem();
            if (sel == null || !sel.hasFile()) return;
            java.io.File src = new java.io.File(sel.getFilePath());
            if (!src.exists()) { showError("Attachment not found on disk:\n" + sel.getFilePath()); return; }
            FileChooser fc = new FileChooser();
            fc.setTitle("Save attachment as…");
            fc.setInitialFileName(src.getName());
            if ("pdf".equalsIgnoreCase(sel.getFileType()))
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
            else
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG images", "*.png"));
            java.io.File dest = fc.showSaveDialog(root.getScene() == null ? null : root.getScene().getWindow());
            if (dest != null) {
                try {
                    java.nio.file.Files.copy(src.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    showInfo("File saved to:\n" + dest.getAbsolutePath());
                } catch (Exception ex) { showError("Download failed: " + ex.getMessage()); }
            }
        });

        Runnable reloadAll = () -> {
            try { noticeRows.setAll(noticeDao.getAllNotices()); }
            catch (Exception ex) { showError(ex.getMessage()); }
        };

        refreshBtn.setOnAction(e -> reloadAll.run());
        searchBtn.setOnAction(e -> {
            String kw = searchField.getText().trim();
            try { noticeRows.setAll(kw.isEmpty() ? noticeDao.getAllNotices() : noticeDao.searchNotices(kw)); }
            catch (Exception ex) { showError(ex.getMessage()); }
        });

        HBox filterBar = new HBox(8, searchField, searchBtn, refreshBtn);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        HBox actionBar = new HBox(10, viewBtn, downloadBtn);
        actionBar.setAlignment(Pos.CENTER_LEFT);
        actionBar.setPadding(new Insets(4, 0, 0, 0));

        VBox layout = new VBox(10, filterBar, table, actionBar);
        layout.setPadding(new Insets(14));
        VBox.setVgrow(layout, Priority.ALWAYS);
        page.setCenter(layout);
        root.getChildren().setAll(page);
        reloadAll.run();
    }

    /** Extracts lowercase extension ("pdf" or "png") from a filename. */
    private String fileExt(String fileName) {
        if (fileName == null) return null;
        int dot = fileName.lastIndexOf('.');
        return dot >= 0 ? fileName.substring(dot + 1).toLowerCase() : null;
    }

    // ═════════════════════════════════════════════════════════════════════
    // ── UNDERGRADUATE DETAILS (Lecturer view) ────────────────────────────
    // ═════════════════════════════════════════════════════════════════════

    private void openUndergraduateDetails() {
        BorderPane page = buildShell("Undergraduate Details");

        TextField searchField = new TextField(); searchField.setPromptText("Search by ID or name…"); searchField.setPrefWidth(240);
        Button searchBtn  = new Button("🔍 Search");
        Button loadAllBtn = new Button("Load All");

        TableView<UserProfile> table = new TableView<>(ugRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<UserProfile,String> idCol      = col("Student ID", u -> u.getId());
        TableColumn<UserProfile,String> nameCol    = col("Full Name",  u -> u.getFullName());
        TableColumn<UserProfile,String> emailCol   = col("Email",      u -> u.getEmail());
        TableColumn<UserProfile,String> contactCol = col("Contact",    u -> u.getContactNo());
        TableColumn<UserProfile,String> statusCol  = col("Status",     u -> u.getStatus() == null ? "" : u.getStatus());
        idCol.setMinWidth(140); idCol.setPrefWidth(160);
        nameCol.setMinWidth(180); nameCol.setPrefWidth(220);
        emailCol.setMinWidth(200); emailCol.setPrefWidth(240);
        contactCol.setMinWidth(120); contactCol.setPrefWidth(150);
        statusCol.setMinWidth(100); statusCol.setPrefWidth(120);
        table.getColumns().addAll(idCol, nameCol, emailCol, contactCol, statusCol);
        table.setPrefHeight(260);
        table.setPlaceholder(new Label("No undergraduates found."));

        // Detail card shown on row selection
        Label detailId      = new Label(); detailId.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label detailName    = new Label();
        Label detailEmail   = new Label();
        Label detailContact = new Label();
        Label detailStatus  = new Label();

        VBox detailCard = new VBox(6,
                new Label("── Selected Undergraduate ──"),
                hRow("Student ID:", detailId),
                hRow("Name:", detailName),
                hRow("Email:", detailEmail),
                hRow("Contact:", detailContact),
                hRow("Status:", detailStatus));
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
            try { ugRows.setAll(userDao.findUndergraduates("")); }
            catch (Exception ex) { showError(ex.getMessage()); }
        };

        loadAllBtn.setOnAction(e -> reload.run());
        searchBtn.setOnAction(e -> {
            String kw = searchField.getText().trim();
            try { ugRows.setAll(userDao.findUndergraduates(kw)); }
            catch (Exception ex) { showError(ex.getMessage()); }
        });

        HBox filterBar = new HBox(8, searchField, searchBtn, loadAllBtn);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        VBox layout = new VBox(12, filterBar, table, detailCard);
        layout.setPadding(new Insets(14));
        page.setCenter(new ScrollPane(layout));
        root.getChildren().setAll(page);
        reload.run();
    }

    /** Helper: builds a two-label row for the detail card. */
    private HBox hRow(String labelText, Label value) {
        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-font-weight: bold; -fx-min-width: 90px;");
        HBox row = new HBox(8, lbl, value);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void openPlaceholder(String message) {
        BorderPane page = buildShell("Module");
        VBox box = new VBox(10); box.setPadding(new Insets(30)); box.setAlignment(Pos.CENTER_LEFT);
        Label t = new Label("Feature Placeholder"); t.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        Label d = new Label(message); d.setWrapText(true);
        box.getChildren().addAll(t, d); page.setCenter(box);
        root.getChildren().setAll(page);
    }

    // ─────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────

    /** Generic column factory for string properties. */
    private <T> TableColumn<T, String> col(String header,
            java.util.function.Function<T, String> extractor) {
        TableColumn<T, String> c = new TableColumn<>(header);
        c.setCellValueFactory(x -> new SimpleStringProperty(extractor.apply(x.getValue())));
        return c;
    }

    private void loadCourseOptions(ComboBox<String> courseBox) {
        try {
            courseBox.getItems().clear();
            for (CourseUnit c : marksService.getAllCourses())
                courseBox.getItems().add(c.getCourseCode() + " - " + c.getTitle());
            if (!courseBox.getItems().isEmpty()) courseBox.getSelectionModel().select(0);
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private String parseCourseCode(String display) {
        int idx = display.indexOf(" - ");
        return idx <= 0 ? display : display.substring(0, idx).trim();
    }

    private String generateMedicalRefNo() {
        long value = Math.abs(System.nanoTime() % 46656L);
        return "REF" + String.format("%3s", Long.toString(value, 36)).replace(' ', '0').toUpperCase();
    }

    private String generateMarkId() {
        return "MK" + String.format("%09d", Math.abs(System.nanoTime() % 1_000_000_000L));
    }

    private String generateId(String prefix) {
        return prefix + String.format("%08d", Math.abs(System.nanoTime() % 100_000_000L));
    }

    private void showMedicalPhoto(String path) {
        try {
            File file = new File(path);
            if (!file.exists() || !file.isFile()) { showError("Photo file not found: " + path); return; }
            ImageView iv = new ImageView(new Image(file.toURI().toString()));
            iv.setFitWidth(520); iv.setFitHeight(360); iv.setPreserveRatio(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Medical Photo"); alert.setHeaderText("Uploaded Medical Photo");
            alert.getDialogPane().setContent(iv); alert.showAndWait();
        } catch (Exception ex) { showError("Unable to open photo: " + ex.getMessage()); }
    }

    private String formatExamLabel(CourseExam exam) {
        String name = exam.getExamName();
        if (name == null || name.isBlank()) name = exam.getExamTypeName();
        if (name == null || name.isBlank()) name = "Exam";
        return exam.getExamTypeId() + " - " + name;
    }

    private String formatStudentLabel(StudentInfo student) {
        return student.getStudentId() + " - " + student.getStudentName();
    }

    private String formatEndStatusForStudent(CourseResultSummary s) {
        return s.isMedicalConcession() ? "MC" : s.getEndStatus();
    }

    private String formatGradeForStudent(CourseResultSummary s) {
        return s.isMedicalConcession() ? "MC" : s.getGrade();
    }

    private String formatTotalForStudent(CourseResultSummary s) {
        return s.isMedicalConcession() ? "MC" : String.format("%.2f", s.getTotalMark());
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Operation Failed"); alert.setHeaderText("Something went wrong");
        alert.setContentText(message); alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success"); alert.setHeaderText("Completed");
        alert.setContentText(message); alert.showAndWait();
    }

    private record FeatureCard(String icon, String title, String description, Runnable action) {}
}
