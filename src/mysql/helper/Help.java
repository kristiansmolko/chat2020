package mysql.helper;

import mysql.TopSecretData;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class Help {
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


}
