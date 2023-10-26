package ru.investment.entity.sites;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.impl.AbstractSite;
import ru.investment.enums.CostType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class InvestFutureRu extends AbstractSite {
    private static final String SOURCE_ONE = "https://investfuture.ru/securities/id/";
    private static final String SOURCE_ONE_SUB = "https://investfuture.ru/securities?filter_name=&filter_tiker=";
    private static final String SOURCE_TWO = "https://investfuture.ru/dividends/id/";
    private static int code = -1;

    public InvestFutureRu(String ticket) {
        super.setName(ticket);
        setActive(true);
        getDto().setSource("investfuture.ru");
        getDto().setTicker(ticket);
    }

    @Override
    public ShareDTO task() throws Exception {
        Elements docElems;
        String temp;
        buildUrl(SOURCE_ONE_SUB + getDto().getTicker());
        Document doc = getDoc();
        docElems = doc.select("#result_panel > div.nra_stat > table > tbody > tr");
        if (docElems.isEmpty()) {
            return null;
        }
        temp = docElems.html()
                .replace("<", " ")
                .replace(">", " ")
                .replace("/a?", "")
                .replace(" a ", "")
                .replace(" td ", "")
                .replace("href=\"securitiesid34\"", "")
                .replace("style=\"color:green;\"", "")
                .replace("\n", " ")
                .replace("(  +)", ";")
                .replace("\"", "")
                .replace("href=securitiesid33", "")
                .trim();

        List<String> data = Arrays.asList(temp.split("\\;"));
        if (data.size() > 1) {
            getDto().setName(data.get(1)
                    .replace("href=securitiesid\\S{1,4} ", "")
                    .replace("'", "")
                    .replace("\"", ""));
        }
        if (data.size() > 4) {
            getDto().addCoast(data.get(5));
        }

        docElems = doc.select("#result_panel > div.nra_stat > table > tbody > tr > td:nth-child(2) > a");
        temp = docElems.outerHtml().split("\"")[1].split("/")[3];
        code = Integer.parseInt(temp);

        buildUrl(SOURCE_ONE + code);
        doc = getDoc();
        try {
            String s = doc.select("#result_panel > div").get(0).text()
                    .substring(doc.select("#result_panel > div").get(0).text().indexOf("составляет")).trim();
            if (s.split("\\s")[2].equals("RUB")) {
                getDto().setCostType(CostType.RUB);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String html = doc.html();
            String[] splited = html.split("торговый лот");
            if (splited.length > 1) {
                double lot = Double.parseDouble(splited[1]
                        .split(";</li>")[0]
                        .replace("‒", "")
                        .trim());
                getDto().setLotSize((int) lot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String txt = doc.selectXpath("/html/body/div[1]/div/div[4]/div/div/div/div/div[2]/div[4]/div[3]/p[9]").text();
            if (txt.length() >= 1 && txt.length() < 6) {
                getDto().addPaySumOnShare(txt);
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        buildUrl(SOURCE_TWO + code);
        doc = getDoc();
        getDto().addDividend(doc.text().split("Доля от прибыли")[1].split(" ")[1]);

        getDto().setLastRefreshDate(LocalDateTime.now());
        return getDto();
    }
}
