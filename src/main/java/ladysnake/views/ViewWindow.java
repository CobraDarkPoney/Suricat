package ladysnake.views;

import ladysnake.helpers.utils.I_MightNoNullParams;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ludwig GUERIN
 */
@SuppressWarnings({"unused", "WeakerAccess", "unchecked"})
public class ViewWindow extends JFrame implements I_MightNoNullParams, I_TaggedComponentContainer{
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    protected Set<TaggedComponent> components;


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**Create a I_TaggedComponentContainer and give it a title
     * @param title being the title of the window
     */
    public ViewWindow(String title){
        this(title, 0,0);
    }

    /**Create a I_TaggedComponentContainer and give it an empty title
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
        this(title, new Dimension(width, height));
    }

    /**Creates a {@link I_TaggedComponentContainer} from its title and {@link Dimension}
     * @param title being this {@link I_TaggedComponentContainer}'s title
     * @param dim being this {@link I_TaggedComponentContainer}'s {@link Dimension}
     * @throws IllegalArgumentException if dim.width or dim.height are < 0
     */
    public ViewWindow(String title, Dimension dim) throws IllegalArgumentException{
        super(title);
        this.assertParamsAreNotNull(title, dim);

        if(dim.width < 0 || dim.height < 0)
            throw new IllegalArgumentException("Width and height cannot be < 0");

        this.components = new HashSet<>();
        super.setPreferredSize(dim);
        super.setSize(dim);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @see I_TaggedComponentContainer#addComponent(TaggedComponent)
     */
    @Override
    public ViewWindow addComponent(TaggedComponent component){
        this.assertParamsAreNotNull(component);

        this.add(component.getRawComponent());
        this.components.add(component);
        return this;
    }

    /**
     * @see I_TaggedComponentContainer#addComponent(String, JComponent)
     */
    @Override
    public ViewWindow addComponent(String tag, JComponent component){
        this.assertParamsAreNotNull(tag, component);

        return this.addComponent( new TaggedComponent(tag, component) );
    }

    /**
     * @see I_TaggedComponentContainer#addComponent(TaggedComponent, Object)
     */
    @Override
    public ViewWindow addComponent(TaggedComponent component, Object constraints){
        this.assertParamsAreNotNull(component, constraints);

        super.add(component.getRawComponent(), constraints);
        this.components.add(component);
        return this;
    }

    /**
     * @see I_TaggedComponentContainer#addComponent(String, JComponent, Object)
     */
    @Override
    public ViewWindow addComponent(String tag, JComponent component, Object constraints){
        this.assertParamsAreNotNull(tag, component, constraints);
        return this.addComponent(new TaggedComponent(tag, component), constraints);
    }

    /**
     * @see I_TaggedComponentContainer#addComponent(TaggedComponent, Object, int)
     */
    @Override
    public ViewWindow addComponent(TaggedComponent component, Object constraints, int index) {
        this.assertParamsAreNotNull(component, constraints, index);
        super.add(component.getRawComponent(), constraints, index);
        this.components.add(component);
        return this;
    }

    /**
     * @see I_TaggedComponentContainer#addComponent(String, JComponent, Object, int)
     */
    @Override
    public ViewWindow addComponent(String tag, JComponent component, Object constraints, int index) {
        this.assertParamsAreNotNull(tag, component, constraints, index);
        return this.addComponent(new TaggedComponent(tag, component), constraints, index);
    }

    /**
     * @see I_TaggedComponentContainer#getComponent(String)
     */
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
    @Override
    public boolean hasComponent(String tag){
        return this.getTaggedComponent(tag) != null;
    }

    /**
     * @see I_TaggedComponentContainer#removeComponent(String)
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

    /**Sets this {@link ViewWindow}'s menu bar
     * @param menubar being the {@link JMenuBar}
     * @return this {@link ViewWindow}
     */
    public ViewWindow setMenubar(JMenuBar menubar){
        //this.assertParamsAreNotNull(menubar);

        super.setJMenuBar(menubar);
        return this;
    }

    /**Sets this {@link ViewWindow}'s  {@link LookAndFeel}
     * @param laf being the desired {@link LookAndFeel} obtained via the {@link LookAndFeelHub}
     * @return this {@link ViewWindow}
     * @throws UnsupportedLookAndFeelException If it couldn't change the {@link LookAndFeel}
     */
    public ViewWindow setLookAndFeel(LookAndFeelHub laf) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(laf.getLookAndFeel());
        SwingUtilities.updateComponentTreeUI(this);
        return this;
    }

    /**Changes this {@link ViewWindow}
     * @param title being the new title
     * @return this {@link ViewWindow}
     */
    public ViewWindow replaceTitle(String title){
        super.setTitle(title);
        return this;
    }

    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();
        ret.append("<ViewWindow>\n")
                .append("<SubTaggedComponents>\n");

        this.components.forEach(c -> {
            ret
                    .append("\t")
                    .append(c.toString())
                    .append("\n");
        });

        ret .append("</SubTaggedComponents>\n")
                .append("</ViewWindow>");

        return ret.toString();
    }
}
