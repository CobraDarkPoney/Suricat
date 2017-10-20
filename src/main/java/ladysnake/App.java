package ladysnake;

import com.google.gson.JsonObject;
import ladysnake.helpers.json.JsonReader;
import ladysnake.helpers.json.JsonWriter;
import ladysnake.models.DBAttribute;
import ladysnake.models.DBModel;
import ladysnake.models.E_DBLockTypes;

public class App {
    private static void out(String msg){  System.out.println(msg); }

    public static void main(String[] args){
        try {
            JsonReader reader = new JsonReader("test.json");

            JsonObject obj = reader.getAsObject().get("model").getAsJsonObject();
            JsonObject objT = reader.getAsObject().get("transactions").getAsJsonArray().get(0).getAsJsonObject();

            DBModel model = DBModel.fromJson(obj);

            out(model.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
