package mysql.helper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class Json {
    public static void createJson(){
        JSONObject object = new JSONObject();
        JSONArray users = new JSONArray();
        JSONObject user = new JSONObject();
        user.put("Brano", "red");
        user.put("CaptainUkraine", "blue");
        user.put("cyberthief", "orange");
        user.put("DANKO", "yellow");
        user.put("Darina", "purple");
        user.put("Heni", "pink");
        user.put("Julius", "green");
        user.put("kristianS", "darkgreen");
        user.put("Kubo", "darkgray");
        user.put("Roman", "brown");
        user.put("Samuel", "aqua");
        user.put("Simon", "firebrick");
        users.add(user);
        object.put("users", users);
        try {
            File file = new File("resources/userColors.json");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(object.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static List<String> getUserColors(){
        List<String> list = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("resources/userColors.json")){
            JSONObject object = (JSONObject) parser.parse(reader);
            JSONArray array = (JSONArray) object.get("users");
            Iterator<Object> iterator = array.iterator();
            while(iterator.hasNext()){
                JSONObject jsonObject = (JSONObject) iterator.next();
                for(Object key : jsonObject.keySet()){
                    String value = key + ":" + jsonObject.get(key);
                    list.add(value);
                }
            }
        } catch (IOException | ParseException e) { e.printStackTrace(); }
        return list;
    }

    public static void addToColors(String value){
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("resources/userColors.json")) {
            JSONObject object = (JSONObject) parser.parse(reader);
            JSONArray array = (JSONArray) object.get("users");
            JSONObject newUser = new JSONObject();
            String[] temp = value.split(":");
            newUser.put(temp[0], temp[1]);

            array.add(newUser);
            FileWriter file = new FileWriter("resources/userColors.json");
            file.write(object.toJSONString());
            file.flush();
            file.close();
        } catch (IOException | ParseException e) { e.printStackTrace(); }
    }
}
