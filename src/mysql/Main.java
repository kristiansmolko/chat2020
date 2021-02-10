package mysql;

import mysql.helper.Help;

public class Main {
    public static void main(String[] args) {
        Database dat = new Database();
        String name = "kristianS";
        String pass = TopSecretData.getChatPass();
        dat.changePassword(name, "kristian", pass, pass);
    }

}
