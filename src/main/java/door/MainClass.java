package door;

import core.NetProcessor;
import fox.Out;
import gui.InvestFrame;

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
                Out.Print(MainClass.class, Out.LEVEL.WARN, "Has a some problem with a loading UI manager: " + e3.getMessage());
            }
        }

//            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
//            UIManager.setLookAndFeel(new MetalLookAndFeel());
    }

    public static void exit() {
        try {
            int err = 0;
            err += NetProcessor.saveTable();
            err += InvestFrame.getPortfel().saveBrokers();
            Out.Print(InvestFrame.class, Out.LEVEL.INFO, "End of work.");
            System.exit(err + Out.close());
        } catch (Exception e2) {
            e2.printStackTrace();
            Out.Print(InvestFrame.class, Out.LEVEL.INFO, "Exit failed!");
        }
    }
}
