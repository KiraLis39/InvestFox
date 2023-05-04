package ru.investment.entity.sites;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import ru.investment.config.ObjectMapperConfig;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.impl.AbstractSite;
import ru.investment.enums.CostType;
import ru.investment.exceptions.root.ParsingException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.OptionalDouble;

@Slf4j
public class RbkRu extends AbstractSite {
    private static final String SOURCE_FIVE = "https://quote.rbc.ru/v5/ajax/catalog/get-tickers?type=share&sort=leaders&limit=15&offset=0&search="; // https://quote.rbc.ru/ticker/"; // 172651

    public RbkRu(String ticket) {
        super.setName(ticket);
        isActive = true;
        getDto().setSource("quote.rbc.ru");
        getDto().setTicker(ticket);
    }

    public ShareDTO task() throws ParsingException {
        buildUrl(SOURCE_FIVE + getDto().getTicker());
        Document doc = getDoc();

        try {
            JsonNode nodes = ObjectMapperConfig.getMapper().readTree(doc.text());
            if (nodes.get(0) != null) {
                if (nodes.get(0).get("beauty_company_name") instanceof NullNode) {
                    getDto().setName(nodes.get(0).get("company").get("title").textValue()
                            .replace("«", "")
                            .replace("»", "")
                            .replace("'", "")
                            .replace("\"", ""));
                } else {
                    getDto().setName(nodes.get(0).get("beauty_company_name").textValue()
                            .replace("«", "")
                            .replace("»", "")
                            .replace("'", "")
                            .replace("\"", "")); //beauty_company_name
                }
            }

            Iterator<JsonNode> nod = nodes.elements();
            while (nod.hasNext()) {
                JsonNode n = nod.next();
                if (n.path("title").textValue().equals(getDto().getTicker())) {
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
        } catch (IOException e) {
            log.error("Exception here: {}", e.getMessage());
            throw new ParsingException(e);
        }

        getDto().setLastRefreshDate(LocalDateTime.now());
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

        OptionalDouble aver = Arrays.stream(dArray).average();
        if (aver.isPresent()) {
            return String.format("%.2f", aver.getAsDouble());
        } else {
            return "0";
        }
    }
}
