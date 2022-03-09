package sites;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import core.NetProcessor;
import dto.ShareDTO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import registry.CostType;
import sites.impl.AbstractSite;
import utils.JsonMapper;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class RbkRu extends AbstractSite {
    private static final String SOURCE_FIVE = "https://quote.rbc.ru/v5/ajax/catalog/get-tickers?type=share&sort=leaders&limit=15&offset=0&search="; // https://quote.rbc.ru/ticker/"; // 172651

    public RbkRu(String ticket) {
        super.setName(ticket);
        isActive = true;
        dto.setSource("quote.rbc.ru");
        dto.setTicket(ticket);
    }

    public ShareDTO task() throws Exception {
        buildUrl(SOURCE_FIVE + dto.getTicket());
        Document doc = getDoc();

        JsonNode nodes = JsonMapper.getMapper().readTree(doc.text());
        if (nodes.get(0) != null) {
            if (nodes.get(0).get("beauty_company_name") instanceof NullNode) {
                dto.setName(nodes.get(0).get("company").get("title").textValue().replaceAll("«", "").replaceAll("»", "").replaceAll("'", "").replaceAll("\"", ""));
            } else {
                dto.setName(nodes.get(0).get("beauty_company_name").textValue().replaceAll("«", "").replaceAll("»", "").replaceAll("'", "").replaceAll("\"", "")); //beauty_company_name
            }
        }

        Iterator<JsonNode> nod = nodes.elements();
        while (nod.hasNext()) {
            JsonNode n = nod.next();
            if (n.path("title").textValue().equals(dto.getTicket())) {
                dto.addCoast(String.format("%.2f", n.get("price").asDouble()));

                if (n.get("currency").textValue().equalsIgnoreCase("rub")) {
                    dto.setCostType(CostType.RUB.value());
                } else if (n.get("currency").textValue().equalsIgnoreCase("usd")) {
                    dto.setCostType(CostType.USD.value());
                } else if (n.get("currency").textValue().equalsIgnoreCase("eur")) {
                    dto.setCostType(CostType.EUR.value());
                } else {
                    dto.setCostType(CostType.UNKNOWN.value());
                }

                if (n.get("dividends") != null) {
                    dto.addDividend(calculateMiddleDividend(n.get("dividends")));
                }
            }
        }

        dto.setLastRefresh(LocalDateTime.now());
        return dto;
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
