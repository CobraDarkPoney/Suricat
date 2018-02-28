package ladysnake.views;

import ladysnake.App;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.View;
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
        return "Suricat - Accueil";
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

        ViewPanel textPanel = new ViewPanel();
//        ViewPanel textHolder = new ViewPanel();
        textPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = 3;
        constraints.weightx = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
//        textHolder.addComponent(TEXT_PANEL, textPanel, constraints);
//        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        Font roboto = Font.createFont(Font.TRUETYPE_FONT, new File(App.ROBOTO_PATH)).deriveFont(MESSAGE_PT);
        Font robotoMedium = Font.createFont(Font.TRUETYPE_FONT, new File(App.ROBOTO_MEDIUM_PATH)).deriveFont(TITLE_PT);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(roboto);
        ge.registerFont(robotoMedium);

        JLabel titleLabel = new JLabel(wrapTitle(TITLE_CONTENT));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(robotoMedium);
        textPanel.addComponent(TITLE_LABEL, titleLabel, constraints);

        JLabel messageLabel = new JLabel(wrapInP(MESSAGE_CONTENT));
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setFont(roboto);
        constraints.gridy = 1;
        textPanel.addComponent(MESSAGE_LABEL, messageLabel, constraints);

//        rhsPanel.addComponent(TEXT_HOLDER, textHolder);
        rhsPanel.addComponent(TEXT_PANEL, textPanel);

        ViewPanel buttonPanel = new ViewPanel();
        buttonPanel.setLayout(new GridBagLayout());
        rhsPanel.addComponent(BUTTON_PANEL, buttonPanel);

        buttonPanel.addComponent(FILE_CHOOSER_BTN, new JButton(FILE_CHOOSER_BUTTON_TEXT));

        return rhsPanel;
    }

    protected String wrapInHtmlTag(String tag, String ctx){
        this.assertParamsAreNotNull(tag, ctx);
        return "<html> <" + tag + ">" + ctx + "</" + tag+  "> </html>";
    }

    protected String wrapInP(String ctx){
        this.assertParamsAreNotNull(ctx);
        return this.wrapInHtmlTag("p", ctx);
    }

    protected String wrapInH1(String ctx){
        this.assertParamsAreNotNull(ctx);
        return this.wrapInHtmlTag("h1", ctx);
    }

    protected String wrapTitle(String ctx){
        this.assertParamsAreNotNull(ctx);
        return this.wrapInP("<font size=+8>" + ctx + "</font>");
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
    public final static float TITLE_PT = 60f;

    public final static String MESSAGE_LABEL = "msg-label";
    public final static String MESSAGE_CONTENT = "Pour commencer la visualisation des transactions de votre base de données, veuillez sélectionner un fichier JSON de format valide les représentant, ou glisser-déposer ce dernier dans cette fenêtre";
    public final static float MESSAGE_PT = 18f;

    public final static int RHS_ROWS = 2;
    public final static int RHS_COLS = 1;
    public final static int RHS_SPACING = GRID_SPACING;

    public final static int LOGO_ROWS = 1;
    public final static int LOGO_COLS = 1;
    public final static int LOGO_SPACING = GRID_SPACING;

    public final static String LOGO_URL = "logo-smooth.png";

    public final static String TEXT_HOLDER = "tholder";
    public final static String BUTTON_PANEL_HOLDER = "bpholder";
    public final static String BUTTON_PANEL = "btn-panel";
    public final static String FILE_CHOOSER_BUTTON_TEXT = "Choisir un fichier JSON";
}
