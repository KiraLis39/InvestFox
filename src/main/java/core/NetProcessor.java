package core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class NetProcessor {
    private static ObjectMapper mapper = new ObjectMapper();

    private final static String SOURCE = "https://ru.tradingview.com/symbols/";
    private final static String COST_FIELD = "";
    private final static String COST_TYPE = "";
//    { "@context": "http://schema.org", "@type": "Corporation", "tickerSymbol": "UPRO", "name": "ЮНИПРО ПАО АО" ,...


    public static void testPrint(String symbol) throws IOException {
        String result = "START:\n\n";

        Document doc = Jsoup.connect(SOURCE + symbol).get();
//        System.out.println("TITLE: " + doc.title() + " (" + doc.data() + ")");

        Elements newsHeadlines = doc.selectXpath("/html/body/div[2]/div[4]/div[3]/header/div/div[2]/div[1]/div[2]");
        for (Element headline : newsHeadlines) {
            result += headline.className() + ": " + headline.data();
        }

        // headline.childElementsList().get(0).childNodes - ИЗУЧИТЬ!

        result += "\nFINISH.";

        System.out.println(result);
    }
}
