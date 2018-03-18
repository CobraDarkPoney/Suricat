package ladysnake.views;

import ladysnake.App;
import ladysnake.helpers.events.I_Observer;
import ladysnake.helpers.log.Logger;
import ladysnake.models.DBLockList;
import ladysnake.models.DBTransactionAction;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class LockStack extends JTable implements I_Observer{
    @Override
    public void handleEvent(String eventName, Object... args) {
        switch (eventName){
            case DBLockList.REPAINT:
                super.invalidate();
                super.repaint();
                super.revalidate();
                break;

            case DBLockList.RESET:
                this.model = getInitialTableModel();
                super.setModel(this.model);
                break;

            case DBLockList.ADD_LOCK:
                DBLockList.Lock lock = ((DBLockList.Lock) args[0]);
                String action = args[1].toString();
                this.addRowData(Operations.ADD, lock.getSource(), action, lock.getType().getName(), lock.getTarget().getName());
                super.setModel(this.model);
                break;

            case DBLockList.RM_LOCK:
                DBLockList.Lock lock_rm = ((DBLockList.Lock) args[0]);
                this.addRowData(Operations.RM, lock_rm.getSource(),  lock_rm.getType().getName(), lock_rm.getTarget().getName());
                super.setModel(this.model);
                break;

            case DBLockList.ADD_PENDING:
                DBTransactionAction transactionAction = ((DBTransactionAction) args[0]);
                this.addRowData(Operations.ADD_PENDING, transactionAction.getSource(), transactionAction.getActionType().getName(), transactionAction.getLock().getName(), transactionAction.getTarget().getName());
                super.setModel(this.model);
                break;

            case DBLockList.RM_PENDING:
                DBTransactionAction transactionAction_rm = ((DBTransactionAction) args[0]);
                this.addRowData(Operations.RM_PENDING, transactionAction_rm.getSource(), transactionAction_rm.getActionType().getName(), transactionAction_rm.getLock().getName(), transactionAction_rm.getTarget().getName());
                super.setModel(this.model);
                break;
            default:
                Logger.triggerEvent(Logger.ERROR, "Unhandled case : LockStack@handleEvent");
                break;
        }

        super.invalidate();
        super.repaint();
        super.revalidate();
    }

    public enum Operations{
        ADD(LockStack.ADD),
        RM(LockStack.RM),
        ADD_PENDING(LockStack.ADD_PENDING),
        RM_PENDING(LockStack.RM_PENDING),
        NONE(LockStack.DEFAULT);

        String name;
        Operations(String name){ this.name = name; }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public static class Lock{
        public Vector<String> getVector(){
            /*Operation, Source, type action, type verrou, cible*/
            Vector<String> v = new Vector<>();
            v.add(LockStack.Lock.this.getOperation().toString());
            v.add(LockStack.Lock.this.getSource());
            v.add(LockStack.Lock.this.getAction());
            v.add(LockStack.Lock.this.getLock());
            v.add(LockStack.Lock.this.getTarget());
            return v;
        }

        public Object[] getArray(){
            return new Object[]{
                    LockStack.Lock.this.getOperation().toString(),
                    LockStack.Lock.this.getSource(),
                    LockStack.Lock.this.getAction(),
                    LockStack.Lock.this.getLock(),
                    LockStack.Lock.this.getTarget()
            };
        }

        public String getSource() {
            return source;
        }

        public Lock setSource(String source) {
            this.source = source;
            return this;
        }

        public String getAction() {
            return action;
        }

        public Lock setAction(String action) {
            this.action = action;
            return this;
        }

        public String getLock() {
            return lock;
        }

        public Lock setLock(String lock) {
            this.lock = lock;
            return this;
        }

        public String getTarget() {
            return target;
        }

        public Lock setTarget(String target) {
            this.target = target;
            return this;
        }

        public Operations getOperation() {
            return operation;
        }

        public Lock setOperation(Operations operation) {
            this.operation = operation;
            return this;
        }

        protected String source, action, lock, target;

        protected Operations operation;

        public Lock(){
            this.operation = Operations.NONE;
            this.source = LockStack.DEFAULT;
            this.action = LockStack.DEFAULT;
            this.lock = LockStack.DEFAULT;
            this.target = LockStack.DEFAULT;
        }


    }

    protected DefaultTableModel model;
    protected List<LockStack.Lock> locks;

    public static DefaultTableModel getInitialTableModel(){
        return new DefaultTableModel(){
//            @Override
//            public int getRowCount() {
//                return LockStack.this.locks.size();
//            }

            @Override
            public int getColumnCount() {
                return LockStack.COLUMN_NAMES.length;
            }

            @Override
            public String getColumnName(int column) {
                return LockStack.COLUMN_NAMES[column];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public LockStack() throws IOException, FontFormatException {
        this.locks = new ArrayList<>();
        this.model = getInitialTableModel();
//        this.model.addRow(COLUMN_NAMES);
        super.setModel(this.model);
        super.setIntercellSpacing(new Dimension(SPACING, SPACING));
        super.setRowSorter(null);//Disable sorting for rows
        super.setShowHorizontalLines(true);
        super.setShowHorizontalLines(true);

        super.getTableHeader().setFont(HEADER_FONT);
        super.setFont(CELLS_FONT);
    }

    public LockStack addRowData(LockStack.Lock lock){
//        this.model.addRow(lock.getVector());
        this.locks.add(lock);
        this.model.addRow(lock.getArray());
        String log = Arrays.stream(lock.getArray()).map(o -> ((String) o)).collect(Collectors.joining(", "));
        this.setModel(this.model);
        Logger.triggerEvent(Logger.VERBOSE, "Adding lock: " + log);
        return this;
    }

    public LockStack addRowData(LockStack.Operations operation, String source, String action, String lockName, String target){
        LockStack.Lock lock = new Lock();
        lock.setOperation(operation)
        .setSource(source)
        .setAction(action)
        .setLock(lockName)
        .setTarget(target);
        return this.addRowData(lock);
    }

    public LockStack addRowData(LockStack.Operations operations, String source, String lockName, String target){
        LockStack.Lock lock = new Lock();
        lock.setOperation(operations)
        .setSource(source)
        .setLock(lockName)
        .setTarget(target);
        return this.addRowData(lock);
    }

    public final static String DEFAULT = " --- ";
    public final static String ADD = "Ajout du verrou";//"+";
    public final static String RM = "Retrait du verrou";//"-";
    public final static String ADD_PENDING = "Mise en attente";//"+@";
    public final static String RM_PENDING = "Fin d'attente";//"-@";
    public final static String[] COLUMN_NAMES = {"Opération", "Source", "Action", "Verrou", "Cible"};

    public final int SPACING = 12;

    public final float HEADER_FONTSIZE = 20f;
    public final float CELLS_FONTSIZE = 14f;

//    public final Font  HEADER_FONT = new Font("Arial", Font.BOLD, 20);
    public final Font  HEADER_FONT = Font.createFont(Font.TRUETYPE_FONT, LockStack.class.getResourceAsStream(App.ROBOTO_MEDIUM_PATH)).deriveFont(HEADER_FONTSIZE);
    public final Font CELLS_FONT = Font.createFont(Font.TRUETYPE_FONT, LockStack.class.getResourceAsStream(App.ROBOTO_PATH)).deriveFont(CELLS_FONTSIZE);
}
