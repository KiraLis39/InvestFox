package ru.investment.entity.sites;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.investment.config.ObjectMapperConfig;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.impl.AbstractSite;
import ru.investment.enums.CostType;
import ru.investment.exceptions.root.ParsingException;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
public class TinkoffRu extends AbstractSite {
    private static final String SOURCE_THREE = "https://www.tinkoff.ru/invest/stocks/";

    public TinkoffRu(String ticket) {
        super.setName(ticket);
        isActive = true;
        getDto().setSource("www.tinkoff.ru");
        getDto().setTicker(ticket);
    }

    @Override
    public ShareDTO task() throws ParsingException {
        buildUrl(SOURCE_THREE + getDto().getTicker());
        Document doc = getDoc();
        if (doc == null || doc.getAllElements().size() < 3) {
            buildUrl(SOURCE_THREE + getDto().getTicker() + "@DE");
            doc = getDoc();
        }
        if (doc == null || doc.getAllElements().size() < 3) {
            return null;
        }

        try {
            Element elems = getISV(doc.getAllElements());
            if (elems != null) {
                String data = elems.toString().replace("<script>var initialState = '", "")
                        .replace("'</script>", "");
                String preparedData = data.replaceAll("\\\\{1,2}", "\\\\").replace("\\'", "'");
                JsonNode tree = ObjectMapperConfig.getMapper().readTree(preparedData).path("stores")
                        .path("investSecurity").path(getDto().getTicker());
                if (tree.path("symbol").path("ticker").textValue() == null) {
                    return null;
                }

                if (tree.path("symbol").path("ticker").textValue().equals(getDto().getTicker())) {
                    getDto().setName(tree.path("symbol").path("showName").textValue()
                            .replace("'", "")
                            .replace("\"", ""));
                    getDto().setSector(tree.path("symbol").path("sector").textValue());

                    String costS = String.valueOf(tree.path("price").path("value").asDouble());
                    if (costS.isEmpty() || costS.equals("0.0")) {
                        costS = String.valueOf(tree.path("prices").path("last").path("value").asDouble());
                    }
                    getDto().addCoast(costS);

                    String costType = tree.path("price").path("currency").textValue();
                    if (costType == null) {
                        costType = tree.path("prices").path("last").path("currency").textValue();
                    }
                    if (costType != null) {
                        if (costType.equalsIgnoreCase("rub")) {
                            getDto().setCostType(CostType.RUB);
                        } else if (costType.equalsIgnoreCase("usd")) {
                            getDto().setCostType(CostType.USD);
                        } else if (costType.equalsIgnoreCase("eur")) {
                            getDto().setCostType(CostType.EUR);
                        }
                    }

                    getDto().setLotSize(tree.path("symbol").path("lotSize").asInt());
                }

                try {
                    String rec = ObjectMapperConfig.getMapper().readTree(preparedData)
                            .path("stores")
                            .path("investPrognosis")
                            .path(getDto().getTicker())
                            .path("consensus")
                            .path("recommendation")
                            .textValue();
                    if (rec != null) {
                        getDto().addRecommendation(rec);
                    }
                } catch (Exception e) {
                    log.error("Exception here: {}", e.getMessage());
                }
            }
        } catch (IOException e) {
            log.error("Exception here: {}", e.getMessage());
            throw new ParsingException(e);
        }

        getDto().setLastRefreshDate(LocalDateTime.now());
        return getDto();
    }

    private Element getISV(Elements els) {
        for (Element el : els) {
            if (el.html().startsWith("var initialState")) {
                return el;
            }
        }
        return null;
    }
}
