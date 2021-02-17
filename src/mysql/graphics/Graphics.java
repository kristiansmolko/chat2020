package mysql.graphics;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import mysql.Database;
import mysql.entity.Message;
import mysql.entity.User;
import mysql.helper.Help;
import mysql.helper.Json;

import java.awt.*;
import java.util.ArrayList;
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

    private static final List<String> usedColors = new ArrayList<>();
    private static boolean hasColor = false;
    private static List<Message> listOfAll;
    private static List<String> listOfUsers;
    private static List<String> listOfUserColors;

    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final double width = screenSize.getWidth();
    private static final double height = screenSize.getHeight();

    public static BorderPane loginScreen(){
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: black");
        Rectangle rect = new Rectangle();
        rect.setFill(Color.DARKGREEN);
        rect.setWidth(200); rect.setHeight(200);
        rect.setTranslateX(0);
        rect.setTranslateY(0);

        FlowPane userNamePane = new FlowPane();
        Label userNameLabel = new Label("Username");
        userNameLabel.setTextFill(Color.BLACK);
        userNamePane.getChildren().addAll(userNameLabel, loginInput);
        FlowPane passwordPane = new FlowPane();
        Label passwordLabel = new Label("Password");
        passwordLabel.setTextFill(Color.BLACK);
        passwordPane.getChildren().addAll(passwordLabel, loginPassword);

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
        root.setStyle("-fx-padding: 20; " +
                "-fx-background-color: darkgreen");
        listOfAll = Help.getAllMessages();
        listOfUsers = Help.getUsers();
        listOfUserColors = Json.getUserColors();

        TextArea messagesArea = getMessageArea();

        GridPane buttons = getButtonsPane(messagesArea, user);

        BorderPane rightSide = getRightSide(user);

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
        loginButton.setTextFill(Color.BLACK);
        loginButton.setMaxWidth(100);
        loginButton.setStyle("-fx-background-radius: 5px; " +
                "-fx-background-color: limegreen;");
        return loginButton;
    }

    private static Button getMessagesButton(String text){
        Button button = new Button(text);
        button.setTextFill(Color.BLACK);
        button.setMaxWidth(200);
        button.setStyle("-fx-background-radius: 5px; " +
                "-fx-background-color: limegreen; " +
                "-fx-padding: 10");
        return button;
    }

    private static Label getSignUp() {
        Label signUp = new Label("Sign up");
        signUp.setTextFill(Color.BLACK);
        signUp.setFont(Font.font("Timew New Roman", FontPosture.REGULAR, 10));
        signUp.setUnderline(true);
        signUp.setTranslateY(90);
        return signUp;
    }

    private static Label getSignIn() {
        Label signIn = new Label("Sign in");
        signIn.setTextFill(Color.BLACK);
        signIn.setFont(Font.font("Timew New Roman", FontPosture.REGULAR, 10));
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
                    messagesArea.appendText(m.getDt() + "-  from " + m.getFrom() + ": " + m.getText() + "\n");
            }
        };
    }

    private static EventHandler<ActionEvent> getAllMessAction(TextArea messagesArea, List<Message> listOfAll){
        return actionEvent -> {
            messagesArea.setText("");
            for (Message m : listOfAll){
                messagesArea.appendText(m.getDt() + " - " + m.getFrom() + " to " + m.getTo() + ": " + m.getText() + "\n");
            }
        };
    }

    private static EventHandler<ActionEvent> getSentMessAction(TextArea messagesArea, List<Message> listOfSent, User user){
        return actionEvent -> {
            messagesArea.setText("");
            for (Message m : listOfSent){
                if (m.getFrom().equals(user.getLogin()))
                    messagesArea.appendText(m.getDt() + " - to " + m.getTo() + ": " + m.getText() + "\n");
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
        root.setStyle("-fx-background-color: darkgreen");
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
        change.setTextFill(Color.BLACK);
        change.setStyle("-fx-background-color: limegreen");
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
        log.setTranslateX(-20);
        log.setStyle("-fx-padding: 10");

        StackPane userInt = new StackPane();
        Circle circle = new Circle(30, Color.LIMEGREEN);
        Label userName = new Label(String.valueOf(user.getLogin().charAt(0)));
        userName.setFont(Font.font(20));
        userInt.getChildren().addAll(circle, userName);

        GridPane options = new GridPane();
        options.setTranslateY(10);
        options.setStyle("-fx-padding: 20; " +
                "-fx-border-style: solid;" +
                "-fx-border-width: 1;" +
                "-fx-border-color: limegreen");
        options.setVgap(10);
        options.setVisible(false);

        userInt.setOnMouseClicked(e -> options.setVisible(true));
        Button changePass = new Button("Change password");
        changePass.setTextFill(Color.BLACK);
        changePass.setOnAction(e -> {
            options.setVisible(false);
            changePassDialog(user);
        });
        Button logOut = new Button("Log out");
        logOut.setTextFill(Color.BLACK);
        logOut.setOnAction(e -> System.exit(0));
        Label logged = new Label("Logged as: " + user.getLogin());
        logged.setTextFill(Color.BLACK);
        options.addRow(0, logged);
        options.addRow(1, changePass);
        options.addRow(2, logOut);

        log.setTop(userInt);
        log.setCenter(options);
        return log;
    }

    private static void colorPicker(String name, Label label){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        BorderPane root = new BorderPane();
        ColorPicker colorPicker = new ColorPicker();
        Label nameLabel = new Label(name);
        Button set = new Button("Set");

        colorPicker.setOnAction(e -> {
            nameLabel.setStyle("-fx-background-color: " + toHexString(colorPicker.getValue()));
        });

        set.setOnAction(e -> {
            String newUser = name + ":" + toHexString(colorPicker.getValue());
            Json.addToColors(newUser);
            label.setStyle("-fx-background-color: " + toHexString(colorPicker.getValue()));
            stage.close();
        });

        root.setTop(nameLabel);
        root.setCenter(colorPicker);
        root.setBottom(set);

        Scene scene = new Scene(root, 200, 200);
        stage.setScene(scene);
        stage.setTitle("Choose color for new user");
        stage.showAndWait();
    }

    private static String toHexString(Color color) {
        int r = ((int) Math.round(color.getRed()     * 255)) << 24;
        int g = ((int) Math.round(color.getGreen()   * 255)) << 16;
        int b = ((int) Math.round(color.getBlue()    * 255)) << 8;
        int a = ((int) Math.round(color.getOpacity() * 255));

        return String.format("#%08X", (r + g + b + a));
    }

    private static BorderPane getRightSide(User user){
        BorderPane root = new BorderPane();
        root.setTop(getLog(user));
        root.setCenter(getListView(user));
        return root;
    }

    private static TextArea getMessageArea(){
        TextArea messagesArea = new TextArea();
        messagesArea.setMaxWidth(width-350);
        messagesArea.setMaxHeight(height-150);
        messagesArea.setStyle("-fx-margin: 20");
        messagesArea.setFont(Font.font(12));
        messagesArea.setEditable(false);
        for (Message m : listOfAll)
            messagesArea.appendText(m.getDt() + " " + m.getFrom() + " to " + m.getTo() + ": " + m.getText() + "\n");
        return messagesArea;
    }

    private static GridPane getButtonsPane(TextArea messagesArea, User user){
        GridPane buttons = new GridPane();
        buttons.setTranslateX(80);
        buttons.setStyle("-fx-margin: 20");
        buttons.setMaxWidth(width - 350);
        buttons.setHgap(20);

        Button myMessages = getMessagesButton("My messages");
        myMessages.setOnAction(getMyMessAction(messagesArea, listOfAll, user));

        Button allMessages = getMessagesButton("All messages");
        allMessages.setOnAction(getAllMessAction(messagesArea, listOfAll));

        Button sentMessages = getMessagesButton("Sent messages");
        sentMessages.setOnAction(getSentMessAction(messagesArea, listOfAll, user));

        buttons.addRow(0, myMessages, allMessages, sentMessages);
        return buttons;
    }

    private static ListView getListView(User user){
        ListView userPane = new ListView();
        userPane.setTranslateX(-20);
        userPane.setTranslateY(20);
        userPane.setBorder(null);
        userPane.setMaxWidth(200);
        userPane.setMaxHeight(400);
        userPane.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        for (int i = 0; i < listOfUsers.size(); i++){
            Label name = new Label(listOfUsers.get(i));
            name.setMaxWidth(200);
            for (String key : listOfUserColors){
                String[] temp = key.split(":");
                if (listOfUsers.get(i).equals(temp[0])){
                    name.setStyle("-fx-background-color: " + temp[1]);
                    usedColors.add(temp[1]);
                }
            }
            userPane.getItems().add(name);

            userPane.setOnMouseClicked(e -> {
                Label label = (Label) userPane.getSelectionModel().getSelectedItem();
                hasColor = false;
                for (String key : listOfUserColors) {
                    String[] temp = key.split(":");
                    if (temp[0].equals(label.getText())) {
                        dialogMessage(label.getText(), user);
                        hasColor = true;
                    }
                }
                if (!hasColor)
                    colorPicker(label.getText(), label);
            });
        }
        return userPane;
    }

}
