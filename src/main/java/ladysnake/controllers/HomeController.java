package ladysnake.controllers;

import ladysnake.App;
import ladysnake.TextApp;
import ladysnake.helpers.log.Logger;
import ladysnake.models.DBLockList;
import ladysnake.models.DBTransactionExecution;
import ladysnake.models.ModelsManager;
import ladysnake.views.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
        ViewPanel viewPanel = this.view.getViewPanel();
        ViewsManager viewsManager = this.getViewsManager();
        this.addListenersToFileChooserButton(viewPanel, viewsManager);
        this.addDragAndDrop(viewPanel, viewsManager);
    }

    private void addDragAndDrop(ViewPanel viewPanel, ViewsManager manager){
        Logger.triggerEvent(Logger.VERBOSE, "Adding Drag&Drop logic");
//        DropTarget target = new DropTarget(viewPanel, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
//            @Override
//            public void drop(DropTargetDropEvent e) {
//                System.out.println("Drop / 20");
//                if((e.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) == 0)
//                    return;
//
//                e.acceptDrop(e.getDropAction());
//                try {
////                    java.util.List files = (List)e.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
////                    File file = (File)files.get(0);
////                    HomeController.this.goExecution(file);
//                    for(DataFlavor df : e.getTransferable().getTransferDataFlavors()) {
//                        if (df.isFlavorJavaFileListType()) {
//                            List<File> files = (List<File>) (e.getTransferable().getTransferData(df));
//                            if (files.size() < 1)
//                                return;
//
//                            File file = files.get(0);
//                            if(!HomeController.this.fileIsJson(file))
//                                e.rejectDrop();
//
//                            HomeController.this.goExecution(file);
//                            e.dropComplete(true);
//                        }
//                    }
//                } catch (UnsupportedFlavorException | IOException | UnsupportedLookAndFeelException exc) {
//                    exc.printStackTrace();
//                }
//            }
//        }, true, null);
        FileDrop.Listener listener = files -> {
            Logger.triggerEvent(Logger.LOG, "Files being dropped");

            if(files.length < 1)
                return;

            File file = files[0];
            Logger.triggerEvent(Logger.VERBOSE, "Drag&Drop file : " + file.getPath());
            HomeController that = HomeController.this;
            if(!that.fileIsJson(file)) {
                Logger.triggerEvent(Logger.WARNING, "File dropped was not JSON : " + file.getPath());
                return;
            }

            try {
                Logger.triggerEvent(Logger.VERBOSE, "Using JSON file : " + file.getPath());
                that.goExecution(file);
            } catch (IOException | UnsupportedLookAndFeelException e) {
                Logger.triggerEvent(Logger.ERROR, "Error while opening the file or while changing the look and feel");
                e.printStackTrace();
            }
        };
        new FileDrop(viewPanel, BORDER, listener);
//        new FileDrop(viewPanel.<ViewPanel>getComponentAs(HomeView.RHS_PANEL), listener);
//        new FileDrop(viewPanel.<ViewPanel>getComponentAs(HomeView.LOGO_PANEL), listener);
    }

    private void addListenersToFileChooserButton(ViewPanel viewPanel, ViewsManager manager) {
        this.assertParamsAreNotNull(viewPanel, manager);
        Logger.triggerEvent(Logger.VERBOSE, "Adding listeners to HomeController's file chooser button");
        JButton button = viewPanel.<ViewPanel>getComponentAs(HomeView.RHS_PANEL)
        .<ViewPanel>getComponentAs(HomeView.BUTTON_PANEL)
        .<JButton>getComponentAs(HomeView.FILE_CHOOSER_BTN);
        button.addMouseListener(this.getFileChooserMouseListener());
        button.addKeyListener(new KeyAdapter() {
            JButton b = button;

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    this.b.doClick();
                    try {
                        HomeController.this.fileChooserLogic();
                    } catch (UnsupportedLookAndFeelException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

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
//        System.out.println("click click modafucka");
        Logger.triggerEvent(Logger.VERBOSE, "File chooser logic");
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

    public void goExecution(File selectedFile) throws IOException, UnsupportedLookAndFeelException {
        if(!this.fileIsJson(selectedFile))
            return;


        Logger.triggerEvent(Logger.VERBOSE, "Going for execution");

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
//        this.getControllersManager().getModelsManager().getExecution()
//        .on(DBTransactionExecution.INDEX, (eventName, args) -> {
//            TextApp.displayEventName(eventName);
//            String index = args[0].toString();
//            TextApp.out("Index: " + index);
//            //out("");
//        }).on(DBTransactionExecution.STOP, (eventName, args) -> TextApp.displayEventName(eventName));
        Logger.triggerEvent(Logger.VERBOSE, "Attaching ExecutionController's listeners");

        ExecutionController executionController = ((ExecutionController) this.getControllersManager().getController(App.EXECUTION_VIEW_TAG));
        ViewPanel view = executionController.view.getViewPanel();
        ViewsManager viewsManager = executionController.getViewsManager();
        executionController.addListenersToLockStackPanel(view, viewsManager);
        executionController.addListenersToPendingPanel(view, viewsManager);
        executionController.addListenersToMenuBar(view, viewsManager);
        executionController.addListenersToTimelines();
        executionController.addKeyboardShortcuts();
    }

    protected void attachLockListListeners(){
//        this.getControllersManager().getModelsManager().getLockList()
//        .on(DBLockList.ADD_LOCK, TextApp::eventHandler)
//        .on(DBLockList.RM_LOCK, TextApp::eventHandler)
//        .on(DBLockList.ADD_PENDING, TextApp::eventHandler)
//        .on(DBLockList.RM_PENDING, TextApp::eventHandler);
    }

    public boolean fileIsJson(File f){
        boolean canOpen = true;

        if(f.isFile() && !f.getName().replaceAll("^.+\\.([^.]+)$", "$1").equals("json"))
            canOpen = false;

        return f.canRead() && canOpen;
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
    public final static int BORDER_SIZE = 6;
    public final static Color BORDER_COLOR = new Color(0x6f3b5998, true); //#3b5998 -- 0xAARRGGBB
    public final static Border BORDER = BorderFactory.createMatteBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_COLOR);
    public final static Border DASHED_BORDER = BorderFactory.createDashedBorder(BORDER_COLOR, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE,  true);
//    public final static Border BORDER = BorderFactory.createTitledBorder(DASHED_BORDER, "Drag & Drop", TitledBorder.CENTER, TitledBorder.ABOVE_TOP);
}
