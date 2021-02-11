package mysql.graphics;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Graphics {
    public static Button loginButton = getLoginButton();
    public static Button registerButton = getRegisterButton();
    public static TextField loginInput = new TextField();
    public static TextField registerInput = new TextField();
    public static PasswordField loginPassword = new PasswordField();
    public static PasswordField pf = new PasswordField();
    public static PasswordField pf2 = new PasswordField();
    public static Label signUp = getSignUp();
    public static Label signIn = getSignIn();

    public static BorderPane loginScreen(){
        BorderPane root = new BorderPane();
        Rectangle rect = new Rectangle();
        rect.setStroke(Color.DARKGRAY);
        rect.setFill(Color.LIGHTGRAY);
        rect.setWidth(200); rect.setHeight(200);
        rect.setTranslateX(0);
        rect.setTranslateY(0);
        FlowPane userNamePane = new FlowPane();
        userNamePane.getChildren().addAll(new Label("Username"), loginInput);
        FlowPane passwordPane = new FlowPane();
        passwordPane.getChildren().addAll(new Label("Password"), loginPassword);
        BorderPane login = new BorderPane();
        login.setTranslateY(-20);
        login.setMaxWidth(100);
        login.setMaxHeight(100);
        login.setTop(userNamePane);
        login.setBottom(passwordPane);

        loginButton.setTranslateY(60);

        StackPane loginPane = new StackPane();
        loginPane.getChildren().addAll(rect, login, loginButton, signUp);
        root.setCenter(loginPane);
        return root;
    }

    public static BorderPane registerScreen(){
        BorderPane root = new BorderPane();
        Rectangle rect = new Rectangle();
        rect.setStroke(Color.DARKGRAY);
        rect.setFill(Color.LIGHTGRAY);
        rect.setWidth(200); rect.setHeight(220);

        FlowPane userPane = new FlowPane();
        userPane.getChildren().addAll(new Label("Username:"), registerInput);
        FlowPane passwordPane = new FlowPane();
        passwordPane.getChildren().addAll(new Label("Password:"), pf, new Label("Confirm password:"), pf2);

        BorderPane register = new BorderPane();
        register.setTranslateY(-15);
        register.setMaxWidth(100);
        register.setMaxHeight(100);
        register.setTop(userPane);
        register.setBottom(passwordPane);

        registerButton.setTranslateY(75);

        signIn.setTranslateY(100);

        StackPane registerPane = new StackPane();
        registerPane.getChildren().addAll(rect, register, registerButton, signIn);
        root.setCenter(registerPane);
        return root;
    }

    private static Button getLoginButton() {
        Button loginButton = new Button("Login");
        loginButton.setTextFill(Color.WHITE);
        loginButton.setMaxWidth(100);
        loginButton.setStyle("-fx-background-radius: 5px; " +
                "-fx-background-color: CYAN;");
        return loginButton;
    }

    private static Button getRegisterButton() {
        Button loginButton = new Button("Register");
        loginButton.setTextFill(Color.WHITE);
        loginButton.setMaxWidth(100);
        loginButton.setStyle("-fx-background-radius: 5px; " +
                "-fx-background-color: CYAN;");
        return loginButton;
    }

    private static Label getSignUp() {
        Label signUp = new Label("Sign up");
        signUp.setTextFill(Color.CYAN);
        signUp.setFont(Font.font("Timew New Roman", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 12));
        signUp.setUnderline(true);
        signUp.setTranslateY(90);
        return signUp;
    }

    private static Label getSignIn() {
        Label signIn = new Label("Sign in");
        signIn.setTextFill(Color.CYAN);
        signIn.setFont(Font.font("Timew New Roman", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 12));
        signIn.setUnderline(true);
        signIn.setTranslateY(90);
        return signIn;
    }

}
