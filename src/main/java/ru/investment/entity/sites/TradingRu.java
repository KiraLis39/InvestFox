package ru.investment.entity.sites;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.investment.config.ObjectMapperConfig;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.impl.AbstractSite;
import ru.investment.enums.CostType;
import ru.investment.exceptions.BrowserException;
import ru.investment.exceptions.NoElementAvailableException;
import ru.investment.utils.BrowserUtils;
import ru.investment.utils.UniversalNumberParser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.codeborne.selenide.Selenide.*;

@Slf4j
public class TradingRu extends AbstractSite {
    private static final String SEARCH = "https://symbol-search.tradingview.com/symbol_search/v3/?text=TICKER&hl=1&exchange=&lang=ru&search_type=stocks&domain=production&sort_by_country=RU";
    private final UUID uuid = UUID.randomUUID();
    private final RestTemplate restTemplate = new RestTemplate();
    private String SOURCE = "https://ru.tradingview.com/symbols/"; // MOEX-LNZL, MOEX-AQUA

    public TradingRu(String ticker) {
        super.setName(ticker);
        isActive = true;
        getDto().setSource("ru.tradingview.com");
        getDto().setTicker(ticker);
    }

    @Override
    public ShareDTO task() throws BrowserException {
        if (!BrowserUtils.openNewBrowser()) {
            throw new BrowserException("Не удалось открыть окно браузера. Парсер: " + getDto().getSource());
        }

        try {
            boolean isFound = false;
            ResponseEntity<String> result = restTemplate.getForEntity(SEARCH.replace("TICKER", getDto().getTicker()), String.class);
            JsonNode tree = ObjectMapperConfig.getMapper().readTree(result.getBody());
            JsonNode symbols = tree.get("symbols");
            for (JsonNode symbol : symbols) {
                if (symbol.get("currency_code").asText().equalsIgnoreCase("RUB")) {
                    isFound = true;
                    SOURCE = SOURCE.concat(symbol.get("provider_id").asText()).concat("-").concat(getDto().getTicker());
                    break;
                }
            }

            // open the web page into opened browser:
            if (!isFound) {
                SOURCE += getDto().getTicker();
            }
            open(SOURCE);
            if (!checkPageAvailable()) {
                log.error("Страница не доступна. Не пройдена проверка абстрактного родителя.");
                return null;
            }

            SelenideElement content = $x(".//*[@id='tv-content']");
            content.shouldBe(Condition.visible);

            try {
                getDto().setName($x("//*[@id='js-category-content']/div[1]/div[1]/div/div/div/h1").text());
                SelenideElement sectorBlock = $x("//*[@id='js-category-content']/div[2]/div/section/div[3]/div[2]/div/div[2]");
                if (sectorBlock.exists()) {
                    getDto().setSector(sectorBlock.$("a div").text());
                } else {
                    sectorBlock = $x("//*[@id='js-category-content']/div[2]/div/section/div[3]/div[2]/div/div[3]");
                    if (sectorBlock.exists()) {
                        getDto().setSector(sectorBlock.$("a div").text());
                    }
                }
                getDto().addInfo($x("//*[@id='js-category-content']/div[2]/div/section/div[3]").text());
                getDto().addCoast($x("//*[@id='js-category-content']/div[1]/div[1]/div/div/div/div[3]/div[1]/div/div[1]/span[1]").text());
                String cType = $x("//*[@id='js-category-content']/div[1]/div[1]/div/div/div/div[3]/div[1]/div/div[1]/span[2]/span[1]").text();
                getDto().setCostType(CostType.valueOf(cType));

                //getDto().setLotSize(1);

                // Разбор табов: 'Теханализ' | 'Новости':
                SelenideElement tabsPane = $("#js-category-content > div.tv-category-symbol-header > div.tv-category-symbol-header__tabs > div > div.tv-tabs__scroll-wrap > div");
                if (!tabsPane.exists()) {
                    throw new NoElementAvailableException("Not found tabs head 'tabsPane' on this page");
                }

                ElementsCollection techAnal = tabsPane.$$("a").filter(Condition.text("Теханализ"));
                if (techAnal.size() == 1) {
                    techAnal.get(0).click();
                    sleep(2000);
                    List<Integer> sellNeutralBye = new ArrayList<>(3);
                    ElementsCollection pazes = $$x("//*[@id='js-category-content']/div[2]/div/div/div[4]/div[2]/div[2]/div");
                    for (SelenideElement paze : pazes) {
                        sellNeutralBye.add(Integer.parseInt(paze.$$("span").get(1).text()));
                    }

                    if (sellNeutralBye.get(0) > sellNeutralBye.get(1) && sellNeutralBye.get(0) > sellNeutralBye.get(2)) {
                        getDto().addRecommendation("Продавать");
                    } else if (sellNeutralBye.get(2) > sellNeutralBye.get(0) && sellNeutralBye.get(2) > sellNeutralBye.get(1)) {
                        getDto().addRecommendation("Покупать");
                    } else {
                        getDto().addRecommendation("Держать");
                    }
                }

                ElementsCollection otchetnost = tabsPane.$$("a").filter(Condition.text("Отчётность"));
                if (otchetnost.size() == 1) {
                    otchetnost.get(0).click();
                    sleep(2500);
                    ElementsCollection divBtn = $$x("//*[@id='id_financials-tabs_tablist']//a").filter(Condition.text("Дивиденды"));
                    if (divBtn.size() == 1) {
                        divBtn.get(0).click();
                        sleep(2000);

                        ElementsCollection dataBlock = $x("//*[@id='js-category-content']/div[2]/div/div/div[5]/div[2]/div/div[1]")
                                .$$x("./div");

                        List<Double> tmpArr = new ArrayList<>();
                        List<String> paySumsOnShare = dataBlock.get(1).$$x("./div")
                                .filter(Condition.not(Condition.empty)).get(1).$$x("./div")
                                .filter(Condition.not(Condition.empty)).texts()
                                .stream().filter(s -> !s.equals("—")).toList();
                        for (String s : paySumsOnShare) {
                            double next = UniversalNumberParser.parseFloat(s);
                            if (next != 0) {
                                tmpArr.add(next);
                            }
                        }
                        float psos = (float) tmpArr.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
                        getDto().addPaySumOnShare(psos);
                        // getDto().addPaySum(psos);

                        tmpArr.clear();
                        List<String> divSums = dataBlock.get(2).$$x("./div")
                                .filter(Condition.not(Condition.empty)).get(1).$$x("./div")
                                .filter(Condition.not(Condition.empty)).texts()
                                .stream().filter(s -> !s.equals("—")).toList();
                        for (String s : divSums) {
                            double next = UniversalNumberParser.parseFloat(s);
                            if (next != 0) {
                                tmpArr.add(next);
                            }
                        }
                        float dss = (float) tmpArr.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
                        getDto().addDividend(dss);

                        //getDto().addPartOfProfit("");
                        //getDto().addStableGrow("");
                        //getDto().addStablePay("");
                        //getDto().setPayDate(LocalDateTime.now()); //*[@id="js-category-content"]/div[1]/div[1]/div/div/div/div[3]/div[1]/div/div[3]/span/span
                    }
                }
            } catch (Exception e) {
                log.error("Exception here: {}", e.getMessage());
            } finally {
                BrowserUtils.closeAndClearAll();
            }

            getDto().setLastRefreshDate(LocalDateTime.now());
            return getDto();
        } catch (Exception e) {
            log.error(getDto().getSource() + " не нашла тикер " + getDto().getTicker() + ". Ex: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TradingRu tradingRu = (TradingRu) o;
        return uuid.equals(tradingRu.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uuid);
    }
}
