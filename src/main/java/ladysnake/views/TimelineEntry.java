package ladysnake.views;

import ladysnake.helpers.utils.I_MightNoNullParams;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class TimelineEntry extends JButton implements I_MightNoNullParams{
    protected String source, type, target;

    public TimelineEntry(String source, String type, String target){
        this.assertParamsAreNotNull(source, type, target);
        this.source = source;
        this.type = type;
        this.target = target;
        this.setLayout(new GridLayout(1,1));
        this.getInsets().set(INSET, INSET, INSET, INSET);
//        this.addComponent(LABEL, new JButton( makeLabel() ));
        this.setText( makeLabel() );
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
    }


//    public ViewPanel getPanel(){
//        ViewPanel viewPanel = new ViewPanel();
//        viewPanel.setLayout(new GridLayout(1,1));
//        viewPanel.getInsets().set(INSET, INSET, INSET, INSET);
//        viewPanel.addComponent(LABEL, new JButton( makeLabel() ));
//        return viewPanel;
//    }

    public String makeLabel(){ return "<{  " + this.source + " | " + this.type + " | " + this.target + "  }>"; }

    public final static int INSET = 12;
    public final static String SOURCE = "TimelineEntry@source";
    public final static String TYPE = "TimelineEntry@type";
    public final static String TARGET = "TimelineEntry@target";
    public final static String LABEL = "TimelineEntry@label";
}
