package ru.investment.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    @Value("${spring.application.version}")
    private String version;

    @Value("${app.selenide.max_error_count}")
    private short maxErrorCount;

    @Value("${app.selenide.each_state_sleep}")
    private int eachStateSleepMs;

    @Value("${app.selenide.tab_click_sleep}")
    private int tabClickSleepMs;

    @Value("${app.selenide.purchases_shift_sleep}")
    private int purchasesSleepMs;

    @Value("${app.selenide.technical_work_sleep}")
    private int technicalWorkSleepMs;

    @Value("${app.selenide.max_depth_sleep}")
    private int maxDepthSleepMs;
}
