package ladysnake;

import ladysnake.helpers.events.I_Observer;
import ladysnake.helpers.utils.I_Stringify;
import ladysnake.models.DBLockList;
import ladysnake.models.DBTransaction;
import ladysnake.models.DBTransactionExecution;
import ladysnake.models.ModelsManager;

import java.io.IOException;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class TextApp {
    protected ModelsManager mm;

    public TextApp(String URI) throws IOException {
        this.mm = ModelsManager.fromFile(URI);
        this.mm.getLockList()
        .on(DBLockList.ADD_LOCK, TextApp::eventHandler)
        .on(DBLockList.RM_LOCK, TextApp::eventHandler)
        .on(DBLockList.ADD_PENDING, TextApp::eventHandler)
        .on(DBLockList.RM_PENDING, TextApp::eventHandler);

        this.mm.getExecution()
        .on(DBTransactionExecution.INDEX, (eventName, args) -> {
                displayEventName(eventName);
                String index = args[0].toString();
                out("Current index: " + index);
        }).on(DBTransactionExecution.STOP, (eventName, args) -> displayEventName(eventName));
    }

    public static void out(Object o){ System.out.println(o); }
    public static void displayEventName(String eventName){ out("Event triggered: " + eventName); }
    public static void eventHandler(String eventName, Object... args){
        I_Stringify argument = (I_Stringify)args[0];
        displayEventName(eventName);
        out(argument.stringify());
    }

    public static void main(String[] args) throws IOException {
        if(args.length != 1)
            throw new RuntimeException("Not enough arguments: java {this program} {path to your json file}");

        final String fileURI = args[0];
        TextApp app = new TextApp(fileURI);
        app.mm.getExecution().run();
        final int pendingsLeft = app.mm.getLockList().getPendings().size();

        if(pendingsLeft > 0)
            out("Blocage !!");
    }
}
