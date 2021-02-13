package mysql;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import mysql.entity.Message;
import mysql.entity.User;

import java.awt.*;
import java.util.List;

import static mysql.graphics.Graphics.*;
import static mysql.helper.Help.*;

public class Main extends Application {
    Database dat = new Database();
    String name = "kristianS";
    String pass = TopSecretData.getChatPass();

    public static void main(String[] args) {
        /*Database dat = new Database();
        List<Message> list = dat.getMyMessages("kristianS");
        for (Message m : list){
            System.out.println(m.getDt() + " " + m.getFrom() + " to " + m.getTo() + ": " + m.getText());
        }*/
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //BorderPane root = loginScreen();
        BorderPane root = messagesScreen(dat.login(name, pass));
        Scene scene = new Scene(root, 400,400);
        loginButton.setOnMouseClicked(e -> {
            String login = loginInput.getText().trim();
            String password = loginPassword.getText().trim();
            User user = dat.login(login, password);
            if (user == null){
                Timeline timer = new Timeline(
                        new KeyFrame(Duration.millis(1), e2 -> warningLogin.setVisible(true)),
                        new KeyFrame(Duration.millis(2000), e2 -> warningLogin.setVisible(false))
                );
                timer.setCycleCount(1);
                timer.play();
            } else {
                BorderPane messagesRoot = messagesScreen(user);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                double width = screenSize.getWidth();
                double height = screenSize.getHeight();
                Scene messagesScene = new Scene(messagesRoot, width, height);
                stage.setScene(messagesScene);
                stage.setMaximized(true);
                stage.setTitle("Messenger");
                stage.show();
            }
        });
        registerButton.setOnMouseClicked(e -> {
            String login = registerInput.getText().trim();
            String pass1 = pf.getText().trim();
            String pass2 = pf2.getText().trim();
            if (isUser(login)){
                Timeline timer = new Timeline(
                        new KeyFrame(Duration.millis(1), e2 -> userExist.setVisible(true)),
                        new KeyFrame(Duration.millis(2000), e2 -> userExist.setVisible(false))
                );
                timer.setCycleCount(1);
                timer.play();
            }else if (!pass1.equals(pass2)){
                Timeline timer = new Timeline(
                        new KeyFrame(Duration.millis(1), e2 -> passwordsMatchLabel.setVisible(true)),
                        new KeyFrame(Duration.millis(2000), e2 -> passwordsMatchLabel.setVisible(false))
                );
                timer.setCycleCount(1);
                timer.play();
            }
            if (isUser(login) && pass1.equals(pass2))
                dat.register(login, pass1);
            registerInput.setText("");
            pf.setText("");
            pf2.setText("");
        });
        signUp.setOnMouseClicked(e -> {
            BorderPane register = registerScreen();
            Scene registerScene = new Scene(register, 400, 400);
            stage.setScene(registerScene);
            stage.setTitle("Register");
            stage.show();
        });
        signIn.setOnMouseClicked(e -> {
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        });
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}
