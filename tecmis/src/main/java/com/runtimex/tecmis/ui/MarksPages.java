package com.runtimex.tecmis.ui;

import com.runtimex.tecmis.models.CourseExam;
import com.runtimex.tecmis.models.CourseResultSummary;
import com.runtimex.tecmis.models.CourseUnit;
import com.runtimex.tecmis.models.Mark;
import com.runtimex.tecmis.models.MarkEntry;
import com.runtimex.tecmis.models.StudentGpaSummary;
import com.runtimex.tecmis.models.StudentInfo;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MarksPages {
    private final UiContext ctx;
    private final UiHelpers ui;
    private final ObservableList<CourseResultSummary> courseResultRows = FXCollections.observableArrayList();
    private final ObservableList<StudentGpaSummary> gpaRows = FXCollections.observableArrayList();

    public MarksPages(UiContext ctx, UiHelpers ui) {
        this.ctx = ctx;
        this.ui = ui;
    }

    public void openMarksUpload(Runnable onHome, Runnable onLogout) {
        BorderPane page = ui.buildShell("Marks Upload", onHome, onLogout);
        ComboBox<String> courseBox = new ComboBox<>();
        courseBox.setPrefWidth(320);
        courseBox.setEditable(true);
        ui.loadCourseOptions(courseBox);
        ComboBox<CourseExam> examBox = new ComboBox<>();
        examBox.setPrefWidth(320);
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
        ComboBox<StudentInfo> studentBox = new ComboBox<>();
        studentBox.setPrefWidth(320);
        studentBox.setCellFactory(list -> new ListCell<>() {
            protected void updateItem(StudentInfo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : ui.formatStudentLabel(item));
            }
        });
        studentBox.setButtonCell(new ListCell<>() {
            protected void updateItem(StudentInfo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : ui.formatStudentLabel(item));
            }
        });
        TextField markField = new TextField();
        markField.setPromptText("Mark (0-100)");
        Button loadBtn = new Button("Load");
        Button saveBtn = new Button("Save Mark");
        loadBtn.setOnAction(e -> {
            String code = ui.parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            if (code.isBlank()) { ui.showError("Select a course first"); return; }
            try {
                List<CourseExam> exams = ctx.getMarksService().getCourseExams(code);
                examBox.getItems().setAll(exams);
                if (!exams.isEmpty()) examBox.getSelectionModel().select(0);
                List<StudentInfo> students = ctx.getMarksService().getStudentsByCourse(code);
                studentBox.getItems().setAll(students);
                if (!students.isEmpty()) studentBox.getSelectionModel().select(0);
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });
        saveBtn.setOnAction(e -> {
            String code = ui.parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            CourseExam exam = examBox.getSelectionModel().getSelectedItem();
            StudentInfo student = studentBox.getSelectionModel().getSelectedItem();
            if (code.isBlank() || exam == null || student == null) { ui.showError("Select course, exam, and student"); return; }
            double value;
            try { value = Double.parseDouble(markField.getText().trim()); }
            catch (NumberFormatException ex) { ui.showError("Enter a valid numeric mark"); return; }
            if (value < 0 || value > 100) { ui.showError("Mark should be between 0 and 100"); return; }
            try {
                MarkEntry existing = ctx.getMarksDao().getMarkEntry(student.getStudentId(), code, exam.getExamTypeId());
                CourseExam ce = new CourseExam();
                ce.setCourseCode(code);
                ce.setExamTypeId(exam.getExamTypeId());
                if (existing == null) {
                    ctx.getMarksDao().addMark(new Mark(ui.generateMarkId(), student.getStudentId(), ce, value));
                    ui.showInfo("Mark added for " + student.getStudentId());
                } else {
                    ctx.getMarksDao().updateMark(new Mark(existing.getMarkId(), student.getStudentId(), ce, value));
                    ui.showInfo("Mark updated for " + student.getStudentId());
                }
                gpaRows.setAll(ctx.getMarksService().getStudentGpaSummariesForCourse(code, true));
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });
        HBox filters = new HBox(8, new Label("Course:"), courseBox, loadBtn);
        HBox entryRow = new HBox(8, new Label("Exam:"), examBox, new Label("Student:"), studentBox, new Label("Mark:"), markField, saveBtn);
        VBox body = new VBox(12, filters, entryRow);
        body.setPadding(new Insets(16));
        page.setCenter(body);
        ctx.show(page);
        loadBtn.fire();
    }

    public void openMarksOverviewForUndergraduate(Runnable onHome, Runnable onLogout) {
        BorderPane page = ui.buildShell("My Grades & GPA", onHome, onLogout);
        TableView<CourseResultSummary> table = new TableView<>(courseResultRows);
        table.getColumns().addAll(
                ui.col("Course", x -> x.getCourseCode()),
                ui.col("CA Status", x -> x.getCaStatus()),
                ui.col("End Status", x -> ui.formatEndStatusForStudent(x)),
                ui.col("Grade", x -> ui.formatGradeForStudent(x)));
        Label sgpaLabel = new Label("SGPA: -");
        Label cgpaLabel = new Label("CGPA: -");
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> {
            try {
                List<CourseUnit> courses = ctx.getMarksService().getCoursesByStudent(ctx.getCurrentUser().getId());
                List<CourseResultSummary> rows = new ArrayList<>();
                for (CourseUnit course : courses)
                    rows.add(ctx.getMarksService().getCourseResult(ctx.getCurrentUser().getId(), course.getCourseCode(), true));
                courseResultRows.setAll(rows);
                StudentGpaSummary gpa = ctx.getMarksService().getStudentGpaSummary(ctx.getCurrentUser().getId(), true);
                sgpaLabel.setText(String.format("SGPA: %.2f", gpa.getSgpa()));
                cgpaLabel.setText(String.format("CGPA: %.2f", gpa.getCgpa()));
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });
        HBox gpaBar = new HBox(16, sgpaLabel, cgpaLabel, refreshBtn);
        VBox body = new VBox(12, table, gpaBar);
        body.setPadding(new Insets(16));
        page.setCenter(body);
        ctx.show(page);
        refreshBtn.fire();
    }

    public void openMarksOverviewForLecturer(Runnable onHome, Runnable onLogout) {
        BorderPane page = ui.buildShell("Marks, Grades & GPA", onHome, onLogout);
        ComboBox<String> courseBox = new ComboBox<>();
        courseBox.setPrefWidth(320);
        courseBox.setEditable(true);
        ui.loadCourseOptions(courseBox);
        TextField studentFilter = new TextField();
        studentFilter.setPromptText("Filter Student ID");
        CheckBox includeMedical = new CheckBox("Count approved medical for attendance");
        includeMedical.setSelected(true);
        Button loadBtn = new Button("Load Results");
        Button gpaBtn = new Button("Load GPA");
        TableView<CourseResultSummary> resultsTable = new TableView<>(courseResultRows);
        resultsTable.getColumns().addAll(
                ui.col("Student ID", x -> x.getStudentId()),
                ui.col("Name", x -> x.getStudentName()),
                ui.col("Attendance%", x -> String.format("%.2f", x.getAttendancePercentage())),
                ui.col("CA%", x -> String.format("%.2f", x.getCaPercentage())),
                ui.col("CA Status", x -> x.getCaStatus()),
                ui.col("Eligibility", x -> x.getEligibilityStatus()),
                ui.col("End%", x -> String.format("%.2f", x.getEndPercentage())),
                ui.col("End Status", x -> ui.formatEndStatusForStudent(x)),
                ui.col("Total", x -> ui.formatTotalForStudent(x)),
                ui.col("Grade", x -> ui.formatGradeForStudent(x)));
        TableView<StudentGpaSummary> gpaTable = new TableView<>(gpaRows);
        gpaTable.getColumns().addAll(
                ui.col("Student ID", x -> x.getStudentId()),
                ui.col("Name", x -> x.getStudentName()),
                ui.col("SGPA", x -> String.format("%.2f", x.getSgpa())),
                ui.col("CGPA", x -> String.format("%.2f", x.getCgpa())));
        loadBtn.setOnAction(e -> {
            String code = ui.parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            if (code.isBlank()) { ui.showError("Select a course first"); return; }
            try {
                List<CourseResultSummary> results = ctx.getMarksService().getCourseResultsForBatch(code, includeMedical.isSelected());
                String filter = studentFilter.getText().trim();
                if (!filter.isBlank()) results.removeIf(r -> !r.getStudentId().contains(filter));
                courseResultRows.setAll(results);
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });
        gpaBtn.setOnAction(e -> {
            String code = ui.parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            if (code.isBlank()) { ui.showError("Select a course first"); return; }
            try {
                List<StudentGpaSummary> results = ctx.getMarksService().getStudentGpaSummariesForCourse(code, includeMedical.isSelected());
                String filter = studentFilter.getText().trim();
                if (!filter.isBlank()) results.removeIf(r -> !r.getStudentId().contains(filter));
                gpaRows.setAll(results);
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });
        HBox controls = new HBox(10, new Label("Course:"), courseBox, studentFilter, includeMedical, loadBtn, gpaBtn);
        VBox body = new VBox(12, controls, resultsTable, new Label("GPA Summary"), gpaTable);
        body.setPadding(new Insets(16));
        page.setCenter(body);
        ctx.show(page);
        loadBtn.fire();
        gpaBtn.fire();
    }

    public void openSummary(Runnable onHome, Runnable onLogout) {
        BorderPane page = ui.buildShell("Eligibility (Attendance + CA)", onHome, onLogout);
        ComboBox<String> courseBox = new ComboBox<>();
        courseBox.setPrefWidth(320);
        courseBox.setEditable(true);
        ui.loadCourseOptions(courseBox);
        TextField studentFilter = new TextField();
        studentFilter.setPromptText("Filter Student ID");
        CheckBox includeMedical = new CheckBox("Count approved medical for attendance");
        includeMedical.setSelected(true);
        Button loadBtn = new Button("Generate Summary");
        TableView<CourseResultSummary> table = new TableView<>(courseResultRows);
        table.getColumns().addAll(
                ui.col("Student ID", x -> x.getStudentId()),
                ui.col("Name", x -> x.getStudentName()),
                ui.col("Attendance%", x -> String.format("%.2f", x.getAttendancePercentage())),
                ui.col("CA%", x -> String.format("%.2f", x.getCaPercentage())),
                ui.col("CA Status", x -> x.getCaStatus()),
                ui.col("Eligibility", x -> x.getEligibilityStatus()));
        loadBtn.setOnAction(e -> {
            String code = ui.parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            if (code.isBlank()) { ui.showError("Select a course first"); return; }
            try {
                List<CourseResultSummary> results = ctx.getMarksService().getCourseResultsForBatch(code, includeMedical.isSelected());
                String filter = studentFilter.getText().trim();
                if (!filter.isBlank()) results.removeIf(r -> !r.getStudentId().contains(filter));
                courseResultRows.setAll(results);
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });
        HBox controls = new HBox(8, new Label("Course:"), courseBox, studentFilter, includeMedical, loadBtn);
        VBox body = new VBox(10, controls, table);
        body.setPadding(new Insets(16));
        page.setCenter(body);
        ctx.show(page);
        loadBtn.fire();
    }
}
