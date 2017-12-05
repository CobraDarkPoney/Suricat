package ladysnake.views;

import javax.swing.*;

/**
 * @author Ludwig GUERIN
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class TaggedComponent{
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    protected JComponent component;
    protected String tag;


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Creates a TaggedComponent from its
     * @param tag being the name tag of this component
     * @param component being the component itself
     */
    public TaggedComponent(String tag, JComponent component){
        this.component = component;
        this.tag = tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Retrieve the {@link JComponent} inside this {@link TaggedComponent}
     * @return the raw component associated to this {@link TaggedComponent}
     */
    public JComponent getRawComponent() {
        return this.component;
    }

    /**Retrieve the tag as a {@link String}
     * @return the tag of this {@link TaggedComponent}
     */
    public String getTag(){
        return this.tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaggedComponent)) return false;

        TaggedComponent that = (TaggedComponent) o;

        return (component != null ? component.equals(that.component) : that.component == null) && (tag != null ? tag.equals(that.tag) : that.tag == null);
    }

    @Override
    public int hashCode() {
        return this.tag.hashCode();
    }
}
