package sites;

import dto.ShareDTO;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import registry.CostType;
import sites.impl.AbstractSite;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;

public class RuInvestingCom extends AbstractSite {
    private static final String SOURCE_SUB = "https://ru.investing.com/";
    private static final String SOURCE = "https://ru.investing.com/search/?q=";


    public RuInvestingCom(String ticket) {
        super.setName(ticket);
        isActive = true;
        dto.setSource("ru.investing.com");
        dto.setTicket(ticket);
    }

    @Override
    public ShareDTO task() throws IOException {
        int index = 0;
        String ticker = dto.getTicket();
        String link = SOURCE + ticker;
        if (ticker.length() > 4 && ticker.endsWith("P")) {
            ticker = dto.getTicket().replace("P", "_p");
            link = SOURCE + ticker;
        }
        System.out.println("ССЫЛКА: " + link);
        try {
            doc = Jsoup.connect(link).get();
            for (Element el : doc.getElementsByClass("fourth")) {
                if (!el.text().contains("Москва")) {
                    index++;
                } else {break;}
            }

            if (doc.getElementsByClass("second").size() <= index) {return null;}

            if (doc.getElementsByClass("second").get(index).text().equals(ticker)) {
                String aim = doc.getElementsByClass("js-inner-all-results-quote-item row").get(index - 1).attr("href");
                dto.setName(doc.getElementsByClass("third").get(index).text().replaceAll("'", "").replaceAll("\"", ""));
                link = SOURCE_SUB + aim;
                doc = Jsoup.connect(link)
                        .ignoreHttpErrors(true)
                        .timeout(16_000)
//                        .userAgent("Mozilla/5.0 (Windows10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102")
                        .get();
            }
        } catch (HttpStatusException hse) {
            hse.printStackTrace();
            return null;
        } catch (SocketTimeoutException ste) {
            ste.printStackTrace();
            return null;
        }

        double test = -1;
        String cost = null;
        if (doc.getElementsByClass("text-2xl").size() > 0) {
            cost = doc.getElementsByClass("text-2xl").get(index + 1).text().replace(".", "");
        }
        try {
            test = Double.parseDouble(cost.replace(",", "."));
        } catch (Exception e) {
            try {
                cost = doc.select("span").eachText().get(61);
                test = Double.parseDouble(cost.replace(",", "."));
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
        dto.addCoast(test + "");

        String ct = doc.getElementsByClass("instrument-metadata_text__2iS5i").get(4).text();
        if (ct == null) {
            ct = doc.select("span").eachText().get(66).equals("Цена в") ? doc.select("span").eachText().get(67) : "NA";
        }
        if (ct.equalsIgnoreCase("rub")) {
            dto.setCostType(CostType.RUB.value());
        } else if (ct.equalsIgnoreCase("usd")) {
            dto.setCostType(CostType.USD.value());
        } else if (ct.equalsIgnoreCase("eur")) {
            dto.setCostType(CostType.EUR.value());
        }

        try {
            Elements coll = doc.getElementsByClass("flex justify-between border-b py-2 desktop:py-0.5");
            for (Element element : coll) {
                if (element.text().startsWith("Дивиденды ")) {
                    String div = doc.getElementsByClass("flex justify-between border-b py-2 desktop:py-0.5").get(8).getElementsByTag("dd").get(0).text();
                    dto.addDividend(div.split(" ")[1].replaceAll("[()%]", ""));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Elements coll = doc.getElementsByClass("companyProfileHeader");
            for (Element element : coll) {
                if (element.text().startsWith("Сектор")) {
                    dto.setSector(null);
                }
            }

            if (coll.size() == 0) {
                dto.setSector(doc.getElementsByClass("font-bold text-secondary hover:underline").get(1).text());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String recomLink = link.split("\\?")[0] + "-technical";
        doc = Jsoup.connect(recomLink).get();
        try {
            Elements indics = doc.getElementById("techinalContent").getElementsByClass("summaryTableLine");
            for (Element indic : indics) {
                dto.addRecommendation(indic.getElementsByClass("bold").text());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dto.setLastRefresh(LocalDateTime.now());

        return dto;
    }
}
