package ladysnake.models;

import ladysnake.helpers.utils.I_MightNoNullParams;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"WeakerAccess", "unused"})
public class DBLockList extends ArrayList<DBLockList.Lock> implements I_MightNoNullParams{
    public class Lock{
        protected String source;
        protected DBGranule target;
        protected E_DBLockTypes type;

        ////////////////////////////////////////////////////////////////////////////////////////////
        ////Constructors
        ////////////////////////////////////////////////////////////////////////////////////////////
        public Lock(String source, DBGranule target, E_DBLockTypes type){
            this.source = source;
            this.target = target;
            this.type = type;
        }



        ////////////////////////////////////////////////////////////////////////////////////////////
        ////Methods
        ////////////////////////////////////////////////////////////////////////////////////////////
        public String getSource(){ return this.source; }

        public DBGranule getTarget() { return this.target; }

        public E_DBLockTypes getType(){ return this.type; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Lock)) return false;

            Lock lock = (Lock) o;

            return getSource().equals(lock.getSource())
                    && getTarget().equals(lock.getTarget())
                    && getType() == lock.getType();
        }

        @Override
        public int hashCode() {
            int result = getSource().hashCode();
            result = 31 * result + getTarget().hashCode();
            result = 31 * result + getType().hashCode();
            return result;
        }
    }

    protected List<DBLockList.Lock> waiting;

    public DBLockList(){
        super();
        this.waiting = new ArrayList<>();
    }

    public boolean hasLockOn(DBGranule granule){
        this.assertParamsAreNotNull(granule);

        return this.stream()
        .map(DBLockList.Lock::getTarget)
        .filter(target -> target.equals(granule))
        .collect(Collectors.toList())
        .size() != 0;
    }

    public boolean hasWaitingOn(DBGranule granule){
        this.assertParamsAreNotNull(granule);

        return this.waiting.stream()
        .filter(wait -> wait.getTarget().equals(granule))
        .count() != 0;
    }

    public List<E_DBLockTypes> getLockTypesOn(DBGranule granule){
        this.assertParamsAreNotNull(granule);

        return this.stream()
        .filter(lock -> lock.getTarget().equals(granule))
        .map(DBLockList.Lock::getType)
        .collect(Collectors.toList());
    }

    public List<DBLockList.Lock> getWaitingLocksOn(DBGranule granule){
        this.assertParamsAreNotNull(granule);

        return this.waiting.stream()
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
                return this.waiting.add(lock);
        }
        return super.add(lock);
    }

    public boolean remove(DBLockList.Lock lock){
        if(!this.contains(lock))
            return false;

        this.assertParamsAreNotNull(lock);

        super.remove(lock);
        if(this.hasWaitingOn(lock.getTarget())){
            this.getWaitingLocksOn(lock.getTarget())
            .forEach(this::add);
        }
        return true;//TMP

        //TODO: Fin de l'implémentation ;  si il y a des transactions en attente, les mettre dans la "file" d'exécution "in-place"
        //Done ?
    }
}
