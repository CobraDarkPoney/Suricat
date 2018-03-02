package ladysnake.helpers.events;

import ladysnake.helpers.utils.I_MightNoNullParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public interface I_Observable extends I_MightNoNullParams{
    Map<String, List<I_Observer>> getObservers();

    default I_Observable trigger(String eventName, Object... args){
        this.assertParamsAreNotNull(eventName, args);
//        Logger.triggerEvent(Logger.VERBOSE, "Triggering the " + (this.eventIsRegistered(eventName) ? "registered" : "unregistered") + " event `" + eventName + "`");
        if(this.eventIsRegistered(eventName))
            this.getObservers().get(eventName)
            .forEach(observer -> observer.handleEvent(eventName, args));

        return this;
    }

    default I_Observable on(String eventName, I_Observer observer){
        this.assertParamsAreNotNull(eventName, observer);
//        Logger.triggerEvent(Logger.VERBOSE, "Adding a listener to the " + (this.eventIsRegistered(eventName) ? "registered" : "unregistered") + " event `" + eventName + "`");
        if(this.eventIsRegistered(eventName))
            this.getObservers().get(eventName).add(observer);

        return this;
    }

    default I_Observable off(String eventName, I_Observer observer){
        this.assertParamsAreNotNull(eventName, observer);
//        Logger.triggerEvent(Logger.VERBOSE, "Removing a listener for the " + (this.eventIsRegistered(eventName) ? "registered" : "unregistered") + " event `" + eventName + "`");
        if(this.eventIsRegistered(eventName))
            this.getObservers().get(eventName).remove(observer);

        return this;
    }

    default I_Observable off(String eventName){
        this.assertParamsAreNotNull(eventName);
//        Logger.triggerEvent(Logger.VERBOSE, "Removing all listeners for " + (this.eventIsRegistered(eventName) ? "registered" : "unregistered") + " event `" + eventName + "`");
        if(this.eventIsRegistered(eventName))
            this.getObservers().replace(eventName, new ArrayList<>());

        return this;
    }


    default boolean eventIsRegistered(String eventName){
        this.assertParamsAreNotNull(eventName);
        return this.getObservers().containsKey(eventName);
    }
}
