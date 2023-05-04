package ru.investment.entity.sites;

import com.codeborne.selenide.Condition;
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

            System.out.println("Test: " + content.$x("./div/div").text());
            closeWindow();
            closeWebDriver();
        } catch (Exception e) {
            log.error(getDto().getSource() + " не нашла тикер " + getDto().getTicker() + ". Ex: {}", e.getMessage());
            return null;
        }

        try {
//            getDto().setName();
//            getDto().addCoast();
//            getDto().setCostType();
//            getDto().addDividend();
//            getDto().addPaySum();
//            getDto().addPaySumOnShare();
//            getDto().addInfo();
//            getDto().addPartOfProfit();
//            getDto().addRecommendation();
//            getDto().addStableGrow();
//            getDto().addStablePay();
//            getDto().setLotSize();
//            getDto().setPayDate();
        } catch (Exception e) {
            log.error("Exception here: {}", e.getMessage());
        }

        getDto().setLastRefreshDate(LocalDateTime.now());
        return getDto();
    }
}
