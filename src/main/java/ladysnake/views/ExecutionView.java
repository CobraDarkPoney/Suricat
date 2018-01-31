package ladysnake.views;

import ladysnake.helpers.utils.Range;

import javax.swing.*;
import java.awt.*;

/**An {@link A_View} dedicated to the execution of the transactions
 */
@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class ExecutionView extends A_View{
    /**
     * @see A_View#A_View(ViewsManager)
     */
    public ExecutionView(ViewsManager manager) {
        super(manager);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected ViewPanel setUp() {
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
        return "Exécution";
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
        ViewPanel innerPanel = new ViewPanel();
        for(char c : Range.make('a', 'z'))
            innerPanel.addComponent(TRANSACTION_PANEL + "_" + c, new JButton(TRANSACTION_PANEL+ "_" + c));

        final String SCROLLPANE = "scroll";
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
        p.addComponent("rect", new JButton(LOCK_STACK_PANEL));
        return p;
    }

    /**Retrieves the {@link ViewPanel} the constitutes this {@link A_View}'s pending graph panel
     * @return the constructed {@link ViewPanel}
     */
    protected ViewPanel getPendingGraphPanel(){
        ViewPanel p =new ViewPanel();
        p.setLayout(new GridLayout());
        p.addComponent("rect", new JButton(PENDING_GRAPH_PANEL));
        return p;
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
}
