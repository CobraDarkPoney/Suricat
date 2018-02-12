package ladysnake.views;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import ladysnake.App;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
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
        try {
            panel.addComponent(LOGO_PANEL, this.getLogoPanel())
            .addComponent(RHS_PANEL, this.getRhsPanel());
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
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
                int aspectRatio;
                try{
                    aspectRatio = width / height;
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

    protected ViewPanel getRhsPanel() throws IOException, FontFormatException {
        ViewPanel rhsPanel = new ViewPanel();
        //rhsPanel.setLayout(new GridLayout(RHS_ROWS, RHS_COLS, RHS_SPACING, RHS_SPACING));
        rhsPanel.setLayout(new GridLayout(RHS_ROWS, RHS_COLS));
        JTextPane textPane = new JTextPane();
        rhsPanel.addComponent(TEXT_PANEL, textPane);
//        .<ViewPanel>getComponentAs(TEXT_PANEL)
//        .setLayout(new BorderLayout());

//        rhsPanel.<ViewPanel>getComponentAs(TEXT_PANEL)
//        .addComponent(TITLE_LABEL, new JLabel(TITLE_CONTENT))
//        .addComponent(MESSAGE_LABEL, new JLabel( MESSAGE_CONTENT ));
        int pos = 0;
        StyledDocument sDoc = textPane.getStyledDocument();
        Style def = textPane.getStyle("default");

        try {
            sDoc.insertString(pos, TITLE_CONTENT, def); pos+=TITLE_CONTENT.length();
            sDoc.insertString(pos, MESSAGE_CONTENT, def);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        rhsPanel.addComponent(FILE_CHOOSER_BTN, new JButton("Choisir un fichier JSON"));

//        ViewPanel text = rhsPanel.getComponentAs(TEXT_PANEL);
//        text.setLayout(new FlowLayout());
//        JLabel title = text.getComponentAs(TITLE_LABEL);
//        JLabel message = text.getComponentAs(MESSAGE_LABEL);
//        message.setPreferredSize(message.getParent().getPreferredSize());

//        Font roboto = Font.createFont(Font.TRUETYPE_FONT, new File(App.ROBOTO_PATH)).deriveFont(TITLE_PT);
//        Font robotoMedium = Font.createFont(Font.TRUETYPE_FONT, new File(App.ROBOTO_MEDIUM_PATH)).deriveFont(MESSAGE_PT);
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        ge.registerFont(roboto);
//        ge.registerFont(robotoMedium);
//
//        title.setFont(robotoMedium);
//        message.setFont(roboto);

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
    public final static String TEXT_PANEL = "textpanel";

    public final static String TITLE_LABEL = "title-label";
    public final static String TITLE_CONTENT = "Bienvenue dans Suricat !";
    public final static float TITLE_PT = 16f;

    public final static String MESSAGE_LABEL = "msg-label";
    public final static String MESSAGE_CONTENT = "Pour commencer la visualisation des transactions de votre base de données, veuillez sélectionner un fichier JSON de format valide les représentant, ou glisser-déposer ce dernier dans cette fenêtre";
    public final static float MESSAGE_PT = 10f;

    public final static int RHS_ROWS = 2;
    public final static int RHS_COLS = 1;
    public final static int RHS_SPACING = GRID_SPACING;

    public final static int LOGO_ROWS = 1;
    public final static int LOGO_COLS = 1;
    public final static int LOGO_SPACING = GRID_SPACING;

    public final static String LOGO_URL = "logo-smooth.png";
}
