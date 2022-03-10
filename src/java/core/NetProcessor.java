package core;

import components.ShareTableRow;
import dto.ResultShareDTO;
import dto.ShareDTO;
import fox.Out;
import gui.InvestFrame;
import gui.TablePane;
import sites.*;
import sites.exceptions.SiteBlockedException;
import sites.impl.AbstractSite;
import utils.JsonMapper;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.*;

public class NetProcessor {
    private static final int sitesCount = 8;
    private static final ExecutorService exec = Executors.newFixedThreadPool(sitesCount + 4);

    public static void save() throws IOException {
        if (!new File("./shares/").exists()) {
            Files.createDirectory(Paths.get("./shares/"));
        }
        Out.Print(NetProcessor.class, Out.LEVEL.INFO, "Saving shares..");
        for (Component row : InvestFrame.getTableRows()) {
            ResultShareDTO strow = ((ShareTableRow) row).getResultDto();
            String res = JsonMapper.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(strow);
            try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("./shares/" + strow.getTICKER() + ".dto"))) {
                osw.write(res);
            } catch (IOException e) {
                throw e;
            }
        }
    }

    public static void load(TablePane tablePane) throws IOException {
        if (!new File("./shares/").exists()) {
            return;
        }

        File[] shares = Paths.get("./shares/").toFile().listFiles();
        ArrayList<ResultShareDTO> loading = new ArrayList<>();
        for (File share : shares) {
            loading.add(JsonMapper.getMapper().readValue(share, ResultShareDTO.class));
        }
        Collections.sort(loading);
        tablePane.addShares(loading);
    }

    public CompletableFuture<ResultShareDTO> checkTicket(String ticket, boolean isHandle) {
        return CompletableFuture.supplyAsync(() -> {
            Out.Print(NetProcessor.class, Out.LEVEL.INFO, String.format("Check the ticket '%s'...", ticket));
            return proceed(ticket, isHandle);
        }, exec)
//                .exceptionally(throwable -> null)
                .handle((r, ex) -> {
            if (r != null) {
                return r;
            } else {
                Out.Print(NetProcessor.class, Out.LEVEL.WARN, "Problem: " + ex);
                return null;
            }
        });
    }

    private ResultShareDTO proceed(String ticket, boolean isHandle) {
        ResultShareDTO resultDTO = new ResultShareDTO();
        ArrayList<AbstractSite> sites = new ArrayList<>(sitesCount) {
            {
                add(new DohodRu(ticket));
                add(new GoogleFinance(ticket));
                add(new InvestFutureRu(ticket));
                add(new RbkRu(ticket));
                add(new RuInvestingCom(ticket));
                add(new TinkoffRu(ticket));
                add(new InvestmintRu(ticket));
                add(new InvestfundsRu(ticket));
            }
        };

        for (AbstractSite site : sites) {
            if (site.isActive()) {
                try {
                    ShareDTO data = site.task();
                    if (data != null) {
                        if (resultDTO.getCOST_TYPE() != null && data.getCostType() != null
                                && !resultDTO.getCOST_TYPE().trim().equalsIgnoreCase(data.getCostType().trim())
                        ) {
                            System.err.println("Cost type is multiply: " + resultDTO.getCOST_TYPE() + " or " + data.getCostType() + " (wrong company '" + data.getName() + "'?..)");
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
                    e.printStackTrace();
                }
            }
        }

        return resultDTO.getNAME() == null && resultDTO.getCOST() == null ? null : resultDTO;
    }
}
