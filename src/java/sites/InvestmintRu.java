package sites;

import dto.ShareDTO;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import registry.CostType;
import sites.exceptions.SiteBlockedException;
import sites.impl.AbstractSite;

import java.io.*;
import java.time.LocalDateTime;

public class InvestmintRu extends AbstractSite {
    private static final String SOURCE = "https://investmint.ru/";

    public InvestmintRu(String ticket) {
        super.setName(ticket);
        isActive = false;
        dto.setSource("investmint.ru");
        dto.setTicket(ticket);
    }

    @Override
    public ShareDTO task() throws SiteBlockedException {
        boolean isRM = false;
        String ticker = dto.getTicket();
        if (ticker.endsWith("-RM")) {
            ticker = ticker.replace("RM", "spb");
            isRM = true;
        }

        buildUrl(SOURCE + ticker.toLowerCase());
        Document doc = getDoc();
        if (doc.text().startsWith("Ваш IP-адрес заблокирован")) {
            System.err.println("IP-адрес заблокирован!");
            return null;
        }

        String name = null;
        if (doc.getElementsByClass("col-12 col-md-4") != null && doc.getElementsByClass("col-12 col-md-4").size() > 0) {
            for (Node childNode : doc.getElementsByClass("col-12 col-md-4").get(0).childNodes()) {
                if (childNode.childNodes() != null && childNode.childNodes().size() > 0) {
                    if (childNode.childNodes().get(0) != null && childNode.childNodes().get(0).toString().contains("Полное название")) {
                        name = doc.getElementsByClass("col-12 col-md-4").get(0).childNodes().get(3).childNodes().get(0).toString();
                        dto.setName(name);
                        break;
                    }
                }
            }
        }
        if (doc.getElementsByClass("mb-0").size() == 0) {
            throw new SiteBlockedException("Something wrong with " + dto.getTicket() + " by " + dto.getSource());
        }
        if (name == null) {
            dto.setName(doc.getElementsByClass("mb-0").get(0).text().replace("Дивиденды ", "").replaceAll("'", "").replaceAll("\"", ""));
        }

        dto.setSector(doc.getElementsByClass("tag").get(1).text());
        String cost = isRM ? doc.getElementsByClass("num150 me-2").get(1).text() : doc.getElementsByClass("num150 me-2").get(0).text();
        if (cost.endsWith(CostType.RUB.value())) {
            dto.addCoast(cost.replace(CostType.RUB.value(), "").trim());
            dto.setCostType(CostType.RUB.value());
        } else if (cost.endsWith(CostType.EUR.value())) {
            dto.addCoast(cost.replace(CostType.EUR.value(), "").trim());
            dto.setCostType(CostType.EUR.value());
        } else if (cost.endsWith(CostType.USD.value())) {
            dto.addCoast(cost.replace(CostType.USD.value(), "").trim());
            dto.setCostType(CostType.USD.value());
        }

        for (Element el : doc.getElementsByClass("num150")) {
            if (el.getElementsByAttribute("class").get(0).attributes().toString().replace("class=", "").replace("\"", "").trim().equals("num150")) {
                dto.addDividend(el.text().replace("%", ""));
            }
        }

        if (doc.getElementById("lot") != null) {
            dto.setLotSize(Integer.parseInt(doc.getElementById("lot").text().trim()));
        } else {
            for (Element row : doc.getElementsByClass("row numbers")) {
                if (row.text().contains("Акций в лоте")) {
                    for (Node childNode : row.childNodes()) {
                        if (childNode.toString().contains("Акций в лоте")) {
                            dto.setLotSize(Integer.parseInt(childNode.childNodes().get(3).childNodes().get(0)
                                    .toString().replace("\n", "").trim())
                            );
                            break;
                        }
                    }
                }
            }
        }

        try {
            String recommendation = doc.getElementsByClass("table-responsive")
                    .get(4).getElementsByTag("table")
                    .get(0).childNodes()
                    .get(3).childNodes()
                    .get(1).childNodes()
                    .get(5).childNode(0)
                    .childNodes().get(0).toString();
            if (recommendation != null) {
                dto.addRecommendation(recommendation);
            }
        } catch (Exception e) {
            /* IGNORE RECOMMENDATIONS ABSENT */
        }

        dto.setLastRefresh(LocalDateTime.now());
        return dto;
    }
}
