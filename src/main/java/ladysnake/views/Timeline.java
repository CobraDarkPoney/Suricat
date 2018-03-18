package ladysnake.views;

import ladysnake.App;
import ladysnake.helpers.utils.I_MightNoNullParams;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class Timeline extends ViewPanel implements I_MightNoNullParams {
    protected String source;
    protected List<TimelineEntry> entries;

    public String getSource(){ return this.source; }
    public List<TimelineEntry> getEntries() { return entries; }

    public Timeline(String source) throws IOException, FontFormatException {
        this.assertParamsAreNotNull(source);
        this.source = source;
        this.entries = new ArrayList<>();

        this.setLayout(new BorderLayout());
        this.addComponent(HEADER_TAG, makeSourceDisplay(this.getSource()), BorderLayout.NORTH);
        this.addComponent(ENTRIES_HOLDER, new ViewPanel(), BorderLayout.CENTER);
        ViewPanel entriesHolder = this.getComponentAs(ENTRIES_HOLDER);
//        entriesHolder.setLayout(new GridLayout(MIN_ROWS, this.entries.size()));
        entriesHolder.setLayout(new BoxLayout(entriesHolder, BoxLayout.Y_AXIS));
        for(int index = 0, size = this.entries.size() ; index < size ; index+=1)
            entriesHolder.addComponent(ENTRY_BASETAG + index, this.entries.get(index));
    }

    protected JButton makeSourceDisplay(String source) throws IOException, FontFormatException {
        this.assertParamsAreNotNull(source);
        JButton label = new JButton(source);
        Font robotoMedium = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream(App.ROBOTO_MEDIUM_PATH)).deriveFont(SOURCE_SIZE);
        label.setFont(robotoMedium);
        return label;
    }

    public Timeline addEntry(TimelineEntry entry){
        this.<ViewPanel>getComponentAs(ENTRIES_HOLDER).addComponent(ENTRY_BASETAG + this.entries.size(), entry);
        this.entries.add(entry);
        return this;
    }

    public Timeline reset(){
        this.<ViewPanel>getComponentAs(ENTRIES_HOLDER).removeAll();
        super.repaint();
        super.revalidate();
        return this;
    }

//    public ViewPanel getPanel() throws IOException, FontFormatException {
//        ViewPanel viewPanel = new ViewPanel();
//        viewPanel.setLayout(new BorderLayout());
//        viewPanel.addComponent(HEADER_TAG, makeSourceDisplay(this.getSource()), BorderLayout.NORTH);
//        viewPanel.addComponent(ENTRIES_HOLDER, new ViewPanel(), BorderLayout.CENTER);
//        ViewPanel entriesHolder = viewPanel.getComponentAs(ENTRIES_HOLDER);
//        entriesHolder.setLayout(new GridLayout(MIN_ROWS, this.entries.size()));
//        for(int index = 0, size = this.entries.size() ; index < size ; index+=1)
//            entriesHolder.addComponent(ENTRY_BASETAG + index, this.entries.get(index).getPanel());
//
//        return viewPanel;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Timeline)) return false;
        Timeline timeline = (Timeline) o;
        return Objects.equals(getSource(), timeline.getSource());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource());
    }

    public final static String SOURCE_DISPLAY = "Timeline@source";

    public final static float SOURCE_SIZE = 14f;
    public final static float TEXT_SIZE = 10f;
    public final static int WIDTH = 60;//px

    public final static int MIN_ROWS = 6;
    public final static String HEADER_TAG = "Timeline@header_tag";
    public final static String ENTRIES_HOLDER = "Timeline@entries_holder";
    public final static String ENTRY_BASETAG = "Timeline@entry#";
}
