package ladysnake.views;

import javax.swing.*;
import java.util.Objects;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess", "SpellCheckingInspection"})
public class ExecutionMenuBar {
    protected static MenuBarBuilder builder;
    protected static JMenuBar built;

    protected static JMenu makeMenu(String text){
        JMenu menu = new JMenu(text);
        menu.setText(text);
        return menu;
    }

    protected static JMenuBar build(){
        JMenu reset = makeMenu(MENU__RESET);
        JMenu run = makeMenu(MENU__RUN);
        JMenu prev = makeMenu(MENU__STEP_BACKWARD);
        JMenu next = makeMenu(MENU__STEP_FORWARD);

        run.setEnabled(false);
        next.setEnabled(false);

        reset.setToolTipText("X | R");
        next.setToolTipText("D | \u2192");
        prev.setToolTipText("Q | \u2190");
        run.setToolTipText("\u21b5 | \u21b9");


        getBuilder()
        .addMenu(reset)
        .addMenu(run)
        .addMenu(prev)
        .addMenu(next);

        return getBuilder().getBuilt();
    }

    public static MenuBarBuilder getBuilder(){
        if(Objects.isNull(builder))
            builder = MenuBarBuilder.getNewBuilder();

        return builder;
    }

    public static JMenuBar getMenuBar() {
        if(Objects.isNull(built))
            built = build();

        return built;
    }


    public final static String MENU__RESET = "↺";//BASENAME + "menu_reset";
    public final static String MENU__RUN = "▶";//BASENAME + "menu_run";
    public final static String MENU__STEP_FORWARD = "\u2771";//"↪";//"⤷";//BASENAME + "menu_run";
    public final static String MENU__STEP_BACKWARD = "\u2770";//"↩";//"⤶";//BASENAME + "menu_run";
}
