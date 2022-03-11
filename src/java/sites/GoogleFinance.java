package sites;

import dto.ShareDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import registry.CostType;
import sites.impl.AbstractSite;

import java.io.IOException;
import java.time.LocalDateTime;


public class GoogleFinance extends AbstractSite {
    private static final String SOURCE_SIX = "https://www.google.com/finance/quote/"; // LKOH:MCX, TSLA:NASDAQ, BAYN:ETR

    public GoogleFinance(String ticket) {
        super.setName(ticket);
        isActive = true;
        dto.setSource("www.google.com");
        dto.setTicket(ticket);
    }

    @Override
    public ShareDTO task() throws Exception {
        Document doc;

        try {
            buildUrl(SOURCE_SIX + dto.getTicket().toUpperCase() + ":MCX");
            doc = getDoc();
            doc.text().substring(0, doc.text().indexOf("Stock"));
            if (doc.getElementsByClass("e1AOyf").text().contains("We couldn't find any match for your search")) {
                throw new Exception();
            }
        } catch (Exception e) {
            try {
                buildUrl(SOURCE_SIX + dto.getTicket().toUpperCase() + ":NYSE");
                doc = getDoc();
                doc.text().substring(0, doc.text().indexOf("Stock"));
                if (doc.getElementsByClass("e1AOyf").text().contains("We couldn't find any match for your search")) {
                    throw new Exception();
                }
            } catch (Exception e2) {
                try {
                    buildUrl(SOURCE_SIX + dto.getTicket().toUpperCase() + ":VIE");
                    doc = getDoc();
                    doc.text().substring(0, doc.text().indexOf("Stock"));
                    if (doc.getElementsByClass("e1AOyf").text().contains("We couldn't find any match for your search")) {
                        throw new Exception();
                    }
                } catch (Exception e3) {
                    try {
                        buildUrl(SOURCE_SIX + dto.getTicket().toUpperCase() + ":NASDAQ");
                        doc = getDoc();
                        doc.text().substring(0, doc.text().indexOf("Stock"));
                        if (doc.getElementsByClass("e1AOyf").text().contains("We couldn't find any match for your search")) {
                            throw new Exception();
                        }
                    } catch (Exception e4) {
                        System.err.println(dto.getSource() + " не нашла тикер " + dto.getTicket());
                        return null;
                    }
                }
            }
        }

        try {
            String name = doc.select("#yDmH0d > c-wiz > div > div.e1AOyf > div > div > div > div:nth-child(3) > ul > li:nth-child(1) > a > div > div > div.iLEcy > div.Q8lakc.W9Vc1e > div").text();
            dto.setName(name.isBlank() ? doc.text().substring(0, doc.text().indexOf("Stock")).trim().replaceAll("'", "").replaceAll("\"", "") : name.replaceAll("'", "").replaceAll("\"", ""));

            try {
                String cost = doc.select(
                        "#yDmH0d > c-wiz > div > div.e1AOyf > div > div > main > div.Gfxi4 > div.yWOrNb > " +
                                "div.VfPpkd-WsjYwc.VfPpkd-WsjYwc-OWXEXe-INsAgc.KC1dQ.Usd1Ac.AaN0Dd.QZMA8b > c-wiz > " +
                                "div > div:nth-child(1) > div > div.rPF6Lc > div > div:nth-child(1) > div > span > div > div").text();
                if (cost.startsWith(CostType.RUB.value()) || cost.startsWith(CostType.USD.value()) || cost.startsWith(CostType.EUR.value())) {
                    dto.setCostType(cost.substring(0, 1));
                    cost = cost.substring(1);
                }
                dto.addCoast(cost.replaceAll(",", ""));
            } catch (Exception e) {
                try {
                    dto.addCoast(doc.getElementsByClass("xVyTdb").get(0).text());
                } catch (Exception e2) {
                    dto.addCoast(doc.text().substring(doc.text().indexOf("Share ") + 6).split(" ")[0]);
                }
            }

            Elements divCont = doc.select("#yDmH0d > c-wiz > div > div.e1AOyf > div > div > main > div.Gfxi4 > div.HKO5Mb > div > div.eYanAe > div");
            Element s = divCont.stream().filter(st -> st.text().contains("Dividend")).findAny().get();
            try {
                dto.addDividend(s.select("div > div.P6K39c").text());
            } catch (Exception e) {
                String divValue = doc
                        .select("#yDmH0d > c-wiz > div > div.e1AOyf > div > div > main > div.Gfxi4 > div.HKO5Mb > div > div.eYanAe > div > div.P6K39c")
                        .get(5).text();
                if (!divValue.equals("-")) {
                    dto.addDividend(divValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dto.setLastRefresh(LocalDateTime.now());
        return dto;
    }
}
