package ru.investment.core;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import ru.investment.components.ShareTableRow;
import ru.investment.dto.ResultShareDTO;
import ru.investment.dto.ShareDTO;
import ru.investment.gui.InvestFrame;
import ru.investment.gui.TablePane;
import ru.investment.sites.*;
import ru.investment.sites.exceptions.SiteBlockedException;
import ru.investment.sites.impl.AbstractSite;
import ru.investment.utils.JsonMapper;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class NetProcessor {
    private static ExecutorService exec;
    @Value("${app.sites.count}")
    private int countOfSites;
    private double usdValue, eurValue;

    public static int saveTable() throws IOException {
        if (!new File("./shares/").exists()) {
            Files.createDirectory(Paths.get("./shares/"));
        }
        log.info("Saving shares..");
        int fails = 0;
        for (Component row : InvestFrame.getTableRows()) {
            ResultShareDTO strow = ((ShareTableRow) row).getResultDto();
            String res = JsonMapper.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(strow);
            try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("./shares/" + strow.getTicker() + ".ru.investment.dto"))) {
                osw.write(res);
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

    public void loadTable(TablePane tablePane) throws IOException {
        if (!new File("./shares/").exists()) {
            return;
        }

        File[] shares = Paths.get("./shares/").toFile().listFiles();
        ArrayList<ResultShareDTO> loading = new ArrayList<>();
        assert shares != null;
        for (File share : shares) {
            loading.add(JsonMapper.getMapper().readValue(share, ResultShareDTO.class));
        }
        Collections.sort(loading);
        tablePane.addShares(loading);
    }

    public void reload() throws IOException {
        saveTable();
        TablePane.clearRows();
        loadTable(InvestFrame.getTablePane());
    }

    public double getUSDValue() {
        return usdValue;
    }

    public double getEURValue() {
        return eurValue;
    }

    public CompletableFuture<ResultShareDTO> checkTicket(String ticket, boolean isHandle) {
        if (exec == null) {
            exec = Executors.newFixedThreadPool(countOfSites + 4);
        }

        return CompletableFuture.supplyAsync(() -> {
                    log.info(String.format("Check the ticket '%s'...", ticket));
                    return proceed(ticket, isHandle);
                }, exec)
//                .exceptionally(throwable -> null)
                .handle((r, ex) -> {
                    if (r != null) {
                        return r;
                    } else {
                        log.warn("Problem: " + ex);
                        return null;
                    }
                });
    }

    private ResultShareDTO proceed(String ticket, boolean isHandle) {
        ResultShareDTO resultDTO = new ResultShareDTO();
        ArrayList<AbstractSite> sites = new ArrayList<>(countOfSites) {
            {
                add(new DohodRu(ticket));
                add(new GoogleFinance(ticket));
                add(new InvestFutureRu(ticket));
                add(new RbkRu(ticket));
                add(new RuInvestingCom(ticket));
                add(new TinkoffRu(ticket));
                add(new InvestfundsRu(ticket));
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
                        if (isHandle) {
                            InvestFrame.addPanel(data);
                        }
                        resultDTO.update(ticket, data);
                    }
                } catch (SiteBlockedException sbe) {
                    sbe.printStackTrace();
                } catch (Exception e) {
                    log.error("Exception here: {}", e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return resultDTO.getName() == null && resultDTO.getCost() == null ? null : resultDTO;
    }

    public void loadValutes() {
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
}
