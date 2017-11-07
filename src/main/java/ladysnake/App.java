package ladysnake;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ladysnake.helpers.json.JsonReader;
import ladysnake.helpers.json.JsonWriter;
import ladysnake.models.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings({"unused", "WeakerAccess"})
public class App {
    private static void out(String msg){  System.out.println(msg); }

    public static void main(String[] args){
        try {
            JsonReader reader = new JsonReader("test.json");
            JsonWriter writer = new JsonWriter("serializeTest.json");

            JsonArray actions = reader.getAsObject().get(DBTransaction.ACTIONS).getAsJsonArray();
            DBModel model = DBModel.fromJson(reader.getAsObject().get("models").getAsJsonArray().get(0).getAsJsonObject());
            DBTransaction transaction = DBTransaction.fromJson(actions);
            JsonArray serializedActions = transaction.toJson().getAsJsonArray();

            out("Is serialized===read: " + (actions.equals(serializedActions) ? "yep" : "heck no"));
            writer.writeJsonElement(serializedActions);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static int compare(JsonArray lhs, JsonArray rhs){
        List<JsonElement> l_list = StreamSupport.stream(lhs.spliterator(), false).collect(Collectors.toList());
        List<JsonElement> r_list = StreamSupport.stream(rhs.spliterator(), false).collect(Collectors.toList());

        if(l_list.size() != r_list.size())
            return l_list.size() - r_list.size();
        else{
            int diff = 0;
            /*Collections.sort(l_list);
            Collections.sort(r_list);*/

            for(int i = 0, size = l_list.size() ; i < size ; i+=1){
                String a = l_list.get(i).getAsJsonObject().getAsString();
                String b = r_list.get(i).getAsJsonObject().getAsString();
                if(!a.equalsIgnoreCase(b))
                    diff += 1;
            }

            return diff;
        }
    }
}
