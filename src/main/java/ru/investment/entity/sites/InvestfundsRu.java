package ru.investment.entity.sites;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import ru.investment.config.ObjectMapperConfig;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.impl.AbstractSite;
import ru.investment.enums.CostType;
import ru.investment.exceptions.BadDataException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class InvestfundsRu extends AbstractSite {
    private static final String SOURCE = "https://investfunds.ru";
    private static final String SOURCE_PRE = "https://investfunds.ru/stocks/?searchString=";
    private static final String SOURCE_POST = "&verifyHash=813dd92bf207bbcdc65be773606698fe";

    public InvestfundsRu(String ticket) {
        super.setName(ticket);
        isActive = true;
        getDto().setSource("www.investfunds.ru");
        getDto().setTicker(ticket);
    }

    @Override
    public ShareDTO task() throws BadDataException {
        buildUrl(SOURCE_PRE + getDto().getTicker().toUpperCase() + SOURCE_POST);
        Document doc;

        JsonNode tree;
        try {
            doc = getDoc();
            tree = ObjectMapperConfig.getMapper().readTree(doc.text());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (tree.path("total").intValue() == 0) {
            log.warn(getDto().getSource() + " не нашла " + getDto().getTicker());
            return null;
        }
        if (tree.path("currentCount").intValue() > 0) {
            boolean tickerExists = tree.path("currentResults").findPath("trading_grounds").findPath("ticker").textValue().equalsIgnoreCase(getDto().getTicker());
            if (!tickerExists) {
                tickerExists = tree.path("currentResults").findValues("ticker").stream()
                        .anyMatch(s -> s.textValue().equalsIgnoreCase(getDto().getTicker()));
            }
            if (tickerExists) {
                int count = 0, index;
                for (String ticker : tree.findValuesAsText("ticker")) {
                    if (ticker.equalsIgnoreCase(getDto().getTicker())) {
                        count++;
                    }
                }
                if (count > 1 && tree.path("currentCount").intValue() > 1) {
                    if (tree.path("total").intValue() == tree.path("currentCount").intValue()) {
                        log.error("Более одного типа '" + getDto().getTicker() + "': " + count + " шт. " +
                                "Попытка угадать не смотря на множественные совпадения...");
                    }
                }

                tree = tree.path("currentResults");
                ArrayList<JsonNode> resArr = new ArrayList<>();
                Iterator<JsonNode> tel = tree.elements();
                while (tel.hasNext()) {
                    JsonNode nex = tel.next();
                    if (nex.has("trading_grounds")) {
                        if (nex.path("trading_grounds").get(0).has("ticker")
                                && nex.path("trading_grounds").get(0).path("ticker").textValue().equals(getDto().getTicker())) {
                            resArr.add(nex);
                        }
                    }
                }

                if (!resArr.isEmpty()) {
                    String newLink;
                    newLink = resArr.get(0).path("url").textValue();
                    index = Integer.parseInt(resArr.get(0).path("trading_grounds").get(0).path("id").textValue());
                    getDto().setName(resArr.get(0).path("name").textValue());
                    buildUrl(SOURCE + newLink + index);
                    doc = getDoc();
                } else {
                    return null;
                }
            } else {
                log.warn(getDto().getSource() + " не нашла тикер " + getDto().getTicker());
                return null;
            }
        }

        if (doc != null && !doc.getElementsByClass("mainPrice").isEmpty()) {
            String costBase = ((TextNode) doc.getElementsByClass("mainPrice").get(0).childNodes().get(0)).text();
            String cost = costBase.trim().split("\\(")[0].trim();

            if (cost.endsWith(CostType.RUB.name())) {
                getDto().addCoast(cost.replace(CostType.RUB.name(), "").trim());
                getDto().setCostType(CostType.RUB);
            } else if (cost.endsWith(CostType.EUR.name())) {
                getDto().addCoast(cost.replace(CostType.EUR.name(), "").trim());
                getDto().setCostType(CostType.EUR);
            } else if (cost.endsWith(CostType.USD.name())) {
                getDto().addCoast(cost.replace(CostType.USD.name(), "").trim());
                getDto().setCostType(CostType.USD);
            } else {
                log.error(getDto().getSource() + " нашла тикер, но валюта не ясна '" + cost + "'. Уточникте код.");
            }
        }

        if (!getDto().getCoasts().isEmpty() || getDto().getCostType() != null) {
            assert doc != null;
            Elements els = doc.getElementsByClass("item");
            if (els.isEmpty()) {
                throw new RuntimeException("WTF");
            }
            Element el = els.stream().filter(element -> element.text().startsWith("Торговый лот")).findAny().orElse(null);
            if (el != null) {
                getDto().setLotSize((int) Double.parseDouble(el.text().replace("Торговый лот", "").replace(" ", "")));
            }

            List<Node> tmp = doc.getElementsByClass("mainParam").get(0).childNodes();
            for (Node node : tmp) {
                List<Node> tmp2 = node.childNodes();
                if (!tmp2.isEmpty()) {
                    TextNode tn = (TextNode) tmp2.get(1).childNodes().get(0);
                    if (tn.text().startsWith("Див. доходность ")) {
                        getDto().addDividend(((TextNode) tmp2.get(3).childNodes().get(0)).text().replace("%", "").trim());
                    }
                    if (tn.text().startsWith("Отрасль")) {
                        getDto().setSector(((TextNode) tmp2.get(3).childNodes().get(0)).text().trim());
                    }
                }
            }
        }

        getDto().setLastRefreshDate(LocalDateTime.now());
        return getDto();
    }
}
