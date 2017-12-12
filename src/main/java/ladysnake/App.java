package ladysnake;

import ladysnake.models.*;
import ladysnake.views.*;
//import ladysnake.controllers.*;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

@SuppressWarnings({"unused", "WeakerAccess"})
public class App {
    private static void out(String msg){  System.out.println(msg); }

    public static void main(String[] args){
        ViewWindow view = new ViewWindow("Fenêtre test", 500, 500);
        view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.Y_AXIS));
        view.setMinimumSize( view.getPreferredSize() );

        view.addComponent("btn", new JButton("Click"));
        view.addComponent("label", new JLabel());
        ((JLabel)view.getComponent("label")).setText("lol");

        MenuBarBuilder menuBuilder = new MenuBarBuilder();
        JMenuBar menuBar = menuBuilder
        .addMenu(new JMenu("A Menu"))
        .setMenuMnemonic("A Menu", KeyEvent.VK_A)
        .setMenuAccessibleDescription("A Menu", "This is for help")
        .addMenuItemToMenu("A Menu", new JMenuItem("That's a text item", KeyEvent.VK_T))
        .setMenuItemAccelerator("A Menu", "That's a text item", KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_MASK))
        .setMenuItemAccessibleDescription("A Menu", "That's a text item", "This is also for help")
        .getBuilt();
        view.setMenubar(menuBar);

        try {
            view.setLookAndFeel(LookAndFeelHub.OS_DEFAULT);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            out("Could not change the look and feel");
        }
        view.display();

        out(menuBuilder.toString());
    }
}
