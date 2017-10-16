package ladysnake.models;

/**An Enum representing most Transaction Action Types that Databases use
 * @author Ludwig GUERIN
 */
public enum E_DBTransactionActionTypes {
    find("find"),
    upd("update"),
    rollback("rollback"),
    commit("commit");

    /**A short description of the Action
     */
    private String description;
    E_DBTransactionActionTypes(String desc){ this.description = desc; }
}
