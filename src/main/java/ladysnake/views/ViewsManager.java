package ladysnake.views;

import com.sun.istack.internal.Nullable;
import ladysnake.helpers.utils.I_MightNoNullParams;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**A class that manages the use of multiple {@link A_View} for a single {@link ViewWindow}
 */
@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class ViewsManager extends ViewWindow implements I_MightNoNullParams{
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    protected Map<String, A_View> views;
    protected A_View currentView;
    protected String currentViewTag;

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Create a ViewsManager and give it a title
     * @param title being the title of the window
     */
    public ViewsManager(String title){
        this(title, 0,0);
    }

    /**Create a ViewsManager and give it an empty title
     */
    public ViewsManager(){
        this("");
    }

    /**Complete constructor
     * @param title being the window's title
     * @param width being the window's preferred width
     * @param height being the window's preferred height
     * @throws IllegalArgumentException if width or height are < 0
     */
    public ViewsManager(String title, int width, int height) throws IllegalArgumentException{
        this(title, new Dimension(width, height));
    }

    /**Creates a {@link ViewsManager} from its title and {@link Dimension}
     * @param title being this {@link ViewsManager}'s title
     * @param dim being this {@link ViewsManager}'s {@link Dimension}
     * @throws IllegalArgumentException if dim.width or dim.height are < 0
     */
    public ViewsManager(String title, Dimension dim) throws IllegalArgumentException{
        super(title, dim);
        this.assertParamsAreNotNull(title, dim);
        this.views = new HashMap<>();
        this.currentView = null;
        this.currentViewTag = null;
        this.setLayout(new GridLayout(1, 1));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Adds a {@link A_View} to this {@link ViewsManager}
     * @param tag being the {@link A_View}'s tag in this {@link ViewsManager}
     * @param view being the {@link A_View} to add
     * @return this {@link ViewsManager}
     */
    public ViewsManager addView(String tag, A_View view){
        this.assertParamsAreNotNull(tag, view);
        this.views.put(tag, view);

        if(!this.hasCurrentView())
            this.setCurrentView(tag);

        return this;
    }

    /**Determines whether or not a {@link A_View} is in this {@link ViewsManager} given its tag
     * @param tag being the tag of the said {@link A_View}
     * @return TRUE if found, FALSE otherwise
     */
    public boolean hasView(String tag){
        this.assertParamsAreNotNull(tag);
        return this.views.containsKey(tag);
    }

    /**Retrieves a {@link A_View} from its tag
     * @param tag being the tag of the desired {@link A_View}
     * @return NULL if not found, the {@link A_View} otherwise
     */
    @Nullable
    public A_View getView(String tag){
        this.assertParamsAreNotNull(tag);
        return this.views.getOrDefault(tag, null);
    }

    /**Removes a {@link A_View} from this {@link ViewsManager}
     * @param tag being the tag associated to the {@link A_View} to remove
     * @return this {@link ViewsManager}
     */
    public ViewsManager removeView(String tag){
        if(!this.hasView(tag))
            return this;

        this.views.remove(tag);
        return this;
    }

    /**Annihilates a {@link A_View}
     * @param tag being the tag associated to the {@link A_View} to annihilate
     * @return this {@link ViewsManager}
     * @throws Throwable if it couldn't use the {@link Object#finalize()} method
     */
    public ViewsManager destroyView(String tag) throws Throwable {
        if(!this.hasView(tag))
            return this;

        A_View viewToDestroy = this.getView(tag);
        if(this.isCurrentView(tag, viewToDestroy)) {
            this.hideCurrentView();
            this.removeComponent(VIEW_TAG);
        }

        viewToDestroy.finalize();
        return this;
    }

    /**Retrieves the current/active view
     * @return NULL if there's no current/active view, the {@link A_View} otherwise
     */
    @Nullable
    public A_View getCurrentView() {
        return this.currentView;
    }

    /**Determines whether or not this {@link ViewsManager} has a current/active {@link A_View} or not
     * @return TRUE if it has one, FALSE otherwise
     */
    public boolean hasCurrentView(){
        return this.getCurrentView() != null;
    }

    /**Determines whether or not the combination of the given tag and {@link A_View} corresponds to the current {@link A_View} or not
     * @param tag being the tested tag
     * @param view being the tested {@link A_View}
     * @return TRUE if it is, FALSE otherwise
     */
    public boolean isCurrentView(String tag, A_View view){
        return tag.equals(this.currentViewTag) && view.equals(this.currentView);
    }

    /**Sets the current view to the on associated to the given tag
     * @param tag being the tag associated to the {@link A_View} that should become the current/active view
     * @return this {@link ViewsManager}
     * @throws IllegalArgumentException if there's no {@link A_View} associated to the given tag
     */
    public ViewsManager setCurrentView(String tag) throws IllegalArgumentException{
        this.assertParamsAreNotNull(tag);
        if(!this.views.containsKey(tag))
            throw new IllegalArgumentException("No such view available (requested: "+ tag + ")");

        A_View view = this.getView(tag);//The VM contains the view -> not null
        if( !this.isCurrentView(tag, view) ){
            this.removeComponent(VIEW_TAG)
                    .addComponent(VIEW_TAG, view.getViewPanel())
                    .replaceTitle(view.getViewTitle());
            if(!Objects.isNull(view.getViewMenuBar()))
                this.setMenubar(view.getViewMenuBar());

            this.currentView = view;
            this.currentViewTag = tag;
        }

        return this;
    }

    /**Displays the current view
     * @return this {@link ViewsManager}
     */
    public ViewsManager displayCurrentView(){
        if(this.hasCurrentView())
            this.getCurrentView().getViewPanel().display();

        return this;
    }

    /**Hides the current view
     * @return this {@link ViewsManager}
     */
    public ViewsManager hideCurrentView(){
        if(this.hasCurrentView())
            this.getCurrentView().getViewPanel().goInvisible();

        return this;
    }

    /**Switches from the current view to the one associated to the given tag
     * @param toTag being the tag associated to the {@link A_View} that shall become the active/current view
     * @return this {@link ViewsManager}
     */
    public ViewsManager switchTo(String toTag){
        this.assertParamsAreNotNull(toTag);
        if(!this.hasView(toTag))
            throw new IllegalArgumentException("There is no such view available (requested: " + toTag + ")");

        return this.hideCurrentView()
        .setCurrentView(toTag)
        .displayCurrentView();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    protected static boolean FINAL_LAYOUT_SET = false;
    protected static String VIEW_TAG = "view";
}
