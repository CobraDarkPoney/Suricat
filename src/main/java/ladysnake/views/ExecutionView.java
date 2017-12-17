package ladysnake.views;

import javax.swing.*;
import java.awt.*;

/**An {@link A_View} dedicated to the execution of the transactions
 */
@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class ExecutionView extends A_View {
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected ViewPanel setUp() {
        ViewPanel panel = new ViewPanel();
        panel.setLayout(new GridLayout(GRID_ROWS, GRID_COLS, GRID_SPACING, GRID_SPACING));
        panel.addComponent(TRANSACTION_PANEL, this.getTransactionPanel());
        panel.addComponent(RHS_PANEL, this.getRhsPanel());

        panel.<ViewPanel>getComponentAs(RHS_PANEL)
        .addComponent(LOCK_STACK_PANEL, this.getLockStackPanel())
        .addComponent(WAITING_GRAPH_PANEL, this.getWaitingGraphPanel());

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
        p.setLayout(new GridLayout());
        p.addComponent("rect", new JButton(TRANSACTION_PANEL));
        return p;
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

    /**Retrieves the {@link ViewPanel} the constitutes this {@link A_View}'s waiting graph panel
     * @return the constructed {@link ViewPanel}
     */
    protected ViewPanel getWaitingGraphPanel(){
        ViewPanel p =new ViewPanel();
        p.setLayout(new GridLayout());
        p.addComponent("rect", new JButton(WAITING_GRAPH_PANEL));
        return p;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    protected static int GRID_ROWS = 1;
    protected static int GRID_COLS = 2;
    protected static int GRID_SPACING = 10;

    protected static int RHS_ROWS = 2;
    protected static int RHS_COLS = 1;

    protected static String TRANSACTION_PANEL = "Transaction";
    protected static String RHS_PANEL = "rhs-panel";
    protected static String LOCK_STACK_PANEL = "Locks";
    protected static String WAITING_GRAPH_PANEL = "Waitings";
}
