package com.runtimex.tecmis.ui;

import com.runtimex.tecmis.models.CourseMaterial;
import com.runtimex.tecmis.models.CourseUnit;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class CourseMaterialPages {
    private static final java.nio.file.Path MATERIAL_STORE =
            java.nio.file.Paths.get(System.getProperty("user.home"), "tecmis_materials");

    private final UiContext ctx;
    private final UiHelpers ui;
    private final ObservableList<CourseMaterial> materialRows = FXCollections.observableArrayList();

    public CourseMaterialPages(UiContext ctx, UiHelpers ui) {
        this.ctx = ctx;
        this.ui = ui;
    }

    public void openLecturerMaterials(Runnable onHome, Runnable onLogout) {
        BorderPane page = ui.buildShell("Course Materials", onHome, onLogout);

        ComboBox<String> courseBox = new ComboBox<>();
        courseBox.setPromptText("Select course");
        courseBox.setPrefWidth(300);
        try {
            List<CourseUnit> courses = ctx.getMarksService().getAllCourses();
            for (CourseUnit c : courses)
                courseBox.getItems().add(c.getCourseCode() + " - " + c.getTitle());
            if (!courseBox.getItems().isEmpty()) courseBox.getSelectionModel().select(0);
        } catch (Exception ex) { ui.showError(ex.getMessage()); }

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextArea descArea = new TextArea();
        descArea.setPromptText("Description (optional)");
        descArea.setPrefRowCount(2);
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("File", "Link", "Text");
        typeBox.setValue("File");

        Label fileLabel = new Label("No file selected");
        fileLabel.setStyle("-fx-text-fill: #475569;");
        fileLabel.setMaxWidth(340);
        fileLabel.setWrapText(true);
        Button browseBtn = new Button("\uD83D\uDCC2 Browse...");
        final File[] chosenFile = {null};
        browseBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Select material file");
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Documents", "*.pdf","*.docx","*.doc","*.pptx","*.xlsx","*.txt"),
                    new FileChooser.ExtensionFilter("Images", "*.png","*.jpg","*.jpeg","*.gif","*.webp"),
                    new FileChooser.ExtensionFilter("All files", "*.*"));
            File f = fc.showOpenDialog(ctx.getRoot().getScene() == null ? null : ctx.getRoot().getScene().getWindow());
            if (f != null) {
                chosenFile[0] = f;
                fileLabel.setText(f.getName() + "  (" + ui.humanSize(f.length()) + ")");
                typeBox.setValue("File");
            }
        });
        HBox fileRow = new HBox(8, browseBtn, fileLabel);
        fileRow.setAlignment(Pos.CENTER_LEFT);

        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Paste URL (Link) or write text content (Text)");
        contentArea.setPrefRowCount(3);

        typeBox.setOnAction(e -> {
            boolean isFile = "File".equals(typeBox.getValue());
            fileRow.setVisible(isFile);
            fileRow.setManaged(isFile);
            contentArea.setVisible(!isFile);
            contentArea.setManaged(!isFile);
        });
        contentArea.setVisible(false);
        contentArea.setManaged(false);

        TableView<CourseMaterial> table = new TableView<>(materialRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        addMaterialColumns(table, true);

        Button loadBtn = new Button("Load");
        Button addBtn = new Button("\u271A Add");
        Button updateBtn = new Button("\u270E Update");
        Button deleteBtn = new Button("\u2716 Delete");
        addBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white;");
        updateBtn.setStyle("-fx-background-color: #0284c7; -fx-text-fill: white;");
        deleteBtn.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white;");

        loadBtn.setOnAction(e -> {
            String code = ui.parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            if (code.isBlank()) { ui.showError("Select a course first"); return; }
            try { materialRows.setAll(ctx.getMaterialDao().getMaterialsByLecturerAndCourse(ctx.getCurrentUser().getId(), code)); }
            catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        addBtn.setOnAction(e -> {
            String code = ui.parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            if (code.isBlank() || titleField.getText().isBlank()) {
                ui.showError("Course and title are required");
                return;
            }
            String type = typeBox.getValue();
            String content = "";
            if ("File".equals(type)) {
                if (chosenFile[0] == null) { ui.showError("Please select a file to upload"); return; }
                try { content = copyToStore(chosenFile[0], ui.generateId("CM")); }
                catch (Exception ex) { ui.showError("File copy failed: " + ex.getMessage()); return; }
            } else {
                content = contentArea.getText().trim();
            }
            CourseMaterial m = new CourseMaterial(
                    ui.generateId("CM"), code, ctx.getCurrentUser().getId(),
                    titleField.getText().trim(), descArea.getText().trim(),
                    type, content, LocalDate.now().toString());
            try {
                ctx.getMaterialDao().addMaterial(m);
                clearLecturerMaterialForm(titleField, descArea, contentArea, fileLabel, chosenFile);
                loadBtn.fire();
                ui.showInfo("Material added successfully");
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, sel) -> {
            if (sel == null) return;
            titleField.setText(sel.getTitle());
            descArea.setText(sel.getDescription() == null ? "" : sel.getDescription());
            typeBox.setValue(sel.getMaterialType());
            boolean isFile = "File".equals(sel.getMaterialType());
            fileRow.setVisible(isFile);
            fileRow.setManaged(isFile);
            contentArea.setVisible(!isFile);
            contentArea.setManaged(!isFile);
            if (isFile) {
                chosenFile[0] = null;
                String stored = sel.getContent();
                fileLabel.setText(stored == null || stored.isBlank() ? "No file stored" : new File(stored).getName());
            } else {
                contentArea.setText(sel.getContent() == null ? "" : sel.getContent());
            }
        });

        updateBtn.setOnAction(e -> {
            CourseMaterial sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select a material first"); return; }
            sel.setTitle(titleField.getText().trim());
            sel.setDescription(descArea.getText().trim());
            sel.setMaterialType(typeBox.getValue());
            sel.setUploadedDate(LocalDate.now().toString());
            if ("File".equals(typeBox.getValue())) {
                if (chosenFile[0] != null) {
                    try { sel.setContent(copyToStore(chosenFile[0], sel.getMaterialId())); }
                    catch (Exception ex) { ui.showError("File copy failed: " + ex.getMessage()); return; }
                }
            } else {
                sel.setContent(contentArea.getText().trim());
            }
            try {
                boolean ok = ctx.getMaterialDao().updateMaterial(sel, ctx.getCurrentUser().getId());
                if (ok) { loadBtn.fire(); ui.showInfo("Material updated"); }
                else ui.showError("Update failed – you may not own this material");
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        deleteBtn.setOnAction(e -> {
            CourseMaterial sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { ui.showError("Select a material first"); return; }
            try {
                boolean ok = ctx.getMaterialDao().deleteMaterial(sel.getMaterialId(), ctx.getCurrentUser().getId());
                if (ok) { loadBtn.fire(); ui.showInfo("Material deleted"); }
                else ui.showError("Delete failed – you may not own this material");
            } catch (Exception ex) { ui.showError(ex.getMessage()); }
        });

        HBox courseBar = new HBox(8, new Label("Course:"), courseBox, loadBtn);
        HBox typeBar = new HBox(8, new Label("Type:"), typeBox);
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
        ctx.show(page);
        loadBtn.fire();
    }

    public void openUndergraduateMaterials(Runnable onHome, Runnable onLogout) {
        BorderPane page = ui.buildShell("Course Materials", onHome, onLogout);

        ComboBox<String> courseBox = new ComboBox<>();
        courseBox.setPromptText("Select course");
        courseBox.setPrefWidth(300);
        try {
            List<CourseUnit> courses = ctx.getMarksService().getCoursesByStudent(ctx.getCurrentUser().getId());
            for (CourseUnit c : courses)
                courseBox.getItems().add(c.getCourseCode() + " - " + c.getTitle());
            if (!courseBox.getItems().isEmpty()) courseBox.getSelectionModel().select(0);
        } catch (Exception ex) { ui.showError(ex.getMessage()); }

        TableView<CourseMaterial> table = new TableView<>(materialRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        addMaterialColumns(table, false);

        Label typeLabel = new Label();
        typeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #0f172a;");

        Label titleLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1e40af;");

        Label descLabel = new Label();
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-text-fill: #334155;");

        TextArea contentView = new TextArea();
        contentView.setEditable(false);
        contentView.setPrefRowCount(4);
        contentView.setPromptText("Content will appear here for Text/Link materials");

        Button openFileBtn = new Button("\uD83D\uDCC2 Open File");
        Button downloadFileBtn = new Button("\u2B07 Download / Save As...");
        openFileBtn.setStyle("-fx-background-color: #0284c7; -fx-text-fill: white; -fx-font-weight: bold;");
        downloadFileBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");
        openFileBtn.setVisible(false);
        openFileBtn.setManaged(false);
        downloadFileBtn.setVisible(false);
        downloadFileBtn.setManaged(false);

        Label fileInfoLabel = new Label();
        fileInfoLabel.setStyle("-fx-text-fill: #475569;");

        HBox fileActionBar = new HBox(10, openFileBtn, downloadFileBtn, fileInfoLabel);
        fileActionBar.setAlignment(Pos.CENTER_LEFT);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, sel) -> {
            if (sel == null) return;
            typeLabel.setText("[" + sel.getMaterialType() + "]");
            titleLabel.setText(sel.getTitle());
            descLabel.setText(sel.getDescription() == null ? "" : sel.getDescription());

            boolean isFile = "File".equals(sel.getMaterialType());
            openFileBtn.setVisible(isFile);
            openFileBtn.setManaged(isFile);
            downloadFileBtn.setVisible(isFile);
            downloadFileBtn.setManaged(isFile);
            contentView.setVisible(!isFile);
            contentView.setManaged(!isFile);

            if (isFile) {
                String storedPath = sel.getContent();
                File f = storedPath == null ? null : new File(storedPath);
                boolean exists = f != null && f.exists() && f.isFile();
                fileInfoLabel.setText(exists
                        ? f.getName() + "  (" + ui.humanSize(f.length()) + ")"
                        : "\u26A0 File not found on disk: " + storedPath);
                openFileBtn.setDisable(!exists);
                downloadFileBtn.setDisable(!exists);

                openFileBtn.setOnAction(ev -> {
                    try { java.awt.Desktop.getDesktop().open(f); }
                    catch (Exception ex) { ui.showError("Cannot open file: " + ex.getMessage()); }
                });

                downloadFileBtn.setOnAction(ev -> {
                    FileChooser fc = new FileChooser();
                    fc.setTitle("Save file as...");
                    fc.setInitialFileName(f.getName());
                    String ext = ui.fileExtension(f.getName());
                    if (!ext.isEmpty())
                        fc.getExtensionFilters().add(
                                new FileChooser.ExtensionFilter(ext.toUpperCase() + " file", "*." + ext));
                    fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files","*.*"));
                    File dest = fc.showSaveDialog(ctx.getRoot().getScene() == null ? null : ctx.getRoot().getScene().getWindow());
                    if (dest == null) return;
                    try {
                        java.nio.file.Files.copy(f.toPath(), dest.toPath(),
                                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        ui.showInfo("File saved to:\n" + dest.getAbsolutePath());
                    } catch (Exception ex) { ui.showError("Download failed: " + ex.getMessage()); }
                });
            } else {
                contentView.setText(sel.getContent() == null ? "" : sel.getContent());
            }
        });

        Button loadBtn = new Button("Load");
        loadBtn.setOnAction(e -> {
            String code = ui.parseCourseCode(courseBox.getValue() == null ? "" : courseBox.getValue());
            if (code.isBlank()) { ui.showError("Select a course first"); return; }
            try { materialRows.setAll(ctx.getMaterialDao().getMaterialsByCourse(code)); }
            catch (Exception ex) { ui.showError(ex.getMessage()); }
        });
        courseBox.setOnAction(e -> loadBtn.fire());

        VBox detailPane = new VBox(6, typeLabel, titleLabel, descLabel, fileActionBar, contentView);
        detailPane.setPadding(new Insets(10, 0, 0, 0));

        Separator sep = new Separator();

        HBox courseBar = new HBox(8, new Label("Course:"), courseBox, loadBtn);
        VBox body = new VBox(10, courseBar, table, sep, detailPane);
        body.setPadding(new Insets(16));
        page.setCenter(body);
        ctx.show(page);
        loadBtn.fire();
    }

    private void addMaterialColumns(TableView<CourseMaterial> table, boolean includeLec) {
        TableColumn<CourseMaterial, String> ttCol = ui.col("Title", x -> x.getTitle());
        TableColumn<CourseMaterial, String> tpCol = ui.col("Type", x -> x.getMaterialType());
        TableColumn<CourseMaterial, String> dtCol = ui.col("Uploaded", x -> x.getUploadedDate());
        TableColumn<CourseMaterial, String> dsCol = ui.col("Description", x -> x.getDescription() == null ? "" : x.getDescription());
        TableColumn<CourseMaterial, String> fnCol = ui.col("File / Content",
                x -> "File".equals(x.getMaterialType())
                        ? (x.getContent() == null ? "-" : new File(x.getContent()).getName())
                        : (x.getContent() == null ? "" : x.getContent().length() > 60
                                ? x.getContent().substring(0, 60) + "..." : x.getContent()));
        table.getColumns().addAll(ttCol, tpCol, dtCol, dsCol, fnCol);
        if (includeLec) table.getColumns().add(ui.col("Lecturer", x -> x.getLecId()));
    }

    private void clearLecturerMaterialForm(TextField titleField, TextArea descArea,
            TextArea contentArea, Label fileLabel, File[] chosenFile) {
        titleField.clear();
        descArea.clear();
        contentArea.clear();
        fileLabel.setText("No file selected");
        chosenFile[0] = null;
    }

    private java.nio.file.Path materialStore() {
        try { java.nio.file.Files.createDirectories(MATERIAL_STORE); }
        catch (Exception ignored) {}
        return MATERIAL_STORE;
    }

    private String copyToStore(File source, String materialId) throws java.io.IOException {
        String safeName = materialId + "_" + source.getName()
                .replaceAll("[^a-zA-Z0-9._\\-]", "_");
        java.nio.file.Path dest = materialStore().resolve(safeName);
        java.nio.file.Files.copy(source.toPath(), dest,
                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        return dest.toString();
    }
}
