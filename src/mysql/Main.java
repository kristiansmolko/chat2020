package mysql;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static mysql.graphics.Graphics.*;

public class Main extends Application {
    Database dat = new Database();
    String name = "kristianS";
    String pass = TopSecretData.getChatPass();

    public static void main(String[] args) {
        launch(args);

        /*List<Message> list = dat.getMyMessages(name);
        for (Message m : list){
            System.out.println(m.getFrom() + " to " + m.getTo() + " at " + m.getDt() + " : " + m.getText());
        }*/


    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = loginScreen();
        Scene scene = new Scene(root, 400,400);
        loginButton.setOnMouseClicked(e -> {
            dat.login(loginInput.getText(), loginPassword.getText());

        });
        registerButton.setOnMouseClicked(e -> {
            dat.register(registerInput.getText(), pf.getText(), pf2.getText());
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
