package ladysnake;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ladysnake.helpers.json.JsonReader;
import ladysnake.helpers.json.JsonWriter;
import ladysnake.models.DBAttribute;
import ladysnake.models.DBModel;
import ladysnake.models.DBTransaction;
import ladysnake.models.E_DBLockTypes;

import java.io.FileWriter;

public class App {
    private static void out(String msg){  System.out.println(msg); }

    public static void main(String[] args){
        try {
            JsonReader reader = new JsonReader("test.json");

            JsonObject obj = reader.getAsObject().get("model").getAsJsonObject();
            JsonArray objT = reader.getAsObject().get( DBTransaction.ACTIONS ).getAsJsonArray();

            DBModel model = DBModel.fromJson(obj);
            DBTransaction transaction = DBTransaction.fromJson(objT);

            out(model.toString());
            out("\n\n");
            out(transaction.stringify());

            FileWriter writer = new FileWriter("output.txt");
            writer.write(transaction.toString());
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
