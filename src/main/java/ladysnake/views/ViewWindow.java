package ladysnake.views;

import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ludwig GUERIN
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ViewWindow extends JFrame{
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
        super(title);
        this.components = new HashSet<>();
    }

    /**Create a ViewWindow and give it an empty title
     */
    public ViewWindow(){
        this("");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Adds a {@link TaggedComponent} to this {@link ViewWindow}
     * @param component being the component to add
     * @return this {@link ViewWindow}
     */
    public ViewWindow addComponent(TaggedComponent component){
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
        return this.addComponent( new TaggedComponent(tag, component) );
    }

    /**Retrieves the {@link JComponent} associated to the given tag
     * @param tag being the tag of the desired {@link JComponent}
     * @return a {@link JComponent} if it has been found, null otherwise
     */
    @Nullable
    public JComponent getComponent(String tag){
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
        if(!this.hasComponent(tag))
            return this;

        TaggedComponent taggedComponent = this.getTaggedComponent(tag);
        this.components.remove(taggedComponent);
        super.remove(taggedComponent.getRawComponent());

        return this;
    }
}
