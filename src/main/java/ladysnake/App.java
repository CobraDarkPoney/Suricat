package ladysnake;

//mport ladysnake.models.*;
import ladysnake.models.ModelsManager;
import ladysnake.views.*;
import ladysnake.controllers.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@SuppressWarnings({"unused", "WeakerAccess"})
public class App {
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    ControllersManager cm;
    ModelsManager mm;
    ViewsManager vm;

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    public App() throws IOException, UnsupportedLookAndFeelException {
        this.vm = new ViewsManager(App.TITLE, App.DIMENSION);
        this.vm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.vm.setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));

        this.mm = null;

        this.cm = new ControllersManager(this.vm, this.mm);
        A_View executionView = new ExecutionView(this.vm);
        A_View homeView = new HomeView(this.vm);

        this.vm
        .addView(App.HOME_VIEW_TAG, homeView)
        .addView(App.EXECUTION_VIEW_TAG, executionView)
        .setCurrentView(App.HOME_VIEW_TAG); //Not necessary -> default behavior

        this.cm.addController(App.EXECUTION_VIEW_TAG, new ExecutionController(executionView, this.cm))
        .addController(App.HOME_VIEW_TAG, new HomeController(homeView, this.cm));

        try {
            vm.setLookAndFeel(LookAndFeelHub.NIMBUS);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            out("Could not change the look and feel");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    public void run(){
        vm.display();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    public final static String TITLE = "Suricat";
    public final static int INIT_WIDTH = 800;
    public final static int INIT_HEIGHT = 500;
    public final static Dimension DIMENSION = new Dimension(INIT_WIDTH, INIT_HEIGHT);

    public final static String EXECUTION_VIEW_TAG = "execution";
    public final static String HOME_VIEW_TAG = "home";

    public final static String ROBOTO_PATH = "rsc/fonts/Roboto-Light.ttf";
    public final static String ROBOTO_MEDIUM_PATH = "rsc/fonts/Roboto-Medium.ttf";

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    protected static App make() throws IOException, UnsupportedLookAndFeelException {
        return new App();
    }

    protected static void out(Object msg){ System.out.println(msg); }

    public static void main(String[] args) throws IOException, UnsupportedLookAndFeelException {
        App.make().run();
    }
}
