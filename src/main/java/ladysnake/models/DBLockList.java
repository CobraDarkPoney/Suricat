package ladysnake.models;

import ladysnake.helpers.utils.I_MightNoNullParams;
import ladysnake.helpers.utils.I_Stringify;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"WeakerAccess", "unused"})
public class DBLockList extends ArrayList<DBLockList.Lock> implements I_MightNoNullParams, I_Stringify{
    public static class Lock implements I_MightNoNullParams, I_Stringify{
        protected String source;
        protected DBGranule target;
        protected E_DBLockTypes type;
        protected boolean freed;

        ////////////////////////////////////////////////////////////////////////////////////////////
        ////Constructors
        ////////////////////////////////////////////////////////////////////////////////////////////
        public Lock(String source, DBGranule target, E_DBLockTypes type, boolean freed){
            this.source = source;
            this.target = target;
            this.type = type;
            this.freed = freed;
        }

        public Lock(String source, DBGranule target, E_DBLockTypes type){ this(source, target, type, false); }
        public Lock(DBTransactionAction action){ this(action.source, action.target, action.lock); }



        ////////////////////////////////////////////////////////////////////////////////////////////
        ////Methods
        ////////////////////////////////////////////////////////////////////////////////////////////
        public String getSource(){ return this.source; }

        public DBGranule getTarget() { return this.target; }

        public E_DBLockTypes getType(){ return this.type; }

        public boolean hasFreed(){ return this.freed; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Lock)) return false;

            Lock lock = (Lock) o;

            return getSource().equals(lock.getSource())
                    && getTarget().equals(lock.getTarget())
                    && getType() == lock.getType()
                    && hasFreed() == lock.hasFreed();
        }

        @Override
        public int hashCode() {
            int result = getSource().hashCode();
            result = 31 * result + getTarget().hashCode();
            result = 31 * result + getType().hashCode();
            result = 31 * result + Boolean.valueOf(hasFreed()).hashCode();
            return result;
        }

        @Override
        public String stringify(String tabLevel) {
            this.assertParamsAreNotNull(tabLevel);

            if(!I_Stringify.isTab(tabLevel))
                return I_Stringify.STRINGIFY_ERROR_MESSAGE;

            String ret = "";

            ret += tabLevel + "<DBLockList.Lock>\n";
            ret += tabLevel + "\t" + "source:  " + getSource() + "\n";
            ret += tabLevel + "\t" + "target: " + "\n";
            ret += getTarget().stringify(tabLevel + "\t\t") + "\n";
            ret += tabLevel + "\t" + "type: " + "\n";
            ret += getType().stringify(tabLevel + "\t\t") + "\n";
            ret += tabLevel + "</DBLockList.Lock>\n";

            return ret;
        }
    }

    protected List<DBLockList.Lock> pending;

    public DBLockList(){
        super();
        this.pending = new ArrayList<>();
    }

    public boolean hasLockOn(DBGranule granule){
        this.assertParamsAreNotNull(granule);

        return this.stream()
        .map(DBLockList.Lock::getTarget)
        .filter(target -> target.equals(granule))
        .collect(Collectors.toList())
        .size() != 0;
    }

    public boolean hasPendingOn(DBGranule granule){
        this.assertParamsAreNotNull(granule);

        return this.pending.stream()
        .anyMatch(wait -> wait.getTarget().equals(granule));
    }

    public List<E_DBLockTypes> getLockTypesOn(DBGranule granule){
        this.assertParamsAreNotNull(granule);

        return this.stream()
        .filter(lock -> lock.getTarget().equals(granule))
        .map(DBLockList.Lock::getType)
        .collect(Collectors.toList());
    }

    public List<DBLockList.Lock> getPendingLocksOn(DBGranule granule){
        this.assertParamsAreNotNull(granule);

        return this.pending.stream()
        .filter(lock -> lock.getTarget().equals(granule))
        .collect(Collectors.toList());
    }

    public boolean add(DBLockList.Lock lock){
        this.assertParamsAreNotNull(lock);

        DBGranule target = lock.getTarget();
        if(this.hasLockOn(target)){
            List<E_DBLockTypes> lockTypes = this.getLockTypesOn(target);
            boolean incompatible = lockTypes.stream()
            .filter(lockType -> lock.getType().compatibleWith(lockType))
            .collect(Collectors.toList())
            .isEmpty();

            if(!incompatible)
                return this.pending.add(lock);
        }
        return super.add(lock);
    }

    public boolean remove(DBLockList.Lock lock){
        this.assertParamsAreNotNull(lock);

        if(!this.contains(lock))
            return false;

        //this.assertParamsAreNotNull(lock);

        super.remove(lock);
        if(this.hasPendingOn(lock.getTarget())){
            this.getPendingLocksOn(lock.getTarget()).stream()
            .filter(pending -> !pending.getType().compatibleWith(lock.getType()))
            .forEach(this::add);

            //This will select the pending locks which were not compatible with the
            //lock that we're removing (means that the removed one was blocking)
            //and add those only to the execution queue
        }
        return true;//TMP

        //TODO: Fin de l'implémentation ;  si il y a des transactions en attente, les mettre dans la "file" d'exécution "in-place"
        //Done ?
    }


    @Override
    public String stringify(String tabLevel) {
        this.assertParamsAreNotNull(tabLevel);

        if(!I_Stringify.isTab(tabLevel))
            return I_Stringify.STRINGIFY_ERROR_MESSAGE;

        String ret = "";

        ret += tabLevel + "<DBLockList>\n";
        ret += tabLevel + "\t" + "locks: \n";
        ret += this.stream()
                .map(lock -> lock.stringify(tabLevel + "\t\t"))
                .reduce("", String::concat) + "\n";
        ret += tabLevel + "\t" + "pending: \n";
        ret += this.pending.stream()
                .map(lock -> lock.stringify(tabLevel + "\t\t"))
                .reduce("", String::concat) + "\n";
        ret += tabLevel + "</DBLockList>\n";

        return ret;
    }
}
