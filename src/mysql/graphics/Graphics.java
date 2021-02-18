package mysql.graphics;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
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

    private static TextArea messagesArea;
    private static GridPane buttons;
    private static StackPane messages = new StackPane();
    private static ImageView img = getWelcome();

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

        messagesArea = getMessageArea();
        messages.getChildren().addAll(messagesArea, img);

        buttons = getButtonsPane(messagesArea, user);

        BorderPane rightSide = getRightSide(user);

        BorderPane messagesPane = new BorderPane();
        messagesPane.setMaxWidth(width - 200);
        messagesPane.setTop(buttons);
        messagesPane.setCenter(messages);

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

    private static EventHandler<ActionEvent> getMyMessAction(TextArea messagesArea, User user){
        return actionEvent -> {
            messages.getChildren().remove(img);
            List<Message> listOfNew = Help.getMyMessages(user.getId());
            messagesArea.setText("");
            if (!listOfNew.isEmpty())
                for (Message m : listOfNew) {
                    String date = Help.getTime(m.getDt());
                    messagesArea.appendText(date + " -  from " + m.getFrom() + ": " + m.getText() + "\n");
                }
            else
                messagesArea.setText("No new messages");
        };
    }

    private static EventHandler<ActionEvent> getAllMyMessAction(TextArea messagesArea, User user){
        return actionEvent -> {
            messages.getChildren().remove(img);
            List<Message> listOfAll = Help.getMessagesArchive(user.getId());
            messagesArea.setText("");
            if (listOfAll.size() < 20)
                for (Message m : listOfAll){
                    String date = Help.getTime(m.getDt());
                    messagesArea.appendText(date + " -  from " + m.getFrom() + ": " + m.getText() + "\n");
                }
            else
                for (int i = listOfAll.size() - 20; i < listOfAll.size(); i++){
                    Message m = listOfAll.get(i);
                    String date = Help.getTime(m.getDt());
                    messagesArea.appendText(date + " -  from " + m.getFrom() + ": " + m.getText() + "\n");
                }
        };
    }

    private static EventHandler<ActionEvent> getSentMessAction(TextArea messagesArea, User user){
        return actionEvent -> {
            messages.getChildren().remove(img);
            List<Message> listOfSent = Help.getSentMessages(user.getId());
            messagesArea.setText("");
            if (listOfSent.size() < 20)
                for (Message m : listOfSent){
                    String date = Help.getTime(m.getDt());
                    messagesArea.appendText(date + " -  to " + m.getTo() + ": " + m.getText() + "\n");
                }
            else {
                for (int i = listOfSent.size() - 20; i < listOfSent.size(); i++){
                    Message m = listOfSent.get(i);
                    String date = Help.getTime(m.getDt());
                    messagesArea.appendText(date + " -  to " + m.getTo() + ": " + m.getText() + "\n");
                }
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
            boolean sent = false;
            if (!(messageText.getText().length() > 255))
                sent = dat.sendMessage(user.getId(), name, messageText.getText());
            if (sent)
                timer.play();
            else {
                wrongTimer.play();
                messageText.setText("");
            }
        });

        Button emojis = new Button("Emojis");


        BorderPane buttons = new BorderPane();
        buttons.setLeft(sendMessage);
        buttons.setRight(emojis);

        ListView list = getEmojiList(messageText);
        list.setEditable(true);
        list.setTranslateY(70);
        list.setMaxSize(200, 70);

        StackPane messageArea = new StackPane();
        messageArea.getChildren().addAll(messageText, messageSent, wrongMessage);

        emojis.setOnAction(e -> {
            if (!list.isEditable()) {
                messageArea.getChildren().remove(list);
                list.setEditable(true);
            }
            else {
                messageArea.getChildren().addAll(list);
                list.setEditable(false);
            }
        });

        dialog.setTop(nameLabel);
        dialog.setCenter(messageArea);
        dialog.setBottom(buttons);

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

        userInt.setOnMouseClicked(e -> {
            if (options.isVisible())
                options.setVisible(false);
            else
                options.setVisible(true);
        });
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
        messagesArea.setMaxWidth(width - 350);
        messagesArea.setMaxHeight(height - 150);
        messagesArea.setStyle("-fx-margin: 20");
        messagesArea.setFont(Font.font("Doh", 12));
        messagesArea.setEditable(false);
        return messagesArea;
    }

    private static GridPane getButtonsPane(TextArea messagesArea, User user){
        GridPane buttons = new GridPane();
        buttons.setTranslateX(70);
        buttons.setTranslateY(-10);
        buttons.setStyle("-fx-margin: 20");
        buttons.setMaxWidth(width - 350);
        buttons.setHgap(20);

        Button myMessages = getMessagesButton("My messages");
        myMessages.setOnAction(getMyMessAction(messagesArea, user));

        Timeline timer = new Timeline(
                new KeyFrame(Duration.millis(100), e2 -> {
                    myMessages.setStyle("-fx-background-radius: 5px; " +
                            "-fx-background-color: limegreen; " +
                            "-fx-padding: 10");
                }),
                new KeyFrame(Duration.millis(2000), e2 -> {
                    myMessages.setStyle("-fx-background-radius: 5px; " +
                            "-fx-padding: 10; " +
                            "-fx-background-color: red");
                })
        );
        timer.setCycleCount(Animation.INDEFINITE);

        if (Help.newMessage(user.getId())) {
            timer.play();
        }

        myMessages.setOnMouseClicked(e -> timer.stop());

        Timeline checkDat = new Timeline(
                new KeyFrame(Duration.millis(1), e2 -> {}),
                new KeyFrame(Duration.millis(60000), e2 -> {
                    System.out.println("New message!");
                    if(Help.newMessage(user.getId()))
                        timer.play();
                })
        );
        checkDat.setCycleCount(Animation.INDEFINITE);
        checkDat.play();

        Button allMessages = getMessagesButton("All messages");
        allMessages.setOnAction(getAllMyMessAction(messagesArea, user));

        Button sentMessages = getMessagesButton("Sent messages");
        sentMessages.setOnAction(getSentMessAction(messagesArea, user));

        buttons.addRow(0, myMessages, allMessages, sentMessages);
        return buttons;
    }

    private static ListView getListView(User user){
        ListView userPane = new ListView();
        userPane.setTranslateX(-20);
        userPane.setBorder(null);
        userPane.setMaxWidth(200);
        userPane.setMaxHeight(400);
        userPane.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        List<String> listOfUsers = Help.getUsers();
        List<String> listOfUserColors = Json.getUserColors();
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

    private static GridPane getEmojiGrid(TextArea area, int num){
        GridPane grid = new GridPane();
        num*=10;
        Button b1 = new Button(String.format("%c", 0x1F600+num));
        Button b2 = new Button(String.format("%c", 0x1F601+num));
        Button b3 = new Button(String.format("%c", 0x1F602+num));
        Button b4 = new Button(String.format("%c", 0x1F603+num));
        Button b5 = new Button(String.format("%c", 0x1F604+num));
        Button b6 = new Button(String.format("%c", 0x1F605+num));
        grid.addRow(0, b1, b2, b3, b4, b5, b6);
        int useNum = num;
        b1.setOnAction(e -> area.appendText(String.format("%c", 0x1F600+useNum)));
        b2.setOnAction(e -> area.appendText(String.format("%c", 0x1F601+useNum)));
        b3.setOnAction(e -> area.appendText(String.format("%c", 0x1F602+useNum)));
        b4.setOnAction(e -> area.appendText(String.format("%c", 0x1F603+useNum)));
        b5.setOnAction(e -> area.appendText(String.format("%c", 0x1F604+useNum)));
        b6.setOnAction(e -> area.appendText(String.format("%c", 0x1F605+useNum)));
        return grid;
    }

    private static GridPane getEmojiGrid2(TextArea area, int num){
        GridPane grid = new GridPane();
        num *= 10;
        Button b7 = new Button(String.format("%c", 0x1F606+num));
        Button b8 = new Button(String.format("%c", 0x1F607+num));
        Button b9 = new Button(String.format("%c", 0x1F608+num));
        Button b10 = new Button(String.format("%c", 0x1F609+num));
        Button b11 = new Button(String.format("%c", 0x1F60A+num));
        Button b12 = new Button(String.format("%c", 0x1F60B+num));
        grid.addRow(0, b7, b8, b9, b10, b11, b12);
        int useNum = num;
        b7.setOnAction(e -> area.appendText(String.format("%c", 0x1F606+useNum)));
        b8.setOnAction(e -> area.appendText(String.format("%c", 0x1F607+useNum)));
        b9.setOnAction(e -> area.appendText(String.format("%c", 0x1F608+useNum)));
        b10.setOnAction(e -> area.appendText(String.format("%c", 0x1F609+useNum)));
        b11.setOnAction(e -> area.appendText(String.format("%c", 0x1F60A+useNum)));
        b12.setOnAction(e -> area.appendText(String.format("%c", 0x1F60B+useNum)));
        return grid;
    }

    private static ListView getEmojiList(TextArea messageText){
        ListView list = new ListView();
        GridPane col1 = getEmojiGrid(messageText, 0);
        GridPane col2 = getEmojiGrid2(messageText, 0);
        GridPane col3 = getEmojiGrid(messageText, 1);
        GridPane col4 = getEmojiGrid2(messageText, 1);
        GridPane col5 = getEmojiGrid(messageText, 2);
        GridPane col6 = getEmojiGrid2(messageText, 2);
        GridPane col7 = getEmojiGrid(messageText, 3);
        GridPane col8 = getEmojiGrid2(messageText, 3);
        GridPane col9 = getEmojiGrid(messageText, 4);
        GridPane col10 = getEmojiGrid2(messageText, 4);
        GridPane col11 = getEmojiGrid(messageText, 5);
        GridPane col12 = getEmojiGrid2(messageText, 5);
        GridPane col13 = getEmojiGrid(messageText, 6);
        GridPane col14 = getEmojiGrid2(messageText, 6);
        GridPane col15 = getEmojiGrid(messageText, 7);
        GridPane col16 = getEmojiGrid2(messageText, 7);
        list.getItems().addAll(col1, col2, col3, col4, col5, col6, col7, col8,
                col9, col10, col11, col12, col13, col14, col15, col16);
        return list;
    }

    private static ImageView getWelcome(){
        ImageView img = new ImageView(new Image("welcome.png"));
        img.setFitWidth(width - 350);
        img.setFitHeight(height - 150);
        return img;
    }
}
