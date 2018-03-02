package ladysnake.controllers;

import com.mxgraph.swing.mxGraphComponent;
import ladysnake.App;
import ladysnake.helpers.log.Logger;
import ladysnake.models.DBLockList;
import ladysnake.models.DBTransactionExecution;
import ladysnake.views.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;
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
//        this.addListenersToMenuBar(this.view.getViewPanel(), this.getViewsManager());
//        this.addListenersToPendingPanel(this.view.getViewPanel(), this.getViewsManager());
//        this.addListenersToLockStackPanel(this.view.getViewPanel(), this.getViewsManager());
    }

    protected void addKeyboardShortcuts(){
        ViewPanel viewPanel = this.view.getViewPanel();
        viewPanel.requestFocus();
//        Arrays.stream(viewPanel.getComponents())
//        .filter(c -> c instanceof JButton)
//        .map(c -> ((JButton) c))
//        .forEach(JButton::doClick);


        viewPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Logger.triggerEvent(Logger.VERBOSE, "Keypressed: " + e.getKeyChar());
                ExecutionController that = ExecutionController.this;
                MenuBarBuilder builder = ExecutionMenuBar.getBuilder();

                switch (e.getKeyCode()){
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        builder.getMenu(ExecutionMenuBar.MENU__STEP_FORWARD).doClick();
                        that.stepForwardLogic();
                        break;

                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_Q:
                        builder.getMenu(ExecutionMenuBar.MENU__STEP_BACKWARD).doClick();
                        that.stepBackwardLogic();
                        break;

                    case KeyEvent.VK_X:
                    case KeyEvent.VK_R:
                        builder.getMenu(ExecutionMenuBar.MENU__RESET).doClick();
                        that.resetLogic();
                        break;

                    case KeyEvent.VK_TAB:
                    case KeyEvent.VK_ENTER:
                        builder.getMenu(ExecutionMenuBar.MENU__RUN).doClick();
                        that.runLogic();
                        break;

                    default:
                        break;
                }
            }
        });
    }

    protected void addListenersToMenuBar(ViewPanel viewPanel, ViewsManager manager){
        Logger.triggerEvent(Logger.VERBOSE, "Adding listeners to ExecutionController's JMenuBar");

        MenuBarBuilder builder = ExecutionMenuBar.getBuilder();

        MouseListener reset = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ExecutionController.this.resetLogic();
            }
        };

        MouseListener next = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ExecutionController.this.stepForwardLogic();
            }
        };

        MouseListener prev = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ExecutionController.this.stepBackwardLogic();
            }
        };

        MouseListener play = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ExecutionController.this.runLogic();
            }
        };


        builder.getMenu(ExecutionMenuBar.MENU__RESET)
         .addMouseListener(reset);

        builder.getMenu(ExecutionMenuBar.MENU__RUN)
        .addMouseListener(play);

        builder.getMenu(ExecutionMenuBar.MENU__STEP_FORWARD)
        .addMouseListener(next);

        builder.getMenu(ExecutionMenuBar.MENU__STEP_BACKWARD)
        .addMouseListener(prev);
    }

    protected void resetLogic(){
        MenuBarBuilder builder = ExecutionMenuBar.getBuilder();
        if(!builder.getMenu(ExecutionMenuBar.MENU__RESET).isEnabled())
            return;

        Logger.triggerEvent(Logger.VERBOSE, "Click on reset button of ExecutionView's JMenuBar, will reset");
        ExecutionView view = ((ExecutionView) this.view);
        ViewPanel viewPanel = view.getViewPanel();

        view.setPendingGraph(new PendingGraph());

        this.getExecution().reset();
//        viewPanel.invalidate();
//        viewPanel.repaint();
//        viewPanel.revalidate();

        builder
        .getMenu(ExecutionMenuBar.MENU__RESET)
        .setEnabled(false);

        builder
        .getMenu(ExecutionMenuBar.MENU__STEP_BACKWARD)
        .setEnabled(false);

        builder
        .getMenu(ExecutionMenuBar.MENU__RUN)
        .setEnabled(true);

        builder
        .getMenu(ExecutionMenuBar.MENU__STEP_FORWARD)
        .setEnabled(true);

        this.repaint();
    }

    protected void stepBackwardLogic(){
        MenuBarBuilder builder = ExecutionMenuBar.getBuilder();
        if(!builder.getMenu(ExecutionMenuBar.MENU__STEP_BACKWARD).isEnabled())
            return;

        Logger.triggerEvent(Logger.VERBOSE, "Click on prev button of ExecutionView's JMenuBar");
        ExecutionView view = ((ExecutionView) this.view);
        int index = this.getExecution().getExecutionIndex();
        if(index == 0)
            return;

        this.resetLogic();
        this.getExecution().willRun(true);
        boolean shouldEnable = !this.getExecution().runUpTo(index - 1);

        builder
        .getMenu(ExecutionMenuBar.MENU__STEP_BACKWARD)
        .setEnabled(shouldEnable && this.getExecution().getExecutionIndex()!=0);

        builder
        .getMenu(ExecutionMenuBar.MENU__RESET)
        .setEnabled(builder.getMenu(ExecutionMenuBar.MENU__STEP_BACKWARD).isEnabled());

        builder
        .getMenu(ExecutionMenuBar.MENU__STEP_FORWARD)
        .setEnabled(true);

        builder
        .getMenu(ExecutionMenuBar.MENU__RUN)
        .setEnabled(true);

        this.repaint();
    }

    protected void stepForwardLogic(){
        MenuBarBuilder builder = ExecutionMenuBar.getBuilder();
        if(!builder.getMenu(ExecutionMenuBar.MENU__STEP_FORWARD).isEnabled())
            return;

        Logger.triggerEvent(Logger.VERBOSE, "Click on next button of ExecutionView's JMenuBar");
        ExecutionView view = ((ExecutionView) this.view);
        this.getExecution().willRun(true);
        boolean shouldEnable = !this.getExecution().next();

        builder
        .getMenu(ExecutionMenuBar.MENU__RESET)
        .setEnabled(true);

        builder
        .getMenu(ExecutionMenuBar.MENU__STEP_BACKWARD)
        .setEnabled(true);

        builder
        .getMenu(ExecutionMenuBar.MENU__STEP_FORWARD)
        .setEnabled(shouldEnable);

        builder
        .getMenu(ExecutionMenuBar.MENU__RUN)
        .setEnabled(shouldEnable);

        this.repaint();
    }

    protected void runLogic(){
        MenuBarBuilder builder = ExecutionMenuBar.getBuilder();
        if(!builder.getMenu(ExecutionMenuBar.MENU__RUN).isEnabled())
            return;

        Logger.triggerEvent(Logger.VERBOSE, "Click on run button of ExecutionView's JMenuBar");
        ExecutionView view = ((ExecutionView) this.view);

        if(this.getExecution().isRunning()){
            this.getExecution().runFromNow();
            return;
        }

        this.getExecution().run();

        builder
        .getMenu(ExecutionMenuBar.MENU__RESET)
        .setEnabled(true);

        builder
        .getMenu(ExecutionMenuBar.MENU__STEP_BACKWARD)
        .setEnabled(true);

        builder
        .getMenu(ExecutionMenuBar.MENU__STEP_FORWARD)
        .setEnabled(false);

        builder
        .getMenu(ExecutionMenuBar.MENU__RUN)
        .setEnabled(false);

        this.repaint();
    }

    protected void repaint(){
        ViewPanel viewPanel = this.view.getViewPanel();
        ViewsManager manager = this.getViewsManager();
        ExecutionView view = ((ExecutionView) this.view);

        viewPanel.requestFocus();
        viewPanel.requestFocusInWindow();
        viewPanel.grabFocus();

//        mxGraphComponent graphComponent = new mxGraphComponent(view.getPendingGraph());
//        viewPanel.<ViewPanel>getComponentAs(ExecutionView.RHS_PANEL)
//        .<ViewPanel>getComponentAs(ExecutionView.PENDING_GRAPH_PANEL)
//        .removeComponent(ExecutionView.PENDING_GRAPH)
//        .addComponent(ExecutionView.PENDING_GRAPH, graphComponent);
//        graphComponent.invalidate();
//        graphComponent.repaint();
//        graphComponent.revalidate();

        Component pendingGraph = viewPanel.<ViewPanel>getComponentAs(ExecutionView.RHS_PANEL)
        .<ViewPanel>getComponentAs(ExecutionView.PENDING_GRAPH_PANEL)
        .getComponent(ExecutionView.PENDING_GRAPH);

        pendingGraph.invalidate();
        pendingGraph.repaint();
        pendingGraph.revalidate();

        viewPanel.invalidate();
        viewPanel.repaint();
        viewPanel.revalidate();
    }

    protected void addListenersToLockStackPanel(ViewPanel view, ViewsManager manager){
        this.assertParamsAreNotNull(view, manager);
        Logger.triggerEvent(Logger.VERBOSE, "Adding listeners to ExecutionController's lockStack panel");
        LockStack lockStack = ((ExecutionView) manager.getView(App.EXECUTION_VIEW_TAG)).getLockStack();

        this.getExecution().getLockList()
        .on(DBLockList.ADD_LOCK, lockStack)
        .on(DBLockList.RM_LOCK, lockStack)
        .on(DBLockList.ADD_PENDING, lockStack)
        .on(DBLockList.RM_PENDING, lockStack)
        .on(DBLockList.RESET, lockStack)
        .on(DBLockList.REPAINT, lockStack);
    }

    protected void addListenersToPendingPanel(ViewPanel view, ViewsManager manager){
        this.assertParamsAreNotNull(view, manager);
        Logger.triggerEvent(Logger.VERBOSE, "Adding listeners to ExecutionController's pending panel");

       ViewPanel pendingPanel = view
        .<ViewPanel>getComponentAs(ExecutionView.RHS_PANEL)
        .getComponentAs(ExecutionView.PENDING_GRAPH_PANEL);

        ExecutionView executionView = ((ExecutionView) this.getViewsManager().getView(App.EXECUTION_VIEW_TAG));
        executionView.getPendingGraph().setLockList( this.getExecution().getLockList() );

//        executionView.setTimelineHub(timelineHub);
    }

    protected void addListenersToTimelines(){
        Logger.triggerEvent(Logger.VERBOSE, "Adding listeners to ExecutionController's timelines hub");
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
