package sites;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import core.NetProcessor;
import dto.ShareDTO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import sites.impl.AbstractSite;
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

    public ShareDTO task() throws IOException {
        String link = SOURCE_FIVE + dto.getTicket();
        System.out.println("ССЫЛКА: " + link);
        Connection conn = Jsoup.connect(link);
        conn.ignoreContentType(true);
        try {
            doc = conn.get();
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
            return null;
        }

//        mapper.writerWithDefaultPrettyPrinter().writeValue(System.out, dto);
        JsonNode nodes = NetProcessor.getMapper().readTree(doc.text());
        Iterator<JsonNode> nod = nodes.elements();
        while (nod.hasNext()) {
            JsonNode n = nod.next();
            if (n.path("title").textValue().equals(dto.getTicket())) {
                dto.addCoast(String.format("%.2f", n.get("price").asDouble()));
                dto.setCostType(n.get("currency").textValue().equalsIgnoreCase("rub") ? "₽" : "other");
//                nodes.get(1).get("dividends").toPrettyString()
                if (n.get("dividends") != null) {
                    dto.addDividend(calculateMiddleDividend(n.get("dividends")));
                }
            }
        }
        if (nodes.get(0) != null) {
            if (nodes.get(0).get("beauty_company_name") instanceof NullNode) {
                dto.setName(nodes.get(0).get("company").get("title").textValue().replaceAll("«", "").replaceAll("»", "").replaceAll("'", "").replaceAll("\"", ""));
            } else {
                dto.setName(nodes.get(0).get("beauty_company_name").textValue().replaceAll("«", "").replaceAll("»", "").replaceAll("'", "").replaceAll("\"", "")); //beauty_company_name
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
