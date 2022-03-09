package sites;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import core.NetProcessor;
import dto.ShareDTO;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import registry.CostType;
import sites.impl.AbstractSite;
import utils.JsonMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class InvestfundsRu extends AbstractSite {
    private static final String SOURCE = "https://investfunds.ru";
    private static final String SOURCE_PRE = "https://investfunds.ru/stocks/?searchString=";
    private static final String SOURCE_POST = "&verifyHash=813dd92bf207bbcdc65be773606698fe";

    public InvestfundsRu(String ticket) {
        super.setName(ticket);
        isActive = true;
        dto.setSource("www.investfunds.ru");
        dto.setTicket(ticket);
    }

    @Override
    public ShareDTO task() throws Exception {
        buildUrl(SOURCE_PRE + dto.getTicket().toUpperCase() + SOURCE_POST);
        Document doc;

        JsonNode tree;
        try {
            doc = getDoc();
            tree = JsonMapper.getMapper().readTree(doc.text());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (tree.path("total").intValue() == 0) {
            System.err.println(dto.getSource() + " не нашла " + dto.getTicket());
            return null;
        }
        if (tree.path("currentCount").intValue() > 0) {
            boolean tickerExists = tree.path("currentResults").findPath("trading_grounds").findPath("ticker").textValue().equalsIgnoreCase(dto.getTicket());
            if (!tickerExists) {
                tickerExists = tree.path("currentResults").findValues("ticker").stream().filter(s -> s.textValue().equalsIgnoreCase(dto.getTicket())).findAny().isPresent();
            }
            if (tickerExists) {
                int count = 0, index = 0;
                for (String ticker : tree.findValuesAsText("ticker")) {
                    if (ticker.equalsIgnoreCase(dto.getTicket())) {
                        count++;
                    }
                }
                if (count > 1 && tree.path("currentCount").intValue() > 1) {
                    if (tree.path("total").intValue() == tree.path("currentCount").intValue()) {
                        System.err.println("Более одного типа '" + dto.getTicket() + "': " + count + " шт. " +
                                "Попытка угадать не смотря на множественные совпадения...");
                    }
                }

                tree = tree.path("currentResults");
                ArrayList<JsonNode> resArr = new ArrayList<>();
                Iterator<JsonNode> tel = tree.elements();
                while (tel.hasNext()) {
                    JsonNode nex = tel.next();
                    if (nex.has("trading_grounds")) {
                        if (nex.path("trading_grounds").get(0).has("ticker") && nex.path("trading_grounds").get(0).path("ticker").textValue().equals(dto.getTicket())) {
                            resArr.add(nex);
                        }
                    }
                }

                if (resArr.size() > 0) {
                    String newLink;
                    newLink = resArr.get(0).path("url").textValue();
                    index = Integer.parseInt(resArr.get(0).path("trading_grounds").get(0).path("id").textValue());
                    dto.setName(resArr.get(0).path("name").textValue());
                    buildUrl(SOURCE + newLink + index);
                    doc = getDoc();
                } else {
                    return null;
                }
            } else {
                System.err.println(dto.getSource() + " не нашла тикер " + dto.getTicket());
                return null;
            }
        }

        if (doc.getElementsByClass("mainPrice").size() > 0) {
            String costBase = ((TextNode) doc.getElementsByClass("mainPrice").get(0).childNodes().get(0)).text();
            if (costBase != null) {
                String cost = costBase.trim().split("\\(")[0].trim();

                if (cost.endsWith(CostType.RUB.name())) {
                    dto.addCoast(cost.replace(CostType.RUB.name(), "").trim());
                    dto.setCostType(CostType.RUB.value());
                } else if (cost.endsWith(CostType.EUR.name())) {
                    dto.addCoast(cost.replace(CostType.EUR.name(), "").trim());
                    dto.setCostType(CostType.EUR.value());
                } else if (cost.endsWith(CostType.USD.name())) {
                    dto.addCoast(cost.replace(CostType.USD.name(), "").trim());
                    dto.setCostType(CostType.USD.value());
                } else {
                    System.err.println(dto.getSource() + " нашла тикер, но валюта не ясна '" + cost + "'. Уточникте код.");
                }
            }
        }

        if (dto.getCoastList().size() > 0 || dto.getCostType() != null) {
            Elements els = doc.getElementsByClass("item");
            if (els.size() == 0) {
                throw new RuntimeException("WTF");
            }
            Element el = els.stream().filter(element -> element.text().startsWith("Торговый лот")).findAny().orElse(null);
            if (el != null) {
                dto.setLotSize((int) Double.parseDouble(el.text().replace("Торговый лот", "").replace(" ", "")));
            }

            List<Node> tmp = doc.getElementsByClass("mainParam").get(0).childNodes();
            for (int i = 0; i < tmp.size(); i++) {
                List<Node> tmp2 = tmp.get(i).childNodes();
                if (tmp2.size() > 0) {
                    TextNode tn = (TextNode) tmp2.get(1).childNodes().get(0);
                    if (tn.text().startsWith("Див. доходность ")) {
                        dto.addDividend(((TextNode) tmp2.get(3).childNodes().get(0)).text().replace("%", "").trim());
                    }
                    if (tn.text().startsWith("Отрасль")) {
                        dto.setSector(((TextNode) tmp2.get(3).childNodes().get(0)).text().trim());
                    }
                }
            }
        }

        dto.setLastRefresh(LocalDateTime.now());
        return dto;
    }
}
