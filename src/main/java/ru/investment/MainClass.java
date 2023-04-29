package ru.investment;

import lombok.extern.slf4j.Slf4j;
import ru.investment.core.NetProcessor;
import ru.investment.gui.InvestFrame;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

@Slf4j
public class MainClass {

    public static void main(String[] args) {
        loadUIM();
        log.info("Start!");

        new InvestFrame();
    }

    private static void loadUIM() {
        log.info("Set the UIManagers view.");

        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e2) {
                e2.printStackTrace();
                log.info("Has a some problem with a loading UI manager: " + e2.getMessage());
            }
        }
    }

    public static void exit() {
        try {
            int err = 0;
            err += NetProcessor.saveTable();
            InvestFrame.getPortfel().saveBrokers();
            log.info("End of work.");
            System.exit(err);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exit failed! {}", e.getMessage());
        }
    }
}
