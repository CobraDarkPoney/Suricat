package ladysnake.controllers;

import ladysnake.App;
import ladysnake.views.A_View;
import ladysnake.views.HomeView;
import ladysnake.views.ViewPanel;
import ladysnake.views.ViewsManager;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class HomeController extends A_Controller{
    boolean fileChooserOpen;
    JDialog fileChooser;


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    public HomeController(A_View view) {
        super(view);
        this.fileChooserOpen = false;
        this.fileChooser = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void addListeners() {
        this.addListenersToBtn(this.view.getViewPanel(), this.getManager());
        this.addListenersToFileChooserButton(this.view.getViewPanel(), this.getManager());
    }

    private void addListenersToFileChooserButton(ViewPanel viewPanel, ViewsManager manager) {
        this.assertParamsAreNotNull(viewPanel, manager);
        viewPanel.<ViewPanel>getComponentAs(HomeView.RHS_PANEL)
        .<JButton>getComponentAs(HomeView.FILE_CHOOSER_BTN)
        .addMouseListener(this.getFileChooserButtonMouseListener());
    }

    protected void addListenersToBtn(ViewPanel view, ViewsManager manager){
        this.assertParamsAreNotNull(view, manager);

        view
                .<ViewPanel>getComponentAs(HomeView.RHS_PANEL)
                .<JButton>getComponentAs("btn")
                .addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        manager.switchTo(App.EXECUTION_VIEW_TAG);
                    }
                });
    }

    private MouseListener getFileChooserButtonMouseListener() {
        HomeController that = this;

        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("click click modafucka");
                if(!that.fileChooserOpen) {
                    that.fileChooserOpen = true;
                    that.fileChooser = new JDialog();
                    that.fileChooser.setLayout(new GridLayout(1,1));
                    that.fileChooser.setPreferredSize(FILE_CHOOSER_DIM);
                    that.fileChooser.setSize(FILE_CHOOSER_DIM);
                    JFileChooser fc = that.getFileChooser();
                    //TODO: Handle "annuler", quitting the selection, handle events, etc...
                    that.fileChooser.add(that.getFileChooser());
                    that.fileChooser.setVisible(true);
                }
            }
        };
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
