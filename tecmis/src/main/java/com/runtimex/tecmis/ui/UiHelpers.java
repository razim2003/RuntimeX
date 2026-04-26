package com.runtimex.tecmis.ui;

import com.runtimex.tecmis.models.CourseExam;
import com.runtimex.tecmis.models.CourseResultSummary;
import com.runtimex.tecmis.models.CourseUnit;
import com.runtimex.tecmis.models.StudentInfo;
import com.runtimex.tecmis.models.Timetable;
import com.runtimex.tecmis.models.UserProfile;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import javax.imageio.ImageIO;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.embed.swing.SwingFXUtils;

public class UiHelpers {
    private static final String LOGO_RESOURCE = "/com/runtimex/tecmis/ui/university-logo.jfif";
    private static final int LOGO_LG = 64;
    private static final int LOGO_SM = 28;

    private final UiContext ctx;

    public UiHelpers(UiContext ctx) {
        this.ctx = ctx;
    }

    public BorderPane buildShell(String section, Runnable onHome, Runnable onLogout) {
        BorderPane page = new BorderPane();
        page.getStyleClass().add("page-bg");

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(12));
        topBar.getStyleClass().add("topbar");

        Label title = new Label("TecMIS");
        title.getStyleClass().add("topbar-title");
        Label sec = new Label("| " + section);
        sec.getStyleClass().add("topbar-section");

        String userLabel = ctx.getCurrentUser() == null
                ? ""
                : ctx.getCurrentUser().getFullName() + "  (" + ctx.getCurrentUser().getUserType() + ")";
        Label userInfo = new Label(userLabel);
        userInfo.getStyleClass().add("topbar-user");

        Region gap = new Region();
        HBox.setHgrow(gap, Priority.ALWAYS);

        Button homeBtn = new Button("🏠 Home");
        homeBtn.setOnAction(e -> onHome.run());
        Button logoutBtn = new Button("⏻ Logout");
        logoutBtn.getStyleClass().addAll("button", "danger");
        logoutBtn.setOnAction(e -> onLogout.run());

        HBox brand = new HBox(8, buildLogo(LOGO_SM), title);
        brand.setAlignment(Pos.CENTER_LEFT);

