package core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import components.FOptionPane;
import components.ShareTableRow;
import dto.ResultShareDTO;
import dto.ShareDTO;
import fox.Out;
import gui.InvestFrame;
import gui.TablePane;
import sites.*;
import sites.exceptions.SiteBlockedException;
import sites.impl.AbstractSite;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NetProcessor {
    private static ObjectMapper mapper = new ObjectMapper();
    private static ExecutorService exec = Executors.newWorkStealingPool();

    static {
        try {
            final JavaTimeModule timeModule = new JavaTimeModule();
            timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
            );
            mapper.registerModule(timeModule);
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Future<ResultShareDTO> checkTicket(String ticket) throws ExecutionException, InterruptedException {
        return exec.submit(() -> {
            Thread.sleep(1000);
            Out.Print(NetProcessor.class, Out.LEVEL.INFO, String.format("Check the ticket '%s'...", ticket));
            return proceed(ticket);
        });
    }

    private ResultShareDTO proceed(String ticket) {
        Out.Print(NetProcessor.class, Out.LEVEL.INFO, String.format("\nUpdate the ticket '%s'...", ticket));
        ArrayList<AbstractSite> sites = new ArrayList<>(6) {
            {
                add(new DohodRu(ticket));
                add(new GoogleFinance(ticket));
                add(new InvestFutureRu(ticket));
                add(new RbkRu(ticket));
                add(new RuInvestingCom(ticket));
                add(new TinkoffRu(ticket));
                add(new InvestmintRu(ticket));
            }
        };

        ResultShareDTO resultDTO = new ResultShareDTO();
        InvestFrame.clearPanel();

        for (AbstractSite site : sites) {
            if (site.isActive()) {
                ShareDTO data = null;
                try {data = site.task();
                } catch (SiteBlockedException e) {
                    System.err.println("Возможно сайт заблокирован!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (data != null) {
                    InvestFrame.updatePanel(data);
                    resultDTO.update(ticket, data);
                }
            }
        }

        return resultDTO;
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static void save() throws IOException {
        if (!new File("./shares/").exists()) {
            Files.createDirectory(Paths.get("./shares/"));
        }

        for (Component row : InvestFrame.getTableRows()) {
            ResultShareDTO strow = ((ShareTableRow) row).getResultDto();
            String res = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(strow);
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
            loading.add(mapper.readValue(share, ResultShareDTO.class));
        }
        Collections.sort(loading);
        tablePane.addShares(loading);
    }
}
