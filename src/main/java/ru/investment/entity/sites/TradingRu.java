package ru.investment.entity.sites;

import com.codeborne.selenide.Condition;
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
                SelenideElement tabsPane = $("#js-category-content > div.tv-category-symbol-header > div.tv-category-symbol-header__tabs > div > div.tv-tabs__scroll-wrap > div");
                if (!tabsPane.exists()) {
                    throw new NoElementAvailableException("Not found tabs head 'tabsPane' on this page");
                }

                // Разбор табов: 'Теханализ' | 'Новости' | 'Обзор':
                // techanalys osc data: $$x(//*[@id="js-category-content"]/div[2]/div/div/div[4]/div[2]/div[2]/div)
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
                //getDto().addDividend("0");
                //getDto().setLotSize(1);
                //getDto().addRecommendation("");

                //getDto().addPartOfProfit("");
                //getDto().addStableGrow("");
                //getDto().addStablePay("");
                //getDto().addPaySum("0");
                //getDto().setPayDate(LocalDateTime.now()); // //*[@id="js-category-content"]/div[1]/div[1]/div/div/div/div[3]/div[1]/div/div[3]/span/span
                //getDto().addPaySumOnShare("0");
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
