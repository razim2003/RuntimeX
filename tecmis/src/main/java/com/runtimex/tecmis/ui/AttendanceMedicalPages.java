package com.runtimex.tecmis.ui;

import com.runtimex.tecmis.models.AttendanceRecord;
import com.runtimex.tecmis.models.CourseExam;
import com.runtimex.tecmis.models.CourseUnit;
import com.runtimex.tecmis.models.ExamMedical;
import com.runtimex.tecmis.models.MedicalRecord;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class AttendanceMedicalPages {
    private final UiContext ctx;
    private final UiHelpers ui;
    private final ObservableList<AttendanceRecord> attendanceRows = FXCollections.observableArrayList();
    private final ObservableList<MedicalRecord> medicalRows = FXCollections.observableArrayList();
    private final ObservableList<ExamMedical> examMedicalRows = FXCollections.observableArrayList();

    public AttendanceMedicalPages(UiContext ctx, UiHelpers ui) {
        this.ctx = ctx;
        this.ui = ui;
    }

    public void openAttendance(boolean canManage, boolean selfOnly, Runnable onHome, Runnable onLogout) {
        BorderPane page = ui.buildShell("Attendance", onHome, onLogout);
        TextField attendanceIdField = new TextField();
        attendanceIdField.setPromptText("Attendance ID");
        TextField studentIdField = new TextField(selfOnly ? ctx.getCurrentUser().getId() : "");
        studentIdField.setPromptText("Student ID");
        studentIdField.setEditable(!selfOnly);
        TextField courseField = new TextField();
        courseField.setPromptText("Course Code");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        ComboBox<String> componentBox = new ComboBox<>();
        componentBox.getItems().addAll("Theory","Practical");
        componentBox.setValue("Theory");
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Present","Absent");
        statusBox.setValue("Present");
        Button addBtn = new Button("Add Attendance");
        addBtn.setDisable(!canManage);
        TextField filterStudent = new TextField(selfOnly ? ctx.getCurrentUser().getId() : "");
        filterStudent.setPromptText("Filter Student ID");
        filterStudent.setEditable(!selfOnly);
        ComboBox<String> filterCourse = new ComboBox<>();
        filterCourse.setPromptText("Filter Course");
        filterCourse.setPrefWidth(220);
        ComboBox<String> filterComponent = new ComboBox<>();
        filterComponent.getItems().addAll("Combined","Theory","Practical");
        filterComponent.setValue("Combined");
        Button refreshBtn = new Button("Refresh");
        ComboBox<String> ugCourseSelector = new ComboBox<>();
        ugCourseSelector.setPrefWidth(420);
        ProgressBar ugHoursBar = new ProgressBar(0);
        ugHoursBar.setPrefWidth(360);
        Label ugHoursLabel = new Label("Presented Hours: 0 / 0");
        TableView<AttendanceRecord> table = new TableView<>(attendanceRows);
        TableColumn<AttendanceRecord,String> idCol2 = ui.col("Attendance ID", x -> x.getAttendanceId());
        TableColumn<AttendanceRecord,String> stuCol = ui.col("Student", x -> x.getStudentId());
        TableColumn<AttendanceRecord,String> courseCol = ui.col("Course", x -> x.getCourseCode());
        TableColumn<AttendanceRecord,String> dateCol = ui.col("Date", x -> x.getSessionDate());
        TableColumn<AttendanceRecord,String> compCol = ui.col("Component", x -> x.getComponent());
        TableColumn<AttendanceRecord,String> statusCol = ui.col("Status", x -> x.getDisplayStatus());
        if (!selfOnly) { table.getColumns().add(idCol2); table.getColumns().add(stuCol); }
        table.getColumns().addAll(courseCol, dateCol, compCol, statusCol);
        ComboBox<String> updateStatus = new ComboBox<>();
        updateStatus.getItems().addAll("Present","Absent");
        updateStatus.setValue("Present");
        Button updateBtn = new Button("Update Selected Status");
        updateBtn.setDisable(!canManage || selfOnly);
        Button editBtn = new Button("Edit Selected Attendance");
        editBtn.setDisable(!canManage || selfOnly);
        addBtn.setOnAction(e -> {
            try {
                AttendanceRecord record = new AttendanceRecord(attendanceIdField.getText().trim(),
                        studentIdField.getText().trim(), courseField.getText().trim(),
                        datePicker.getValue().toString(), componentBox.getValue(), statusBox.getValue());
                ctx.getAttendanceDao().addAttendance(record);
                refreshBtn.fire();
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });
        refreshBtn.setOnAction(e -> {
            try {
                String courseFilter = "";
                if (filterCourse.getValue() != null && !filterCourse.getValue().isBlank()) {
                    courseFilter = ui.parseCourseCode(filterCourse.getValue());
                }
                attendanceRows.setAll(ctx.getAttendanceDao().findAttendance(
                        filterStudent.getText().trim(), courseFilter, filterComponent.getValue()));
                if (selfOnly) {
                    int totalHours = attendanceRows.size() * 2, presentHours = 0;
                    for (AttendanceRecord r : attendanceRows) if ("Present".equals(r.getStatus())) presentHours += 2;
                    double ratio = totalHours == 0 ? 0.0 : (double) presentHours / totalHours;
                    ugHoursBar.setProgress(ratio);
                    ugHoursLabel.setText("Presented Hours: " + presentHours + " / " + totalHours);
                }
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });
        updateBtn.setOnAction(e -> {
            AttendanceRecord sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select an attendance row first"); return; }
            try { ctx.getAttendanceDao().updateAttendanceStatus(sel.getAttendanceId(), updateStatus.getValue()); refreshBtn.fire(); }
            catch (Exception ex) { ui.showError(ex.getMessage()); }
        });
        editBtn.setOnAction(e -> {
            AttendanceRecord sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select an attendance row first"); return; }
            try {
                AttendanceRecord record = new AttendanceRecord(sel.getAttendanceId(),
                        studentIdField.getText().trim(), courseField.getText().trim(),
                        datePicker.getValue().toString(), componentBox.getValue(), statusBox.getValue());
                ctx.getAttendanceDao().updateAttendance(record);
                refreshBtn.fire();
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs,oldV,sel) -> {
            if (sel != null) {
                attendanceIdField.setText(sel.getAttendanceId());
                studentIdField.setText(sel.getStudentId());
                courseField.setText(sel.getCourseCode());
                if (sel.getSessionDate() != null && !sel.getSessionDate().isBlank())
                    datePicker.setValue(LocalDate.parse(sel.getSessionDate()));
                componentBox.setValue(sel.getComponent());
                statusBox.setValue(sel.getStatus());
                updateStatus.setValue(sel.getStatus());
            }
        });
        HBox addBar = new HBox(8, attendanceIdField, studentIdField, courseField, datePicker, componentBox, statusBox, addBtn, editBtn);
        HBox filterBar = new HBox(8, new Label("Filter:"), filterStudent, filterCourse, filterComponent, refreshBtn);
        HBox updateBar = new HBox(8, new Label("New Status:"), updateStatus, updateBtn);
        VBox body;
        if (selfOnly) {
            List<CourseUnit> courses = ctx.getAttendanceDao().getCoursesByStudent(ctx.getCurrentUser().getId());
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
            HBox hoursBar = new HBox(10, ugHoursLabel, ugHoursBar);
            body = new VBox(10, courseBar2, hoursBar, table);
        } else if (canManage) {
            try {
                for (CourseUnit c : ctx.getMarksService().getAllCourses())
                    filterCourse.getItems().add(c.getCourseCode() + " - " + c.getTitle());
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
            body = new VBox(10, addBar, filterBar, table, updateBar);
        } else {
            try {
                for (CourseUnit c : ctx.getMarksService().getAllCourses())
                    filterCourse.getItems().add(c.getCourseCode() + " - " + c.getTitle());
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
            body = new VBox(10, filterBar, table);
        }
        body.setPadding(new Insets(16));
        page.setCenter(body);
        ctx.show(page);
        refreshBtn.fire();
    }

    public void openMedical(boolean canManage, boolean selfOnly, Runnable onHome, Runnable onLogout) {
        BorderPane page = ui.buildShell("Medical", onHome, onLogout);
        boolean canSubmitMedical = selfOnly;
        TextField refField = new TextField(ui.generateMedicalRefNo());
        refField.setPromptText("Ref No");
        refField.setEditable(false);
        TextField studentId = new TextField(selfOnly ? ctx.getCurrentUser().getId() : "");
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
            FileChooser fc = new FileChooser();
            fc.setTitle("Select medical image");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.jpeg","*.webp"));
            File file = fc.showOpenDialog(ctx.getRoot().getScene() == null ? null : ctx.getRoot().getScene().getWindow());
            if (file != null) proofPath.setText(file.getAbsolutePath());
        });
        DatePicker startDate = new DatePicker(LocalDate.now());
        DatePicker endDate = new DatePicker(LocalDate.now());
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Pending","Approved","Rejected");
        statusBox.setValue("Pending");
        statusBox.setDisable(true);
        Button addBtn = new Button("Submit Medical");
        addBtn.setDisable(!canSubmitMedical);
        TextField filterStudent = new TextField(selfOnly ? ctx.getCurrentUser().getId() : "");
        filterStudent.setPromptText("Filter by Student ID");
        filterStudent.setEditable(!selfOnly);
        Button refreshBtn = new Button("Filter");
        TableView<MedicalRecord> table = new TableView<>(medicalRows);
        table.getColumns().addAll(ui.col("Ref No", x -> x.getRefNo()), ui.col("Student", x -> x.getStudentId()),
                ui.col("Status", x -> x.getStatus()), ui.col("Start", x -> x.getStartDate()),
                ui.col("End", x -> x.getEndDate()), ui.col("Photo", x -> x.getProofImagePath() == null ? "-" : x.getProofImagePath()));
        ComboBox<String> newStatus = new ComboBox<>();
        newStatus.getItems().addAll("Pending","Approved","Rejected");
        newStatus.setValue("Pending");
        Button updateStatusBtn = new Button("Update Selected Status");
        updateStatusBtn.setDisable(!canManage);
        Button editBtn = new Button("Edit Selected Medical");
        editBtn.setDisable(!(canManage || canSubmitMedical));
        Button viewPhotoBtn = new Button("View Selected Photo");
        Label selectedPhoto = new Label("Selected Photo: -");
        selectedPhoto.setWrapText(true);
        addBtn.setOnAction(e -> {
            try {
                ctx.getMedicalDao().addMedical(new MedicalRecord(refField.getText().trim(), studentId.getText().trim(),
                        reason.getText().trim(), "Pending", startDate.getValue().toString(),
                        endDate.getValue().toString(), proofPath.getText().trim()));
                reason.clear();
                proofPath.clear();
                startDate.setValue(LocalDate.now());
                endDate.setValue(LocalDate.now());
                refField.setText(ui.generateMedicalRefNo());
                refreshBtn.fire();
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs,oldV,sel) -> {
            if (sel != null) {
                refField.setText(sel.getRefNo());
                studentId.setText(sel.getStudentId());
                reason.setText(sel.getReason());
                statusBox.setValue(sel.getStatus());
                if (sel.getStartDate() != null) startDate.setValue(LocalDate.parse(sel.getStartDate()));
                if (sel.getEndDate() != null) endDate.setValue(LocalDate.parse(sel.getEndDate()));
                proofPath.setText(sel.getProofImagePath() == null ? "" : sel.getProofImagePath());
                selectedPhoto.setText("Selected Photo: " + (sel.getProofImagePath() == null || sel.getProofImagePath().isBlank() ? "-" : sel.getProofImagePath()));
            }
        });
        refreshBtn.setOnAction(e -> {
            try { medicalRows.setAll(ctx.getMedicalDao().findMedicalByStudent(filterStudent.getText().trim())); }
            catch (Exception ex) { ui.showError(ex.getMessage()); }
        });
        updateStatusBtn.setOnAction(e -> {
            MedicalRecord sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select a medical record first"); return; }
            try { ctx.getMedicalDao().updateMedicalStatus(sel.getRefNo(), newStatus.getValue()); refreshBtn.fire(); }
            catch (Exception ex) { ui.showError(ex.getMessage()); }
        });
        viewPhotoBtn.setOnAction(e -> {
            MedicalRecord sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select a medical record first"); return; }
            String path = sel.getProofImagePath();
            if (path == null || path.isBlank()) { ui.showError("No photo uploaded for selected medical record"); return; }
            ui.showMedicalPhoto(path);
        });
        editBtn.setOnAction(e -> {
            MedicalRecord sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select a medical record first"); return; }
            if (selfOnly && !ctx.getCurrentUser().getId().equals(sel.getStudentId())) { ui.showError("You can edit only your own medical records"); return; }
            try {
                ctx.getMedicalDao().updateMedical(new MedicalRecord(sel.getRefNo(), sel.getStudentId(),
                        reason.getText().trim(), statusBox.getValue(), startDate.getValue().toString(),
                        endDate.getValue().toString(), proofPath.getText().trim()));
                refreshBtn.fire();
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });
        HBox addBar1 = new HBox(8, refField, studentId, statusBox, startDate, endDate, addBtn);
        HBox addBar2 = new HBox(8, new Label("Reason:"), reason);
        HBox addBar3 = new HBox(8, new Label("Photo:"), proofPath, uploadBtn, editBtn);
        HBox filterBar = new HBox(8, new Label("Filter:"), filterStudent, refreshBtn);
        HBox updateBar = new HBox(8, new Label("New Status:"), newStatus, updateStatusBtn);
        VBox body;
        if (canManage) body = new VBox(10, filterBar, table, updateBar, viewPhotoBtn, selectedPhoto);
        else if (canSubmitMedical) body = new VBox(10, addBar1, addBar2, addBar3, filterBar, table, viewPhotoBtn, selectedPhoto);
        else body = new VBox(10, filterBar, table, viewPhotoBtn, selectedPhoto);
        body.setPadding(new Insets(16));
        page.setCenter(body);
        ctx.show(page);
        refreshBtn.fire();
    }

    public void openExamMedical(boolean canManage, boolean selfOnly, Runnable onHome, Runnable onLogout) {
        BorderPane page = ui.buildShell("Exam Medical", onHome, onLogout);
        TableView<ExamMedical> table = new TableView<>(examMedicalRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(
                ui.col("Ref No", x -> x.getRefNo()),
                ui.col("Student", x -> x.getStudentId()),
                ui.col("Course", x -> x.getCourseExam() == null ? "-" : x.getCourseExam().getCourseCode()),
                ui.col("Type", x -> x.getCourseExam() == null ? "-" : x.getCourseExam().getExamTypeId()),
                ui.col("Status", x -> x.getStatus()),
                ui.col("Submitted", x -> x.getSubmittedDate()),
                ui.col("Photo", x -> x.getProofImagePath() == null ? "-" : x.getProofImagePath()));

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> {
            try {
                if (canManage) examMedicalRows.setAll(ctx.getExamMedicalService().getPending());
                else examMedicalRows.setAll(ctx.getExamMedicalService().getByStudent(ctx.getCurrentUser().getId()));
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        VBox body;
        if (canManage) {
            ComboBox<String> statusBox = new ComboBox<>();
            statusBox.getItems().addAll("Approved", "Rejected");
            statusBox.setValue("Approved");
            Button updateBtn = new Button("Update Status");
            updateBtn.setStyle("-fx-background-color: #0ea5e9; -fx-text-fill: white;");
            updateBtn.setOnAction(e -> {
                ExamMedical sel = table.getSelectionModel().getSelectedItem();
                if (sel == null) { ui.showError("Select an exam medical request first"); return; }
                try {
                    ctx.getExamMedicalService().updateStatus(sel.getRefNo(), statusBox.getValue());
                    ui.showInfo("Status updated for " + sel.getRefNo());
                    refreshBtn.fire();
                } catch (Exception ex) { ui.showError(ex.getMessage()); }
            });
            HBox updateBar = new HBox(8, new Label("New Status:"), statusBox, updateBtn);
            Button viewPhotoBtn = new Button("View Selected Photo");
            viewPhotoBtn.setOnAction(e -> {
                ExamMedical sel = table.getSelectionModel().getSelectedItem();
                if (sel == null) { ui.showError("Select an exam medical request first"); return; }
                String path = sel.getProofImagePath();
                if (path == null || path.isBlank()) { ui.showError("No photo uploaded for selected request"); return; }
                ui.showMedicalPhoto(path);
            });
            body = new VBox(10, refreshBtn, table, updateBar, viewPhotoBtn);
        } else if (selfOnly) {
            ComboBox<String> courseBox = new ComboBox<>();
            courseBox.setPrefWidth(320);
            ComboBox<CourseExam> examBox = new ComboBox<>();
            examBox.setPrefWidth(240);
            examBox.setCellFactory(list -> new ListCell<>() {
                protected void updateItem(CourseExam item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : ui.formatExamLabel(item));
                }
            });
            examBox.setButtonCell(new ListCell<>() {
                protected void updateItem(CourseExam item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : ui.formatExamLabel(item));
                }
            });
            Button loadExamsBtn = new Button("Load Exams");
            Runnable loadExams = () -> {
                String code = ui.parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
                if (code.isBlank()) { return; }
                try {
                    List<CourseExam> exams = ctx.getMarksService().getCourseExams(code);
                    List<CourseExam> allowed = new ArrayList<>();
                    for (CourseExam ex : exams) {
                        String typeId = ex.getExamTypeId();
                        if ("MID".equals(typeId) || "FIN".equals(typeId) || "ASST".equals(typeId)) allowed.add(ex);
                    }
                    examBox.getItems().setAll(allowed);
                    if (!allowed.isEmpty()) examBox.getSelectionModel().select(0);
                } catch (Exception ex) { ui.showError(ex.getMessage()); }
            };
            loadExamsBtn.setOnAction(e -> {
                if (courseBox.getValue() == null || ui.parseCourseCode(courseBox.getValue()).isBlank()) {
                    ui.showError("Select a course first");
                    return;
                }
                loadExams.run();
                if (examBox.getItems().isEmpty()) ui.showError("No MID/FIN/ASST exams found for this course");
            });
            courseBox.setOnAction(e -> loadExams.run());

            TextField proofPath = new TextField();
            proofPath.setPromptText("Medical proof image path");
            proofPath.setEditable(false);
            Button uploadBtn = new Button("Upload Photo");
            uploadBtn.setOnAction(e -> {
                FileChooser fc = new FileChooser();
                fc.setTitle("Select exam medical image");
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.jpeg","*.webp"));
                File file = fc.showOpenDialog(ctx.getRoot().getScene() == null ? null : ctx.getRoot().getScene().getWindow());
                if (file != null) proofPath.setText(file.getAbsolutePath());
            });

            Button submitBtn = new Button("Submit Request");
            submitBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white;");
            submitBtn.setOnAction(e -> {
                String code = ui.parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
                CourseExam exam = examBox.getSelectionModel().getSelectedItem();
                if (code.isBlank() || exam == null) { ui.showError("Select a course and exam"); return; }
                if (proofPath.getText().isBlank()) { ui.showError("Upload a medical proof image"); return; }
                try {
                    ctx.getExamMedicalService().submitMedical(ctx.getCurrentUser().getId(), code, exam.getExamTypeId(), proofPath.getText().trim());
                    ui.showInfo("Exam medical submitted");
                    proofPath.clear();
                    refreshBtn.fire();
                } catch (Exception ex) { ui.showError(ex.getMessage()); }
            });

            try {
                courseBox.getItems().clear();
                for (CourseUnit c : ctx.getMarksService().getCoursesByStudent(ctx.getCurrentUser().getId()))
                    courseBox.getItems().add(c.getCourseCode() + " - " + c.getTitle());
                if (!courseBox.getItems().isEmpty()) courseBox.getSelectionModel().select(0);
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
            loadExams.run();

            Label note = new Label("Only MID, FIN, and ASST exams are allowed for exam medical.");
            note.setStyle("-fx-text-fill: #475569;");
            HBox formBar = new HBox(8, new Label("Course:"), courseBox, loadExamsBtn, new Label("Exam:"), examBox, submitBtn);
            HBox proofBar = new HBox(8, new Label("Photo:"), proofPath, uploadBtn);
            Button viewPhotoBtn = new Button("View Selected Photo");
            viewPhotoBtn.setOnAction(e -> {
                ExamMedical sel = table.getSelectionModel().getSelectedItem();
                if (sel == null) { ui.showError("Select an exam medical request first"); return; }
                String path = sel.getProofImagePath();
                if (path == null || path.isBlank()) { ui.showError("No photo uploaded for selected request"); return; }
                ui.showMedicalPhoto(path);
            });
            body = new VBox(10, formBar, proofBar, note, refreshBtn, table, viewPhotoBtn);
        } else {
            body = new VBox(10, refreshBtn, table);
        }

        body.setPadding(new Insets(16));
        page.setCenter(body);
        ctx.show(page);
        refreshBtn.fire();
    }
}
