package ladysnake.helpers.log;

import ladysnake.helpers.events.A_Observable;
import ladysnake.helpers.events.I_Observer;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class Logger extends A_Observable{
    private static Logger instance = null;

    public static void bootstrap(){
        Logger.getInstance();
    }

    private static Logger getInstance(){
        if(instance == null)
            instance = new Logger();

        return instance;
    }

    private Logger(){
        super();
        super.registerEvent(LOG);
        super.registerEvent(ERROR);
        super.registerEvent(WARNING);
        super.registerEvent(VERBOSE);
    }

    public static void addListener(String eventName, I_Observer observer) {
        Logger.getInstance().on(eventName, observer);
    }

    public static void removeListeners(String eventName) {
        Logger.getInstance().off(eventName);
    }

    public static void removeListener(String eventName, I_Observer observer) {
        Logger.getInstance().off(eventName, observer);
    }

    public static void triggerEvent(String eventName, Object... args){
        Logger.getInstance().trigger(eventName, args);
    }

    //    public static PrintStream out = System.out;
    public final static String EVENTS_BASENAME = "Logger@";
    public final static String LOG = EVENTS_BASENAME + "log";
    public final static String ERROR = EVENTS_BASENAME + "error";
    public final static String WARNING = EVENTS_BASENAME + "warning";
    public final static String VERBOSE = EVENTS_BASENAME + "verbose";

//    public static void log(String msg){ Logger.out.println(msg); }

    /*
        signature when triggering event:

        trigger(String eventName, String message)
     */
}
