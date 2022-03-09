package sites;

import dto.ShareDTO;
import org.jsoup.nodes.Document;
import sites.impl.AbstractSite;

import java.io.IOException;
import java.time.LocalDateTime;

public class DohodRu extends AbstractSite {
    private static final String SOURCE_FOUR = "https://www.dohod.ru/ik/analytics/dividend/";

    public DohodRu(String ticket) {
        super.setName(ticket);
        isActive = true;
        dto.setSource("www.dohod.ru");
        dto.setTicket(ticket);
    }

    @Override
    public ShareDTO task() throws Exception {
        buildUrl(SOURCE_FOUR + dto.getTicket().toLowerCase());
        Document doc = getDoc();
        if (doc == null) {
            return null;
        }

        if (doc.getElementsByClass("container").size() > 0) {
            if (doc.getElementsByClass("container").get(0).text().startsWith("404")) {return null;}
        }
        dto.setName(doc.html().substring(doc.html().indexOf("leftside-col") + 28).split("</")[0].replaceAll("'", "").replaceAll("\"", ""));
        String div = doc.select("#dividends-brief > tbody > tr.frow > td").text().split(" ")[0].replaceAll("%", "");
        if (div == null) {
            div = doc.body().getElementsByClass("frow").get(0).childNodes().get(1).childNodes().get(0).toString();
        }
        dto.addDividend(div);
        dto.addPartOfProfit(doc.select("#dividends-brief > tbody > tr.frow > td.gray").text());
        dto.addStablePay(doc.select("#rightside-col > p:nth-child(2) > strong:nth-child(5) > span").text());
        dto.addStableGrow(doc.select("#rightside-col > p:nth-child(2) > strong:nth-child(7) > span").text());
        dto.addPaySum(doc.select("#leftside-col > p:nth-child(3) > strong:nth-child(5)").text());

        String nextDivPercent = null, payDate = null;
        String divData = doc.select("#leftside-col > p:nth-child(3) > strong").text();
        if (divData != null && !divData.isEmpty()) {
            try {
                String[] data = doc.select("#leftside-col > p:nth-child(3) > strong").text().replaceAll(" руб.", "").split(" ");
                if (data.length > 4) {
                    nextDivPercent = data[3];
                    payDate = data[4];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (payDate != null) {
            dto.setPayDate(LocalDateTime.parse(payDate.split("\\.")[2] + "-" + payDate.split("\\.")[1] + "-" + payDate.split("\\.")[0] + "T12:00:00"));
        }
        if (nextDivPercent != null) {
            dto.addDividend(nextDivPercent);
        }

        dto.setLastRefresh(LocalDateTime.now());
        return dto;
    }
}
