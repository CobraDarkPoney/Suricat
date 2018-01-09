package ladysnake.views;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class HomeView extends A_View{
    /**
     * @see A_View#A_View(ViewsManager)
     */
    public HomeView(ViewsManager manager) {
        super(manager);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @see A_View#setUp()
     */
    @Override
    protected ViewPanel setUp() {
        ViewPanel panel = new ViewPanel();
        panel.setLayout(new GridLayout(GRID_ROWS, GRID_COLS, GRID_SPACING, GRID_SPACING));
        panel.addComponent(LOGO_PANEL, this.getLogoPanel())
        .addComponent(RHS_PANEL, this.getRhsPanel());
        return panel;
    }

    /**
     * @see A_View#getViewTitle()
     */
    @Override
    public String getViewTitle() {
        return "home";
    }

    protected ViewPanel getLogoPanel(){
        ViewPanel logoPanel = new ViewPanel();
        logoPanel.setLayout(new GridLayout(LOGO_ROWS, LOGO_COLS, LOGO_SPACING, LOGO_SPACING));
        return logoPanel;
    }

    protected ViewPanel getRhsPanel(){
        ViewPanel rhsPanel = new ViewPanel();
        rhsPanel.setLayout(new GridLayout(RHS_ROWS, RHS_COLS, RHS_SPACING, RHS_SPACING));
        rhsPanel.addComponent("btn", new JButton("Go Transaction"));
        return rhsPanel;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    public final static int GRID_ROWS = 1;
    public final static int GRID_COLS = 2;

    public final static int GRID_SPACING = 10;

    public final static String LOGO_PANEL = "logo";
    public final static String RHS_PANEL = "rhs";

    public final static int RHS_ROWS = 2;
    public final static int RHS_COLS = 1;
    public final static int RHS_SPACING = GRID_SPACING;

    public final static int LOGO_ROWS = 1;
    public final static int LOGO_COLS = 1;
    public final static int LOGO_SPACING = GRID_SPACING;
}
