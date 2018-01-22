package ladysnake.helpers.events;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public interface I_Observer {
    I_Observer handleEvent(String eventName, Object... args);
}
