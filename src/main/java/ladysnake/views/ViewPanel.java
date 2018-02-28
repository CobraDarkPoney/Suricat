package ladysnake.views;

import ladysnake.helpers.utils.I_MightNoNullParams;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings({"unused", "WeakerAccess", "unchecked"})
public class ViewPanel extends JPanel implements I_MightNoNullParams, I_TaggedComponentContainer{
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    protected Set<TaggedComponent> components;

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Most advanced constructor
     * @param layoutManager being this {@link ViewPanel}'s layout
     * @param isDoubleBuffered determining whether or not it should use a double buffer
     */
    public ViewPanel(LayoutManager layoutManager, boolean isDoubleBuffered){
        super(layoutManager, isDoubleBuffered);
        this.components = new HashSet<>();
    }

    /**Constructs a double buffered {@link ViewPanel} given a layout
     * @param layoutManager being this {@link ViewPanel}'s layout
     */
    public ViewPanel(LayoutManager layoutManager){
        this(layoutManager, true);
    }

    /**Constructs a {@link ViewPanel} which will use a flow layout
     * @param isDoubleBuffered determining whether or not it should use a double buffer
     */
    public ViewPanel(boolean isDoubleBuffered){
        this(new FlowLayout(), isDoubleBuffered);
    }

    public ViewPanel(){
        this(true);
    }

    public ViewPanel(JComponent component){
        this(component.getLayout(), component.isDoubleBuffered());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @see I_TaggedComponentContainer#addComponent(TaggedComponent)
     */
    @Override
    public ViewPanel addComponent(TaggedComponent component){
        this.assertParamsAreNotNull(component);

        super.add(component.getRawComponent());
        this.components.add(component);
        return this;
    }

    /**
     * @see I_TaggedComponentContainer#addComponent(String, JComponent)
     */
    @Override
    public ViewPanel addComponent(String tag, JComponent component){
        this.assertParamsAreNotNull(tag, component);
        return this.addComponent( new TaggedComponent(tag, component) );
    }

    /**
     * @see I_TaggedComponentContainer#addComponent(TaggedComponent, Object)
     */
    @Override
    public ViewPanel addComponent(TaggedComponent component, Object constraints){
        this.assertParamsAreNotNull(component, constraints);

        super.add(component.getRawComponent(), constraints);
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
        super.add(component.getRawComponent(), constraints, index);
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
    public ViewPanel removeComponent(String tag){
        this.assertParamsAreNotNull(tag);

        if(!this.hasComponent(tag))
            return this;

        TaggedComponent taggedComponent = this.getTaggedComponent(tag);
        this.components.remove(taggedComponent);
        super.remove(taggedComponent.getRawComponent());

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewPanel)) return false;
        ViewPanel viewPanel = (ViewPanel) o;
        return Objects.equals(components, viewPanel.components);
    }

    @Override
    public int hashCode() {
        return components.hashCode();
    }

    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();
        ret.append("<ViewPanel>\n")
        .append("<SubTaggedComponents>\n");

        String components = this.components
        .stream()
        .map(c -> c.toString()+"\n")
        .reduce(String::concat)
        .orElse("");
        ret.append(components);

        ret .append("</SubTaggedComponents>\n")
        .append("</ViewPanel>");

        return ret.toString();
    }
}
