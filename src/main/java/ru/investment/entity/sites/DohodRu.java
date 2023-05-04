package ru.investment.entity.sites;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.impl.AbstractSite;
import ru.investment.exceptions.root.ParsingException;

import java.time.LocalDateTime;

public class DohodRu extends AbstractSite {
    private static final String SOURCE_FOUR = "https://www.dohod.ru/ik/analytics/dividend/";

    public DohodRu(String ticket) {
        super.setName(ticket);
        isActive = true;
        getDto().setSource("www.dohod.ru");
        getDto().setTicker(ticket);
    }

    @Override
    public ShareDTO task() throws ParsingException {
        buildUrl(SOURCE_FOUR + getDto().getTicker().toLowerCase());
        Document doc = getDoc();
        if (doc == null) {
            return null;
        }

        if (!doc.getElementsByClass("container").isEmpty()
                && (doc.getElementsByClass("container").get(0).text().startsWith("404"))) {
            return null;
        }
        String nameTxt = doc.html().substring(doc.html().indexOf("leftside-col") + 27).split("</")[0]
                .replace("'", "")
                .replace("\"", "");
        getDto().setName(nameTxt);
        String div;
        Elements elem = doc.select("#dividends-brief > tbody > tr.frow > td");
        if (elem.hasText()) {
            String[] txt = elem.text().split(" ");
            if (txt[0].replace("%", "").isEmpty()) {
                div = doc.body().getElementsByClass("frow").get(0).childNodes().get(1).childNodes().get(0).toString();
                getDto().addDividend(div);
            }
        }

        getDto().addPartOfProfit(doc.select("#dividends-brief > tbody > tr.frow > td.gray").text());
        getDto().addStablePay(doc.select("#rightside-col > p:nth-child(2) > strong:nth-child(5) > span").text());
        getDto().addStableGrow(doc.select("#rightside-col > p:nth-child(2) > strong:nth-child(7) > span").text());
        getDto().addPaySum(doc.select("#leftside-col > p:nth-child(3) > strong:nth-child(5)").text());

        String nextDivPercent = null, payDate = null;
        String divData = doc.select("#leftside-col > p:nth-child(3) > strong").text();
        if (!divData.isEmpty()) {
            try {
                String[] data = doc.select("#leftside-col > p:nth-child(3) > strong")
                        .text().replaceAll(" руб.", "").split(" ");
                if (data.length > 4) {
                    nextDivPercent = data[3];
                    payDate = data[4];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (payDate != null) {
            getDto().setPayDate(LocalDateTime.parse(payDate.split("\\.")[2]
                    + "-" + payDate.split("\\.")[1]
                    + "-" + payDate.split("\\.")[0]
                    + "T12:00:00"));
        }
        if (nextDivPercent != null) {
            getDto().addDividend(nextDivPercent);
        }

        getDto().setLastRefreshDate(LocalDateTime.now());
        return getDto();
    }
}
