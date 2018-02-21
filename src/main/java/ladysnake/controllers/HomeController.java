package ladysnake.controllers;

import ladysnake.App;
import ladysnake.TextApp;
import ladysnake.models.DBLockList;
import ladysnake.models.DBTransactionExecution;
import ladysnake.models.ModelsManager;
import ladysnake.views.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class HomeController extends A_Controller{
    boolean fileChooserOpen;
    JDialog fileChooser;


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    public HomeController(A_View view, ControllersManager cm) {
        super(view, cm);
        this.fileChooserOpen = false;
        this.fileChooser = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void addListeners() {
        this.addListenersToFileChooserButton(this.view.getViewPanel(), this.getViewsManager());
    }

    private void addListenersToFileChooserButton(ViewPanel viewPanel, ViewsManager manager) {
        this.assertParamsAreNotNull(viewPanel, manager);
        viewPanel.<ViewPanel>getComponentAs(HomeView.RHS_PANEL)
        .<ViewPanel>getComponentAs(HomeView.BUTTON_PANEL)
        .<JButton>getComponentAs(HomeView.FILE_CHOOSER_BTN)
        .addMouseListener(this.getFileChooserMouseListener());

        //TODO: Add listeners to SharedMenuBar
        SharedMenuBar.getBuilder().getMenuItemFromMenu(SharedMenuBar.FILE, SharedMenuBar.FILE_IMPORT)
        .addActionListener(this.getFileChooserActionListener());
    }

    private ActionListener getFileChooserActionListener(){
        HomeController that = this;
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    that.fileChooserLogic();
                } catch (UnsupportedLookAndFeelException e1) {
                    e1.printStackTrace();
                }
            }
        };
    }

    private void fileChooserLogic() throws UnsupportedLookAndFeelException {
        System.out.println("click click modafucka");
        if(!this.fileChooserOpen) {
            this.fileChooserOpen = true;
            this.fileChooser = new JDialog();
            this.fileChooser.setLayout(new GridLayout(1,1));
            this.fileChooser.setPreferredSize(FILE_CHOOSER_DIM);
            this.fileChooser.setSize(FILE_CHOOSER_DIM);
            JFileChooser fc = this.getFileChooser();
            this.fileChooser.add(this.getFileChooser());
            int selectionFlag = fc.showOpenDialog(this.fileChooser);
            if(selectionFlag == JFileChooser.APPROVE_OPTION) {
                try {
                    this.goExecution(fc.getSelectedFile());
                } catch (IOException e1) {
                    //TODO: Handle errors
                    e1.printStackTrace();
                }
            }

            this.fileChooserOpen = false;
            fc.setVisible(false);
        }
    }

    private MouseListener getFileChooserMouseListener() {
        HomeController that = this;
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    that.fileChooserLogic();
                } catch (UnsupportedLookAndFeelException e1) {
                    e1.printStackTrace();
                }
            }
        };
    }

    protected void goExecution(File selectedFile) throws IOException, UnsupportedLookAndFeelException {
        String path = selectedFile.getAbsolutePath();
        this.getControllersManager().setModelsManager(ModelsManager.fromFile(path));
        this.attachLockListListeners();
        this.attachExecutionListeners();
        this.getViewsManager().switchTo(App.EXECUTION_VIEW_TAG);
        A_Controller a_controller = this.getControllersManager().getController(App.EXECUTION_VIEW_TAG); //TODO: Remove this if manual start
        ExecutionController controller = ((ExecutionController) a_controller); //TODO: Remove this if manual start
        controller.startExecution(); //TODO: Remove this if manual start
    }

    protected void attachExecutionListeners() {
        this.getControllersManager().getModelsManager().getExecution()
        .on(DBTransactionExecution.INDEX, (eventName, args) -> {
            TextApp.displayEventName(eventName);
            String index = args[0].toString();
            TextApp.out("Index: " + index);
            //out("");
        }).on(DBTransactionExecution.STOP, (eventName, args) -> TextApp.displayEventName(eventName));
    }

    protected void attachLockListListeners(){
        this.getControllersManager().getModelsManager().getLockList()
        .on(DBLockList.ADD_LOCK, TextApp::eventHandler)
        .on(DBLockList.RM_LOCK, TextApp::eventHandler)
        .on(DBLockList.ADD_PENDING, TextApp::eventHandler)
        .on(DBLockList.RM_PENDING, TextApp::eventHandler);
    }

    protected JFileChooser getFileChooser(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                boolean canOpen = true;

                if(f.isFile() && !f.getName().replaceAll("^.+\\.([^.]+)$", "$1").equals("json"))
                    canOpen = false;

                return f.canRead() && canOpen;
            }

            @Override
            public String getDescription() {
                return "JSON file (.json)";
            }
        });

        return fileChooser;
    }

    public final static Dimension FILE_CHOOSER_DIM = new Dimension(600, 400);
}
