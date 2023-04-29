package ru.investment.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

@Slf4j
public class DividendDataTable {
    private static final String HIGHT_DIVS = "https://ru.tradingview.com/markets/stocks-russia/market-movers-high-dividend/";
    //    "Лидеры роста":"https://ru.tradingview.com/markets/stocks-russia/market-movers-gainers/"
//    "Лидеры падения":"https://ru.tradingview.com/markets/stocks-russia/market-movers-losers/"
    public Document doc;

    @SneakyThrows
    public DividendDataTable() {
        String link = HIGHT_DIVS;
        log.info("ССЫЛКА: " + link);
        doc = Jsoup.connect(link).get();

        Element elm = doc.selectXpath("//*[@id=\"js-screener-container\"]").get(0).getElementsByTag("div").get(1).getElementsByClass("tv-data-table__tbody").get(0);
        List<Node> nodes = elm.childNodes();
        for (Node node : nodes) {
            if (node.toString().isBlank()) {
                continue;
            }
            getData(node);
        }
    }

    private String[] getData(Node childNode) {
        String[] data = new String[6];

        for (Node node : childNode.childNodes()) {
            if (node.toString().isBlank()) {
                continue;
            }

            // ticket:
            data[0] = ((Element) childNode).getElementsByClass("tv-screener__symbol").text();
            // name:
            data[1] = ((Element) childNode).getElementsByClass("tv-screener__description").text().replaceAll("\"", "");

            // cost:
            data[2] = ((Element) childNode).getElementsByClass("tv-data-table__cell tv-screener-table__cell tv-screener-table__cell--big").get(0).text();
            // dividend:
            data[3] = ((Element) childNode).getElementsByClass("tv-data-table__cell tv-screener-table__cell tv-screener-table__cell--big").get(1).text();
            // pay sum:
            data[4] = ((Element) childNode).getElementsByClass("tv-data-table__cell tv-screener-table__cell tv-screener-table__cell--big").get(2).text();
            // pay on share:
            data[5] = ((Element) childNode).getElementsByClass("tv-data-table__cell tv-screener-table__cell tv-screener-table__cell--big").get(3).text();
        }

        return data;
    }
}
