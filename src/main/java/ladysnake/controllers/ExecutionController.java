package ladysnake.controllers;

import ladysnake.App;
import ladysnake.models.DBLockList;
import ladysnake.models.DBTransactionExecution;
import ladysnake.views.*;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class ExecutionController extends A_Controller{
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    public ExecutionController(A_View view, ControllersManager cm) {
        super(view, cm);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    protected DBTransactionExecution getExecution(){
        //TODO: Warning /!\ --> NullPointerException possible
        return this.getControllersManager().getModelsManager().getExecution(); //.run()
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
//        this.addListenersToPendingPanel(this.view.getViewPanel(), this.getViewsManager());
//        this.addListenersToLockStackPanel(this.view.getViewPanel(), this.getViewsManager());
    }

    protected void addListenersToLockStackPanel(ViewPanel view, ViewsManager manager){
        this.assertParamsAreNotNull(view, manager);
        LockStack lockStack = ((ExecutionView) manager.getView(App.EXECUTION_VIEW_TAG)).getLockStack();

        this.getExecution().getLockList()
        .on(DBLockList.ADD_LOCK, lockStack)
        .on(DBLockList.RM_LOCK, lockStack)
        .on(DBLockList.ADD_PENDING, lockStack)
        .on(DBLockList.RM_PENDING, lockStack);
    }

    protected void addListenersToPendingPanel(ViewPanel view, ViewsManager manager){
        this.assertParamsAreNotNull(view, manager);

       ViewPanel pendingPanel = view
        .<ViewPanel>getComponentAs(ExecutionView.RHS_PANEL)
        .getComponentAs(ExecutionView.PENDING_GRAPH_PANEL);

//        pendingPanel
//        .<JButton>getComponentAs("rect")
//        .addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                try {
//                    manager.switchTo(App.HOME_VIEW_TAG);
//                } catch (UnsupportedLookAndFeelException e1) {
//                    System.out.println("Could not update LookAndFeel");
//                    e1.printStackTrace();
//                }
//            }
//        });

        ExecutionView executionView = ((ExecutionView) this.getViewsManager().getView(App.EXECUTION_VIEW_TAG));
        executionView.getPendingGraph().setLockList( this.getExecution().getLockList() );

//        executionView.setTimelineHub(timelineHub);
    }

    protected void addListenersToTimelines(){
        ExecutionView executionView = ((ExecutionView) this.getViewsManager().getView(App.EXECUTION_VIEW_TAG));
        TimelineHub timelineHub = executionView.getTimelineHub();
        TimelineHub.EVENTS_HANDLED.forEach(eventName -> this.getExecution().on(eventName, timelineHub));
        List<String> sources = this.getControllersManager().getModelsManager().getSources();
        try {
            for(String source : sources)
                timelineHub.addTimelineFor(source);
        } catch (FontFormatException|IOException e) {
            e.printStackTrace();
        }
    }

    public final static String SWITCH = "ExecutionController@switch";
}
