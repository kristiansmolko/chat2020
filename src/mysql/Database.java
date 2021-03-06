package mysql;

import mysql.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static mysql.helper.Help.*;

public class Database {
    public User login(String name, String password){
        String query = "SELECT id, login, password FROM user " +
                "WHERE login = ?";
        try (Connection connection = getConnection()) {
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    String usedPass = rs.getString("password");
                    if (usedPass.equals(getEncryptedPass(password))){
                        System.out.println("\033[34mSuccessfully logged in!\033[0m");
                        int id = rs.getInt("id");
                        connection.close();
                        return new User(id, name, password);
                    } else {
                        System.out.println("\033[31mWrong password!\033[0m");
                    }
                } else {
                    System.out.println("\033[31mThis user does not exist!\033[0m");
                }
                connection.close();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public void register(String name, String password){
        if (password == null || password.equals("")){
            System.out.println("\033[31mPlease write your password!\033[0m");
        }
        if (isUser(name)) {
            System.out.println("\033[31mThis user already exist!\033[0m");
            return;
        }
        try (Connection connection = getConnection()) {
            if (connection != null){
                String registerUser = "INSERT INTO user(login, password) VALUES(?, ?)";
                PreparedStatement ps = connection.prepareStatement(registerUser);
                ps.setString(1, name);
                String hashedPass = getEncryptedPass(password);
                ps.setString(2, hashedPass);
                ps.executeUpdate();
                System.out.println("You have been registered!");
                connection.close();
            }
        } catch (Exception e) { e.printStackTrace(); }

    }

    public void changePassword(String name, String oldPassword, String newPass, String newPass2){
        String query = "UPDATE user " +
                "SET password = ? " +
                "WHERE login = ? AND password = ?";
        if (!isUser(name)){
            System.out.println("\033[31mThis user does not exist!\033[0m");
            return;
        }
        if (!newPass.equals(newPass2)){
            System.out.println("\033[31mPaswords don't match!\033[0m");
            return;
        }
        if (newPass.equals(oldPassword)){
            System.out.println("\033[31mYou can't use the same password\033[0m");
            return;
        }
        if (login(name, oldPassword) == null)
            return;
        try (Connection connection = getConnection()) {
            if (connection != null){
                //in javafx you will be asked for name once again
                PreparedStatement ps = connection.prepareStatement(query);
                String pass = getEncryptedPass(newPass);
                ps.setString(1, pass);
                ps.setString(2, name);
                ps.setString(3, getEncryptedPass(oldPassword));
                ps.executeUpdate();
                System.out.println("\033[34mSuccessfully changed password!\033[0m");
                connection.close();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public boolean sendMessage(int from, String toUser, String text){
        if (text == null || text.equals(""))
            return false;
        int to = getUserId(toUser);
        if (to == -1)
            return false;
        try (Connection connection = getConnection()) {
             if (connection != null) {
                 String newMessage = "INSERT INTO message(fromUser, toUser, text) VALUES(?, ?, ?)";
                 PreparedStatement ps = connection.prepareStatement(newMessage);
                 ps.setInt(1, from);
                 ps.setInt(2, to);
                 ps.setString(3, text);
                 int result = ps.executeUpdate();
                 if (result < 1) {
                     System.out.println("\033[31mMessage not sent!\033[0m");
                 } else {
                     System.out.println("\033[34mMessage sent!\033[0m");
                     return true;
                 }
                 connection.close();
             }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
