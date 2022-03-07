package sites;

import dto.ShareDTO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sites.impl.AbstractSite;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Stream;


public class GoogleFinance extends AbstractSite {
    private static final String SOURCE_SIX = "https://www.google.com/finance/quote/"; // LKOH:MCX, TSLA:NASDAQ, BAYN:ETR

    public GoogleFinance(String ticket) {
        super.setName(ticket);
        isActive = true;
        dto.setSource("www.google.com");
        dto.setTicket(ticket);
    }

    @Override
    public ShareDTO task() throws IOException {
        try {
            String link = SOURCE_SIX + dto.getTicket().toUpperCase();
            try {
                doc = Jsoup.connect(link + ":MCX").get(); //ALRM
                doc.text().substring(0, doc.text().indexOf("Stock"));
                link += ":MCX";
            } catch (Exception e) {
                try {
                    doc = Jsoup.connect(link + ":NYSE").get(); //:NASDAQ
                    doc.text().substring(0, doc.text().indexOf("Stock"));
                    link += ":NYSE";
                } catch (Exception e2) {
                    try {
                        doc = Jsoup.connect(link + ":VIE").get();
                        doc.text().substring(0, doc.text().indexOf("Stock"));
                        link += ":VIE";
                    } catch (Exception e3) {
                        try {
                            doc = Jsoup.connect(link + ":NASDAQ").get();
                            doc.text().substring(0, doc.text().indexOf("Stock"));
                            link += ":NASDAQ";
                        } catch (Exception e4) {
                            try {
                                doc = Jsoup.connect(link + ":NASDAQ").get();
                                doc.text().substring(0, doc.text().indexOf("Stock"));
                                link += ":NASDAQ";
                            } catch (Exception e5) {
                                return null;
                            }
                        }

                    }
                }
            }
            if (link == null) {return null;}
            System.out.println("ССЫЛКА: " + link);

//            System.out.println("ОБЩЕЕ: " + doc.getElementsByClass("sbnBtf xJvDsc ANokyb").first().text());
            String name = doc.select("#yDmH0d > c-wiz > div > div.e1AOyf > div > div > div > div:nth-child(3) > ul > li:nth-child(1) > a > div > div > div.iLEcy > div.Q8lakc.W9Vc1e > div").text();
            dto.setName(name.isBlank() ? doc.text().substring(0, doc.text().indexOf("Stock")).trim().replaceAll("'", "").replaceAll("\"", "") : name.replaceAll("'", "").replaceAll("\"", ""));

            try {
                String cost = doc.select(
                        "#yDmH0d > c-wiz > div > div.e1AOyf > div > div > main > div.Gfxi4 > div.yWOrNb > " +
                                "div.VfPpkd-WsjYwc.VfPpkd-WsjYwc-OWXEXe-INsAgc.KC1dQ.Usd1Ac.AaN0Dd.QZMA8b > c-wiz > " +
                                "div > div:nth-child(1) > div > div.rPF6Lc > div > div:nth-child(1) > div > span > div > div").text();
                if (cost.startsWith("₽") || cost.startsWith("$") || cost.startsWith("€")) {
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
                String divValue =
                        doc.select("#yDmH0d > c-wiz > div > div.e1AOyf > div > div > main > div.Gfxi4 > div.HKO5Mb > div > div.eYanAe > div > div.P6K39c").get(5).text();
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
