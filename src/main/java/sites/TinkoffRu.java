package sites;

import com.fasterxml.jackson.databind.JsonNode;
import core.NetProcessor;
import dto.ShareDTO;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import registry.CostType;
import sites.impl.AbstractSite;
import java.io.IOException;
import java.net.SocketTimeoutException;
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
    public ShareDTO task() throws IOException {
        String link = SOURCE_THREE + dto.getTicket();
        System.out.println("ССЫЛКА: " + link);

        try {
            doc = Jsoup.connect(link).get();
        } catch (SocketTimeoutException ste) {
            System.out.println(ste.getMessage() + " > " + ste.getCause());
            return null;
        } catch (HttpStatusException hse) {
            System.out.println(hse.getMessage() + " > " + hse.getCause());
            return null;
        }

        if (doc == null || doc.getAllElements().size() < 3) {
            link = SOURCE_THREE + dto.getTicket() + "@DE";
            doc = Jsoup.connect(link).get();
        }

        Element aim = getISV();
        String data = aim.toString().replace("<script>var initialState = '", "").replace("'</script>", "");
        String preparedData = data.replaceAll("\\\\{1,2}", "\\\\").replace("\\'", "'");
        JsonNode tree = NetProcessor.getMapper().readTree(preparedData).path("stores").path("investSecurity").path(dto.getTicket());
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
            if (costType.equalsIgnoreCase("rub")) {
                dto.setCostType(CostType.RUB.value());
            } else if (costType.equalsIgnoreCase("usd")) {
                dto.setCostType(CostType.USD.value());
            } else if (costType.equalsIgnoreCase("eur")) {
                dto.setCostType(CostType.EUR.value());
            }

            dto.setLotSize(tree.path("symbol").path("lotSize").asInt());
        }

        try {
            String rec = NetProcessor.getMapper().readTree(preparedData)
                    .path("stores")
                    .path("investPrognosis")
                    .path(dto.getTicket())
                    .path("consensus")
                    .path("recommendation").textValue();
            if (rec != null) {
                dto.addRecommendation(rec);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dto.setLastRefresh(LocalDateTime.now());

        return dto;
    }

    private Element getISV() {
        Elements els = doc.getAllElements();
        for (Element el : els) {
            if (el.html().startsWith("var initialState")) {
                return el;
            }
        }
        return null;
    }
}
