package mysql.helper;

import mysql.TopSecretData;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class Help {
    private static final String getUserId = "SELECT id, login FROM user WHERE login = ?";
    private static final String getUserName = "SELECT id, login FROM user WHERE id = ?";
    private static final String deleteMyMessages = "DELETE FROM message WHERE toUser = ?";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(TopSecretData.getUrl(), TopSecretData.getUsername(), TopSecretData.getPassword());
    }

    public static boolean isUser(String name){
        String query = "SELECT login FROM user";
        try {
            Connection connection = getConnection();
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    if (rs.getString("login").equals(name)) {
                        connection.close();
                        return true;
                    }
                }
                connection.close();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public static String getEncryptedPass(String input){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getUserId(String login){
        try {
            Connection connection = getConnection();
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(getUserId);
                ps.setString(1, login);
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    int id = rs.getInt("id");
                    connection.close();
                    return id;
                } else {
                    System.out.println("\033[31mThis user does not exist!\033[0m");
                }
                connection.close();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    public static void deleteAllMyMessages(String login){
        try {
            Connection connection = getConnection();
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(deleteMyMessages);
                ps.setInt(1, getUserId(login));
                ps.executeUpdate();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static String getUserName(int id){
        try {
            Connection connection = getConnection();
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(getUserName);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String login = rs.getString("login");
                    connection.close();
                    return login;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

}
