package ladysnake.helpers.events;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public interface I_Observer {
    void handleEvent(String eventName, Object... args);
}
