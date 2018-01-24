package ladysnake.models;

import ladysnake.helpers.utils.I_MightNoNullParams;
import ladysnake.helpers.utils.I_Stringify;

/**An Enum representing most Lock Types used in Databases
 * @author Ludwig GUERIN
 */
@SuppressWarnings("unused")
public enum E_DBLockTypes {
    XE("ExclusiveExtended", 0, E_Permissions.READWRITE, E_Permissions.NONE),
    X("Exclusive", 1, E_Permissions.READWRITE, E_Permissions.NONE),
    UE("UpdateExtended", 2, E_Permissions.READTRANSFORM, E_Permissions.READ),
    U("Update", 3, E_Permissions.READTRANSFORM, E_Permissions.READ),
    SE("SharedExclusive", 4, E_Permissions.READ, E_Permissions.READ),
    S("Shared", 5, E_Permissions.READ, E_Permissions.READ),
    NONE("None", 6, E_Permissions.READWRITE, E_Permissions.READWRITE);

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**A short description of the lock
     */
    private final String lock;
    /**The index used as an ID for the lock
     */
    private final int index;

    private final E_Permissions selfPerms;

    private final E_Permissions otherPerms;


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    E_DBLockTypes(String type, int val, E_Permissions selfPerms, E_Permissions otherPerms){
        I_MightNoNullParams.assertNoneNull(type, val, selfPerms, otherPerms);

        this.lock = type;
        this.index = val;
        this.selfPerms = selfPerms;
        this.otherPerms = otherPerms;
    }

    @Override
    public String toString() {
        return this.stringify();
    }

    public String stringify(){return this.stringify("");}

    public String stringify(String tabLevel){
        I_MightNoNullParams.assertNoneNull(tabLevel);

        if(!I_Stringify.isTab(tabLevel))
            return I_Stringify.STRINGIFY_ERROR_MESSAGE;

        String ret = "";

        ret += tabLevel + "<E_DBLockTypes>\n";
        ret += tabLevel + "\t" + "lock: " + this.lock + "\n";
        ret += tabLevel + "\t" + "index: " + this.index + "\n";
        ret += tabLevel + "</E_DBLockTypes>\n";

        return ret;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**A handy method that retrieves the name of this {@link E_DBLockTypes}
     * @return the name as a {@link String}
     */
    public String getName(){ return lock; }

    public E_Permissions getSelfPerms() {
      return selfPerms;
    }

    public E_Permissions getOtherPerms() {
      return otherPerms;
    }

    /**Determine whether this {@link E_DBLockTypes} is compatible with the given one or not
     * @param otherLock being the lock to check the compatibility with the current ont
     * @return TRUE if compatible, FALSE otherwise
     */
    public boolean compatibleWith(E_DBLockTypes otherLock){
        I_MightNoNullParams.assertNoneNull(otherLock);
        //TODO: Upgrade with special cases (eg. None cases)  :: Théowen ?
        return  E_DBLockTypes.grantPermissions[this.index][otherLock.index];
    }

    public boolean stricterThan(E_DBLockTypes rhs){
      return this.index < rhs.index;
    }

    public boolean isShared() {
      return this.equals(E_DBLockTypes.S) || this.equals(E_DBLockTypes.SE);
    }

    public boolean isUpdate() {
      return this.equals(E_DBLockTypes.U) || this.equals(E_DBLockTypes.UE);
    }

    public boolean isExclusive() {
      return this.equals(E_DBLockTypes.X) || this.equals(E_DBLockTypes.XE);
    }

    public boolean isExtended() {
      return this.equals(E_DBLockTypes.SE) || this.equals(E_DBLockTypes.UE) || this.equals(E_DBLockTypes.XE);
    }

    public boolean isNone() {
      return this.equals(E_DBLockTypes.NONE);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**A handy method that retrieves a {@link E_DBLockTypes} from its name (!= from the one given by getName)
     * @param name being the name of the Lock to get
     * @return The created {@link E_DBLockTypes}
     */
    public static E_DBLockTypes get(String name){
        I_MightNoNullParams.assertNoneNull(name);
        return E_DBLockTypes.valueOf(name.toUpperCase());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**A compatibility matrix*/
    //                             x y
    public static boolean[][] grantPermissions =  {
            //TODO: fix for special cases of NONE (eg. upd)

            //  y\x      XE         X     UE    U      SE    S      NONE
           /*XE*/       {false, false, false, false, false, false, true},
           /*X*/         {false, false, false, false, false, false, true},
            /*UE*/      {false, false, false, false, true, true, true},
            /*U*/        {false, false, false, false, true, true, true},
            /*SE*/      {false, false, true, true, true, true, true},
            /*S*/        {false, false, true, true, true, true, true},
            /*NONE*/ {true, true, true, true, true, true, true}

            //How it works: X is compatible with Y if compatibility[Y[X] is true
    };

    public enum E_Permissions {
      NONE,
      READ,
      READTRANSFORM,
      READWRITE;

      public boolean matches(E_DBTransactionActionTypes type){
        if(type.equals(E_DBTransactionActionTypes.find))
          return !this.equals(E_Permissions.NONE);
        return this.equals(E_Permissions.READWRITE);
      }
    }
}
