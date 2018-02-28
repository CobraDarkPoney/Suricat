package ladysnake.views;

import com.sun.istack.internal.Nullable;
import ladysnake.helpers.events.I_Observer;
import ladysnake.helpers.utils.I_MightNoNullParams;
import ladysnake.models.DBTransactionAction;
import ladysnake.models.DBTransactionExecution;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class TimelineHub extends ViewPanel implements I_Observer, I_MightNoNullParams {
    protected List<Timeline> timelines;
    protected List<Integer> processedIndexes;

    public TimelineHub(){
        this(new ArrayList<>());
    }

    public TimelineHub(List<Timeline> timelines){
        this.timelines = new ArrayList<>(timelines);
        this.resetProcessedIndexes();
    }

    @Nullable
    protected Timeline getTimelineFor(String source){
        this.assertParamsAreNotNull(source);
        return this.timelines.stream()
        .filter(timeline -> timeline.getSource().equals(source))
        .findFirst()
        .orElse(null);
    }

    @Override
    public void handleEvent(String eventName, Object... args) {
//        System.out.println("Handling event: " + eventName);

        if(!EVENTS_HANDLED.contains(eventName))
            return;

        DBTransactionAction action = ((DBTransactionAction) args[0]);
        Integer index = action.getIndex();
        if(this.processedIndexes.contains(index))
            return;

        String source = action.getSource();
        String type = action.getLock().getName();
        String target = action.getTarget().getName();
        Timeline correspondingTimeline = this.getTimelineFor(source);
        if(correspondingTimeline == null)
            return;

        correspondingTimeline.addEntry(new TimelineEntry(source, type, target));
        this.stream().filter(timeline -> !timeline.equals(correspondingTimeline))
        .forEach(timeline -> timeline.addEntry(new EmptyTimelineEntry()));
        this.processedIndexes.add(index);
    }

    protected TimelineHub resetProcessedIndexes(){
        this.processedIndexes = new ArrayList<>();
        return this;
    }

    public boolean hasTimelineFor(String source){
        return this.timelines.stream()
        .anyMatch(timeline -> timeline.getSource().equals(source));
    }

    public TimelineHub addTimelineFor(String source) throws IOException, FontFormatException {
        if(!this.hasTimelineFor(source)) {
            Timeline timeline = new Timeline(source);
            super.addComponent(TIMELINE_BASETAG + this.timelines.size(), timeline);
            this.timelines.add(timeline);
        }

        return this;
    }

//    public TimelineHub removeTimelineFor(String source){
//        if(this.hasTimelineFor(source))
//            this.timelines.remove( this.getTimelineFor(source) );
//
//        return this;
//    }

    public Iterator<Timeline> iterator(){ return new ArrayList<>(this.timelines).iterator(); }

    public Stream<Timeline> stream(){ return this.timelines.stream(); }

    public final static String TIMELINE_BASETAG = "TimelineHub@timeline#";

    public final static List<String> EVENTS_HANDLED = Arrays.asList(DBTransactionExecution.STEP_BEING_PROCESSED);
}
