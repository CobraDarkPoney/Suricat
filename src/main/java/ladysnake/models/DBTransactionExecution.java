package ladysnake.models;

import ladysnake.helpers.events.A_Observable;

import java.util.List;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class DBTransactionExecution extends A_Observable{
    protected boolean isRunning;
    protected DBTransaction transaction;
    protected DBLockList lockList;

    public DBTransactionExecution(DBTransaction t){
        super();
        this.isRunning = false;
        this.transaction = t;
        this.lockList = new DBLockList(this);

        this.registerEvent(STOP);
        this.registerEvent(INDEX);
    }

    public DBTransactionExecution run(){
        if(this.isRunning)
            return this;

        this.isRunning = true;
        List<DBTransactionAction> actions = this.transaction.getActions();
        actions.forEach(this::execute);
        this.stop();

        return this;
    }

    public DBLockList getLockList() { return lockList; }

    public void execute(DBTransactionAction action){ this.executeAction(action); }

    public DBTransactionAction executeAction(DBTransactionAction action) {
        this.trigger(DBTransactionExecution.INDEX, action.index);
        if(!this.isRunning)
            return null;

        if(this.transaction.hasFailed(action.getSource())) {
            return null;
          }

      DBLockList.Lock lock = new DBLockList.Lock(action);
      boolean canProcess = this.lockList.canProcessAction(lock, action.index);
      if(!canProcess){
        this.lockList.addPending(action);
        return null;
      }

      E_DBTransactionActionTypes actionType = action.getActionType();

      if(
        actionType.equals(E_DBTransactionActionTypes.commit)
        || actionType.equals(E_DBTransactionActionTypes.rollback)
      )
        this.lockList.removeAll(lock);
      else {
          if(!lock.getType().equals(E_DBLockTypes.NONE)) {
            if(!this.lockList.add(action))
              return null;
        }

        DBLockList.Lock realLock = this.lockList.getLockOn(action.getTarget(), action.getSource());

        if(realLock.getType().isUpdate()) {
          if(actionType.equals(E_DBTransactionActionTypes.upd)) {
            action.setLock(realLock.getType().equals(E_DBLockTypes.U) ? E_DBLockTypes.X : E_DBLockTypes.XE);
            if(!this.lockList.add(action))
              return null;
          }
        }

        if(realLock.getType().isNone()) {
          DBGranule granule = action.getTarget();
          List<E_DBLockTypes> lockTypes = lockList.getLockTypesOn(granule);
          boolean matches = lockTypes.stream()
          .map(E_DBLockTypes::getOtherPerms)
          .allMatch(perms -> perms.matches(actionType));
          if(!matches) {
            this.lockList.addPending(action);
            return null;
          }
        } else {
          if(!realLock.getType().getSelfPerms().matches(action.getActionType())) {
            this.transaction.setFailed(action.getSource());
            this.lockList.removeAll(realLock);
            return null;
          }
        }

        if(!realLock.getType().isExtended()) {
          this.lockList.remove(realLock);
        }
      }

      action.complete();
      return action;
    }

    public DBTransactionExecution stop(){
        if(this.isRunning)
            this.isRunning = false;

        this.trigger(STOP);

        return this;
    }

    public static final String STOP = "DBTransactionExecution@stop";
    public static final String INDEX = "DBTransactionExecution@index";
}
