package ladysnake.helpers.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public abstract class A_Observable {
    protected Map<String, List<I_Observer>> observers;

    public A_Observable(){ this.observers = new HashMap<>(); }

    public A_Observable trigger(String eventName, Object... args){
        if(this.eventIsRegistered(eventName))
            this.observers.get(eventName)
            .forEach(observer -> observer.handleEvent(eventName, args));

        return this;
    }

    protected A_Observable registerEvent(String eventName){
        if(!this.eventIsRegistered(eventName))
            this.observers.put(eventName, new ArrayList<>());

        return this;
    }

    public A_Observable on(String eventName, I_Observer observer){
        if(this.eventIsRegistered(eventName))
            this.observers.get(eventName).add(observer);

        return this;
    }

    public A_Observable off(String eventName, I_Observer observer){
        if(this.eventIsRegistered(eventName))
            this.observers.get(eventName).remove(observer);

        return this;
    }

    public A_Observable off(String eventName){
        if(this.eventIsRegistered(eventName))
            this.observers.replace(eventName, new ArrayList<>());

        return this;
    }


    protected boolean eventIsRegistered(String eventName){
        return this.observers.containsKey(eventName);
    }
}
