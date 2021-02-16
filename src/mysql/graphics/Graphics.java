package mysql.graphics;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
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
        Database dat = new Database();
        BorderPane root = new BorderPane();
        root.setStyle("-fx-padding: 20");
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

        BorderPane rightSide = new BorderPane();

        GridPane userPane = new GridPane();
        userPane.setStyle("-fx-padding: 0 10");
        if (listOfUsers.size() > 16)
            userPane.setVgap(5);
        else
            userPane.setVgap(20);
        for (int i = 0; i < listOfUsers.size(); i++){
            Label userLabel = new Label(listOfUsers.get(i));
            userLabel.setOnMouseClicked(e -> {
                dialogMessage(userLabel.getText(), user);
            });
            userPane.addRow(i, userLabel);
        }

        rightSide.setTop(getLog(user));
        rightSide.setCenter(userPane);

        BorderPane messagesPane = new BorderPane();
        messagesPane.setMaxWidth(width - 200);
        messagesPane.setTop(buttons);
        messagesPane.setCenter(messagesArea);

        root.setCenter(messagesPane);
        root.setRight(rightSide);
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

    private static void dialogMessage(String name, User user){
        Database dat = new Database();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        BorderPane dialog = new BorderPane();
        Label nameLabel = new Label("Message to: " + name);
        nameLabel.setFont(Font.font(15));
        nameLabel.setTranslateX(100);

        Label messageSent = new Label("Message sent!");
        messageSent.setVisible(false);
        messageSent.setTextFill(Color.GREEN);
        messageSent.setFont(Font.font(20));
        //message will display for few seconds
        Timeline timer = new Timeline(
                new KeyFrame(Duration.millis(1), e2 -> messageSent.setVisible(true)),
                new KeyFrame(Duration.millis(3000), e2 -> messageSent.setVisible(false))
        );
        timer.setCycleCount(1);

        Label wrongMessage = new Label("Error sending message!");
        wrongMessage.setVisible(false);
        wrongMessage.setTextFill(Color.RED);
        wrongMessage.setFont(Font.font(20));
        Timeline wrongTimer = new Timeline(
                new KeyFrame(Duration.millis(1), e2 -> wrongMessage.setVisible(true)),
                new KeyFrame(Duration.millis(3000), e2 -> wrongMessage.setVisible(false))
        );
        wrongTimer.setCycleCount(1);

        timer.setOnFinished(e -> {
            stage.close();
        });

        TextArea messageText = new TextArea();
        messageText.setMaxSize(250,250);
        Button sendMessage = new Button("Send");
        sendMessage.setOnAction(e -> {
            boolean sent = dat.sendMessage(user.getId(), name, messageText.getText());
            if (sent)
                timer.play();
            else {
                wrongTimer.play();
                messageText.setText("");
            }
        });

        dialog.setTop(nameLabel);
        StackPane messageArea = new StackPane();
        messageArea.getChildren().addAll(messageText, messageSent, wrongMessage);
        dialog.setCenter(messageArea);
        dialog.setBottom(sendMessage);

        Scene scene = new Scene(dialog, 300, 300);
        stage.setScene(scene);
        stage.setTitle("Send message");
        stage.showAndWait();
    }

    private static void changePassDialog(User user){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Database dat = new Database();
        BorderPane root = new BorderPane();
        StackPane passwordsPane = new StackPane();
        GridPane passwords = new GridPane();
        passwords.setStyle("-fx-padding: 10");
        passwords.setVgap(10);
        passwords.setHgap(10);
        TextField oldPassword = new TextField();
        TextField newPassword = new TextField();
        TextField newPassword2 = new TextField();
        passwords.add(new Label("Old password:"), 0, 0);
        passwords.add(new Label("New password:"), 0, 1);
        passwords.add(new Label("New password:"), 0, 2);
        passwords.add(oldPassword, 1, 0);
        passwords.add(newPassword, 1, 1);
        passwords.add(newPassword2, 1, 2);

        Label changedPass = new Label("Password changed!");
        changedPass.setVisible(false);
        changedPass.setTextFill(Color.GREEN);
        changedPass.setFont(Font.font(20));
        Timeline timer = new Timeline(
                new KeyFrame(Duration.millis(1), e2 -> changedPass.setVisible(true)),
                new KeyFrame(Duration.millis(3000), e2 -> changedPass.setVisible(false))
        );
        timer.setCycleCount(1);
        timer.setOnFinished(e -> stage.close());
        Label wrongPass = new Label("Error changing password");
        wrongPass.setVisible(false);
        wrongPass.setTextFill(Color.RED);
        wrongPass.setFont(Font.font(20));
        Timeline wrongTimer = new Timeline(
                new KeyFrame(Duration.millis(1), e2 -> wrongPass.setVisible(true)),
                new KeyFrame(Duration.millis(3000), e2 -> wrongPass.setVisible(false))
        );
        wrongTimer.setCycleCount(1);

        Button change = new Button("Change");
        change.setTranslateX(50);
        change.setOnAction(e -> {
            if (!newPassword.equals(newPassword2) || !oldPassword.equals(user.getPassword())){
                wrongTimer.play();
            } else {
                dat.changePassword(user.getLogin(),
                        oldPassword.getText(),
                        newPassword.getText(),
                        newPassword2.getText());
                timer.play();
            }
        });

        passwordsPane.getChildren().addAll(passwords, changedPass, wrongPass);

        Label label = new Label("Change password");
        label.setTranslateX(70);
        root.setTop(label);
        root.setCenter(passwordsPane);
        root.setBottom(change);

        Scene scene = new Scene(root, 300, 180);
        stage.setScene(scene);
        stage.setTitle("Change password");
        stage.showAndWait();
    }

    private static BorderPane getLog(User user){
        BorderPane log = new BorderPane();
        log.setStyle("-fx-padding: 10");

        StackPane userInt = new StackPane();
        Circle circle = new Circle(30, Color.AZURE);
        Label userName = new Label(String.valueOf(user.getLogin().charAt(0)));
        userName.setFont(Font.font(20));
        userInt.getChildren().addAll(circle, userName);

        GridPane options = new GridPane();
        options.setStyle("-fx-padding: 20; " +
                "-fx-border-style: solid;" +
                "-fx-border-width: 1;" +
                "-fx-border-color: gray");
        options.setVgap(10);
        options.setVisible(false);

        userInt.setOnMouseClicked(e -> options.setVisible(true));
        Button changePass = new Button("Change password");
        changePass.setOnAction(e -> {
            options.setVisible(false);
            changePassDialog(user);
        });
        Button logOut = new Button("Log out");
        logOut.setOnAction(e -> System.exit(0));
        options.addRow(0, new Label("Logged as: " + user.getLogin()));
        options.addRow(1, changePass);
        options.addRow(2, logOut);

        log.setTop(userInt);
        log.setCenter(options);
        return log;
    }

}
