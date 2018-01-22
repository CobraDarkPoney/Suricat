package ladysnake.helpers.log;

import ladysnake.helpers.events.I_Observer;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public interface LoggerObserver extends I_Observer{
    @Override
    default I_Observer handleEvent(String eventName, Object... args){
        if(eventName.equals(Logger.LOG)){
            String message = (String)args[0];
            Logger.log(message);
        }

        return this;
    }
}
