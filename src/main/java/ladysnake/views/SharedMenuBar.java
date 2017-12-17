package ladysnake.views;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class SharedMenuBar {
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    protected MenuBarBuilder builder;


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Instantiate a {@link SharedMenuBar}
     */
    private SharedMenuBar(){
        this.builder = new MenuBarBuilder();
        this.builder
                .addMenu(new JMenu(FILE))
                .setMenuAccessibleDescription(FILE,  ACCESSIBLE_DESCRIPTION.get(FILE))
                .addMenuItemToMenu(FILE, new JMenuItem(FILE_IMPORT))
                .setMenuItemAccessibleDescription(FILE, FILE_IMPORT, ACCESSIBLE_DESCRIPTION.get(FILE_IMPORT));
                //TODO: finish this if necessary
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Retrieves the {@link JMenuBar} from this {@link SharedMenuBar}
     * @return the {@link JMenuBar}
     */
    public JMenuBar getJMenuBar(){
        return this.builder.getBuilt();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    private static SharedMenuBar instance = null;

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Retrieves the only existing instance of {@link SharedMenuBar}
     * @return the instance of {@link SharedMenuBar}
     */
    public static SharedMenuBar getInstance(){
        if(instance == null)
            instance = new SharedMenuBar();

        return instance;
    }

    /**Retrieve the {@link JMenuBar} associated to the only instance of {@link SharedMenuBar}
     * @return the only {@link JMenuBar}
     */
    public static JMenuBar retrieveJMenuBar(){
        return getInstance().getJMenuBar();
    }

    public final static String FILE = "File";
    public final static String FILE_IMPORT = "Import JSON file ...";
    public final static Map<String, String> ACCESSIBLE_DESCRIPTION = new HashMap<String, String>(){{
        put(FILE, "Menu related to the use of files");
        put(FILE_IMPORT, "Import a file to be used");
    }};
}
