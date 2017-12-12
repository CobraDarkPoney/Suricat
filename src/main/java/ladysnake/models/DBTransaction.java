package ladysnake.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import ladysnake.helpers.json.I_JsonSerializable;
import ladysnake.helpers.utils.I_MightNoNullParams;
import ladysnake.helpers.utils.I_Stringify;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**TODO: Modify UML + add functionnalities (solve execution, etc...) + add classes (lockStack, executionGraph, waitingGraph, etc...)
 */

/**A class representing a Transaction occurring in a Database
 * @author Ludwig GUERIN
 */
@SuppressWarnings({"WeakerAccess","unused"})
public class DBTransaction implements I_Stringify, I_JsonSerializable{
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
        this.assertParamsAreNotNull(collec);
        this.addAll(collec).sort();
    }



    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Add a {@link DBTransactionAction} to this {@link DBTransaction}
     * @param action being the {@link DBTransactionAction} that will be added
     * @return this {@link DBTransaction} (for chaining purposes)
     */
    public DBTransaction add(DBTransactionAction action){
        this.assertParamsAreNotNull(action);

        this.actions.add(action);
        return this;
    }

    /**Add every single {@link DBTransaction} from a {@link Collection} to this {@link DBTransaction}
     * @param actions being the {@link Collection} of {@link DBTransactionAction} that will be added
     * @return this {@link DBTransaction} (for chaining purposes)
     */
    public DBTransaction addAll(Collection<? extends DBTransactionAction> actions){
        this.assertParamsAreNotNull(actions);

        this.actions.addAll(actions);
        return this;
    }

    /**Sort this {@link DBTransaction} using the given {@link Comparator}
     * @param comparator being the {@link Comparator} used to sort this {@link DBTransaction}
     * @return this {@link DBTransaction} (for chaining purposes)
     */
    public DBTransaction sort(Comparator<? super DBTransactionAction> comparator){
        this.assertParamsAreNotNull(comparator);

        this.actions.sort(comparator);
        return this;
    }

    /**Sort this {@link DBTransaction} using the compareTo method of a {@link DBTransactionAction}
     * @return this {@link DBTransaction} (for chaining purposes)
     */
    public DBTransaction sort(){
        return this.sort(DBTransactionAction::compareTo);
    }

    /**Retrieve a {@link Stream} of {@link DBTransactionAction} from this {@link DBTransaction} (for collection manipulation mainly)
     * @return the {@link Stream} created from this {@link DBTransaction}
     */
    public Stream<DBTransactionAction> stream(){
        return this.actions.stream();
    }

    @Override
    public String stringify(String tabLevel) {
        this.assertParamsAreNotNull(tabLevel);
        if(!I_Stringify.isTab(tabLevel))
            return I_Stringify.STRINGIFY_ERROR_MESSAGE;

        String ret = "";

        ret += tabLevel + "<DBTransaction>" + "\n";
        ret += tabLevel + "\t" + "actions: " + "\n";
        ret += tabLevel + String.join("\n", this.stream()
        .map(action -> action.stringify(tabLevel + "\t\t"))
        .collect(Collectors.toList())
        .toArray(new String[0]));
        ret += tabLevel + "</DBTransaction>" + "\n";

        return ret;
    }

    @Override
    public String toString(){ return this.stringify(); }

    @Override
    public JsonElement toJson(){
        List<JsonElement> actions = stream()
        .map(DBTransactionAction::toJson)
        .collect(Collectors.toList());

        JsonArray arr = new JsonArray();
        actions.forEach(arr::add);

        return arr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DBTransaction)) return false;

        DBTransaction that = (DBTransaction) o;

        return actions.equals(that.actions);
    }

    @Override
    public int hashCode() {
        return actions.hashCode();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**The key to the array of actions in a JSON file/object*/
    public final static String ACTIONS = "transactions";



    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class methods
    ////////////////////////////////////////////////////////////////////////////////////////////

    /**Create a {@link DBTransaction} from its representation in a {@link JsonArray}
     * @param obj being the {@link JsonArray} to read from
     * @return the created {@link DBTransaction}
     */
    public static DBTransaction fromJson(JsonArray obj){
        I_MightNoNullParams.assertNoneNull(obj);

        Collection<DBTransactionAction> actions = StreamSupport.stream(obj.spliterator(), false)
                .map(JsonElement::getAsJsonObject)
                .map(DBTransactionAction::fromJson)
                .collect(Collectors.toList());

        return new DBTransaction(actions);
    }


}
