package ladysnake.views;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class HomeView extends A_View{
    /**
     * @see A_View#A_View(ViewsManager)
     */
    public HomeView(ViewsManager manager) throws IOException {
        super(manager);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @see A_View#setUp()
     */
    @Override
    protected ViewPanel setUp() throws IOException {
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

    protected ViewPanel getLogoPanel() throws IOException {
        BufferedImage logo = ImageIO.read(new File(LOGO_URL));
        ViewPanel logoPanel = new ViewPanel(){
            protected Image bg = logo;

            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                int width = bg.getWidth(null), height = bg.getHeight(null);
                int selfWidth = getWidth(), selfHeight = getHeight();
                int aspectRatio, selfAspectRatio;
                try{
                    aspectRatio = width / height;
                    selfAspectRatio = selfWidth / selfHeight;
                }catch(ArithmeticException e){
                    e.printStackTrace();
                    System.exit(-1);
                    return;
                }

                if(width > selfWidth){
                    width = selfWidth;
                    height = width / aspectRatio;
                }

                if(height > selfHeight){
                    height = selfHeight;
                    width = aspectRatio * height;
                }

                int dx = selfWidth - width;
                int dy = selfHeight - height;
                Image reScaled = bg.getScaledInstance(width, height, Image.SCALE_SMOOTH);

                g.drawImage(reScaled, dx/2, dy/2, width, height, this);
            }
        };
        logoPanel.setLayout(new GridLayout(LOGO_ROWS, LOGO_COLS, LOGO_SPACING, LOGO_SPACING));
        return logoPanel;
    }

    protected ViewPanel getRhsPanel(){
        ViewPanel rhsPanel = new ViewPanel();
        rhsPanel.setLayout(new GridLayout(RHS_ROWS, RHS_COLS, RHS_SPACING, RHS_SPACING));
        rhsPanel.addComponent("btn", new JButton("Go Transaction"));
        rhsPanel.addComponent(FILE_CHOOSER_BTN, new JButton("Choisir un fichier JSON"));
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
    public final static String FILE_CHOOSER_BTN = "filechooserbtn";

    public final static int RHS_ROWS = 2;
    public final static int RHS_COLS = 1;
    public final static int RHS_SPACING = GRID_SPACING;

    public final static int LOGO_ROWS = 1;
    public final static int LOGO_COLS = 1;
    public final static int LOGO_SPACING = GRID_SPACING;

    public final static String LOGO_URL = "logo-smooth.png";
}
