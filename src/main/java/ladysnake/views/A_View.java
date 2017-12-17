package ladysnake.views;

import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.util.Objects;

/**An abstract class factorizing the shared behavior of all views
 */
@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public abstract class A_View {
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    protected ViewPanel viewPanel;
    protected String viewTitle;

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Constructor (initializes the fields using the overridden methods)
     */
    public A_View(){
        this.viewPanel = this.setUp();
        this.viewTitle = this.getViewTitle();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////

    /**Retrieves this {@link A_View}'s {@link ViewPanel}
     * @return {@link ViewPanel}
     */
    public final ViewPanel getViewPanel(){
        return this.viewPanel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof A_View)) return false;
        A_View a_view = (A_View) o;
        return Objects.equals(getViewPanel(), a_view.getViewPanel()) &&
                Objects.equals(getViewTitle(), a_view.getViewTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getViewPanel(), getViewTitle());
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Override these if needed
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Setup this {@link A_View}'s {@link ViewPanel}
     * @return A prepared {@link ViewPanel}
     */
    protected abstract ViewPanel setUp();

    /**Retrieves this {@link A_View}'s title
     * @return the title of this view (for the window's bar) as a {@link String}
     */
    public abstract String getViewTitle();

    /**Retrieves the {@link JMenuBar} associated to this {@link A_View}
     * @return the desired {@link JMenuBar}
     */
    @Nullable
    public JMenuBar getViewMenuBar(){
        return SharedMenuBar.retrieveJMenuBar();
    }
}
