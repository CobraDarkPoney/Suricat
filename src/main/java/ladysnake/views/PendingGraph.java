package ladysnake.views;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.view.mxGraph;
import ladysnake.helpers.events.I_Observer;
import ladysnake.helpers.log.Logger;
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
        .on(DBLockList.RM_PENDING, this)
        .on(DBLockList.RESET, this)
        .on(DBLockList.REPAINT, this);
        return this;
    }

    public Object getParent(){
        return this.getDefaultParent();
    }

    public void empty(){
        mxIGraphModel model = super.getModel();
        model.beginUpdate();
        try{
            this.vertices.values()
            .forEach(model::remove);

            this.edges.values()
            .forEach(model::remove);

            this.vertices.clear();
            this.edges.clear();
        }finally{
            model.endUpdate();
        }
    }

    public PendingGraph reset(){
//        Object[] cells = this.vertices.values().toArray();
//        Logger.triggerEvent(Logger.VERBOSE, "Resetting pending graph");
//        super.getModel().beginUpdate();
//        try{
//            super.removeCells(cells, true);
////            super.removeCells(super.getChildVertices(this.getParent()), true);
//        }finally{
//            super.getModel().endUpdate();
//        }
//        this.vertices.clear();
//        this.edges.clear();

        this.empty();

        return this;
    }

    public void handleRepaint(){
        super.refresh();
        super.repaint();
    }

    @Override
    public void handleEvent(String eventName, Object... args) {
        if(eventName.equals(DBLockList.RESET)){
            Logger.triggerEvent(Logger.VERBOSE, "Reset graph");
            this.reset();
            mxGraphLayout layout = new mxCircleLayout(this);
            layout.execute(getParent());
            super.getModel().beginUpdate();
            try{
                super.refresh();
                super.repaint();
                super.foldCells(true, true);
                super.repaint();
            }finally{
                super.getModel().endUpdate();
            }
            return;
        }

        if(eventName.equals(DBLockList.REPAINT)){
            Logger.triggerEvent(Logger.VERBOSE, "Repaint lock to graph");
            mxGraphLayout layout = new mxCircleLayout(this);
            layout.execute(getParent());
            super.refresh();
            super.repaint();
            super.foldCells(true, true);
            return;
        }

        if(!Arrays.asList(DBLockList.ADD_PENDING, DBLockList.RM_PENDING).contains(eventName))
            return;

        DBTransactionAction transactionAction = ((DBTransactionAction) args[0]);
        DBGranule targetGranule = transactionAction.getTarget();
        String target = targetGranule.getName();
        String type = transactionAction.getLock().getName();
        String source = transactionAction.getSource();
//        String blocking = this.getLockList().getLockOn(targetGranule, source).getSource();
        String blocking = this.getLockList().whoHasStrictestLockOn(targetGranule);
        String edge = PendingGraph.makeEdgeLabel(source, target, type, blocking);
        Object parent = getParent();
        Object sourceVertex, blockingVertex, insertedEdge;

        super.getModel().beginUpdate();
        try{
            switch (eventName){
                case DBLockList.ADD_PENDING:
                    Logger.triggerEvent(Logger.VERBOSE, "Adding pending lock to graph");
                    if(!this.vertices.containsKey(source)) {
//                        sourceVertex = super.insertVertex(parent, null, source, POS, POS, DIM, DIM);
                        sourceVertex = super.insertVertex(parent, source, source, POS, POS, DIM, DIM);
                        this.vertices.put(source, sourceVertex);
                    }else
                        sourceVertex = this.vertices.get(source);

                    if(!this.vertices.containsKey(blocking)) {
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
                    Logger.triggerEvent(Logger.VERBOSE, "Removing pending lock from graph");
                    List<Object> toRemove = new ArrayList<>();
                    Object vSource = this.vertices.getOrDefault(source, null);
                    Object vBlocking;

                    if(super.getOutgoingEdges(vSource).length == 0 && super.getIncomingEdges(vSource).length == 0)
                        toRemove.add(vSource);

                    vBlocking = this.vertices.getOrDefault(blocking, null);
                    if(super.getOutgoingEdges(vBlocking).length == 0 && super.getIncomingEdges(vBlocking).length == 0)
                        toRemove.add(vBlocking);


//                    super.getModel().beginUpdate();
//                    try{
                        super.removeCells(toRemove.toArray(), true);
//                    }finally{
//                        super.getModel().endUpdate();
//                    }
                    break;

                default:
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

    public static String makeEdgeLabel(String source, String target, String type, String blocking){
        return source + "#" + type + "@" + target + "#" + blocking;
    }

    public final static int POS = 100;
    public final static int DIM = 50;
}
