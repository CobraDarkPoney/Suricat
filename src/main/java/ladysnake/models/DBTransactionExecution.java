package ladysnake.models;

import java.util.List;

@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class DBTransactionExecution {
    protected boolean isRunning;
    protected DBTransaction transaction;
    protected DBLockList lockList;

    public DBTransactionExecution(DBTransaction t){
        this.isRunning = false;
        this.transaction = t;
        this.lockList = new DBLockList();
    }

    public DBTransactionExecution run(){
        if(this.isRunning)
            return this;

        this.isRunning = true;
        List<DBTransactionAction> actions = this.transaction.actions;
        for(DBTransactionAction action : actions){
            //TODO: handle actions
        }

        return this;
    }

    public DBTransactionExecution stop(){
        if(this.isRunning)
            this.isRunning = false;

        return this;
    }
}
