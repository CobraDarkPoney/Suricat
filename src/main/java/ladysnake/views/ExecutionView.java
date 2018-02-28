package ladysnake.views;


import com.mxgraph.swing.mxGraphComponent;
import ladysnake.helpers.utils.Range;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**An {@link A_View} dedicated to the execution of the transactions
 */
@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class ExecutionView extends A_View{
    protected LockStack lockStack;
    protected PendingGraph pendingGraph;
    protected TimelineHub timelineHub;

    /**
     * @see A_View#A_View(ViewsManager)
     */
    public ExecutionView(ViewsManager manager) throws IOException, FontFormatException {
        super(manager);
        if(this.getLockStack() == null)
            this.lockStack = new LockStack();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    public LockStack getLockStack() {
        return this.lockStack;
    }

//    public ExecutionView setTimelineHub(TimelineHub t){
//        this.timelineHub = t;
//        return this;
//    }

    @Override
    protected ViewPanel setUp() {
        try {
            if(this.getLockStack() == null)
                this.lockStack = new LockStack();

            this.timelineHub = new TimelineHub();
        } catch (IOException|FontFormatException e) {
            e.printStackTrace();
        }

        ViewPanel panel = new ViewPanel();
        panel.setLayout(new GridLayout(GRID_ROWS, GRID_COLS, GRID_SPACING, GRID_SPACING));
        panel.addComponent(TRANSACTION_PANEL, this.getTransactionPanel())
        .addComponent(RHS_PANEL, this.getRhsPanel());

        panel.<ViewPanel>getComponentAs(RHS_PANEL)
        .addComponent(LOCK_STACK_PANEL, this.getLockStackPanel())
        .addComponent(PENDING_GRAPH_PANEL, this.getPendingGraphPanel());

        return panel;
    }

    @Override
    public String getViewTitle() {
        return "Suricat - Exécution";
    }

    /**Retrieves the {@link ViewPanel} the constitutes this {@link A_View}'s right hand side panel
     * @return the constructed {@link ViewPanel}
     */
    protected ViewPanel getRhsPanel(){
        ViewPanel p = new ViewPanel();
        p.setLayout(new GridLayout(RHS_ROWS, RHS_COLS, GRID_SPACING, GRID_SPACING));
        return p;
    }

    /**Retrieves the {@link ViewPanel} the constitutes this {@link A_View}'s left hand side panel
     * @return the constructed {@link ViewPanel}
     */
    protected ViewPanel getTransactionPanel(){
        ViewPanel p = new ViewPanel();
        p.setLayout(new GridLayout(1,1));
//        ViewPanel innerPanel = new ViewPanel();
        ViewPanel innerPanel = this.timelineHub;
        p.addComponent(SCROLLPANE, new JScrollPane(innerPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        return p;
    }

    protected ScrollPaneLayout getNewBothScrollingLayout(){
        ScrollPaneLayout scrollPaneLayout = new ScrollPaneLayout();
        scrollPaneLayout.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneLayout.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPaneLayout;
    }

    /**Retrieves the {@link ViewPanel} the constitutes this {@link A_View}'s lock stack panel
     * @return the constructed {@link ViewPanel}
     */
    protected ViewPanel getLockStackPanel(){
        ViewPanel p = new ViewPanel();
        p.setLayout(new GridLayout());
//        p.addComponent(LOCK_STACK, lockStack);
        ViewPanel innerPanel = new ViewPanel();
        innerPanel.setLayout(new GridBagLayout());
        innerPanel.addComponent(LOCK_STACK, this.getLockStack());
        JScrollPane scrollPane = new JScrollPane(innerPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(this.getLockStack());
        p.addComponent(LOCK_STACK, scrollPane);
        return p;
    }

    /**Retrieves the {@link ViewPanel} the constitutes this {@link A_View}'s pending graph panel
     * @return the constructed {@link ViewPanel}
     */
    protected ViewPanel getPendingGraphPanel(){
        ViewPanel p =new ViewPanel();
        p.setLayout(new GridLayout());
//        this.pendingGraph = new PendingGraph();
//        VisualPendingGraph graph = new VisualPendingGraph(this.pendingGraph);
//        this.graphComponent = new mxGraphComponent(graph);
////        JScrollPane scrollPane = new JScrollPane(this.graphComponent, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        p.addComponent(PENDING_GRAPH, this.graphComponent);
        this.pendingGraph = new PendingGraph();
        mxGraphComponent graphComponent = new mxGraphComponent(this.pendingGraph);
        p.addComponent(PENDING_GRAPH, graphComponent);
        return p;
    }

    public PendingGraph getPendingGraph() {
        return pendingGraph;
    }

    public TimelineHub getTimelineHub() {
        return timelineHub;
    }

    @Override
    public JMenuBar getViewMenuBar() { return null; }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    public final static int GRID_ROWS = 1;
    public final static int GRID_COLS = 2;
    public final static int GRID_SPACING = 10;

    public final static int RHS_ROWS = 2;
    public final static int RHS_COLS = 1;

    public final static String TRANSACTION_PANEL = "Transaction";
    public final static String RHS_PANEL = "rhs-panel";
    public final static String LOCK_STACK_PANEL = "Locks";
    public final static String PENDING_GRAPH_PANEL = "Pendings";
    public final static String LOCK_STACK = "lock-stack";
    public final static String LOCK_STACK_INNER = "stack-inner";

    public final static String PENDING_GRAPH = "ExecutionController@pendingGraph";
    public final static String TIMELINE_HUB = "ExecutionController@timelineHub";
    public final static String SCROLLPANE = "scroll";
}
