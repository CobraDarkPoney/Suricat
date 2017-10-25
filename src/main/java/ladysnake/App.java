package ladysnake;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ladysnake.helpers.json.JsonReader;
import ladysnake.helpers.json.JsonWriter;
import ladysnake.models.*;

public class App {
    private static void out(String msg){  System.out.println(msg); }

    public static void main(String[] args){
        try {
            JsonReader reader = new JsonReader("test.json");
            JsonWriter writer = new JsonWriter("serializeTest.json");

            JsonArray actions = reader.getAsObject().get(DBTransaction.ACTIONS).getAsJsonArray();
            DBModel model = DBModel.fromJson(reader.getAsObject().get("model").getAsJsonObject());
            DBTransaction transaction = DBTransaction.fromJson(actions);
            JsonArray serializedActions = transaction.toJson().getAsJsonArray();

            out("Is serialized===read: " + (actions.equals(serializedActions) ? "yep" : "heck no"));
            writer.writeJsonElement(serializedActions);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
