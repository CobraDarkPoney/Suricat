package ladysnake.models;

/**An Enum representing most Lock Types used in Databases
 * @author Ludwig GUERIN
 */
public enum E_DBLockTypes {
    XE("ExclusiveExtended", 0),
    X("Exclusive", 1),
    SE("SharedExclusive", 2),
    S("Shared", 3),
    UE("UpdateExtended", 4),
    U("Update", 5);

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**A short description of the lock
     */
    private final String lock;
    /**The index used as an ID for the lock
     */
    private final int index;


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    E_DBLockTypes(String type, int val){
        this.lock = type;
        this.index = val;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**A handy method that retrieves the name of this {@link E_DBLockTypes}
     * @return the name as a {@link String}
     */
    public String getName(){ return lock; }

    /**Determine whether this {@link E_DBLockTypes} is compatible with the given one or not
     * @param otherLock being the lock to check the compatibility with the current ont
     * @return TRUE if compatible, FALSE otherwise
     */
    public boolean compatibleWith(E_DBLockTypes otherLock){
        return  E_DBLockTypes.compatibility[this.index][otherLock.index];
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**A handy method that retrieves a {@link E_DBLockTypes} from its name (!= from the one given by getName)
     * @param name
     * @return
     */
    public static E_DBLockTypes get(String name){
        return E_DBLockTypes.valueOf(name.toUpperCase());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**A compatibility matrix*/
    public static boolean[][] compatibility =  {
            //  y\x      XE    X       UE    U      SE    S
           /*XE*/    {false, false, false, false, false, false},
           /*X*/      {false, false, false, false, false, false},
            /*UE*/  {false, false, false, false, true, true},
            /*U*/    {false, false, false, false, true, true},
            /*SE*/  {false, false, true, true, true, true},
            /*S*/    {false, false, true, true, true, true}

            //How it works: X is compatible with Y if compatibility[X][Y] is true
    };
}
