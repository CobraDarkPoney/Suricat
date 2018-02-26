package ladysnake.models;
import ladysnake.helpers.events.I_Observable;
import ladysnake.helpers.events.I_Observer;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.*;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class PendingGraph extends DirectedPseudograph<String, String> implements I_Observer, I_Observable {
    protected Map<String, List<I_Observer>> observers;
    protected DBLockList lockList;

    public PendingGraph(Class<? extends String> edgeClass, DBLockList lockList) {
        super(edgeClass);
        this.setupObservable();
        this.initLockList(lockList);
    }
    public PendingGraph(DBLockList lockList){ this(String.class, lockList); }
    public PendingGraph(){
        super(String.class);
        this.setupObservable();
    }
    public PendingGraph(EdgeFactory<String, String> ef, DBLockList lockList) {
        super(ef);
        this.initLockList(lockList);
        this.setupObservable();
    }
    public PendingGraph(EdgeFactory<String, String> ef, boolean weighted, DBLockList lockList) {
        super(ef, weighted);
        this.initLockList(lockList);
        this.setupObservable();
    }

    public void setupObservable(){
        this.observers = new HashMap<>();
    }

    @Override
    public Map<String, List<I_Observer>> getObservers() {
        return observers;
    }

    protected I_Observable registerEvent(String eventName){
        if(!this.eventIsRegistered(eventName))
            this.observers.put(eventName, new ArrayList<>());

        return this;
    }

    public DBLockList getLockList() { return lockList; }
    public PendingGraph setLockList(DBLockList lockList){
        this.initLockList(lockList);
        return this;
    }

    protected void initLockList(DBLockList lockList){
        this.lockList = lockList;
        this.lockList
        .on(DBLockList.ADD_PENDING, this)
        .on(DBLockList.RM_PENDING, this);
    }

    @Override
    public void handleEvent(String eventName, Object... args) {
        if(!Arrays.asList(DBLockList.ADD_PENDING, DBLockList.RM_PENDING).contains(eventName))
            return;

        DBTransactionAction transactionAction = ((DBTransactionAction) args[0]);
        DBGranule targetGranule = transactionAction.getTarget();
        String target = targetGranule.getName();
        String type = transactionAction.getLock().getName();
        String source = transactionAction.getSource();
        String blocking = this.getLockList().getLockOn(targetGranule, source).getSource();
        String edge = PendingGraph.makeEdgeLabel(target, type);

        switch (eventName){
            case DBLockList.ADD_PENDING:
                if(!this.containsVertex(source))
                    this.addVertex(source);
                if(!this.containsVertex(blocking))
                    this.addVertex(blocking);

                this.addEdge(source, blocking, edge);
                break;

            case DBLockList.RM_PENDING:
                this.removeEdge(edge);
                if(this.inDegreeOf(source)==0 && this.outDegreeOf(source)==0)
                    this.removeVertex(source);
                if(this.inDegreeOf(blocking)==0 && this.outDegreeOf(blocking)==0)
                    this.removeVertex(blocking);
                break;

            default:
                break;
        }

        this.trigger(REFRESH);
    }

    public static String makeEdgeLabel(String target, String type){
        return type + "@" + target;
    }

    public final static String REFRESH = "PendingGraph@refresh";
}
