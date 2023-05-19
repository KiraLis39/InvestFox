package ru.investment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.investment.config.ApplicationProperties;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class MainClass {
    public static void main(String[] args) {
        loadUIM();

        SpringApplication app = new SpringApplication(MainClass.class);
        app.setHeadless(false);
        app.run(args);
    }

    private static void loadUIM() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e2) {
                log.warn("Has a some problem with a loading UI manager: " + e2.getMessage());
            }
        }
    }
}
