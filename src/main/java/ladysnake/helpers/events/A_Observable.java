package ladysnake.helpers.events;

import ladysnake.helpers.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public abstract class A_Observable implements I_Observable {
    protected Map<String, List<I_Observer>> observers;

    public A_Observable(){ this.observers = new HashMap<>(); }

    @Override
    public Map<String, List<I_Observer>> getObservers() {
        return observers;
    }

    protected A_Observable registerEvent(String eventName){
        if(!this.eventIsRegistered(eventName))
            this.observers.put(eventName, new ArrayList<>());

//        Logger.triggerEvent(Logger.VERBOSE, "Trying to register the " + (this.eventIsRegistered(eventName) ? "registered" : "unregistered") + " event `" + eventName + "`");
        return this;
    }
}
