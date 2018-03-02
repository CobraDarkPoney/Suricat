package ladysnake.models;

import ladysnake.helpers.events.A_Observable;
import ladysnake.helpers.log.Logger;

import java.util.List;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class DBTransactionExecution extends A_Observable{
    protected boolean isRunning;
    protected DBTransaction transaction;
    protected DBLockList lockList;
    protected List<DBTransactionAction> actionsBackup;
    protected int index;
    protected int max;

    public DBTransactionExecution(DBTransaction t){
        super();
        this.isRunning = false;
        this.transaction = t;
        this.lockList = new DBLockList(this);

        this.index = 0;
//        this.max = getAmount();
        this.max = -1;
        this.actionsBackup = null;

        this.registerEvent(STOP);
        this.registerEvent(INDEX);
        this.registerEvent(STEP_BEING_PROCESSED);
        this.registerEvent(RESET);
        this.registerEvent(REPAINT);
    }

    public boolean isRunning(){
        return this.isRunning;
    }

    public DBTransactionExecution willRun(boolean willRun){
        this.isRunning = willRun;
        return this;
    }

    public int getExecutionIndex(){
        return this.index;
    }

    public DBTransactionExecution run(){
        if(this.isRunning)
            return this;

        this.isRunning = true;
        List<DBTransactionAction> actions = this.transaction.getActions();
        actions.forEach(this::execute);
        this.stop();
        this.repaint();

        return this;
    }

    public DBTransactionExecution reset(){
//        if(!this.isRunning)
//            return this;

        Logger.triggerEvent(Logger.VERBOSE, "Resetting execution");

        this.isRunning = false;
        this.index = 0;
        this.transaction.getActionsStream()
        .forEach(DBTransactionAction::reset);

        this.getLockList().reset();
        this.trigger(RESET);
        this.repaint();
        return this;
    }

    public DBTransactionExecution repaint(){
        this.getLockList().repaint();
        this.trigger(REPAINT);
        return this;
    }

    public DBLockList getLockList() { return lockList; }

    public boolean runFromNow(){
        List<DBTransactionAction> actions = this.transaction.getActions();
        for(int i = this.getExecutionIndex(), length = actions.size(); i  < length ; i+=1)
            this.execute(actions.get(i));


        this.repaint();
        return true;
    }

    public boolean runUpTo(int index){
        List<DBTransactionAction> actions = this.transaction.getActions();
        if(index < 0 || index >= actions.size() || index <= this.getExecutionIndex())
            return false;

        for(int i = this.getExecutionIndex() ; i  < index ; i+=1)
            this.execute(actions.get(i));


        this.repaint();
        return this.getExecutionIndex() > actions.size();
    }

    public boolean next(){
        List<DBTransactionAction> actions = this.transaction.getActions();
        int index = this.getExecutionIndex();

        if(this.index >= actions.size())
            return false;

        this.execute( actions.get(index) );

        this.repaint();
        return this.index >= actions.size();
    }

    public void execute(DBTransactionAction action){ this.executeAction(action); }

    public DBTransactionAction executeAction(DBTransactionAction action) {
        Logger.triggerEvent(Logger.VERBOSE, "Executing action " + action.getSource() + "#" + action.getActionType().getName() + "@" + action.getTarget().getName());
        this.index = this.transaction.getActions().indexOf(action) + 1;
        this.trigger(DBTransactionExecution.INDEX, action.getIndex());
        this.trigger(DBTransactionExecution.STEP_BEING_PROCESSED, action);
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

      this.repaint();
      action.complete();
      return action;
    }

    public DBTransactionExecution stop(){
        if(this.isRunning)
            this.isRunning = false;

        Logger.triggerEvent(Logger.VERBOSE, "Stopping execution");
        this.trigger(STOP);

        return this;
    }

    public final static String EVENTS_BASENAME = "DBTransactionExecution@";
    public final static String STOP = EVENTS_BASENAME + "stop";
    public final static String INDEX = EVENTS_BASENAME + "index";
    public final static String STEP_BEING_PROCESSED = EVENTS_BASENAME + "stepProcessed";
    public final static String RESET = EVENTS_BASENAME + "reset";
    public final static String REPAINT = EVENTS_BASENAME + "repaint";
}
