package ru.investment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import ru.investment.entity.Share;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.InvestfundsRu;
import ru.investment.entity.sites.RuInvestingCom;
import ru.investment.entity.sites.TradingRu;
import ru.investment.entity.sites.impl.AbstractSite;
import ru.investment.gui.InvestFrame;
import ru.investment.gui.TablePane;
import ru.investment.mapper.ShareMapper;
import ru.investment.service.ShareService;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@org.springframework.stereotype.Component
public class NetProcessor {
    private static ExecutorService exec;
    private final ShareService shareService;
    private final ShareMapper shareMapper;
    private final TablePane tablePane;
    private InvestFrame investFrame;

    @Value("${app.sites.count}")
    private int countOfSites;

    @Autowired
    public void setInvestFrame(@Lazy InvestFrame investFrame) {
        this.investFrame = investFrame;
        exec = Executors.newFixedThreadPool(countOfSites);
    }

    public void runScan(String tiker) throws ExecutionException, InterruptedException {
        log.info("Scanning " + tiker.toUpperCase().trim() + "...");

        CompletableFuture<ShareCollectedDTO> fut = checkTicket(tiker.toUpperCase().trim(), true)
                .handle((result, ex) -> {
                    if (result != null) {
                        return result;
                    } else {
                        throw new RuntimeException(ex);
                    }
                });
        while (!fut.isDone()) {
            Thread.yield();
        }

        log.info("Scanning " + tiker.toUpperCase().trim() + " is done!");
        if (fut.get() != null) {
            investFrame.updateDownPanel(fut.get());
        }
    }

    public CompletableFuture<ShareCollectedDTO> checkTicket(String ticker, boolean isFirstTabShowed
    ) throws InterruptedException {
        CompletableFuture<ShareCollectedDTO> cfAs = CompletableFuture.supplyAsync(() -> {
                    log.info(String.format("Check the ticket '%s'...", ticker));
                    try {
                        Optional<Share> exists = shareService.findShareByTicker(ticker);
                        ShareCollectedDTO resultDTO = exists.isPresent() ? shareMapper.toDto(exists.get())
                                : ShareCollectedDTO.builder().build();
                        ArrayList<AbstractSite> sites = new ArrayList<>(countOfSites) {
                            {
                                // selenide:
                                add(new TradingRu(ticker));
                                add(new RuInvestingCom(ticker));
                                add(new InvestfundsRu(ticker));

                                // jquery:
                                // add(new InvestmintRu(ticker));
                                // add(new RbkRu(ticker));
                                // add(new TinkoffRu(ticker));
                                // add(new SimplyWallSt(ticker));
                            }
                        };

                        for (AbstractSite site : sites) {
                            if (site.isActive()) {
                                try {
                                    ShareDTO data = site.task();
                                    if (data != null) {
                                        if (resultDTO.getCostType() != null && data.getCostType() != null
                                                && !resultDTO.getCostType().name().trim().equalsIgnoreCase(data.getCostType().name().trim())
                                        ) {
                                            log.info("Cost type is multiply: {} or {} (wrong company '{}'?..)",
                                                    resultDTO.getCostType(), data.getCostType(), data.getName());
                                            continue;
                                        }
                                        if (isFirstTabShowed) {
                                            investFrame.addPanel(data);
                                        }
                                        resultDTO.update(ticker, data);
                                    }
                                } catch (Exception sbe) {
                                    log.error("Exception here: {}", sbe.getMessage());
                                    sbe.printStackTrace();
                                }
                            }
                        }
                        return (resultDTO.getName() == null && resultDTO.getCost() == 0) ? null : resultDTO;
                    } catch (Exception e) {
                        log.warn("CompletableFuture has exception: {}", e.getMessage());
                        return null;
                    }
                }, exec)
                .handle((r, ex) -> {
                    if (r != null) {
                        return r;
                    } else {
                        log.warn("Problem in the executor service: " + ex);
                        return null;
                    }
                });

        log.debug("Awaits for CompletableFuture accomplished the ticker '{}'...", ticker);
        while (!cfAs.isDone() && !cfAs.isCancelled()) {
            TimeUnit.MILLISECONDS.sleep(1000);
            Thread.yield();
        }
        log.debug("The CompletableFuture accomplished the ticker '{}'!", ticker);
        return cfAs;
    }

    public void exit() {
        int err = 0;
        try {
            if (exec != null) {
                exec.shutdown();
                if (!exec.awaitTermination(6, TimeUnit.SECONDS)) {
                    exec.shutdownNow();
                }
            }
            shareService.saveTable(tablePane.getRows());
            investFrame.getPortfel().saveBrokers();
        } catch (Exception e) {
            err += e.getMessage().length() / 10;
            log.error("Exit failed! {}", e.getMessage());
        }
        log.info("End of work with error code {}", err);
        System.exit(err);
    }
}
