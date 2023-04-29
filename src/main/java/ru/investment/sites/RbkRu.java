package ru.investment.sites;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import ru.investment.dto.ShareDTO;
import ru.investment.enums.CostType;
import ru.investment.sites.impl.AbstractSite;
import ru.investment.utils.JsonMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

@Slf4j
public class RbkRu extends AbstractSite {
    private static final String SOURCE_FIVE = "https://quote.rbc.ru/v5/ajax/catalog/get-tickers?type=share&sort=leaders&limit=15&offset=0&search="; // https://quote.rbc.ru/ticker/"; // 172651

    public RbkRu(String ticket) {
        super.setName(ticket);
        isActive = true;
        getDto().setSource("quote.rbc.ru");
        getDto().setTicket(ticket);
    }

    public ShareDTO task() throws Exception {
        buildUrl(SOURCE_FIVE + getDto().getTicket());
        Document doc = getDoc();

        JsonNode nodes = JsonMapper.getMapper().readTree(doc.text());
        if (nodes.get(0) != null) {
            if (nodes.get(0).get("beauty_company_name") instanceof NullNode) {
                getDto().setName(nodes.get(0).get("company").get("title").textValue().replaceAll("«", "").replaceAll("»", "").replaceAll("'", "").replaceAll("\"", ""));
            } else {
                getDto().setName(nodes.get(0).get("beauty_company_name").textValue().replaceAll("«", "").replaceAll("»", "").replaceAll("'", "").replaceAll("\"", "")); //beauty_company_name
            }
        }

        Iterator<JsonNode> nod = nodes.elements();
        while (nod.hasNext()) {
            JsonNode n = nod.next();
            if (n.path("title").textValue().equals(getDto().getTicket())) {
                getDto().addCoast(String.format("%.2f", n.get("price").asDouble()));

                if (n.get("currency").textValue().equalsIgnoreCase("rub")) {
                    getDto().setCostType(CostType.RUB);
                } else if (n.get("currency").textValue().equalsIgnoreCase("usd")) {
                    getDto().setCostType(CostType.USD);
                } else if (n.get("currency").textValue().equalsIgnoreCase("eur")) {
                    getDto().setCostType(CostType.EUR);
                } else {
                    getDto().setCostType(CostType.UNKNOWN);
                }

                if (n.get("dividends") != null) {
                    getDto().addDividend(calculateMiddleDividend(n.get("dividends")));
                }
            }
        }

        getDto().setLastRefresh(LocalDateTime.now());
        return getDto();
    }

    private String calculateMiddleDividend(JsonNode dividends) {
        ArrayList<Double> divs = new ArrayList<>();
        for (int i = 0; i < dividends.size(); i++) {
            if (dividends.get(i).get("dividends_yield") instanceof NullNode) {
                continue;
            }
            String value = dividends.get(i).get("dividends_yield").textValue();
            divs.add(Double.parseDouble(value));
        }

        double[] dArray = new double[divs.size()];
        int i = 0;
        for (double d : divs) {
            dArray[i++] = d;
        }

        return String.format("%.2f", Arrays.stream(dArray).average().getAsDouble());
    }
}