        topBar.getChildren().addAll(brand, sec, gap, buildTopBarAvatar(), userInfo, homeBtn, logoutBtn);
        page.setTop(topBar);
        return page;
    }

    private StackPane buildTopBarAvatar() {
        StackPane avatar = new StackPane();
        avatar.setPrefSize(34, 34);
        avatar.setMinSize(34, 34);
        avatar.setMaxSize(34, 34);
        avatar.setStyle("-fx-background-color: #1e293b; -fx-background-radius: 17;"
                + "-fx-border-color: #64748b; -fx-border-radius: 17; -fx-border-width: 1;");
        try {
            if (ctx.getCurrentUser() != null) {
                UserProfile profile = ctx.getUserDao().findById(ctx.getCurrentUser().getId());
                String imagePath = profile == null ? null : profile.getProfileImagePath();
                if (imagePath != null && !imagePath.isBlank()) {
                    File file = new File(imagePath);
                    if (file.exists() && file.isFile()) {
                        Image image = new Image(file.toURI().toString(), 34, 34, true, true);
                        ImageView iv = new ImageView(image);
                        iv.setFitWidth(34);
                        iv.setFitHeight(34);
                        iv.setPreserveRatio(false);
                        iv.setClip(new Circle(17, 17, 17));
                        avatar.getChildren().add(iv);
                        return avatar;
                    }
                }
            }
        } catch (Exception ignored) {}
        Label fallback = new Label("👤");
        fallback.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 14px;");
        avatar.getChildren().add(fallback);
        return avatar;
    }

    public StackPane buildLogo(int size) {
        StackPane logo = new StackPane();
        logo.getStyleClass().add("logo-badge");
        logo.setPrefSize(size, size);
        logo.setMinSize(size, size);
        logo.setMaxSize(size, size);

        Image image = loadLogoImage(size);
        if (image != null && !image.isError()) {
            ImageView iv = new ImageView(image);
            iv.setFitWidth(size);
            iv.setFitHeight(size);
            iv.setPreserveRatio(true);
            logo.getChildren().add(iv);
        } else {
            Label fallback = new Label("UNI");
            fallback.getStyleClass().add("logo-fallback");
            logo.getChildren().add(fallback);
        }
        return logo;
    }

    public VBox card(String title, String description, Runnable action) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(14));
        box.setMinHeight(130);
        box.getStyleClass().add("card");
        Label t = new Label(title);
        t.getStyleClass().add("card-title");
        Label d = new Label(description);
        d.setWrapText(true);
        d.getStyleClass().add("card-body");
        Button open = new Button("Open");
        open.getStyleClass().addAll("button", "primary");
        open.setOnAction(e -> action.run());
        box.getChildren().addAll(t, d, open);
        return box;
    }

    public boolean isValidEmail(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return value.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public boolean isValidPhone(String value) {
        if (value == null) {
            return false;
        }
        String digits = value.replaceAll("\\D", "");
        return digits.length() >= 10;
    }

    public <T> TableColumn<T, String> col(String header,
            java.util.function.Function<T, String> extractor) {
        TableColumn<T, String> c = new TableColumn<>(header);
        c.setCellValueFactory(x -> new SimpleStringProperty(extractor.apply(x.getValue())));
        return c;
    }

    public void addTimetableColumns(TableView<Timetable> table) {
        table.getColumns().addAll(
                col("ID", x -> x.getTimetableId()),
                col("Course", x -> x.getCourseCode()),
                col("Lecturer", x -> x.getLecturerId() == null ? "-" : x.getLecturerId()),
                col("Level", x -> String.valueOf(x.getLevel())),
                col("Type", x -> x.getType()),
                col("Day", x -> x.getDayOfWeek() == null ? "-" : x.getDayOfWeek()),
                col("Start", x -> x.getStartTime() == null ? "-" : x.getStartTime()),
                col("Hours", x -> String.valueOf(x.getHours())),
                col("Location", x -> x.getLocation())
        );
    }

    public void loadCourseOptions(ComboBox<String> courseBox) {
        try {
            courseBox.getItems().clear();
            List<CourseUnit> courses = ctx.getMarksService().getAllCourses();
            for (CourseUnit c : courses) {
                courseBox.getItems().add(c.getCourseCode() + " - " + c.getTitle());
            }
            if (!courseBox.getItems().isEmpty()) {
                courseBox.getSelectionModel().select(0);
            }
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    public String parseCourseCode(String display) {
        int idx = display.indexOf(" - ");
        return idx <= 0 ? display : display.substring(0, idx).trim();
    }

    public String parseStudentId(String display) {
        if (display == null) return "";
        int idx = display.indexOf(" - ");
        return idx <= 0 ? display.trim() : display.substring(0, idx).trim();
    }

    public String generateMedicalRefNo() {
        long value = Math.abs(System.nanoTime() % 46656L);
        return "REF" + String.format("%3s", Long.toString(value, 36)).replace(' ', '0').toUpperCase();
    }

    public String generateMarkId() {
        return "MK" + String.format("%09d", Math.abs(System.nanoTime() % 1_000_000_000L));
    }

    public String generateId(String prefix) {
        return prefix + String.format("%08d", Math.abs(System.nanoTime() % 100_000_000L));
    }

    public String humanSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024));
    }

    public String fileExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        return idx < 0 ? "" : filename.substring(idx + 1).toLowerCase();
    }

    public String fileExt(String fileName) {
        if (fileName == null) return null;
        int dot = fileName.lastIndexOf('.');
        return dot >= 0 ? fileName.substring(dot + 1).toLowerCase() : null;
    }

    public void showMedicalPhoto(String path) {
        try {
            File file = new File(path);
            if (!file.exists() || !file.isFile()) {
                showError("Photo file not found: " + path);
                return;
            }
            ImageView iv = new ImageView(new Image(file.toURI().toString()));
            iv.setFitWidth(520);
            iv.setFitHeight(360);
            iv.setPreserveRatio(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Medical Photo");
            alert.setHeaderText("Uploaded Medical Photo");
            alert.getDialogPane().setContent(iv);
            alert.showAndWait();
        } catch (Exception ex) {
            showError("Unable to open photo: " + ex.getMessage());
        }
    }

    public String formatExamLabel(CourseExam exam) {
        String name = exam.getExamName();
        if (name == null || name.isBlank()) name = exam.getExamTypeName();
        if (name == null || name.isBlank()) name = "Exam";
        return exam.getExamTypeId() + " - " + name;
    }

    public String formatStudentLabel(StudentInfo student) {
        return student.getStudentId() + " - " + student.getStudentName();
    }

    public String formatEndStatusForStudent(CourseResultSummary s) {
        return s.isMedicalConcession() ? "MC" : s.getEndStatus();
    }

    public String formatGradeForStudent(CourseResultSummary s) {
        return s.isMedicalConcession() ? "MC" : s.getGrade();
    }

    public String formatTotalForStudent(CourseResultSummary s) {
        return s.isMedicalConcession() ? "MC" : String.format("%.2f", s.getTotalMark());
    }

    public HBox hRow(String labelText, Label value) {
        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-font-weight: bold; -fx-min-width: 90px;");
        HBox row = new HBox(8, lbl, value);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Operation Failed");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Completed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Image loadLogoImage(int size) {
        try {
            URL resource = getClass().getResource(LOGO_RESOURCE);
            if (resource == null) {
                return null;
            }
            Image image = new Image(resource.toExternalForm(), size, size, true, true);
            if (image != null && !image.isError()) {
                return image;
            }
            try (InputStream in = resource.openStream()) {
                BufferedImage buffered = ImageIO.read(in);
                if (buffered == null) {
                    return null;
                }
                return SwingFXUtils.toFXImage(buffered, null);
            }
        } catch (Exception ignored) {
            return null;
        }
    }

    public int getLogoLargeSize() {
        return LOGO_LG;
    }

    public int getLogoSmallSize() {
        return LOGO_SM;
    }
}
