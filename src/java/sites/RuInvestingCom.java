package sites;

import dto.ShareDTO;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
    public ShareDTO task() throws Exception {
        int index = 0;
        String ticker = dto.getTicket();
        Document doc;
        if (ticker.length() > 4 && ticker.endsWith("P")) {
            ticker = dto.getTicket().replace("P", "_p");
        }
        buildUrl(SOURCE + ticker);
        doc = getDoc();

        for (Element el : doc.getElementsByClass("fourth")) {
            if (!el.text().contains("Москва")) {
                index++;
            } else {
                break;
            }
        }

        if (doc.getElementsByClass("second").size() <= index) {
            return null;
        }

        String tickerTest = doc.getElementsByClass("second").get(index).text();
        if (tickerTest.equalsIgnoreCase(ticker) || tickerTest.equalsIgnoreCase(ticker + "DR")) {
            String aim = doc.getElementsByClass("js-inner-all-results-quote-item row").get(index - 1).attr("href");
            dto.setName(doc.getElementsByClass("third").get(index).text().replaceAll("'", "").replaceAll("\"", ""));

            buildUrl((SOURCE_SUB + aim).replaceAll("//e", "/e"));
            doc = getDoc();
        } else {
            return null;
        }

        if (doc.getElementsByClass("text-2xl").size() > 0) {
            double test = -1;
            String cost = doc.getElementsByClass("text-2xl").get(index + 1).text().replace(".", "");
            try {
                test = Double.parseDouble(cost.replace(",", "."));
            } catch (Exception e) {
                try {
                    cost = doc.select("span").eachText().get(61);
                    if (cost.contains(".") && cost.contains(",")) {
                        cost = cost.replace(".", "");
                    }
                    test = Double.parseDouble(cost.replace(",", "."));
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return null;
                }
            } finally {
                if (test > -1) {
                    dto.addCoast(test + "");
                }
            }
        }

        String ct = doc.getElementsByClass("instrument-metadata_text__2iS5i").get(4).text();
        if (ct == null) {
            ct = doc.select("span").eachText().get(66).equals("Цена в") ? doc.select("span").eachText().get(67) : "NA";
        }
        if (ct != null) {
            if (ct.equalsIgnoreCase("rub")) {
                dto.setCostType(CostType.RUB.value());
            } else if (ct.equalsIgnoreCase("usd")) {
                dto.setCostType(CostType.USD.value());
            } else if (ct.equalsIgnoreCase("eur")) {
                dto.setCostType(CostType.EUR.value());
            } else {
                throw new RuntimeException(dto.getSource() + " Check the cost type!");
            }
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

        try {
            buildUrl(getUrl().split("\\?")[0] + "-technical");
            doc = getDoc();
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
