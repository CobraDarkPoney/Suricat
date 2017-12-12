package ladysnake.models;

import ladysnake.helpers.utils.I_MightNoNullParams;

/**An Enum representing most Transaction Action Types that Databases use
 * @author Ludwig GUERIN
 */
public enum E_DBTransactionActionTypes {
    find("find"),
    upd("update"),
    rollback("rollback"),
    commit("commit");

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**A short description of the Action*/
    private String description;

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    E_DBTransactionActionTypes(String desc){
        I_MightNoNullParams.assertNoneNull(desc);

        this.description = desc;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**A handy method to retrieve the name of this {@link E_DBTransactionActionTypes}
     * @return the name as a String
     */
    public String getName(){ return this.description; }



    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**A handy method to retrieve a {@link E_DBTransactionActionTypes} from its name (!= from the one given by getName)
     * @param name being the name of the desired {@link E_DBTransactionActionTypes}
     * @return the desired {@link E_DBTransactionActionTypes} if it exists
     */
    public static E_DBTransactionActionTypes get(String name){
        I_MightNoNullParams.assertNoneNull(name);

        return E_DBTransactionActionTypes.valueOf(name.toLowerCase());
    }

    @Override
    public String toString() {
        String ret = "";

        ret += "<E_DBTransactionActionTypes>\n";
        ret += "name: "+this.getName()+"\n";
        ret += "</E_DBTransactionActionTypes>\n";

        return ret;
    }
}
