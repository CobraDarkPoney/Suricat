package ladysnake.models;

import ladysnake.helpers.events.*;
import ladysnake.helpers.utils.I_MightNoNullParams;
import ladysnake.helpers.utils.I_Stringify;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class DBLockList extends ArrayList<DBLockList.Lock> implements I_MightNoNullParams, I_Stringify, I_Observable{
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

        public boolean stricterThan(DBLockList.Lock rhs){
          return this.getType().stricterThan(rhs.getType());
        }


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

    //protected List<DBLockList.Lock> pending;
    protected List<DBTransactionAction> pending;

    protected DBTransactionExecution execution;


    protected Map<String, List<I_Observer>> observers;

    public DBLockList(DBTransactionExecution execution){
        super();
        this.execution = execution;
        this.pending = new ArrayList<>();
        this.observers = new HashMap<>();

        this.registerEvent(DBLockList.ADD_LOCK)
        .registerEvent(DBLockList.RM_LOCK)
        .registerEvent(DBLockList.ADD_PENDING)
        .registerEvent(DBLockList.RM_PENDING);
    }

    protected DBLockList registerEvent(String eventName){
        if(!this.eventIsRegistered(eventName))
            this.getObservers().put(eventName, new ArrayList<>());

        return this;
    }

    public List<DBLockList.Lock> pendingLocks(){
      return this.pending.stream()
      .map(DBLockList.Lock::new)
      .collect(Collectors.toList());
    }

    public boolean hasLockOn(DBGranule granule){
        this.assertParamsAreNotNull(granule);

        return this.stream()
        .map(DBLockList.Lock::getTarget)
        .anyMatch(target -> target.equals(granule));
    }

    public boolean hasPendingOn(DBGranule granule){
        this.assertParamsAreNotNull(granule);

        return this.pendingLocks().stream()
        .anyMatch(wait -> wait.getTarget().equals(granule));
    }

    public boolean canProcessAction(DBLockList.Lock lock, int index) {
        String source = lock.getSource();
        return this.pendingLocks().stream()
        .noneMatch(dblock -> source.equals(dblock.getSource()))
        || this.pending.stream()
            .filter(action -> action.getSource().equals(source))
            .map(action -> action.index)
            .min(Integer::compareTo).orElse(-1) == index;
    }

    public List<DBTransactionAction> getPendings() { return pending; }

    public List<E_DBLockTypes> getLockTypesOn(DBGranule granule){
        this.assertParamsAreNotNull(granule);

        return this.stream()
        .filter(lock -> lock.getTarget().equals(granule))
        .map(DBLockList.Lock::getType)
        .collect(Collectors.toList());
    }

    public DBLockList.Lock getLockOn(DBGranule granule, String source){
      return this.stream()
//      .peek(e -> System.out.println(e.getSource()))
      .filter(lock -> lock.getSource().equals(source))
      .filter(lock -> lock.getTarget().equals(granule))
      .findFirst()
      .orElse(ERROR__GET_LOCK_ON);
    }

    public String whoHasLockOn(DBGranule granule){
        this.assertParamsAreNotNull(granule);
        return this.stream()
        .filter(lock -> lock.getTarget().equals(granule))
        .map(DBLockList.Lock::getSource)
        .findFirst()
        .orElse(ERROR__WHO_HAS_LOCK_ON);
    }

    public String whoHasStrictestLockOn(DBGranule granule){
        this.assertParamsAreNotNull(granule);
        return this.stream()
        .filter(lock -> lock.getTarget().equals(granule))
        .sorted((lhs, rhs) ->{
            if(lhs.stricterThan(rhs))
                return -1;
            else if(rhs.stricterThan(lhs))
                return 1;
            else
                return 0;
        }).map(DBLockList.Lock::getSource)
        .findFirst()
        .orElse(ERROR__WHO_HAS_STRICTEST_LOCK_ON);

    }

    public List<DBLockList.Lock> getPendingLocksOn(DBGranule granule){
        this.assertParamsAreNotNull(granule);

        return this.getPendingActionsOn(granule).stream()
        .map(DBLockList.Lock::new)
        .collect(Collectors.toList());
    }

    public List<DBTransactionAction> getPendingActionsOn(DBGranule granule){
        this.assertParamsAreNotNull(granule);
        return this.pending.stream()
        .filter(lock -> lock.getTarget().equals(granule))
        .collect(Collectors.toList());
    }

    public boolean add(DBTransactionAction action){
        this.assertParamsAreNotNull(action);
        DBLockList.Lock lock = new DBLockList.Lock(action);
        DBGranule target = lock.getTarget();
        DBLockList.Lock existingLock = this.getLockOn(target, lock.getSource());

        if(!lock.stricterThan(existingLock))
            return true;

        if(this.hasLockOn(target)){
            List<E_DBLockTypes> lockTypes = this.getLockTypesOn(target);
            boolean incompatible = lockTypes.stream()
            .anyMatch(lockType -> !lock.getType().compatibleWith(lockType));

            if(incompatible){
                this.addPending(action);
                return false;
            }
        }
        if(!existingLock.getType().equals(E_DBLockTypes.NONE))
            this.remove(existingLock);

        super.add(lock);
        this.trigger(DBLockList.ADD_LOCK, lock, action.getActionType().getName());
        return true;
    }

    public boolean addPending(DBTransactionAction action){
        boolean ret = this.pending.add(action);
        this.trigger(DBLockList.ADD_PENDING, action);
        return ret;
    }

    public boolean removePending(DBTransactionAction action){
        boolean ret = this.pending.remove(action);
        this.trigger(DBLockList.RM_PENDING, action);
        return ret;
    }

    public boolean remove(DBLockList.Lock lock){
        this.assertParamsAreNotNull(lock);
//        if(lock.getType().isExclusive())
//            throw new RuntimeException("Ouch :@ !");

        if(!this.contains(lock))
            return false;

        //this.assertParamsAreNotNull(lock);

        super.remove(lock);
        this.trigger(DBLockList.RM_LOCK, lock);
        //this.executePendings(lock);
        return true;//TMP

        //TODO: Fin de l'implémentation ;  si il y a des transactions en attente, les mettre dans la "file" d'exécution "in-place"
        //Done ?
    }

    public boolean removeAll(DBLockList.Lock lock){
      List<DBLockList.Lock> toRemove = this.stream()
      .filter(dblock -> dblock.getSource().equals(lock.getSource()))
      .collect(Collectors.toList());

      boolean ret  = toRemove.stream()
      .map(this::remove)
      .reduce(true, Boolean::logicalAnd);

      this.executePendings();

      return ret;
    }

    public DBLockList executePendings(DBLockList.Lock lock){
      if(this.hasPendingOn(lock.getTarget())){
          this.getPendingActionsOn(lock.getTarget())
          .forEach(execution::execute);

          //This will select the pending locks which were not compatible with the
          //lock that we're removing (means that the removed one was blocking)
          //and add those only to the execution queue
      }

      return this;
    }

    public DBLockList executePendings(){
        List<DBTransactionAction> pendingCopy = new ArrayList<>(this.pending);
        List<DBTransactionAction> toRemove = pendingCopy.stream()
        .map(execution::executeAction)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
        toRemove.forEach(this::removePending); //trigger event
        return this;

        //ajouté avant d'être retiré de la liste d'attente, problématique ?
    }

    @Override
    public Map<String, List<I_Observer>> getObservers() {
        return this.observers;
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

    public final static String ADD_LOCK = "DBLockList@add";
    public final static String RM_LOCK = "DBLockList@remove";
    public final static String ADD_PENDING = "DBLockList@add.pending";
    public final static String RM_PENDING = "DBLockList@remove.pending";

    public final static String ERROR__WHO_HAS_LOCK_ON = "ERROR#DBLockList@whoHasLockOn";
    public final static String ERROR__WHO_HAS_STRICTEST_LOCK_ON = "ERROR#DBLockList@whoHasStrictestLockOn";
    public final static DBLockList.Lock ERROR__GET_LOCK_ON = new DBLockList.Lock("ERROR#DBLockList@getLockOn", null, E_DBLockTypes.NONE);
}
