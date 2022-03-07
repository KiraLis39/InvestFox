package door;

import fox.Out;
import gui.InvestFrame;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class MainClass {
    public static void main(String[] args) {
        Out.setEnabled(true);
        Out.setLogsCountAllow(10);
        Out.setErrorLevel(Out.LEVEL.INFO);
        loadUIM();
        Out.Print(MainClass.class, Out.LEVEL.INFO, "Start!");

        new InvestFrame();
    }

    private static void loadUIM() {
        Out.Print(MainClass.class, Out.LEVEL.DEBUG, "Set the UIManagers view.");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            try {UIManager.setLookAndFeel(new NimbusLookAndFeel());
            } catch (Exception e2) {
                e2.printStackTrace();
                Out.Print(MainClass.class, Out.LEVEL.WARN, "Has a some problem with a loading UI manager: " + e2.getMessage());
            }
        }
    }
}
