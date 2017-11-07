package ladysnake.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ladysnake.helpers.json.I_JsonSerializable;
import ladysnake.helpers.utils.I_Stringify;
import ladysnake.helpers.utils.Pair;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**A class used to describe a {@link DBModel}'s Attribute
 * @author Ludwig GUERIN
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DBAttribute implements DBGranule{
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////

    /**The name of this {@link DBAttribute}*/
    protected String name;

    /**The name of its type in the Database*/
    protected String dbtype;

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////

    /**Default constructor
     * @param name being the name of this {@link DBAttribute}
     * @param dbtype being the database type of this {@link DBAttribute}
     */
    public DBAttribute(String name, String dbtype){
        this.name = name;
        this.dbtype = dbtype.toUpperCase();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String getName(){
        return this.name;
    }

    @Override
    public String toString(){ return this.stringify(); }

    @Override
    public String stringify(String tabLevel) {
        if(I_Stringify.isTab(tabLevel)){
            String ret = "";

            ret += tabLevel + "<DBAttribute>\n";
            ret += tabLevel + "\t" + "name: " + this.name + "\n";
            ret += tabLevel + "\t" + "database type: " + this.dbtype + "\n";
            ret += tabLevel + "</DBAttribute>\n";

            return ret;
        }else
            return I_Stringify.STRINGIFY_ERROR_MESSAGE;
    }

    @Override
    public JsonElement toJson(){
        JsonObject obj = new JsonObject();

        obj.addProperty(NAME, this.name);
        obj.addProperty(DBTYPE, this.dbtype);

        return obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DBAttribute)) return false;

        DBAttribute that = (DBAttribute) o;

        return name.equals(that.name) && dbtype.equals(that.dbtype);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + dbtype.hashCode();
        return result;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class methods
    ////////////////////////////////////////////////////////////////////////////////////////////

    /**Get the Java type associated to a Database type
     * @param dbtype being a {@link String} that represents the Database type to convert from
     * @return a {@link Class}
     */
    public static Class getJavaTypeFromDBType(String dbtype){
        return JdbcToJava.stream()
                    .filter(e -> e.first().equals(JDBCType.valueOf(dbtype.toUpperCase())))
                    .findFirst()
                    .map(Pair::last)
                    .orElse(null);
    }

    /**Create a {@link DBAttribute} from a {@link JsonObject}
     * @param obj being the {@link JsonObject} to create the {@link DBAttribute} from
     * @return a {@link DBAttribute}
     */
    public static DBAttribute fromJson(JsonObject obj){
        String dbtype = obj.get(DBAttribute.DBTYPE).getAsString();
        String name = obj.get(DBAttribute.NAME).getAsString();

        return new DBAttribute(name, dbtype);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class properties
    ////////////////////////////////////////////////////////////////////////////////////////////

    /**The name of the property in the JSON file*/
    public final static String NAME = "name";

    /**The name of the property in the JSON file*/
    public final static String DBTYPE = "dbtype";

    /**A List holding the mapping from JDBCType to a Java Class*/
    public final static List<Pair<JDBCType, Class>> JdbcToJava = new ArrayList<Pair<JDBCType, Class>>(){{
        add( new Pair<>(JDBCType.CHAR, String.class) );
        add( new Pair<>(JDBCType.VARCHAR, String.class) );
        add( new Pair<>(JDBCType.LONGVARCHAR, String.class) );
        add( new Pair<>(JDBCType.BIT, Boolean.class) );
        add( new Pair<>(JDBCType.NUMERIC, BigDecimal.class) );
        add( new Pair<>(JDBCType.TINYINT, Byte.class) );
        add( new Pair<>(JDBCType.SMALLINT, Short.class) );
        add( new Pair<>(JDBCType.INTEGER, Integer.class) );
        add( new Pair<>(JDBCType.BIGINT, Long.class) );
        add( new Pair<>(JDBCType.REAL, Float.class) );
        add( new Pair<>(JDBCType.FLOAT, Float.class) );
        add( new Pair<>(JDBCType.DOUBLE, Double.class) );
        add( new Pair<>(JDBCType.VARBINARY, Byte[].class) );
        add( new Pair<>(JDBCType.BINARY, Byte[].class) );
        add( new Pair<>(JDBCType.DATE, Date.class) );
        add( new Pair<>(JDBCType.TIME, Time.class) );
        add( new Pair<>(JDBCType.TIMESTAMP, Timestamp.class) );
        add( new Pair<>(JDBCType.CLOB, Clob.class) );
        add( new Pair<>(JDBCType.BLOB, Blob.class) );
        add( new Pair<>(JDBCType.ARRAY, Array.class) );
        add( new Pair<>(JDBCType.REF, Ref.class) );
        add( new Pair<>(JDBCType.STRUCT, Struct.class) );
    }};
}
