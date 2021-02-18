package mysql.helper;

import mysql.TopSecretData;
import mysql.entity.Message;
import mysql.entity.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Help {
    private static final String getUserId = "SELECT id, login FROM user WHERE login = ?";
    private static final String getUserName = "SELECT id, login FROM user WHERE id = ?";
    private static final String deleteMyMessages = "DELETE FROM message WHERE toUser = ?";
    private static final String getAllMessages = "SELECT * FROM message";
    private static final String getMyMessages = "SELECT * FROM message WHERE toUser = ?";
    private static final String getSentMessages = "SELECT * FROM archive WHERE fromUser = ?";
    private static final String getUsers = "SELECT login FROM user";
    private static final String getArchiveMess = "SELECT * FROM archive WHERE toUser = ?";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(TopSecretData.getUrl(), TopSecretData.getUsername(), TopSecretData.getPassword());
    }

    public static boolean isUser(String name){
        String query = "SELECT login FROM user";
        try (Connection connection = getConnection()) {
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
        try (Connection connection = getConnection()) {
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

    public static void deleteAllMyMessages(int id){
        try (Connection connection = getConnection()) {
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(deleteMyMessages);
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static String getUserName(int id){
        try (Connection connection = getConnection()) {
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

    public static List<String> getUsers(){
        List<String> list = new ArrayList<>();
        try (Connection connection = getConnection()) {
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(getUsers);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    list.add(rs.getString("login"));
                }
                connection.close();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public static List<Message> getMyMessages(int toUser){
        List<Message> list = new ArrayList<>();
        try (Connection connection = getConnection()) {
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(getMyMessages);
                ps.setInt(1, toUser);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int idMessage = rs.getInt("id");
                    Date time = rs.getTimestamp("dt");
                    String from = getUserName(rs.getInt("fromUser"));
                    String to = getUserName(rs.getInt("toUser"));
                    String text = rs.getString("text");
                    list.add(new Message(idMessage, from, to, time, text));
                }
                connection.close();
            }
        } catch (Exception e) { e.printStackTrace(); }
        deleteAllMyMessages(toUser);
        return list;
    }

    public static List<Message> getMessagesArchive(int toUser){
        List<Message> list = new ArrayList<>();
        try (Connection connection = getConnection()){
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(getArchiveMess);
                ps.setInt(1, toUser);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    int idMessage = rs.getInt("id");
                    Date time = rs.getTimestamp("dt");
                    String from = getUserName(rs.getInt("fromUser"));
                    String to = getUserName(rs.getInt("toUser"));
                    String text = rs.getString("text");
                    list.add(new Message(idMessage, from, to, time, text));
                }
                connection.close();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public static List<Message> getSentMessages(int fromUser){
        List<Message> list = new ArrayList<>();
        try (Connection connection = getConnection()){
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(getSentMessages);
                ps.setInt(1, fromUser);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    int idMessage = rs.getInt("id");
                    Date time = rs.getTimestamp("dt");
                    String from = getUserName(rs.getInt("fromUser"));
                    String to = getUserName(rs.getInt("toUser"));
                    String text = rs.getString("text");
                    list.add(new Message(idMessage, from, to, time, text));
                }
                connection.close();
            }
        } catch (Exception e) { e.printStackTrace(); }
        deleteAllMyMessages(fromUser);
        return list;
    }

    public static boolean newMessage(int toUser){
        try (Connection connection = getConnection()){
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(getMyMessages);
                ps.setInt(1, toUser);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    connection.close();
                    return true;
                }
                connection.close();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public static String getTime(Date date){
        Date today = new Date();
        Date mes = new Date(date.getTime());
        DateFormat y = new SimpleDateFormat("yyyy");
        DateFormat m = new SimpleDateFormat("MM");
        DateFormat d = new SimpleDateFormat("dd");
        DateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat hour = new SimpleDateFormat("hh:mm:ss");
        int todayYear = Integer.parseInt(y.format(today));
        int todayMonth = Integer.parseInt(m.format(today));
        int todayDay = Integer.parseInt(d.format(today));
        int messYear = Integer.parseInt(y.format(mes));
        int messMon = Integer.parseInt(m.format(mes));
        int messDay = Integer.parseInt(d.format(mes));
        if (todayYear == messYear && todayMonth == messMon && todayDay == messDay)
            return hour.format(mes);
        else
            return day.format(mes);
    }
}
