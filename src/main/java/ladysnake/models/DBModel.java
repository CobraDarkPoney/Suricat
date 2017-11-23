package ladysnake.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ladysnake.helpers.utils.I_Stringify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**A class representing a database model/table
 * @author Ludwig GUERIN
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DBModel implements DBGranule{
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**A List containing all attributes of this database table/model*/
    protected List<DBAttribute> attributes;

    /**The name of this database table/model*/
    protected String name;


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Private constructor used to factorize initialization
     * @param name being the name of this {@link DBModel}
     */
    private DBModel(String name){
        this.name = name;
        this.attributes = new ArrayList<>();
        AvailableGranules.add(this); //register this DBModel as an available model
    }

    /**Default constructor
     * @param name being the name of this {@link DBModel}
     * @param first_attr being the first {@link DBAttribute} of this {@link DBModel} (a table/model must have at least one attribute)
     */
    public DBModel(String name, DBAttribute first_attr){
        this(name);
        this.attributes.add(first_attr);
    }

    /**Advanced constructor : variadic list of attributes
     * @param name being the name of this {@link DBModel}
     *  @param attributes being any amount of {@link DBAttribute} (cf. variadic arguments)
     */
    public DBModel(String name, DBAttribute... attributes){
        this(name, Arrays.asList(attributes));
    }

    /**Advanced constructor : Collection of attributes
     * @param name being the name of this {@link DBModel}
     * @param attributes being a {@link Collection} of {@link DBAttribute}s
     */
    public DBModel(String name, Collection<? extends DBAttribute> attributes){
        this(name);
        this.attributes.addAll(attributes);
    }



    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Retrieve the {@link DBAttribute} from this {@link DBModel} as a {@link Stream}
     * @return the attributes of this model as a {@link Stream}
     */
    public Stream<DBAttribute> attrStream(){
        return this.attributes.stream();
    }

    /**Get the name of this {@link DBModel}
     *@return the name of this {@link DBModel}
     */
    public String getName(){ return this.name; }

    @Override
    public String stringify(String tabLevel) {
        if(I_Stringify.isTab(tabLevel)){
            String ret = "";

            ret += tabLevel + "<DBModel>\n";
            ret += tabLevel + "\t" + "name: " + this.name +"\n";
            ret += tabLevel + "\t" + "attributes: \n" + String.join("\n", this.attrStream()
                    .map(attr -> attr.stringify(tabLevel + "\t\t"))
                    .collect(Collectors.toList()).toArray(new String[0]));
            ret += tabLevel + "</DBModel>\n";

            return ret;
        }else
            return I_Stringify.STRINGIFY_ERROR_MESSAGE;
    }

    @Override
    public String toString(){ return this.stringify(); }

    @Override
    public JsonElement toJson(){
        List<JsonElement>attr = attrStream()
                .map(DBAttribute::toJson)
                .collect(Collectors.toList());

        JsonArray attr_arr = new JsonArray();
        attr.forEach(attr_arr::add);

        JsonObject obj = new JsonObject();
        obj.addProperty(NAME, this.name);
        obj.add(ATTR_ARR, attr_arr);

        return obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DBModel)) return false;

        DBModel dbModel = (DBModel) o;

        return attributes.equals(dbModel.attributes) && getName().equals(dbModel.getName());
    }

    @Override
    public int hashCode() {
        int result = attributes.hashCode();
        result = 31 * result + getName().hashCode();
        return result;
    }



    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**The string that should serve as a key to find the name of a DBModel in a JSON file/object*/
    protected final static String NAME = "name";

    /**The string that should serve as a key to find the array of attributes of a DBModel in a JSON file/object*/
    protected final static String ATTR_ARR= "attributes";



    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    public static DBModel fromJson(JsonObject obj){
        String name = obj.get(NAME).getAsString();
        JsonArray attrs = obj.get(ATTR_ARR).getAsJsonArray();

        Stream<JsonElement> attr_arr_stream = StreamSupport.stream(attrs.spliterator(), false);
        List<DBAttribute> attributes = attr_arr_stream
        .map(JsonElement::getAsJsonObject)
        .map(DBAttribute::fromJson)
        .collect(Collectors.toList());

        return new DBModel(name, attributes);
    }
}
