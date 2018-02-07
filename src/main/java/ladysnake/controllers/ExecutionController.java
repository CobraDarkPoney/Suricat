package ladysnake.controllers;

import ladysnake.App;
import ladysnake.models.DBTransactionExecution;
import ladysnake.views.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class ExecutionController extends A_Controller {
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    public ExecutionController(A_View view, ControllersManager cm) { super(view, cm); }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    protected DBTransactionExecution getExecution(){
        //TODO: Warning /!\ --> NullPointerException possible
        return this.getControllersManager().getModelsManager().getExecution().run();
    }

    public ExecutionController startExecution(){
        this.getExecution().run();
        return this;
    }

    public ExecutionController stopExecution(){
        this.getExecution().stop();
        return this;
    }


    @Override
    public void addListeners() {
        this.addListenersToPendingPanel(this.view.getViewPanel(), this.getViewsManager());
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
                try {
                    manager.switchTo(App.HOME_VIEW_TAG);
                } catch (UnsupportedLookAndFeelException e1) {
                    System.out.println("Could not update LookAndFeel");
                    e1.printStackTrace();
                }
            }
        });
    }
}
