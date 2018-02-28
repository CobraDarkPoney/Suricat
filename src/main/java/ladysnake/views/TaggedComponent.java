package ladysnake.views;

import ladysnake.helpers.utils.I_MightNoNullParams;

import javax.swing.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Ludwig GUERIN
 */
@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class TaggedComponent implements I_MightNoNullParams, I_TaggedComponentContainer{
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * The inner component of this {@link TaggedComponent}
     */
    protected JComponent component;

    /**
     * The tag of this {@link TaggedComponent}
     */
    protected String tag;

    /**
     * The {@link TaggedComponent}s contained in this {@link TaggedComponent}
     */
    protected Set<TaggedComponent> components;


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Creates a TaggedComponent from its
     * @param tag being the name tag of this component
     * @param component being the component itself
     */
    public TaggedComponent(String tag, JComponent component){
        this.assertParamsAreNotNull(tag, component);

        this.component = component;
        this.components = new HashSet<>();
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

    /**
     * @see I_TaggedComponentContainer#addComponent(TaggedComponent)
     */
    @Override
    public TaggedComponent addComponent(TaggedComponent component){
        this.assertParamsAreNotNull(component);

        this.component.add(component.getRawComponent());
        this.components.add(component);
        return this;
    }

    /**
     * @see I_TaggedComponentContainer#addComponent(String, JComponent)
     */
    @Override
    public TaggedComponent addComponent(String tag, JComponent component){
        this.assertParamsAreNotNull(tag, component);
        return this.addComponent( new TaggedComponent(tag, component) );
    }

    /**
     * @see I_TaggedComponentContainer#addComponent(TaggedComponent, Object)
     */
    @Override
    public TaggedComponent addComponent(TaggedComponent component, Object constraints){
        this.assertParamsAreNotNull(component, constraints);

        this.component.add(component.getRawComponent(), constraints);
        this.components.add(component);
        return this;
    }

    /**
     * @see I_TaggedComponentContainer#addComponent(String, JComponent, Object)
     */
    @Override
    public I_TaggedComponentContainer addComponent(String tag, JComponent component, Object constraints){
        this.assertParamsAreNotNull(tag, component, constraints);
        return this.addComponent(new TaggedComponent(tag, component), constraints);
    }

    /**
     * @see I_TaggedComponentContainer#addComponent(TaggedComponent, Object, int)
     */
    @Override
    public I_TaggedComponentContainer addComponent(TaggedComponent component, Object constraints, int index) {
        this.assertParamsAreNotNull(component, constraints, index);
        this.component.add(component.getRawComponent(), constraints, index);
        this.components.add(component);
        return this;
    }

    /**
     * @see I_TaggedComponentContainer#addComponent(String, JComponent, Object, int)
     */
    @Override
    public I_TaggedComponentContainer addComponent(String tag, JComponent component, Object constraints, int index) {
        this.assertParamsAreNotNull(tag, component, constraints, index);
        return this.addComponent(new TaggedComponent(tag, component), constraints, index);
    }

    /**
     * @see I_TaggedComponentContainer#getComponent(String)
     */
    @Override
    public JComponent getComponent(String tag){
        this.assertParamsAreNotNull(tag);

        TaggedComponent taggedComponent = this.getTaggedComponent(tag);
        if(taggedComponent == null)
            return null;

        return taggedComponent.getRawComponent();
    }

    /**
     *@see I_TaggedComponentContainer#getComponentAs(String)
     */
    @Override
    public <T extends JComponent> T getComponentAs(String tag) throws ClassCastException {
        this.assertParamsAreNotNull(tag);

        JComponent component = this.getComponent(tag);
        if(component == null)
            return null;

        return (T)component;
    }

    /**
     *@see I_TaggedComponentContainer#getTaggedComponent(String)
     */
    @Override
    public TaggedComponent getTaggedComponent(String tag){
        this.assertParamsAreNotNull(tag);

        return this.components.stream()
                .filter(component -> component.getTag().equals(tag))
                .findFirst()
                .orElse(null);
    }

    /**
     * @see I_TaggedComponentContainer#hasComponent(String)
     */
    public boolean hasComponent(String tag){
        return this.getTaggedComponent(tag) != null;
    }

    /**
     * @see I_TaggedComponentContainer#removeComponent(String)
     */
    @Override
    public TaggedComponent removeComponent(String tag){
        this.assertParamsAreNotNull(tag);

        if(!this.hasComponent(tag))
            return this;

        TaggedComponent taggedComponent = this.getTaggedComponent(tag);
        this.components.remove(taggedComponent);
        this.component.remove(taggedComponent.getRawComponent());

        return this;
    }

    /**
     * @see I_TaggedComponentContainer#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean flag){
        this.component.setVisible(flag);
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

    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();
        ret.append("<TaggedComponent>\n")
        .append("\tTag: ").append(this.getTag()).append("\n")
        .append("<SubTaggedComponents>\n");

        String components = this.components
        .stream()
        .map(c -> c.toString()+"\n")
        .reduce(String::concat)
        .orElse("");
        ret.append(components);

        ret
        .append("</SubTaggedComponents>\n")
        .append("</TaggedComponent>");

        return ret.toString();
    }
}
