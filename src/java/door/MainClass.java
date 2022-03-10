package door;

import fox.Out;
import gui.InvestFrame;
import render.foxLFui.FoxLookAndFeel;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;


public class MainClass {
    public static void main(String[] args) {
        Out.setEnabled(true);
        Out.setLogsCountAllow(6);
        Out.setErrorLevel(Out.LEVEL.INFO);
        loadUIM();
        Out.Print(MainClass.class, Out.LEVEL.INFO, "Start!");

        new InvestFrame();
    }

    private static void loadUIM() {
        Out.Print(MainClass.class, Out.LEVEL.DEBUG, "Set the UIManagers view.");
        try {UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception e2) {
            try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e3) {
                try {UIManager.setLookAndFeel(new FoxLookAndFeel());
                } catch (Exception e) {
                    e2.printStackTrace();
                    e3.printStackTrace();
                    e.printStackTrace();
                    Out.Print(MainClass.class, Out.LEVEL.WARN, "Has a some problem with a loading UI manager: " + e.getMessage());
                }
            }
        }

//            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
//            UIManager.setLookAndFeel(new MetalLookAndFeel());
    }
}
