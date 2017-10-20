package ladysnake.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ladysnake.helpers.utils.I_Stringify;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**TODO:  Write the stringify method + write all the missing documentations
 * */

/**A class representing a Transaction occurring in a Database
 * @author Ludwig GUERIN
 */
@SuppressWarnings({"WeakerAccess","unused"})
public class DBTransaction implements I_Stringify{
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**The list of {@link DBTransactionAction} occuring in this {@link DBTransaction}*/
    protected List<DBTransactionAction> actions;



    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Default constructor
     */
    public DBTransaction(){
        this.actions = new ArrayList<>();
    }

    /**Advanced constructor : from a {@link Collection}
     * @param collec being the {@link Collection} to get the {@link DBTransactionAction} from
     */
    public DBTransaction(Collection<? extends DBTransactionAction> collec){
        this();
        this.addAll(collec).sort();
    }



    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    public DBTransaction add(DBTransactionAction action){
        this.actions.add(action);
        return this;
    }

    public DBTransaction addAll(Collection<? extends DBTransactionAction> actions){
        this.actions.addAll(actions);
        return this;
    }

    public DBTransaction sort(Comparator<? super DBTransactionAction> comparator){
        this.actions.sort(comparator);
        return this;
    }

    public DBTransaction sort(){
        return this.sort(DBTransactionAction::compareTo);
    }

    public Stream<DBTransactionAction> stream(){
        return this.actions.stream();
    }

    @Override
    public String stringify(String tabLevel) {
        String ret = "";

        return ret;
    }




    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    public final static String ACTIONS = "transactions";



    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    public static DBTransaction fromJson(JsonObject obj){
        JsonArray actions = obj.get(ACTIONS).getAsJsonArray();
        Collection<DBTransactionAction> collec = StreamSupport.stream(actions.spliterator(), false)
                .map(JsonElement::getAsJsonObject)
                .map(DBTransactionAction::fromJson)
                .collect(Collectors.toList());

        return new DBTransaction(collec);
    }


}
