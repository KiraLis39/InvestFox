package ru.investment.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.investment.config.constants.Constant;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class RollingEvents {

    /**
     * Автоматическое переключение статусов Опросов
     */
    @Async
    @Scheduled(initialDelay = 6_000, fixedRate = Constant.NEXT_STATUS_SWITCH_MINUTES * 60 * 1_000)
    public void autoStatusSwitcher() {
        try {
            log.info("Scheduled check for update events statuses...");
//            schedulerService.updateStatesByDate(null);
//            eventsService.notifyAllByDate();
            log.info("Scheduled check for update events statuses accomplished.");
        } catch (Exception e) {
            log.info("Scheduled check for update events statuses failed: {}", ExceptionsUtil.getFullExceptionMessage(e));
        }
    }

    // пока что переопределено org.springframework.statemachine.config.configuration.StateMachineCommonConfiguration
//    @Bean
//    public TaskScheduler taskScheduler() {
//        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
//        threadPoolTaskScheduler.setPoolSize(Constant.SCHEDULER_POOL_SIZE);
//        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
//        return threadPoolTaskScheduler;
//    }
}
