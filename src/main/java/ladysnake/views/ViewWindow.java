package ladysnake.views;

import com.sun.istack.internal.Nullable;
import ladysnake.helpers.utils.I_MightNoNullParams;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ludwig GUERIN
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ViewWindow extends JFrame implements I_MightNoNullParams{
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    protected Set<TaggedComponent> components;


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Create a ViewWindow and give it a title
     * @param title being the title of the window
     */
    public ViewWindow(String title){
        this(title, 0,0);
        this.assertParamsAreNotNull(title);
    }

    /**Create a ViewWindow and give it an empty title
     */
    public ViewWindow(){
        this("");
    }

    /**Complete constructor
     * @param title being the window's title
     * @param width being the window's preferred width
     * @param height being the window's preferred height
     * @throws IllegalArgumentException if width or height are < 0
     */
    public ViewWindow(String title, int width, int height) throws IllegalArgumentException{
        super(title);

        if(width < 0 || height < 0)
            throw new IllegalArgumentException("Width and height cannot be < 0");

        this.components = new HashSet<>();
        Dimension dim = new Dimension(width, height);
        super.setPreferredSize(dim);
        super.setSize(dim);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Adds a {@link TaggedComponent} to this {@link ViewWindow}
     * @param component being the component to add
     * @return this {@link ViewWindow}
     */
    public ViewWindow addComponent(TaggedComponent component){
        this.assertParamsAreNotNull(component);

        this.add(component.getRawComponent());
        this.components.add(component);
        return this;
    }

    /**Adds a {@link JComponent} to this {@link ViewWindow}
     * @param tag being the tag associated to the given component
     * @param component being the component to add to this {@link ViewWindow}
     * @return this {@link ViewWindow}
     */
    public ViewWindow addComponent(String tag, JComponent component){
        this.assertParamsAreNotNull(tag, component);

        return this.addComponent( new TaggedComponent(tag, component) );
    }

    /**Retrieves the {@link JComponent} associated to the given tag
     * @param tag being the tag of the desired {@link JComponent}
     * @return a {@link JComponent} if it has been found, null otherwise
     */
    @Nullable
    public JComponent getComponent(String tag){
        this.assertParamsAreNotNull(tag);

        TaggedComponent taggedComponent = this.getTaggedComponent(tag);
        if(taggedComponent == null)
            return null;

        return taggedComponent.getRawComponent();
    }

    /**Retrieves the {@link TaggedComponent} associated to the given tag
     * @param tag being the tag of the desired {@link TaggedComponent}
     * @return a {@link TaggedComponent} if it has been found, null otherwise
     */
    @Nullable
    public TaggedComponent getTaggedComponent(String tag){
        this.assertParamsAreNotNull(tag);

        return this.components.stream()
        .filter(component -> component.getTag().equals(tag))
        .findFirst()
        .orElse(null);
    }

    /**Determines whether or not there's a component associated to the given tag in this {@link TaggedComponent}
     * @param tag being the tag of the component you're looking for
     * @return TRUE if it is in this {@link ViewWindow}, FALSE otherwise
     */
    public boolean hasComponent(String tag){
        return this.getTaggedComponent(tag) != null;
    }

    /**Removes a component from this {@link ViewWindow}
     * @param tag being the tag of the component to remove
     * @return this {@link ViewWindow}
     */
    public ViewWindow removeComponent(String tag){
        this.assertParamsAreNotNull(tag);

        if(!this.hasComponent(tag))
            return this;

        TaggedComponent taggedComponent = this.getTaggedComponent(tag);
        this.components.remove(taggedComponent);
        super.remove(taggedComponent.getRawComponent());

        return this;
    }

    public ViewWindow setMenubar(JMenuBar menubar){
        this.assertParamsAreNotNull(menubar);

        super.setJMenuBar(menubar);
        return this;
    }

    public ViewWindow display(){
        super.setVisible(true);
        return this;
    }

    public ViewWindow goInvisible(){
        super.setVisible(false);
        return this;
    }

    public ViewWindow setLookAndFeel(LookAndFeelHub laf) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(laf.getLookAndFeel());
        SwingUtilities.updateComponentTreeUI(this);
        return this;
    }
}
