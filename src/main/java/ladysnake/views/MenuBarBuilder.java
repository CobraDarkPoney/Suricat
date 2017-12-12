package ladysnake.views;

import javax.swing.*;

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
    public JMenuBar getBuilt(){
        return this.menuBar;
    }

    public MenuBarBuilder addMenu(JMenu menu){
        this.assertParamsAreNotNull(menu);
        this.menuBar.add(menu);
        return this;
    }

    public MenuBarBuilder setMenuMnemonic(String menuID, int mnemonic){
        this.assertParamsAreNotNull(menuID, mnemonic);

        JMenu menu = this.getMenu(menuID);
        if(menu != null)
            menu.setMnemonic(mnemonic);
        return this;
    }

    public MenuBarBuilder setMenuAccessibleDescription(String menuID, String description){
        this.assertParamsAreNotNull(menuID, description);

        JMenu menu = this.getMenu(menuID);
        if(menu != null)
            menu.getAccessibleContext().setAccessibleDescription(description);

        return this;
    }

    public MenuBarBuilder addMenuSeparator(String menuID){
        this.assertParamsAreNotNull(menuID);

        JMenu menu = this.getMenu(menuID);
        if(menu != null)
            menu.addSeparator();

        return this;
    }

    public MenuBarBuilder addMenuItemToMenu(String menuID, JMenuItem menuItem){
        this.assertParamsAreNotNull(menuID, menuItem);

        JMenu menu = this.getMenu(menuID);
        if(menu != null)
            menu.add(menuItem);

        return this;
    }

    public MenuBarBuilder setMenuItemAccelerator(String menuID, String menuItemId, KeyStroke accelerator){
        this.assertParamsAreNotNull(menuItemId);

        JMenuItem item = this.getMenuItemFromMenu(menuID, menuItemId);

        if(item != null)
            item.setAccelerator(accelerator);

        return this;
    }

    public MenuBarBuilder setMenuItemMnemonic(String menuID, String menuItemID, int mnemonic){
        this.assertParamsAreNotNull(menuID, menuItemID, mnemonic);

        JMenuItem item = this.getMenuItemFromMenu(menuID, menuItemID);

        if(item != null)
            item.setMnemonic(mnemonic);

        return this;
    }

    public MenuBarBuilder setMenuItemAccessibleDescription(String menuID, String menuItemID, String description){
        this.assertParamsAreNotNull(menuID, description);

        JMenuItem item = this.getMenuItemFromMenu(menuID, menuItemID);
        if(item != null)
            item.getAccessibleContext().setAccessibleDescription(description);

        return this;
    }

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

    protected JMenu getMenu(String menuID){
        this.assertParamsAreNotNull(menuID);

        int index = this.getIndexForMenu(menuID);
        if(index < 0)
            return null;

        return this.menuBar.getMenu( index );
    }

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

    protected JMenuItem getMenuItemFromMenu(String menuID, String menuItemID){
        this.assertParamsAreNotNull(menuID, menuItemID);

        int index = this.getIndexForMenuItemInMenu(menuID, menuItemID);

        if(index < 0)
            return null;

        JMenu menu = this.getMenu(menuItemID);
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
}
