package ladysnake.controllers;

import ladysnake.App;
import ladysnake.views.A_View;
import ladysnake.views.HomeView;
import ladysnake.views.ViewPanel;
import ladysnake.views.ViewsManager;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class HomeController extends A_Controller{
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    public HomeController(A_View view) { super(view); }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void addListeners() {
        this.addListenersToBtn(this.view.getViewPanel(), this.getManager());
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
}
