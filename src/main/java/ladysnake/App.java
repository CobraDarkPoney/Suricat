package ladysnake;

import ladysnake.helpers.json.JsonReader;
import ladysnake.helpers.json.JsonWriter;

public class App {
    private static void out(String msg){  System.out.println(msg); }

    public static void main(String[] args){
        JsonReader reader = null;
        JsonWriter writer = null;
        try {
             reader = new JsonReader("test.json");
             writer = new JsonWriter("echo.json");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        try {
            reader.entryStream().map(Object::toString).forEach(App::out);
            writer.writeJsonElement(reader.getAsJsonElement());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
