package core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetProcessor {
    private static ObjectMapper mapper = new ObjectMapper();

    private final static String SOURCE_ONE = "https://investfuture.ru/securities/id/";
    private static final String SOURCE_ONE_SUB = "https://investfuture.ru/securities?filter_name=&filter_tiker=";
    private final static String SOURCE_TWO = "https://investfuture.ru/dividends/id/";
    private final static String SOURCE_THREE = "https://www.tinkoff.ru/invest/stocks/";
    private static final String SOURCE_FOUR = "https://www.dohod.ru/ik/analytics/dividend/";
    private static final String SOURCE_FIVE = "https://quote.rbc.ru/ticker/"; // 172651
    private static final String SOURCE_SIX = "https://www.google.com/finance/quote/"; // LKOH:MCX

    private static Document doc;

    private static String TICKET;
    private static String NAME;
    private static String SECTOR;
    private static String COAST;
    private static String COST_TYPE;
    private static String DIVIDENTS;
    private static String INFO;
    private static String RECOMENDATION;

    private static String PAY_SUM;
    private static String PAY_SUM_ON_SHARE;
    private static String PART_OF_PROFIT;
    private static String STABLE_PAY;
    private static String STABLE_GROW;
    private static LocalDateTime PAY_DATE, LAST_REFRESH;

    private static Integer CODE = -1;


    public static void testPrint(String ticket) {
        TICKET = ticket;

        source01();
        source02();
        source03();
        source04();
        source05();
        source06();

        // result output:
        System.out.println();
        System.out.println("Тикет:\t" + TICKET);
        System.out.println("Имя:\t" + NAME);
        System.out.println("Сектор:\t" + SECTOR);
        System.out.println("Цена:\t" + COAST + " " + COST_TYPE);
        System.out.println("Дивиденты:\t" + DIVIDENTS);
        System.out.println("Рекомендация:\t" + RECOMENDATION.split(" ")[1]);
        System.out.println("Инфо:\t" + INFO);

        System.out.println("Ближ.выплата:\t" + PAY_SUM + " " + PAY_DATE);
        System.out.println("Стабильность выплат:\t" + STABLE_PAY);
        System.out.println("Стабильность роста:\t" + STABLE_GROW);
        System.out.println("Доля от прибыли:\t" + PART_OF_PROFIT);
        System.out.println("Выплата на акцию:\t" + PAY_SUM_ON_SHARE);
    }

    private static void source01() {
        try {
            doc = Jsoup.connect(SOURCE_ONE_SUB + TICKET).get();
            Elements docElems = doc.select("#result_panel > div.nra_stat > table > tbody > tr");

            String temp = docElems.html()
                    .replaceAll("<", " ")
                    .replaceAll(">", " ")
                    .replaceAll("/a?", "")
                    .replaceAll(" a ", "")
                    .replaceAll(" td ", "")
                    .replaceAll("href=\"securitiesid34\"", "")
                    .replaceAll("style=\"color:green;\"", "")
                    .replaceAll("\n", " ")
                    .replaceAll("(  +)", ";").trim();

            List<String> data = Arrays.asList(temp.split(";"));
            System.out.println("\nDATA:\n" +
                    "Ticket:\t" + data.get(0) +
                    "\nName:\t" + data.get(1) +
                    "\nOpen:\t" + data.get(2) +
                    "\nMin:\t" + data.get(3) +
                    "\nMax:\t" + data.get(4) +
                    "\nLast:\t" + data.get(5) +
                    "\nChange:\t" + data.get(6) +
                    "\nUpdated: " + data.get(7) + "\n"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Elements docElems = doc.select("#result_panel > div.nra_stat > table > tbody > tr > td:nth-child(2) > a");
            String temp = docElems.outerHtml().split("\"")[1].split("/")[3];
            CODE = Integer.parseInt(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String result = "";
        try {
            Document doc = Jsoup.connect(SOURCE_ONE + CODE).get();
            Elements elms = doc.select("#company_quote_table_gor > tbody");
            for (Element elm : elms) {
                result += clean(elm.html());
            }

            PAY_SUM_ON_SHARE = doc.selectXpath("/html/body/div[1]/div/div[4]/div/div/div/div/div[2]/div[4]/div[3]/p[9]").text();
        } catch (Exception e) {
            result = "Не удалось прочитать.";
            e.printStackTrace();
        }
        System.out.println(result);
    }

    private static void source02() {
        String result;
        try {
            Document docDiv = Jsoup.connect(SOURCE_TWO + CODE).get();
            int begin2 = docDiv.html().indexOf("<table class=\"table table-striped\">");
            int finish2 = docDiv.html().indexOf("<h2 style=\"font-size: 18px;\">");
            result = Arrays.toString(docDiv.html().substring(begin2, finish2)
                    .replaceAll("<*td>", "")
                    .replaceAll("<*th>", "")
                    .replaceAll("<*tr>", "")
                    .replaceAll("<*tbody>", "")
                    .replaceAll("<*table>", "")
                    .replaceAll("<*div>", "")
                    .replaceAll("<*thead>", "")
                    .replaceAll("</", "")
                    .replaceAll("\n", "")
                    .replaceAll(" +", " ")
                    .trim()
                    .split(", ")
            ).split("> ")[1];
        } catch (Exception e) {
            result = "Не удалось прочитать.";
        }

        System.out.println(result);
    }

    private static void source03() {
        try {
            doc = Jsoup.connect(SOURCE_THREE + TICKET).get();

            Elements coastElems = doc.selectXpath("/html/body/div[1]/div/div[2]/div/div[1]/div[2]/div[1]/div[2]/div/div[2]/div[1]/div/div/div[2]/span/span");
            for (Element elm : coastElems) {
                COAST += elm.text();
            }

            Elements nameElems = doc.selectXpath("/html/body/div[1]/div/div[2]/div/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/h1/span[1]");
            for (Element elm : nameElems) {
                NAME += elm.text();
            }

            Elements sectorElems = doc.selectXpath("/html/body/div[1]/div/div[2]/div/div[1]/div[2]/div[1]/div[1]/div[1]");
            for (Element elm : sectorElems) {
                SECTOR += elm.text();
            }

            Elements infoElems = doc.selectXpath("/html/body/div[1]/div/div[2]/div/div[1]/div[2]/div[1]/div[1]/div[4]");
            int start = infoElems.text().indexOf("О компании ");
            INFO = infoElems.text().substring(start, infoElems.text().indexOf("Рекомендация "));

            Elements tipElems = doc.selectXpath("/html/body/div[1]/div/div[2]/div/div[1]/div[2]/div[1]/div[1]/div[4]");
            int index = tipElems.text().indexOf("Рекомендация");
            RECOMENDATION = tipElems.text().substring(index, index + 64);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void source04() {
        Document doc2 = null;
        try {
            doc2 = Jsoup.connect(SOURCE_FOUR + TICKET.toLowerCase()).get();
            DIVIDENTS = doc2.select("#dividends-brief > tbody > tr.frow > td.greendark").text();
            PART_OF_PROFIT = doc2.select("#dividends-brief > tbody > tr.frow > td.gray").text();
            STABLE_PAY = doc2.select("#rightside-col > p:nth-child(2) > strong:nth-child(5) > span").text();
            STABLE_GROW = doc2.select("#rightside-col > p:nth-child(2) > strong:nth-child(7) > span").text();
            PAY_SUM = doc2.select("#leftside-col > p:nth-child(3) > strong:nth-child(5)").text();
        } catch (Exception e) {
            DIVIDENTS = PART_OF_PROFIT = STABLE_PAY = STABLE_GROW = PAY_SUM = "NA";
            e.printStackTrace();
        }

        try {
            String[] dateTime = doc2.select("#leftside-col > p:nth-child(3) > strong:nth-child(7)").text().split("\\.");
            System.out.println("\nTIME: " + Arrays.asList(dateTime));
            if (dateTime.length == 3) {
                PAY_DATE = LocalDateTime.parse(
                        dateTime[2] + "-" + dateTime[1] + "-" + dateTime[0] + "T12:00:00"
                );
            } else {PAY_DATE = null;}
        } catch (Exception e) {
            PAY_DATE = null;
            e.printStackTrace();
        }
    }

    private static void source05() {

    }

    private static void source06() {

    }

    // TABLE if needs:
//  int begin3 = docDiv.html().indexOf("Выплаты дивидендов по акциям ");
//  int finish3 = docDiv.html().indexOf("Совокупные выплаты дивидендов по годам");
//  System.out.println("\nTABLE:\n" + docDiv.html().substring(begin3, finish3));

    private static String clean(String html) {
        return html
                .replaceAll("<td>", "")
                .replaceAll("</td>", "")
                .replaceAll("<*td>", "")
                .replaceAll("<*tr>", "")
                .replaceAll("<*tbody>", "")
                .replaceAll("<*table>", "")
                .replaceAll("<*div>", "")
                .replaceAll("</", "")
                .replaceAll("\n", "")
                .replaceAll(" +", " ")
                .trim();
    }
}
