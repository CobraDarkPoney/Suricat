package ladysnake.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ladysnake.helpers.json.I_JsonSerializable;
import ladysnake.helpers.utils.I_MightNoNullParams;
import ladysnake.helpers.utils.I_Stringify;

/** A class representing a {@link DBTransaction}'s Action
 * @author Ludwig GUERIN
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DBTransactionAction implements I_Stringify, I_JsonSerializable, Comparable<DBTransactionAction>{
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**A {@link String} containing the name of the source of this {@link DBTransactionAction}*/
    protected String source;

    /**A {@link DBGranule} that is the target of this {@link DBTransactionAction}*/
    protected DBGranule target;

    /**The lock used for this {@link DBTransactionAction}*/
    protected E_DBLockTypes lock;

    /**The type of this {@link DBTransactionAction}*/
    protected E_DBTransactionActionTypes type;

    /**The index (for the execution of the Transaction)*/
    protected int index;

    /**Execution flag*/
    protected boolean executed;


    
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Advanced constructor
     * @param source being the source of this {@link DBTransactionAction} (an identifier)
     * @param index being the index in the execution of a {@link DBTransaction}
     * @param target being the {@link DBGranule} targeted by this {@link DBTransactionAction}
     * @param lock being the lock required/used by this {@link DBTransactionAction}
     * @param type being the type of action of this {@link DBTransactionAction}
     */
    public DBTransactionAction(String source, int index, DBGranule target, E_DBLockTypes lock, E_DBTransactionActionTypes type){
        this.assertParamsAreNotNull(source, index, target, lock, type);

        this.source = source;
        this.index = index;
        this.target = target;
        this.lock = lock;
        this.type = type;

        this.executed = false;
    }

    /**Advanced constructor
     * @param source being the source of this {@link DBTransactionAction} (an identifier)
     * @param index being the index in the execution of a {@link DBTransaction}
     * @param target being the name of the {@link DBModel} targeted by this {@link DBTransactionAction}
     * @param lock being the name of the lock required/used by this {@link DBTransactionAction}
     * @param type being the name of the type of action of this {@link DBTransactionAction}
     */
    public DBTransactionAction(String source, int index, String target, String lock, String type){
        this(
                source,
                index,
                AvailableGranules.get(target),
                E_DBLockTypes.get(lock.equals("") ? "NONE" : lock),
                E_DBTransactionActionTypes.get(type)
        );
    }



    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**A method that completes this {@link DBTransactionAction}
     * @return this {@link DBTransactionAction} (for chaining purposes)
     */
    public DBTransactionAction complete(){
        this.executed = true;
        return this;
    }

    @Override
    public String stringify(String tabLevel) {
        this.assertParamsAreNotNull(tabLevel);

        if(!I_Stringify.isTab(tabLevel))
            return I_Stringify.STRINGIFY_ERROR_MESSAGE;

        String ret = "";

        ret += tabLevel + "<DBTransactionAction>\n";
        ret += tabLevel + "\t" + "source: " + this.source + "\n";
        ret += tabLevel + "\t" + "index: " + this.index + "\n";
        ret += tabLevel + "\t" + "target:\n" + this.target.stringify(tabLevel + "\t\t") + "\n";
        ret += tabLevel + "\t" + "lock: " + this.lock.getName() + "\n";
        ret += tabLevel + "\t" + "type: " + this.type.getName() + "\n";
        ret += tabLevel + "</DBTransactionAction>\n";

        return ret;
    }

    @Override
    public int compareTo(DBTransactionAction other) {
        this.assertParamsAreNotNull(other);

        Integer t_i = this.index;
        Integer t_o = other.index;

        return t_i.compareTo(t_o);
    }

    @Override
    public JsonElement toJson(){
        JsonObject obj = new JsonObject();
        obj.addProperty(SOURCE, this.source);
        obj.addProperty(INDEX, this.index);
        obj.addProperty(LOCK, this.lock.name());//As it's an enum value
        obj.addProperty(TYPE,  this.type.name());//As it's an enum value
        obj.addProperty(TARGET, this.target.getName());

        return obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DBTransactionAction)) return false;

        DBTransactionAction that = (DBTransactionAction) o;

        return index == that.index
                && executed == that.executed
                && source.equalsIgnoreCase(that.source)
                && target.equals(that.target)
                && lock == that.lock
                && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + target.hashCode();
        result = 31 * result + lock.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + index;
        result = 31 * result + (executed ? 1 : 0);
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**The key to the source string in a JSON object*/
    public final static String SOURCE = "source";

    /**The key to the index number in a JSON object*/
    public final static String INDEX = "index";

    /**The key to the lock string in a JSON object*/
    public final static String LOCK = "lock";

    /**The key to the type string in a JSON object*/
    public final static String TYPE = "type";

    /**The key to the target string in a JSON object*/
    public final static String TARGET = "target";

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class methods
    ////////////////////////////////////////////////////////////////////////////////////////////

    /**Get a {@link DBTransactionAction} from its representation in a {@link JsonObject}
     * @param obj being the {@link JsonObject} to read from
     * @return a {@link DBTransactionAction} generated from the given {@link JsonObject}
     */
    public static DBTransactionAction fromJson(JsonObject obj){
        I_MightNoNullParams.assertNoneNull(obj);

        String source = obj.get(SOURCE).getAsString();
        int index = obj.get(INDEX).getAsInt();
        String target = obj.get(TARGET).getAsString();
        String lock = obj.get(LOCK).getAsString();
        String type = obj.get(TYPE).getAsString();

        return new DBTransactionAction(source, index, target, lock, type);
    }
}
