package mysql;

import mysql.entity.Message;
import mysql.helper.Help;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Database dat = new Database();
        String name = "kristianS";
        String pass = TopSecretData.getChatPass();
        List<Message> list = dat.getMyMessages(name);
        for (Message m : list){
            System.out.println(m.getFrom() + " to " + m.getTo() + " at " + m.getDt() + " : " + m.getText());
        }
    }

}
