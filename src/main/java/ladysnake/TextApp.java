package ladysnake;

import ladysnake.helpers.events.I_Observer;
import ladysnake.helpers.utils.I_Stringify;
import ladysnake.models.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class TextApp {
    protected ModelsManager mm;

    public static Map<String, String> eventDescription = new HashMap<String, String>(){{
        put(DBLockList.ADD_LOCK, "Ajout d'un verrou à la pile");
        put(DBLockList.RM_LOCK, "Retrait d'un verrou de la pile");
        put(DBLockList.ADD_PENDING, "Ajout d'un verrou à la liste de verrous en attente");
        put(DBLockList.RM_PENDING, "Retrait d'un verrou de la liste de verrous en attente");
        put(DBTransactionExecution.INDEX, "Index de l'action courante");
        put(DBTransactionExecution.STOP, "Arrêt d'exécution");
    }};

    public static String getEventDescription(String eventName){
        return eventDescription.getOrDefault(eventName, eventName);
    }

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
                out("Index: " + index);
                //out("");
        }).on(DBTransactionExecution.STOP, (eventName, args) -> displayEventName(eventName));
    }

    public static void out(Object o){ System.out.println(o); }
    public static void displayEventName(String eventName){ out("> Évènement déclenché : " + getEventDescription(eventName)); }
    public static void eventHandler(String eventName, Object... args){
        /*I_Stringify argument = (I_Stringify)args[0];
        displayEventName(eventName);
        out(argument.stringify());*/
        
        if(args[0] instanceof DBTransactionAction)
            eventHandler(eventName, ((DBTransactionAction) args[0]));

        if(args[0] instanceof DBLockList.Lock){
            switch (eventName){
                case DBLockList.ADD_LOCK:
                    eventHandler(eventName, ((DBLockList.Lock) args[0]), ((String) args[1]));
                    break;
                default:
                    eventHandler(eventName, ((DBLockList.Lock) args[0]));
                    break;
            }
        }

        out("");
    }

    public static String formatLock(String source, String actionType, String lock, String target){
        return "<"
        + String.join(", ", source, actionType, lock, target)
        + ">";
    }

    public static String formatLock(String source, String lock, String target){
        return "<"
                + String.join(", ", source, lock, target)
                + ">";
    }

    public static void eventHandler(String eventName, DBTransactionAction action){
        String source = action.getSource();
        String actionType = action.getActionType().getName();
        String target = action.getTarget().getName();
        String lock = action.getLock().getName();
        String repr = formatLock(source, actionType, lock, target);

        displayEventName(eventName);
        out(repr);
    }

    public static void eventHandler(String eventName, DBLockList.Lock lock, String actionType){
        String source = lock.getSource();
        String target = lock.getTarget().getName();
        String lockType = lock.getType().getName();
        String repr = formatLock(source, actionType, lockType, target);

        displayEventName(eventName);
        out(repr);
    }

    public static void eventHandler(String eventName, DBLockList.Lock lock){
        String source = lock.getSource();
        String target = lock.getTarget().getName();
        String lockType = lock.getType().getName();
        String repr = formatLock(source, lockType, target);

        displayEventName(eventName);
        out(repr);
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
