package ru.investment.config;

import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BrowserSetupConfig {

    @Value("${app.selenide.test_timeout}")
    private int timeout;

    @Value("${app.selenide.page_load_timeout}")
    private int pageLoadTimeout;

//    @Value("${app.selenide.remote_read_timeout}")
//    private int remoteReadTimeout;

//    @Value("${app.selenide.remote_connect_timeout}")
//    private int remoteConnectionTimeout;

    @Value("${app.selenide.polling_interval}")
    private int pollingInterval;

    @Value("${server.port}")
    private int serverPort;

    public void setupDefaultBrowser() {
        String os = System.getProperty("os.name").toLowerCase();
        log.info("Текущая ОС: " + os);

        Configuration.browser = Browsers.FIREFOX; // default "chrome"
        Configuration.browserSize = "1680x1050";
        Configuration.headless = true; // default "false"
        Configuration.driverManagerEnabled = true; // default "true"
        Configuration.pageLoadStrategy = "eager"; // default "normal"
        Configuration.holdBrowserOpen = false; // default "false"
        Configuration.downloadsFolder = "downloads";
        Configuration.screenshots = true; // default "true"

        Configuration.timeout = timeout; // default "4000"
        Configuration.pageLoadTimeout = pageLoadTimeout; // default "30000"
        Configuration.pollingInterval = pollingInterval; // default "200"

        log.info("Selenide сконфигурирован");
    }
}
