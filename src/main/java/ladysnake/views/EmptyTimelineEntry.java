package ladysnake.views;

import java.awt.*;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class EmptyTimelineEntry extends TimelineEntry{
    public EmptyTimelineEntry(String source, String type, String target) {
        super(source, type, target);
//        Component[] components = super.getComponents();
//        for(Component component : components)
//            super.remove(component);
        super.setContentAreaFilled(false);
        super.setBorderPainted(false);
    }

    public EmptyTimelineEntry(){ this("", "", ""); }

    @Override
    public String makeLabel() {
//        return "∅";
        return " ";
    }
}
