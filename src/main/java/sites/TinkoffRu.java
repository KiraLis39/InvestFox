package sites;

import com.fasterxml.jackson.databind.JsonNode;
import dto.ShareDTO;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import registry.CostType;
import sites.impl.AbstractSite;
import utils.JsonMapper;

import java.time.LocalDateTime;

public class TinkoffRu extends AbstractSite {
    private final static String SOURCE_THREE = "https://www.tinkoff.ru/invest/stocks/";

    public TinkoffRu(String ticket) {
        super.setName(ticket);
        isActive = true;
        dto.setSource("www.tinkoff.ru");
        dto.setTicket(ticket);
    }

    @Override
    public ShareDTO task() throws Exception {
        buildUrl(SOURCE_THREE + dto.getTicket());
        Document doc = getDoc();
        if (doc == null || doc.getAllElements().size() < 3) {
            buildUrl(SOURCE_THREE + dto.getTicket() + "@DE");
            doc = getDoc();
        }
        if (doc == null || doc.getAllElements().size() < 3) {
            return null;
        }

        String data = getISV(doc.getAllElements()).toString().replace("<script>var initialState = '", "").replace("'</script>", "");
        String preparedData = data.replaceAll("\\\\{1,2}", "\\\\").replace("\\'", "'");
        JsonNode tree = JsonMapper.getMapper().readTree(preparedData).path("stores").path("investSecurity").path(dto.getTicket());
        if (tree.path("symbol").path("ticker").textValue() == null) {return null;}

        if (tree.path("symbol").path("ticker").textValue().equals(dto.getTicket())) {
            dto.setName(tree.path("symbol").path("showName").textValue().replaceAll("'", "").replaceAll("\"", ""));
            dto.setSector(tree.path("symbol").path("sector").textValue());

            String costS = String.valueOf(tree.path("price").path("value").asDouble());
            if (costS == null || costS.isEmpty() || costS.equals("0.0")) {
                costS = String.valueOf(tree.path("prices").path("last").path("value").asDouble());
            }
            dto.addCoast(costS);

            String costType = tree.path("price").path("currency").textValue();
            if (costType == null) {
                costType = tree.path("prices").path("last").path("currency").textValue();
            }
            if (costType != null) {
                if (costType.equalsIgnoreCase("rub")) {
                    dto.setCostType(CostType.RUB.value());
                } else if (costType.equalsIgnoreCase("usd")) {
                    dto.setCostType(CostType.USD.value());
                } else if (costType.equalsIgnoreCase("eur")) {
                    dto.setCostType(CostType.EUR.value());
                }
            }

            dto.setLotSize(tree.path("symbol").path("lotSize").asInt());
        }

        try {
            String rec = JsonMapper.getMapper()
                    .readTree(preparedData)
                    .path("stores")
                    .path("investPrognosis")
                    .path(dto.getTicket())
                    .path("consensus")
                    .path("recommendation")
                    .textValue();
            if (rec != null) {
                dto.addRecommendation(rec);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dto.setLastRefresh(LocalDateTime.now());
        return dto;
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
