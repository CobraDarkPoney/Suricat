package ladysnake.controllers;

import ladysnake.App;
import ladysnake.views.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class ExecutionController extends A_Controller {
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    public ExecutionController(A_View view) { super(view); }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void addListeners() {
        this.addListenersToPendingPanel(this.view.getViewPanel(), this.getManager());
    }

    protected void addListenersToPendingPanel(ViewPanel view, ViewsManager manager){
        this.assertParamsAreNotNull(view, manager);

       ViewPanel pendingPanel = view
        .<ViewPanel>getComponentAs(ExecutionView.RHS_PANEL)
        .getComponentAs(ExecutionView.PENDING_GRAPH_PANEL);

        pendingPanel
        .<JButton>getComponentAs("rect")
        .addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manager.switchTo(App.HOME_VIEW_TAG);
            }
        });
    }
}
