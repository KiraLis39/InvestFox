package ru.investment.entity.sites;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.impl.AbstractSite;
import ru.investment.enums.CostType;
import ru.investment.exceptions.root.ParsingException;

import java.time.LocalDateTime;

@Slf4j
public class RuInvestingCom extends AbstractSite {
    private static final String SOURCE_SUB = "https://ru.investing.com/";
    private static final String SOURCE_URL = "https://ru.investing.com/search/?q=";


    public RuInvestingCom(String ticket) {
        super.setName(ticket);
        isActive = true;
        getDto().setSource("ru.investing.com");
        getDto().setTicker(ticket);
    }

    @Override
    public ShareDTO task() throws ParsingException {
        int index = 0;
        String ticker = getDto().getTicker();
        Document doc;
        if (ticker.length() > 4 && ticker.endsWith("P")) {
            ticker = getDto().getTicker().replace("P", "_p");
        }
        buildUrl(SOURCE_URL + ticker);
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
            String aim = doc.getElementsByClass("js-inner-all-results-quote-item row")
                    .get(index - 1).attr("href").replace("`", "%60");
            getDto().setName(doc.getElementsByClass("third").get(index).text()
                    .replace("'", "")
                    .replace("\"", ""));

            buildUrl((SOURCE_SUB + aim)
                    .replace("//e", "/e"));
            doc = getDoc();
        } else {
            return null;
        }

        Elements elems = doc.getElementsByClass("text-2xl");
        if (!elems.isEmpty() && elems.size() > 1) {
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
                    getDto().addCoast(test + "");
                }
            }
        }

        elems = doc.getElementsByClass("instrument-metadata_text__2iS5i");
        if (elems.size() > 3) {
            String ct = doc.getElementsByClass("instrument-metadata_text__2iS5i").get(4).text();
            ct = doc.select("span").eachText().get(66).equals("Цена в") ? doc.select("span").eachText().get(67) : "NA";

            if (ct != null) {
                if (ct.equalsIgnoreCase("rub")) {
                    getDto().setCostType(CostType.RUB);
                } else if (ct.equalsIgnoreCase("usd")) {
                    getDto().setCostType(CostType.USD);
                } else if (ct.equalsIgnoreCase("eur")) {
                    getDto().setCostType(CostType.EUR);
                } else {
                    throw new ParsingException(getDto().getSource() + " Check the cost type!");
                }
            }
        }

        try {
            Elements coll = doc.getElementsByClass("flex justify-between border-b py-2 desktop:py-0.5");
            for (Element element : coll) {
                if (element.text().startsWith("Дивиденды ")) {
                    String div = doc.getElementsByClass("flex justify-between border-b py-2 desktop:py-0.5").get(8).getElementsByTag("dd").get(0).text();
                    getDto().addDividend(div.split(" ")[1].replaceAll("[()%]", ""));
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        }

        try {
            Elements coll = doc.getElementsByClass("companyProfileHeader");
            for (Element element : coll) {
                if (element.text().startsWith("Сектор")) {
                    getDto().setSector(null);
                }
            }

            if (coll.isEmpty()) {
                getDto().setSector(doc.getElementsByClass("font-bold text-secondary hover:underline").get(1).text());
            }
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        }

        try {
            buildUrl(getUrl().split("\\?")[0] + "-technical");
            doc = getDoc();
            Elements indics = doc.getElementById("techinalContent").getElementsByClass("summaryTableLine");
            for (Element indic : indics) {
                getDto().addRecommendation(indic.getElementsByClass("bold").text());
            }
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        }

        getDto().setLastRefreshDate(LocalDateTime.now());
        return getDto();
    }
}
