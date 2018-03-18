package ladysnake.views;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * An interface representing a Container that uses TaggedComponent
 * @param <T> Being a class from which this {@link I_TaggedComponentContainer} inherits from (e.g. JFrame, JPanel, etc...)
 */
@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public interface I_TaggedComponentContainer{
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Adds a {@link TaggedComponent} to this {@link I_TaggedComponentContainer}
     * @param component being the tagged component to  add
     * @return this {@link I_TaggedComponentContainer}
     */
    @NotNull
    I_TaggedComponentContainer addComponent(TaggedComponent component);

    /**Creates a {@link TaggedComponent} from a tag and a {@link JComponent} and adds it to this {@link I_TaggedComponentContainer}
     * @param tag being the tag of the given {@link JComponent}
     * @param component being the {@link JComponent} of the {@link TaggedComponent} to add
     * @return this {@link I_TaggedComponentContainer}
     */
    @NotNull
    I_TaggedComponentContainer addComponent(String tag, JComponent component);

    /**Adds a {@link TaggedComponent} with its constraints to this {@link I_TaggedComponentContainer}
     * @param component being the {@link TaggedComponent} to add
     * @param constraints being the constraints tied to the given {@link TaggedComponent}
     * @return this {@link I_TaggedComponentContainer}
     */
    @NotNull
    I_TaggedComponentContainer addComponent(TaggedComponent component, Object constraints);

    /**Constructs a {@link TaggedComponent} from its constraints  and tag as well as {@link JComponent} ; adds it to this {@link I_TaggedComponentContainer}
     * @param tag being the tag associated to the given {@link JComponent}
     * @param component being the {@link JComponent} to add
     * @param constraints being the constraints tied to the given {@link JComponent}
     * @return this {@link I_TaggedComponentContainer}
     */
    @NotNull
    I_TaggedComponentContainer addComponent(String tag, JComponent component, Object constraints);

    /**Adds a {@link TaggedComponent} with its constraints and index to this {@link I_TaggedComponentContainer}
     * @param component being the {@link TaggedComponent} to add
     * @param constraints being the constraints tied to the given {@link TaggedComponent}
     * @param index being the index of the given {@link TaggedComponent}
     * @return this {@link I_TaggedComponentContainer}
     */
    @NotNull
    I_TaggedComponentContainer addComponent(TaggedComponent component, Object constraints, int index);

    /**Constructs a {@link TaggedComponent} from its constraints  and tag as well as {@link JComponent} and index ; adds it to this {@link I_TaggedComponentContainer}
     * @param tag being the tag associated to the given {@link JComponent}
     * @param component being the {@link JComponent} to add
     * @param constraints being the constraints tied to the given {@link JComponent}
     * @param index being the index of the given {@link JComponent}
     * @return this {@link I_TaggedComponentContainer}
     */
    @NotNull
    I_TaggedComponentContainer addComponent(String tag, JComponent component, Object constraints, int index);

    /**Retrieves the {@link JComponent} associated to the given tag
     * @param tag being the tag of the desired {@link JComponent}
     * @return a {@link JComponent} if it has been found, null otherwise
     */
    @Nullable
    JComponent getComponent(String tag);

    /**Retrieves the {@link JComponent} associated to the given tag and casts it to the given type
     * @param tag being the tag of the desired {@link JComponent}
     * @param <CP> being the type to cast the {@link JComponent} to
     * @return the desired {@link CP}, or null
     * @throws ClassCastException if the {@link JComponent} couldn't be casted to {@link CP}
     */
    @Nullable
    <CP extends JComponent> CP getComponentAs(String tag) throws ClassCastException;

    /**Retrieves the {@link TaggedComponent} associated to the given tag
     * @param tag being the tag of the desired {@link TaggedComponent}
     * @return a {@link TaggedComponent} if it has been found, null otherwise
     */
    @Nullable
    TaggedComponent getTaggedComponent(String tag);

    /**Determines whether or not there's a component associated to the given tag in this {@link TaggedComponent}
     * @param tag being the tag of the component you're looking for
     * @return TRUE if it is in this {@link I_TaggedComponentContainer}, FALSE otherwise
     */
    boolean hasComponent(String tag);

    /**Removes a component from this {@link I_TaggedComponentContainer}
     * @param tag being the tag of the component to remove
     * @return this {@link I_TaggedComponentContainer}
     */
    I_TaggedComponentContainer removeComponent(String tag);

    /**Shows this {@link I_TaggedComponentContainer}
     * @return this {@link I_TaggedComponentContainer}
     */
    default I_TaggedComponentContainer display(){
        this.setVisible(true);
        return this;
    }

    /**Hides this {@link I_TaggedComponentContainer}
     * @return this {@link I_TaggedComponentContainer}
     */
    default I_TaggedComponentContainer goInvisible(){
        this.setVisible(false);
        return this;
    }

    /**
     * @see Component#setVisible(boolean)
     */
    void setVisible(boolean flag);
}
