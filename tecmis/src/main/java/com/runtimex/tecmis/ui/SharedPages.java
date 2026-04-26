package com.runtimex.tecmis.ui;

import com.runtimex.tecmis.models.Notice;
import com.runtimex.tecmis.models.UserProfile;
import java.awt.Desktop;
import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class SharedPages {
    private final UiContext ctx;
    private final UiHelpers ui;
    private final ObservableList<Notice> noticeRows = FXCollections.observableArrayList();

    public SharedPages(UiContext ctx, UiHelpers ui) {
        this.ctx = ctx;
        this.ui = ui;
    }

    public void openMyProfileEditor(Runnable onHome, Runnable onLogout) {
        if (ctx.getCurrentUser() == null) {
            ui.showError("You must log in first");
            return;
        }
        BorderPane page = ui.buildShell("My Profile", onHome, onLogout);
        UserProfile me = ctx.getUserDao().findById(ctx.getCurrentUser().getId());
        if (me == null) {
            ui.showError("Unable to load your profile");
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
            FileChooser fc = new FileChooser();
            fc.setTitle("Select profile image");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.jpeg","*.webp"));
            File file = fc.showOpenDialog(ctx.getRoot().getScene() == null ? null : ctx.getRoot().getScene().getWindow());
            if (file != null) profileImagePath.setText(file.getAbsolutePath());
        });
        Button clearBtn = new Button("Remove Photo");
        clearBtn.setOnAction(e -> profileImagePath.clear());
        Button save = new Button("Save");
        save.setOnAction(e -> saveMyProfile(email.getText().trim(), contact.getText().trim(), profileImagePath.getText().trim()));
        HBox photoBar = new HBox(8, profileImagePath, uploadBtn, clearBtn);
        VBox box = new VBox(10, new Label("User ID"), id, new Label("Name"), name,
                new Label("Email"), email, new Label("Contact"), contact,
                new Label("Profile Picture"), photoBar, roleNote, save);
        box.setPadding(new Insets(16));
        page.setCenter(box);
        ctx.show(page);
    }

    public void openNoticeBoard(Runnable onHome, Runnable onLogout) {
        BorderPane page = ui.buildShell("Notice Board", onHome, onLogout);

        TextField searchField = new TextField();
        searchField.setPromptText("Search notices...");
        searchField.setPrefWidth(240);
        Button searchBtn = new Button("\uD83D\uDD0D Search");
        Button refreshBtn = new Button("\uD83D\uDD04 Refresh");

        TableView<Notice> table = new TableView<>(noticeRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setPlaceholder(new Label("No notices available."));
        TableColumn<Notice,String> idCol = ui.col("Notice ID", n -> n.getNoticeId());
        TableColumn<Notice,String> titleCol = ui.col("Title", n -> n.getTitle());
        TableColumn<Notice,String> dateCol = ui.col("Date", n -> n.getDate() == null ? "" : n.getDate().toString());
        TableColumn<Notice,String> fileCol = ui.col("Attachment", n -> n.hasFile() ? "\uD83D\uDCCE " + n.getFileType().toUpperCase() : "—");
        table.getColumns().addAll(idCol, titleCol, dateCol, fileCol);

        Button viewBtn = new Button("\uD83D\uDC41 View / Open");
        Button downloadBtn = new Button("\u2B07 Download");
        viewBtn.setDisable(true);
        downloadBtn.setDisable(true);

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            boolean hasFile = sel != null && sel.hasFile();
            viewBtn.setDisable(!hasFile);
            downloadBtn.setDisable(!hasFile);
        });

        viewBtn.setOnAction(e -> {
            Notice sel = table.getSelectionModel().getSelectedItem();
            if (sel == null || !sel.hasFile()) return;
            try {
                File f = new File(sel.getFilePath());
                if (!f.exists()) {
                    ui.showError("Attachment not found on disk:\n" + sel.getFilePath());
                    return;
                }
                if (Desktop.isDesktopSupported()) Desktop.getDesktop().open(f);
                else ui.showError("Desktop open is not supported on this system.");
            } catch (Exception ex) {
                ui.showError("Cannot open file: " + ex.getMessage());
            }
        });

        downloadBtn.setOnAction(e -> {
            Notice sel = table.getSelectionModel().getSelectedItem();
            if (sel == null || !sel.hasFile()) return;
            File src = new File(sel.getFilePath());
            if (!src.exists()) {
                ui.showError("Attachment not found on disk:\n" + sel.getFilePath());
                return;
            }
            FileChooser fc = new FileChooser();
            fc.setTitle("Save attachment as...");
            fc.setInitialFileName(src.getName());
            if ("pdf".equalsIgnoreCase(sel.getFileType()))
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
            else
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG images", "*.png"));
            File dest = fc.showSaveDialog(ctx.getRoot().getScene() == null ? null : ctx.getRoot().getScene().getWindow());
            if (dest != null) {
                try {
                    java.nio.file.Files.copy(src.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    ui.showInfo("File saved to:\n" + dest.getAbsolutePath());
                } catch (Exception ex) {
                    ui.showError("Download failed: " + ex.getMessage());
                }
            }
        });

        Runnable reloadAll = () -> {
            try { noticeRows.setAll(ctx.getNoticeDao().getAllNotices()); }
            catch (Exception ex) { ui.showError(ex.getMessage()); }
        };

        refreshBtn.setOnAction(e -> reloadAll.run());
        searchBtn.setOnAction(e -> {
            String kw = searchField.getText().trim();
            try { noticeRows.setAll(kw.isEmpty() ? ctx.getNoticeDao().getAllNotices() : ctx.getNoticeDao().searchNotices(kw)); }
            catch (Exception ex) { ui.showError(ex.getMessage()); }
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
        ctx.show(page);
        reloadAll.run();
    }

    private void saveMyProfile(String email, String contact, String profileImagePath) {
        if (!ui.isValidEmail(email)) {
            ui.showError("Enter a valid email address");
            return;
        }
        if (!ui.isValidPhone(contact)) {
            ui.showError("Contact number must have at least 10 digits");
            return;
        }
        try {
            ctx.getUserDao().updateMyProfile(ctx.getCurrentUser().getId(), email, contact, profileImagePath);
            ui.showInfo("Profile updated");
        } catch (Exception ex) {
            ui.showError(ex.getMessage());
        }
    }

    private String getProfileRuleText(String role) {
        if ("Lecturer".equals(role) || "TechnicalOfficer".equals(role))
            return "You can update your profile except username and password.";
        if ("Undergraduate".equals(role)) return "You can update contact details and profile picture.";
        return "You can update your profile picture and contact details here.";
    }
}
