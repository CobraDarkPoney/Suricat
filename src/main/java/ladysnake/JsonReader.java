package ladysnake;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JsonReader {

    public static void main(String[] args) {

        Gson gson = new Gson();

        try {

            BufferedReader reader = new BufferedReader(
                    new FileReader("test.json"));

            //convert the json string back to object
            JsonObject database = gson.fromJson(reader, JsonObject.class);

            System.out.println("Base de donénes: "+database.getAsJsonObject("model").get("name"));
            System.out.println("Attributs: "+database.getAsJsonObject("model").get("attributes"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
