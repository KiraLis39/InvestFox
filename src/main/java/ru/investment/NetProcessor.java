package ru.investment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.openqa.selenium.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import ru.investment.config.ObjectMapperConfig;
import ru.investment.config.constants.Constant;
import ru.investment.entity.Share;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.InvestfundsRu;
import ru.investment.entity.sites.RuInvestingCom;
import ru.investment.entity.sites.TradingRu;
import ru.investment.entity.sites.impl.AbstractSite;
import ru.investment.gui.BrokersPane;
import ru.investment.gui.InvestFrame;
import ru.investment.gui.TablePane;
import ru.investment.gui.components.ShareTableRow;
import ru.investment.mapper.ShareMapper;
import ru.investment.service.ShareService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

@Slf4j
@RequiredArgsConstructor
@org.springframework.stereotype.Component
public class NetProcessor {
    private static ExecutorService exec;
    private final ShareService shareService;
    private final BrokersPane brokersPane;
    private final ShareMapper shareMapper;
    private InvestFrame investFrame;
    private double usdValue, eurValue;
    @Value("${app.sites.count}")
    private int countOfSites;

    @Autowired
    public void setInvestFrame(@Lazy InvestFrame investFrame) {
        this.investFrame = investFrame;
    }

    public void reload() throws IOException {
        saveTable();
        TablePane.clearRows();
        loadTable(investFrame.getTablePane());
    }

    public double getUSDValue() {
        return usdValue;
    }

    public double getEURValue() {
        return eurValue;
    }

    public void loadVaults() {
        Document doc;
        AbstractSite as = new AbstractSite() {
            @Override
            public ShareDTO task() {
                return null;
            }
        };

        String usdLink = "https://ru.investing.com/currencies/usd-rub";
        as.setUrl(usdLink);
        doc = as.getDoc();
        if (doc != null) {
            String usdCost = doc.getElementsByClass("text-2xl").get(2).text();
            usdValue = Double.parseDouble(usdCost.replace(",", "."));
        }

        String eurLink = "https://ru.investing.com/currencies/eur-rub";
        as.setUrl(eurLink);
        doc = as.getDoc();
        if (doc != null) {
            String eurCost = doc.getElementsByClass("text-2xl").get(2).text();
            eurValue = Double.parseDouble(eurCost.replace(",", "."));
        }
    }

    public void exit() {
        int err = 0;
        try {
            if (exec != null) {
                exec.shutdown();
                if (!exec.awaitTermination(10, TimeUnit.SECONDS)) {
                    exec.shutdownNow();
                }
            }
            saveTable();
            investFrame.getPortfel().saveBrokers();
        } catch (Exception e) {
            err += e.getMessage().length() / 10;
            log.error("Exit failed! {}", e.getMessage());
        }
        log.info("End of work with error code {}", err);
        System.exit(err);
    }

    public int exportTable() throws IOException {
        if (!new File(Constant.SHARES_DIR).exists()) {
            Files.createDirectory(Paths.get(Constant.SHARES_DIR));
        }

        int fails = 0;
        log.info("Saving shares..");
        for (ShareTableRow row : investFrame.getTableRows()) {
            try {
                ObjectMapperConfig.getMapper().writeValue(
                        new File(Constant.SHARES_DIR + row.getResultDto().getTicker() + Constant.BROKER_SAVE_POSTFIX),
                        row.getResultDto());
            } catch (IOException e) {
                fails++;
                e.printStackTrace();
            }
        }

        if (fails > 0) {
            log.error("Не удалось сохранить акций: {}", fails);
        }
        return fails;
    }

    public void exportBrokers() {
        brokersPane.exportBrokers();
    }

    public void importTable(TablePane tablePane) throws IOException {
        if (!new File(Constant.SHARES_DIR).exists()) {
            throw new NotFoundException("Требуемая директория не найдена");
        }

        File[] shares = Paths.get(Constant.SHARES_DIR).toFile().listFiles();
        ArrayList<ShareCollectedDTO> loading = new ArrayList<>();
        assert shares != null;
        for (File share : shares) {
            loading.add(ObjectMapperConfig.getMapper().readValue(share, ShareCollectedDTO.class));
        }
        Collections.sort(loading);
        tablePane.addShares(loading);
    }

    public void importBrokers() {
        brokersPane.importBrokers();
    }

    public void loadTable(TablePane tablePane) throws IOException {
        log.info("Loading shares..");
        List<ShareCollectedDTO> loading = new ArrayList<>(shareService.findAll().stream().map(shareMapper::toDto).toList());
        Collections.sort(loading);
        tablePane.addShares(loading);
    }

    public void saveTable() {
        log.info("Saving shares..");
        List<ShareCollectedDTO> shares = investFrame.getTableRows().stream().map(ShareTableRow::getResultDto).toList();
        shareService.updateOrSave(shares);
    }

    public void runScan(String tiker) throws ExecutionException, InterruptedException {
        log.info("Scanning " + tiker.toUpperCase().trim() + "...");

        CompletableFuture<ShareCollectedDTO> fut = checkTicket(tiker.toUpperCase().trim(), true)
//                .exceptionally(throwable -> null)
                .handle((r, ex) -> {
                    if (r != null) {
                        return r;
                    } else {
                        log.warn("A problem here: {}", ex.getMessage());
                        return null;
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

    public CompletableFuture<ShareCollectedDTO> checkTicket(String ticker, boolean isFirstTabShowed) throws InterruptedException {
        if (exec == null) {
            exec = Executors.newFixedThreadPool(countOfSites);
        }

        CompletableFuture<ShareCollectedDTO> cfAs = CompletableFuture.supplyAsync(() -> {
                    log.info(String.format("Check the ticket '%s'...", ticker));
                    try {
                        return proceed(ticker, isFirstTabShowed);
                    } catch (InterruptedException e) {
                        log.warn("CompletableFuture was interrupted");
                        return null;
                    }
                }, exec)
//                .exceptionally(throwable -> null)
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

    private ShareCollectedDTO proceed(String ticker, boolean isFirstTabShowed) throws InterruptedException {
        Optional<Share> exists = shareService.findShareByTicker(ticker);
        ShareCollectedDTO resultDTO = exists.isPresent() ? shareMapper.toDto(exists.get()) : new ShareCollectedDTO();
        ArrayList<AbstractSite> sites = new ArrayList<>(countOfSites) {
            {
                // selenide:
                add(new TradingRu(ticker));
                add(new RuInvestingCom(ticker));
                add(new InvestfundsRu(ticker)); // есть лот

                // jquery:
//                add(new InvestmintRu(ticker));
//                add(new RbkRu(ticker));
//                add(new TinkoffRu(ticker));
//                add(new SimplyWallSt(ticker));
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
    }
}
