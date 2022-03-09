package sites;

import dto.ShareDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import registry.CostType;
import sites.impl.AbstractSite;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class InvestFutureRu extends AbstractSite {
    private final static String SOURCE_ONE = "https://investfuture.ru/securities/id/";
    private final static String SOURCE_ONE_SUB = "https://investfuture.ru/securities?filter_name=&filter_tiker=";
    private final static String SOURCE_TWO = "https://investfuture.ru/dividends/id/";

    private static int CODE = -1;


    public InvestFutureRu(String ticket) {
        super.setName(ticket);
        isActive = true;
        dto.setSource("investfuture.ru");
        dto.setTicket(ticket);
    }

    @Override
    public ShareDTO task() throws Exception {
        Elements docElems;
        String temp;
        buildUrl(SOURCE_ONE_SUB + dto.getTicket());
        Document doc = getDoc();
        docElems = doc.select("#result_panel > div.nra_stat > table > tbody > tr");
        if (docElems.size() == 0) {
            return null;
        }
        temp = docElems.html()
                .replaceAll("<", " ")
                .replaceAll(">", " ")
                .replaceAll("/a?", "")
                .replaceAll(" a ", "")
                .replaceAll(" td ", "")
                .replaceAll("href=\"securitiesid34\"", "")
                .replaceAll("style=\"color:green;\"", "")
                .replaceAll("\n", " ")
                .replaceAll("(  +)", ";")
                .replaceAll("\"", "")
                .replaceAll("href=securitiesid33", "")
                .trim();

        List<String> data = Arrays.asList(temp.split(";"));
        dto.setName(data.get(1).replaceAll("href=securitiesid\\S{1,4} ", "").replaceAll("'", "").replaceAll("\"", ""));
        dto.addCoast(data.get(5));

        docElems = doc.select("#result_panel > div.nra_stat > table > tbody > tr > td:nth-child(2) > a");
        temp = docElems.outerHtml().split("\"")[1].split("/")[3];
        CODE = Integer.parseInt(temp);

        buildUrl(SOURCE_ONE + CODE);
        doc = getDoc();
        try {
            String s = doc.select("#result_panel > div").get(0).text().substring(doc.select("#result_panel > div").get(0).text().indexOf("составляет")).trim();
            if (s.split(" ")[2].equals("RUB")) {
                dto.setCostType(CostType.RUB.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (doc.html().split("<li>торговый лот ").length > 1) {
                Double lot = Double.parseDouble(doc.html().split("<li>торговый лот ")[1].split(";</li>")[0].replace("‒", "").trim());
                dto.setLotSize(lot.intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dto.addPaySumOnShare(doc.selectXpath("/html/body/div[1]/div/div[4]/div/div/div/div/div[2]/div[4]/div[3]/p[9]").text());

        buildUrl(SOURCE_TWO + CODE);
        doc = getDoc();
        dto.addDividend(doc.text().split("Доля от прибыли")[1].split(" ")[1]);

        dto.setLastRefresh(LocalDateTime.now());
        return dto;
    }
}
