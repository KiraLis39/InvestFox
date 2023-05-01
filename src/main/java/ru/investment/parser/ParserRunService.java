package ru.investment.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;
import ru.investment.config.BrowserSetupConfig;
import ru.investment.parser.enums.ParserEvents;
import ru.investment.parser.enums.ParserStates;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParserRunService {
    private final ApplicationContext appCtx;
    private final BrowserSetupConfig browserSetupConfig;
    private final StateMachineFactory<ParserStates, ParserEvents> stateMachineFactory;
    private final ConcurrentMap<String, ParserThread> taskThreadsMap = new ConcurrentHashMap<>(6);
    private final ConcurrentMap<String, ParserRunnable> tasksMap = new ConcurrentHashMap<>(6);

    @PostConstruct
    public void init() {
        log.debug("Detect and setting up browser...");
        browserSetupConfig.setupDefaultBrowser();
        log.info("Parser service is active now");
    }

    public void start(String taskBeanName) {
//        log.debug("Запуск потока парсера '" + taskBeanName + "'...");
//        UUID uuid = UUID.randomUUID();
//
//        ParserRunnable parserTask = (ParserRunnable) appCtx.getBean(
//                taskBeanName.substring(0, 1).toLowerCase() + taskBeanName.substring(1)
//        );
//        parserTask.setUuid(uuid);
//
//        ParserTaskDTO parserTaskDTO = new ParserTaskDTO() {
//            {
//                setUuid(uuid);
//                setBeanName(ParserRunnable.class.getSimpleName());
//                setType("Purchases URL Parser (ZakupkiGov)");
//                setWebSiteUrl("https://zakupki.gov.ru/");
//                setState(ParserStates.INIT);
//                setStartedDate(Instant.now());
//                setParsedItemsCount(0L);
//                setParsedPagesCount(0L);
//                setErrorCount(0L);
//                setIsAutostarted(true); // автозапуск старых потоков при рестарте сервиса
//            }
//        };
//
//        tasksMap.put(guid, parserTask);
//
//        parserTask.setParserRunService(this);
//        parserTask.setParserThread(
//                new Thread(parserTask) {
//                    {
//                        setName(taskBeanName + "_" + guid);
//                        start();
//                    }
//                }
//        );
    }

//    public void start(String taskBeanName) {
//        try {
//            log.debug("Запуск потока парсера '" + taskBeanName + "'...");
//            UUID uuid = UUID.randomUUID();
//            ParserTask task = new ParserTask();
//            task.setUuid(uuid);
//            task.setBeanName(taskBeanName);
//
//            buildNewOneParserThread(task);
//            taskThreadsMap.get(uuid).start();
//        } catch (Throwable t) {
//            log.warn("The service is busy now. Try to run it again later please.");
//        }
//    }

    private void buildNewOneParserThread() {
//        ParserThread parserThread = new ParserThread(
//            appCtx.getBean(CardsTaskRunnable.class).name(task.getBeanName() + "_" + task.getUuid())
//        );
//        parserThread.setUuid(task.getUuid());
//        parserThread.setStateMachine(stateMachineFactory.getStateMachine("ZakupkiGov_PurchaseItem"));
//        parserThread.store(ParserThread.ENVIRONMENTS.TASK, task);
//        parserThread.setThreadState(Thread.State.RUNNABLE.name());
//        parserThread.setCardParseTimeStart(System.currentTimeMillis());
//        taskThreadsMap.put(task.getUuid(), parserThread);
    }

    public void finish(String guid) {
//        if (taskThreadsMap.containsKey(guid)) {
//            try {
//                log.info("User requested stopping the task {}", guid);
//            } catch (NoSuchElementException nsee) {
//                log.warn("NoSuchElementException was here. Возможно, парсер просто был прерван вручную.", nsee);
//            } catch (Exception e) {
//                log.error("Завершение задачи " + guid + " с ошибкой: " + e.getMessage());
//            }
//        }
    }

    public void stop(UUID uuid) {

    }
}
