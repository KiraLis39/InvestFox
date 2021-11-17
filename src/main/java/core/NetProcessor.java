package core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class NetProcessor {
    private static ObjectMapper mapper = new ObjectMapper();

    private final static String SOURCE_ONE = "https://investfuture.ru/securities/id/";
    private final static String SOURCE_TWO = "https://investfuture.ru/dividends/id/";
    private final static String SOURCE_THREE = "https://www.tinkoff.ru/invest/stocks/";
    private static final String SOURCE_FOUR = "https://www.dohod.ru/ik/analytics/dividend/";
    private static final String SOURCE_FIVE = "https://investfuture.ru/securities?filter_name=&filter_tiker=";

//    https://www.google.com/finance/quote/LKOH:MCX; https://quote.rbc.ru/ticker/172651

    private static String TICKET;
    private static String COST_FIELD;
    private static String COST_TYPE;


    private static String coast = "", name = "", sector = "", tip = "", dividents, partOfPribil, stablePay, stableGrow, paySum, info, payOnActio;
    private static LocalDateTime lastRefresh, payDate;

    public static void testPrint(String ticket) {
        TICKET = ticket;

        Document doc;
        try {
            doc = Jsoup.connect(SOURCE_THREE + ticket).get();

            Elements coastElems = doc.selectXpath("/html/body/div[1]/div/div[2]/div/div[1]/div[2]/div[1]/div[2]/div/div[2]/div[1]/div/div/div[2]/span/span");
            for (Element elm : coastElems) {
                coast += elm.text();
            }

            Elements nameElems = doc.selectXpath("/html/body/div[1]/div/div[2]/div/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/h1/span[1]");
            for (Element elm : nameElems) {
                name += elm.text();
            }

            Elements sectorElems = doc.selectXpath("/html/body/div[1]/div/div[2]/div/div[1]/div[2]/div[1]/div[1]/div[1]");
            for (Element elm : sectorElems) {
                sector += elm.text();
            }

            Elements infoElems = doc.selectXpath("/html/body/div[1]/div/div[2]/div/div[1]/div[2]/div[1]/div[1]/div[4]");
//            System.out.println(infoElems.text());
            int start = infoElems.text().indexOf("О компании ");
            info = infoElems.text().substring(start, infoElems.text().indexOf("Рекомендация "));

            Elements tipElems = doc.selectXpath("/html/body/div[1]/div/div[2]/div/div[1]/div[2]/div[1]/div[1]/div[4]");
            int index = tipElems.text().indexOf("Рекомендация");
            tip = tipElems.text().substring(index, index + 64);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // another site:
        Document doc2 = null;
        try {
            doc2 = Jsoup.connect(SOURCE_FOUR + ticket.toLowerCase()).get();
            dividents = doc2.select("#dividends-brief > tbody > tr.frow > td.greendark").text();
            partOfPribil = doc2.select("#dividends-brief > tbody > tr.frow > td.gray").text();
            stablePay = doc2.select("#rightside-col > p:nth-child(2) > strong:nth-child(5) > span").text();
            stableGrow = doc2.select("#rightside-col > p:nth-child(2) > strong:nth-child(7) > span").text();
            paySum = doc2.select("#leftside-col > p:nth-child(3) > strong:nth-child(5)").text();
        } catch (Exception e) {
            dividents = partOfPribil = stablePay = stableGrow = paySum = "NA";
            e.printStackTrace();
        }

        try {
            String[] dateTime = doc2.select("#leftside-col > p:nth-child(3) > strong:nth-child(7)").text().split("\\.");
            System.out.println("\nTIME: " + Arrays.asList(dateTime));
            if (dateTime.length == 3) {
                payDate = LocalDateTime.parse(
                        dateTime[2] + "-" + dateTime[1] + "-" + dateTime[0] + "T12:00:00"
                );
            } else {payDate = null;}
        } catch (Exception e) {
            payDate = null;
            e.printStackTrace();
        }

        // result output:
        System.out.println();
        System.out.println("Тикет:\t" + ticket);
        System.out.println("Имя:\t" + name);
        System.out.println("Цена:\t" + coast);
        System.out.println("Дивиденты:\t" + dividents);
        System.out.println("Доля от прибыли:\t" + partOfPribil);
        System.out.println("Инфо:\t" + info);
        System.out.println("Рекомендация:\t" + tip.split(" ")[1]);
        System.out.println("Стабильность выплат:\t" + stablePay);
        System.out.println("Стабильность роста:\t" + stableGrow);
        System.out.println("Ближ.выплата:\t" + paySum + " " + payDate);

        testPrint1(ticket);
    }

    public static void testPrint1(String ticket) {
        int code = -1;

        Document doc1 = null;
        try {
            doc1 = Jsoup.connect(SOURCE_FIVE + ticket).get();
            Elements docElems = doc1.select("#result_panel > div.nra_stat > table > tbody > tr");

            String temp = docElems.text();
            String[] data = temp.split(" ");
            System.out.println("\nDATA TEST: " + Arrays.asList(data));
            System.out.println("\nDATA:\n" +
                    "Ticket:\t" + data[0] +
                    "\nName:\t" + data[1] + " (" + data[2] + ")" + // не все имена содержат пробелы!
                    "\nOpen:\t" + data[3] +
                    "\nMin:\t" + data[4] +
                    "\nMax:\t" + data[5] +
                    "\nLast:\t" + data[6] +
                    "\nChange:\t" + data[7] +
                    "\nUpdated: " + data[8] + "\n"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Elements docElems = doc1.select("#result_panel > div.nra_stat > table > tbody > tr > td:nth-child(2) > a");
            String temp = docElems.outerHtml().split("\"")[1].split("/")[3];
            code = Integer.parseInt(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String result = "";
        try {
            Document doc = Jsoup.connect(SOURCE_ONE + code).get();
            Elements elms = doc.select("#company_quote_table_gor > tbody");
            for (Element elm : elms) {
                result += clean(elm.html());
            }

            payOnActio = doc.selectXpath("/html/body/div[1]/div/div[4]/div/div/div/div/div[2]/div[4]/div[3]/p[9]").text();
            System.out.println("\n" + payOnActio + "\n");
        } catch (Exception e) {
            result = "Не удалось прочитать.";
            e.printStackTrace();
        }
        System.out.println(result);

        try {
            Document docDiv = Jsoup.connect(SOURCE_TWO + code).get();
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

        // TABLE if needs:
//        int begin3 = docDiv.html().indexOf("Выплаты дивидендов по акциям ");
//        int finish3 = docDiv.html().indexOf("Совокупные выплаты дивидендов по годам");
//        System.out.println("\nTABLE:\n" + docDiv.html().substring(begin3, finish3));
    }

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
