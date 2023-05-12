package ru.investment.entity.sites;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.investment.config.BrowserSetupConfig;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.impl.AbstractSite;
import ru.investment.enums.CostType;
import ru.investment.exceptions.NoElementAvailableException;
import ru.investment.utils.BrowserUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.codeborne.selenide.Selenide.*;

@Slf4j
public class TradingRu extends AbstractSite {

    private static final String SOURCE = "https://ru.tradingview.com/symbols/"; // MOEX-LNZL, MOEX-AQUA

    public TradingRu(String ticker) {
        super.setName(ticker);
        isActive = true;
        getDto().setSource("ru.tradingview.com");
        getDto().setTicker(ticker);
    }

    @Override
    public ShareDTO task() {
        int openTries = 3;
        boolean isFailed;
        do {
            isFailed = false;
            openTries--;
            try {
                // open the browser instant:
                open();
            } catch (Exception e) {
                log.warn("Selenide jpen exception: {}", e.getMessage());
                isFailed = true;
                Selenide.sleep(500);
            }
        } while (isFailed && openTries > 0);

        try {
            // open the web page into opened browser:
            open(SOURCE + getDto().getTicker());

            if (BrowserUtils.isPageNotFound()) {
                log.error("isPageNotFound");
                return null;
            }
            if (BrowserUtils.isPageNotExists()) {
                log.error("isPageNotExists");
                return null;
            }
            if (BrowserUtils.isPageNotAvailable()) {
                log.error("isPageNotAvailable");
                return null;
            }
            if (BrowserUtils.isTechnicalWorks()) {
                log.error("isTechnicalWorks");
                return null;
            }

            SelenideElement content = $x(".//*[@id='tv-content']");
            content.shouldBe(Condition.visible);

            // System.out.println("Test: " + content.$x("./div/div").text());

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
                    sleep(2500);
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
                        sleep(2500);

                        ElementsCollection dataBlock = $x("//*[@id='js-category-content']/div[2]/div/div/div[5]/div[2]/div/div[1]").$$("div");
                        ElementsCollection years = dataBlock.get(0).$$("div").filter(Condition.not(Condition.empty)).get(1).$$("div");
                        for (SelenideElement div : years) {
                            //...
                        }

                        //getDto().addPaySumOnShare("0");
                        //getDto().addDividend("0");
                        //getDto().addPaySum("0");

                        //getDto().addPartOfProfit("");
                        //getDto().addStableGrow("");
                        //getDto().addStablePay("");
                        //getDto().setPayDate(LocalDateTime.now()); //*[@id="js-category-content"]/div[1]/div[1]/div/div/div/div[3]/div[1]/div/div[3]/span/span
                    }
                }
            } catch (Exception e) {
                log.error("Exception here: {}", e.getMessage());
            } finally {
                closeWindow();
                closeWebDriver();
            }

            getDto().setLastRefreshDate(LocalDateTime.now());
            return getDto();
        } catch (Exception e) {
            log.error(getDto().getSource() + " не нашла тикер " + getDto().getTicker() + ". Ex: {}", e.getMessage());
            return null;
        }
    }
}
