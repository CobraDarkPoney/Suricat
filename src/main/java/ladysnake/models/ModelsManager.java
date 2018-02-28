package ladysnake.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ladysnake.helpers.json.I_JsonSerializable;
import ladysnake.helpers.json.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class ModelsManager implements I_JsonSerializable{
    protected List<DBModel> tables;
    protected DBTransaction transaction;
    protected DBTransactionExecution execution;

    public ModelsManager(JsonObject obj){
        this.tables = new ArrayList<>();

        JsonArray models = obj.getAsJsonArray(MODELS);
        JsonArray transactions = obj.getAsJsonArray(TRANSACTIONS);

        StreamSupport.stream(models.spliterator(), false)
        .map(e -> (JsonObject)e)
        .map(DBModel::fromJson)
        .forEach(this.tables::add);

        this.transaction = DBTransaction.fromJson(transactions);
        this.execution = new DBTransactionExecution(this.transaction);
    }

    public List<DBModel> getTables(){ return this.tables; }
    public List<DBModel> getModels(){ return this.getTables(); }
    public DBTransaction getTransaction() { return this.transaction; }
    public DBTransactionExecution getExecution() { return execution; }
    public DBLockList getLockList(){ return this.getExecution().getLockList(); }
    public List<String> getSources(){
        return this.getTransaction().getActionsStream()
        .map(DBTransactionAction::getSource)
        .collect(Collectors.toList());
    }

    @Override
    public JsonElement toJson() {
        JsonArray transactions = (JsonArray) this.transaction.toJson();
        JsonArray models = new JsonArray();
        this.tables.stream()
        .map(DBModel::toJson)
        .forEach(models::add);

        JsonObject ret = new JsonObject();
        ret.add(MODELS, models);
        ret.add(TRANSACTIONS, transactions);
        return ret;
    }

    public static String MODELS = "models";
    public static String TRANSACTIONS = "transactions";

    public static ModelsManager fromJson(JsonObject obj){
        return new ModelsManager(obj);
    }

    public static ModelsManager fromFile(String path) throws IOException {
        JsonReader jsonReader = new JsonReader(path);
        return new ModelsManager(jsonReader.getAsObject());
    }
}
