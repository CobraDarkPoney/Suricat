package ladysnake.helpers.log;

import ladysnake.helpers.events.A_Observable;
import java.io.PrintStream;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class Logger extends A_Observable{
    public Logger(){
        super();
        super.registerEvent(LOG);
    }

    public static PrintStream out = System.out;
    public final static String LOG = "Logger@log";

    public static void log(String msg){ Logger.out.println(msg); }

    /*
        signature when triggering event:

        trigger(String eventName, String message)
     */
}
