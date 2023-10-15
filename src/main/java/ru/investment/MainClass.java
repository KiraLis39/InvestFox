package ru.investment;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import ru.investment.config.ApplicationProperties;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

@Slf4j
//@EnableAsync
//@EnableStateMachine
@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class MainClass {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        SpringApplication app = new SpringApplication(MainClass.class);
        app.setDefaultProperties(Map.of("spring.profiles.active", "dev"));
        app.setHeadless(false);

        loadUIM();

        logApplicationStartup(app.run(args).getEnvironment());
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store"))
                .map(key -> "https").orElse("http");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional
                .ofNullable(env.getProperty("server.servlet.context-path"))
                .filter(StringUtils::isNotBlank)
                .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info(
                """
                            ----------------------------------------------------------
                            \tApplication '{}' is running! Access URLs:
                            \tLocal: \t\t{}://localhost:{}{}
                            \tExternal: \t{}://{}:{}{}
                            \tSwagger: \t{}://localhost:{}{}{}
                            \tProfile(s): \t{}
                            ----------------------------------------------------------
                        """,
                env.getProperty("spring.application.name"),
                protocol, serverPort, contextPath,
                protocol, hostAddress, serverPort, contextPath,
                protocol, serverPort, contextPath, "/swagger-ui/index.html",
                env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
        );
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
