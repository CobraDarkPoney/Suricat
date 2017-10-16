package ladysnake.models;

import ladysnake.helpers.utils.Pair;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**A class used to describe a {@link DBModel}'s Attribute
 * @author Ludwig GUERIN
 */
@SuppressWarnings("WeakerAccess")
public class DBAttribute<T> {
    /**The value of this {@link DBAttribute}*/
    protected T value;

    /**The name of this {@link DBAttribute}*/
    protected String name;

    /**The name of its type in the Database*/
    protected String dbtype;

    /**Default constructor
     */
    public DBAttribute(T value){
        this.value = value;
    }

    /**A getter for the value of this {@link DBAttribute}
     * @return the value of this {@link DBAttribute}
     */
    public T getValue(){ return value; }

    /**A setter for the value of this {@link DBAttribute}
     * @param value being the new value for this {@link DBAttribute}
     * @return a reference to the current {@link DBAttribute} (for chaining purposes)
     */
    public DBAttribute<T> setValue(T value){
        this.value = value;
        return this;
    }

    public static <Type> Class<Type> getJavaTypeFromDBType(String dbtype){
        return JdbcToJava.stream()
                .filter(e -> e.equals(JDBCType.valueOf(dbtype)))
                .findFirst()
                .get()
                .last();
    }

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
