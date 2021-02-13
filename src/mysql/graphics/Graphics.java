package mysql.graphics;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import mysql.Database;
import mysql.entity.Message;
import mysql.entity.User;
import mysql.helper.Help;

import java.awt.*;
import java.util.List;

public class Graphics {
    public static Button loginButton = getButton("Login");
    public static Button registerButton = getButton("Register");
    public static TextField loginInput = new TextField();
    public static TextField registerInput = new TextField();
    public static PasswordField loginPassword = new PasswordField();
    public static PasswordField pf = new PasswordField();
    public static PasswordField pf2 = new PasswordField();
    public static Label signUp = getSignUp();
    public static Label signIn = getSignIn();

    public static Label warningLogin = getWarningLogin();
    public static Label passwordsMatchLabel = getPasswordsMatchAlert();
    public static Label userExist = getUserExist();

    public static BorderPane loginScreen(){
        BorderPane root = new BorderPane();
        Rectangle rect = new Rectangle();
        rect.setStroke(Color.DARKGRAY);
        rect.setFill(Color.AZURE);
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
        loginPane.getChildren().addAll(rect, login, loginButton, signUp, warningLogin);
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
        registerPane.getChildren().addAll(rect, register, registerButton, signIn, passwordsMatchLabel, userExist);
        root.setCenter(registerPane);
        return root;
    }

    public static BorderPane messagesScreen(User user){
        BorderPane root = new BorderPane();
        List<Message> listOfAll = Help.getAllMessages();
        List<Message> listOfMy = Help.getMyMessages(user.getLogin());
        List<String> listOfUsers = Help.getUsers();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        TextArea messagesArea = new TextArea();
        messagesArea.setMaxWidth(width-200);
        messagesArea.setMaxHeight(height-150);
        messagesArea.setStyle("-fx-margin: 20");
        messagesArea.setEditable(false);
        for (Message m : listOfAll)
            messagesArea.appendText(m.getDt() + " " + m.getFrom() + " to " + m.getTo() + ": " + m.getText() + "\n");

        Button myMessages = getButton("My messages");
        myMessages.setOnAction(getMyMessAction(messagesArea, listOfMy, user));

        Button allMessages = getButton("All messages");
        allMessages.setOnAction(getAllMessAction(messagesArea, listOfAll));

        GridPane buttons = new GridPane();
        buttons.setStyle("-fx-margin: 20");
        buttons.setMaxWidth(200);
        buttons.setHgap(20);
        buttons.addRow(0, myMessages, allMessages);

        GridPane userPane = new GridPane();
        userPane.setVgap(20);
        for (int i = 0; i < listOfUsers.size(); i++){
            Label userLabel = new Label(listOfUsers.get(i));
            userPane.addRow(i, userLabel);
        }

        BorderPane messagesPane = new BorderPane();
        messagesPane.setMaxWidth(width - 200);
        messagesPane.setTop(buttons);
        messagesPane.setCenter(messagesArea);

        root.setCenter(messagesPane);
        root.setRight(userPane);
        return root;
    }

    private static Button getButton(String text) {
        Button loginButton = new Button(text);
        loginButton.setTextFill(Color.DARKGRAY);
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

    private static Label getWarningLogin(){
        Label warning = new Label("Incorrect login");
        warning.setTranslateY(-80);
        warning.setTextFill(Color.RED);
        warning.setFont(Font.font(20));
        warning.setVisible(false);
        return warning;
    }

    private static Label getPasswordsMatchAlert(){
        Label warning = new Label("Passwords must match!");
        warning.setTranslateY(-90);
        warning.setTextFill(Color.RED);
        warning.setFont(Font.font(18));
        warning.setVisible(false);
        return warning;
    }

    private static Label getUserExist(){
        Label warning = new Label("This user exist!");
        warning.setTranslateY(-90);
        warning.setTextFill(Color.RED);
        warning.setFont(Font.font(20));
        warning.setVisible(false);
        return warning;
    }

    private static EventHandler<ActionEvent> getMyMessAction(TextArea messagesArea, List<Message> listOfMy, User user){
        return actionEvent -> {
            messagesArea.setText("");

            for (Message m : listOfMy){
                if (m.getTo().equals(user.getLogin()))
                    messagesArea.appendText(m.getDt() + " " + m.getFrom() + ": " + m.getText() + "\n");
            }
        };
    }

    private static EventHandler<ActionEvent> getAllMessAction(TextArea messagesArea, List<Message> listOfAll){
        return actionEvent -> {
            messagesArea.setText("");
            for (Message m : listOfAll){
                messagesArea.appendText(m.getDt() + " " + m.getFrom() + " to " + m.getTo() + ": " + m.getText() + "\n");
            }
        };
    }



}
