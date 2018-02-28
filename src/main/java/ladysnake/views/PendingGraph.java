package ladysnake.views;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.view.mxGraph;
import ladysnake.helpers.events.I_Observer;
import ladysnake.models.DBGranule;
import ladysnake.models.DBLockList;
import ladysnake.models.DBTransactionAction;

import java.util.*;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class PendingGraph extends mxGraph implements I_Observer{
    protected DBLockList lockList;
    protected Map<String, Object> vertices = new HashMap<>();
    protected Map<String, Object> edges = new HashMap<>();

    public PendingGraph(DBLockList lockList){
        this.setupLockList(lockList);
        setupRules();
    }
    public PendingGraph(){ setupRules(); }

    public void setupRules(){
        super.allowLoops = false;
        super.allowDanglingEdges = false;

        super.cellsBendable = false;
        super.cellsCloneable = false;
        super.cellsDisconnectable = false;
        super.cellsDeletable = false;
        super.cellsEditable = false;

        super.cellsMovable = true;
        super.cellsResizable = false;
        super.multigraph = true;

//        super.edgeLabelsMovable = false;
        super.connectableEdges = false;
        super.cloneInvalidEdges = false;

        super.allowNegativeCoordinates = true;
        super.enabled = true;
        super.constrainChildren = true;
    }

    public DBLockList getLockList(){ return this.lockList; }

    public PendingGraph setLockList(DBLockList lockList){ return this.setupLockList(lockList); }

    protected PendingGraph setupLockList(DBLockList lockList){
        this.lockList = lockList;
        this.lockList
        .on(DBLockList.ADD_PENDING, this)
        .on(DBLockList.RM_PENDING, this);
        return this;
    }

    public Object getParent(){
        return this.getDefaultParent();
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
//        String blocking = this.getLockList().getLockOn(targetGranule, source).getSource();
        String blocking = this.getLockList().whoHasStrictestLockOn(targetGranule);
        String edge = PendingGraph.makeEdgeLabel(target, type);
        Object parent = getParent();
        Object sourceVertex=null, blockingVertex=null, insertedEdge=null;

        super.getModel().beginUpdate();
        try{
            switch (eventName){
                case DBLockList.ADD_PENDING:
                    if(!this.vertices.containsKey(source)) {
                        System.out.println("Source doesn't exist !");
                        System.out.println("Name: " + source);
//                        sourceVertex = super.insertVertex(parent, null, source, POS, POS, DIM, DIM);
                        sourceVertex = super.insertVertex(parent, source, source, POS, POS, DIM, DIM);
                        this.vertices.put(source, sourceVertex);
                    }else
                        sourceVertex = this.vertices.get(source);

                    if(!this.vertices.containsKey(blocking)) {
                        System.out.println("Blocking doesn't exist !");
                        System.out.println("Name: " + blocking);
                        blockingVertex = super.insertVertex(parent, blocking, blocking, POS, POS, DIM, DIM);
//                        blockingVertex = super.insertVertex(parent, null, blocking, POS, POS, DIM, DIM);
                        this.vertices.put(blocking, blockingVertex);
                    }else
                        blockingVertex = this.vertices.get(blocking);

                    if(!this.edges.containsKey(edge)) {
                        insertedEdge = super.insertEdge(parent, edge, edge, sourceVertex, blockingVertex, null);
                        this.edges.put(edge, insertedEdge);
                    }
                    break;

                case DBLockList.RM_PENDING:
                    List<Object> toRemove = new ArrayList<>();
                    Object vSource = this.vertices.getOrDefault(source, null);
                    Object vBlocking;

                    if(super.getOutgoingEdges(vSource).length == 0 && super.getIncomingEdges(vSource).length == 0)
                        toRemove.add(vSource);

                    vBlocking = this.vertices.getOrDefault(blocking, null);
                    if(super.getOutgoingEdges(vBlocking).length == 0 && super.getIncomingEdges(vBlocking).length == 0)
                        toRemove.add(vBlocking);


                    super.removeCells(toRemove.toArray(), true);
                    break;
            }
        }finally{
            super.getModel().endUpdate();
        }

//        mxGraphLayout layout = new mxCompactTreeLayout(this);
        mxGraphLayout layout = new mxCircleLayout(this);
        layout.execute(getParent());
        super.refresh();
        super.repaint();
        super.foldCells(true, true);

//        switch (eventName){
//            case DBLockList.ADD_PENDING:
//                if(!this.containsVertex(source))
//                    this.addVertex(source);
//                if(!this.containsVertex(blocking))
//                    this.addVertex(blocking);
//
//                this.addEdge(source, blocking, vertices);
//                break;
//
//            case DBLockList.RM_PENDING:
//                this.removeEdge(vertices);
//                if(this.inDegreeOf(source)==0 && this.outDegreeOf(source)==0)
//                    this.removeVertex(source);
//                if(this.inDegreeOf(blocking)==0 && this.outDegreeOf(blocking)==0)
//                    this.removeVertex(blocking);
//                break;
//
//            default:
//                break;
//        }
    }

    public static String makeEdgeLabel(String target, String type){
        return type + "@" + target;
    }

    public final static int POS = 100;
    public final static int DIM = 50;
}
