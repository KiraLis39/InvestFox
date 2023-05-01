package ru.investment.entity.old.sites;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.old.sites.impl.AbstractSite;
import ru.investment.enums.CostType;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class GoogleFinance extends AbstractSite {
    private static final String SOURCE_SIX = "https://www.google.com/finance/quote/"; // LKOH:MCX, TSLA:NASDAQ, BAYN:ETR

    public GoogleFinance(String ticket) {
        super.setName(ticket);
        isActive = true;
        getDto().setSource("www.google.com");
        getDto().setTicker(ticket);
    }

    @Override
    public ShareDTO task() {
        Document doc;

        try {
            buildUrl(SOURCE_SIX + getDto().getTicker().toUpperCase() + ":MCX");
            doc = getDoc();
            String someString = doc.text().substring(0, doc.text().indexOf("Stock"));
            if (doc.getElementsByClass("e1AOyf").text().contains("We couldn't find any match for your search")) {
                throw new Exception();
            }
        } catch (Exception e) {
            try {
                buildUrl(SOURCE_SIX + getDto().getTicker().toUpperCase() + ":NYSE");
                doc = getDoc();
                String someString = doc.text().substring(0, doc.text().indexOf("Stock"));
                if (doc.getElementsByClass("e1AOyf").text().contains("We couldn't find any match for your search")) {
                    throw new Exception();
                }
            } catch (Exception e2) {
                try {
                    buildUrl(SOURCE_SIX + getDto().getTicker().toUpperCase() + ":VIE");
                    doc = getDoc();
                    String someString = doc.text().substring(0, doc.text().indexOf("Stock"));
                    if (doc.getElementsByClass("e1AOyf").text().contains("We couldn't find any match for your search")) {
                        throw new Exception();
                    }
                } catch (Exception e3) {
                    try {
                        buildUrl(SOURCE_SIX + getDto().getTicker().toUpperCase() + ":NASDAQ");
                        doc = getDoc();
                        String someString = doc.text().substring(0, doc.text().indexOf("Stock"));
                        if (doc.getElementsByClass("e1AOyf").text().contains("We couldn't find any match for your search")) {
                            throw new Exception();
                        }
                    } catch (Exception e4) {
                        log.error(getDto().getSource() + " не нашла тикер " + getDto().getTicker());
                        return null;
                    }
                }
            }
        }

        try {
            String name = doc.select("#yDmH0d > c-wiz > div > div.e1AOyf > div > div > div > div:nth-child(3) > ul > li:nth-child(1) > a > div > div > div.iLEcy > div.Q8lakc.W9Vc1e > div").text();
            getDto().setName(name.isBlank()
                    ? doc.text().substring(0, doc.text().indexOf("Stock")).trim()
                    .replace("'", "")
                    .replace("\"", "")
                    : name
                    .replace("'", "")
                    .replace("\"", ""));

            try {
                String cost = doc.select(
                        "#yDmH0d > c-wiz > div > div.e1AOyf > div > div > main > div.Gfxi4 > div.yWOrNb > " +
                                "div.VfPpkd-WsjYwc.VfPpkd-WsjYwc-OWXEXe-INsAgc.KC1dQ.Usd1Ac.AaN0Dd.QZMA8b > c-wiz > " +
                                "div > div:nth-child(1) > div > div.rPF6Lc > div > div:nth-child(1) > div > span > div > div").text();
                if (cost.startsWith(CostType.RUB.value()) || cost.startsWith(CostType.USD.value()) || cost.startsWith(CostType.EUR.value())) {
                    getDto().setCostType(CostType.valueOf(cost.substring(0, 1)));
                    cost = cost.substring(1);
                }
                getDto().addCoast(cost.replace(",", ""));
            } catch (Exception e) {
                try {
                    getDto().addCoast(doc.getElementsByClass("xVyTdb").get(0).text());
                } catch (Exception e2) {
                    getDto().addCoast(doc.text().substring(doc.text().indexOf("Share ") + 6).split(" ")[0]);
                }
            }

            Elements divCont = doc.select("#yDmH0d > c-wiz > div > div.e1AOyf > div > div > main > div.Gfxi4 > div.HKO5Mb > div > div.eYanAe > div");
            Optional<Element> elOpt = divCont.stream().filter(st -> st.text().contains("Dividend")).findAny();
            if (elOpt.isPresent()) {
                try {
                    getDto().addDividend(elOpt.get().select("div > div.P6K39c").text());
                } catch (Exception e) {
                    String divValue = doc
                            .select("#yDmH0d > c-wiz > div > div.e1AOyf > div > div > main > div.Gfxi4 > div.HKO5Mb > div > div.eYanAe > div > div.P6K39c")
                            .get(5).text();
                    if (!divValue.equals("-")) {
                        getDto().addDividend(divValue);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Exception here: {}", e.getMessage());
        }

        getDto().setLastRefreshDate(LocalDateTime.now());
        return getDto();
    }
}
