package com.runtimex.tecmis.ui;

import com.runtimex.tecmis.models.AuthUser;
import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoginPage {
    private final UiContext ctx;
    private final UiHelpers ui;

    public LoginPage(UiContext ctx, UiHelpers ui) {
        this.ctx = ctx;
        this.ui = ui;
    }

    public void show(Consumer<AuthUser> onLogin) {
        BorderPane page = new BorderPane();
        page.getStyleClass().add("page-bg");

        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(28));
        card.setMaxWidth(420);
        card.getStyleClass().add("login-card");

        Label heading = new Label("TecMIS Login");
        heading.getStyleClass().add("section-title");

        Label sub = new Label("Role-based access for User, Attendance, Medical, Course Materials and Timetable");
        sub.setWrapText(true);
        sub.getStyleClass().add("section-subtitle");

        VBox titleBox = new VBox(4, heading, sub);
        HBox brandRow = new HBox(12, ui.buildLogo(ui.getLogoLargeSize()), titleBox);
        brandRow.setAlignment(Pos.CENTER_LEFT);

        TextField userIdField = new TextField();
        userIdField.setPromptText("User ID (e.g. AD001, LEC001, TO001, TG/2023/1780)");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Label note = new Label("Demo tip: use password 1234 for seeded users");
        note.getStyleClass().add("muted");

        Button loginBtn = new Button("\uD83D\uDD10 Login");
        loginBtn.getStyleClass().addAll("button", "primary");

        loginBtn.setOnAction(e -> {
            String userId = userIdField.getText().trim();
            String password = passwordField.getText().trim();
            if (userId.isEmpty() || password.isEmpty()) {
                ui.showError("Enter both User ID and password");
                return;
            }
            try {
                AuthUser authUser = ctx.getUserDao().authenticate(userId, password);
                if (authUser == null) {
                    ui.showError("Invalid credentials");
                    return;
                }
                onLogin.accept(authUser);
            } catch (Exception ex) {
                ui.showError(ex.getMessage());
            }
        });

        card.getChildren().addAll(brandRow, userIdField, passwordField, loginBtn, note);
        StackPane center = new StackPane(card);
        center.setPadding(new Insets(24));
        page.setCenter(center);
        ctx.show(page);
    }
}
