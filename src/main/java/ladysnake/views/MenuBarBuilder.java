package ladysnake.views;

import javax.swing.*;
import org.jetbrains.annotations.Nullable;
import ladysnake.helpers.utils.I_MightNoNullParams;

import java.util.Arrays;

/**
 * @author Ludwig GUERIN
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class MenuBarBuilder implements I_MightNoNullParams{
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    protected JMenuBar menuBar;


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Create a MenuBarBuilder
     */
    public MenuBarBuilder(){
        this.menuBar = new JMenuBar();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Retrieves the {@link JMenuBar} built in its current state in the builder
     * @return {@link JMenuBar}
     */
    public JMenuBar getBuilt(){
        return this.menuBar;
    }

    /**Adds a {@link JMenu} to the {@link JMenuBar} under construction
     * @param menu being the {@link JMenu} to add
     * @return this {@link MenuBarBuilder}
     */
    public MenuBarBuilder addMenu(JMenu menu){
        this.assertParamsAreNotNull(menu);
        this.menuBar.add(menu);
        return this;
    }

    /**Sets the mnemonic of a menu given its menuID
     * @param menuID being the {@link JMenu}'s menuID
     * @param mnemonic being the desired mnemonic
     * @return this {@link MenuBarBuilder}
     */
    public MenuBarBuilder setMenuMnemonic(String menuID, int mnemonic){
        this.assertParamsAreNotNull(menuID, mnemonic);

        JMenu menu = this.getMenu(menuID);
        if(menu != null)
            menu.setMnemonic(mnemonic);
        return this;
    }

    /**Sets a {@link JMenu}'s accessible description given its menuID
     * @param menuID being the menuID associated to the desired {@link JMenu}
     * @param description being the new description
     * @return this {@link MenuBarBuilder}
     */
    public MenuBarBuilder setMenuAccessibleDescription(String menuID, String description){
        this.assertParamsAreNotNull(menuID, description);

        JMenu menu = this.getMenu(menuID);
        if(menu != null)
            menu.getAccessibleContext().setAccessibleDescription(description);

        return this;
    }

    /**Adds a separator inside of a menu given its menuID
     * @param menuID being the menuID associated to the desired {@link JMenu}
     * @return this {@link MenuBarBuilder}
     */
    public MenuBarBuilder addMenuSeparator(String menuID){
        this.assertParamsAreNotNull(menuID);

        JMenu menu = this.getMenu(menuID);
        if(menu != null)
            menu.addSeparator();

        return this;
    }

    /**Adds a{@link JMenuItem} to a {@link JMenu} given its menuID
     * @param menuID being the menudID associated to the desired {@link JMenu}
     * @param menuItem being the {@link JMenuItem} being added
     * @return this {@link MenuBarBuilder}
     */
    public MenuBarBuilder addMenuItemToMenu(String menuID, JMenuItem menuItem){
        this.assertParamsAreNotNull(menuID, menuItem);

        JMenu menu = this.getMenu(menuID);
        if(menu != null)
            menu.add(menuItem);

        return this;
    }

    /**Sets a {@link JMenuItem}'s accelerator given its menuItemID and menuID associated to the {@link JMenu} it is in
     * @param menuID being the {@link JMenu}'s menuID
     * @param menuItemId being the {@link JMenuItem}'s menuItemID
     * @param accelerator being the new accelerator
     * @return this {@link MenuBarBuilder}
     */
    public MenuBarBuilder setMenuItemAccelerator(String menuID, String menuItemId, KeyStroke accelerator){
        this.assertParamsAreNotNull(menuItemId);

        JMenuItem item = this.getMenuItemFromMenu(menuID, menuItemId);

        if(item != null)
            item.setAccelerator(accelerator);

        return this;
    }

    /**Sets a {@link JMenuItem}'s mnemonic
     * @param menuID being the {@link JMenu}'s ID
     * @param menuItemID being the {@link JMenuItem}'s ID
     * @param mnemonic being the new mnemonic
     * @return this {@link MenuBarBuilder}
     */
    public MenuBarBuilder setMenuItemMnemonic(String menuID, String menuItemID, int mnemonic){
        this.assertParamsAreNotNull(menuID, menuItemID, mnemonic);

        JMenuItem item = this.getMenuItemFromMenu(menuID, menuItemID);

        if(item != null)
            item.setMnemonic(mnemonic);

        return this;
    }

    /**Sets the {@link JMenuItem}'s accessible description
     * @param menuID being the {@link JMenu}'s ID
     * @param menuItemID being the {@link JMenuItem}'s ID
     * @param description being the new description
     * @return this {@link MenuBarBuilder}
     */
    public MenuBarBuilder setMenuItemAccessibleDescription(String menuID, String menuItemID, String description){
        this.assertParamsAreNotNull(menuID, description);

        JMenuItem item = this.getMenuItemFromMenu(menuID, menuItemID);
        if(item != null)
            item.getAccessibleContext().setAccessibleDescription(description);

        return this;
    }

    /**Retrieve the index iof a {@link JMenu} in the inner array of component from its menuID
     * @param menuID being the {@link JMenu}'s ID
     * @return the index (or -1 if not found)
     */
    protected int getIndexForMenu(String menuID){
        this.assertParamsAreNotNull(menuID);

        for(int i = 0 ; i < this.menuBar.getMenuCount() ; i+=1){
            JMenu cur = this.menuBar.getMenu(i);
            if(cur != null) {
                if (menuID.equals(cur.getText()))
                    return i;
            }
        }

        return -1;
    }

    /**Retrieves a {@link JMenu} from its menuID
     * @param menuID being the menuID associated to the desired
     * @return NULL if not found, otherwise the {@link JMenu}
     */
    @Nullable
    public JMenu getMenu(String menuID){
        this.assertParamsAreNotNull(menuID);

        int index = this.getIndexForMenu(menuID);
        if(index < 0)
            return null;

        return this.menuBar.getMenu( index );
    }

    /**Get the index of a {@link JMenuItem}  in the inner array of the given {@link JMenu} from its ID and the item's ID
     * @param menuID being the {@link JMenu}'s ID
     * @param menuItemID being the {@link JMenuItem}'s ID
     * @return -1 if not found, the index otherwise
     */
    protected int getIndexForMenuItemInMenu(String menuID, String menuItemID){
        this.assertParamsAreNotNull(menuID, menuItemID);

        JMenu menu = this.getMenu(menuID);
        if(menu != null){
            for(int i = 0 ; i < menu.getItemCount() ; i+=1){
                JMenuItem cur = menu.getItem(i);
                if(cur != null) {
                    if (menuItemID.equals(cur.getText()))
                        return i;
                }
            }
        }

        return -1;
    }

    /**Retrieves a {@link JMenuItem} from its ID
     * @param menuID being the {@link JMenu}'s ID
     * @param menuItemID being the {@link JMenuItem}'s ID
     * @return NULL if not found, the {@link JMenuItem} otherwise
     */
    @Nullable
    public JMenuItem getMenuItemFromMenu(String menuID, String menuItemID){
        this.assertParamsAreNotNull(menuID, menuItemID);

        int index = this.getIndexForMenuItemInMenu(menuID, menuItemID);

        if(index < 0)
            return null;

        JMenu menu = this.getMenu(menuID);
        if(menu == null)
            return null;

        return menu.getItem(index);
    }

    @Override
    public String toString(){
        String ret = "";
        ret += "<MenuBarBuilder>\n";
        ret += Arrays.stream(this.menuBar.getSubElements())
        .map((MenuElement menuElement) -> {
            MenuElement[] sub = this.menuBar.getSubElements();
            String r = "\t"
                    + "@type: " + menuElement.getClass().getTypeName()
                    + ", @ctx: " + menuElement.getSubElements().toString();

            if(sub.length > 1)
                r += (sub[sub.length - 1].equals(menuElement)) ? "" : "\n";

            return r;
        }).reduce("", String::concat)+ "\n";
        ret += "</MenuBarBuilder>\n";
        return ret;
    }

    public static MenuBarBuilder getNewBuilder(){ return new MenuBarBuilder(); }
}
