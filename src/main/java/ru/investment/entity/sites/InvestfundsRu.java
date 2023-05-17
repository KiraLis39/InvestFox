package ru.investment.entity.sites;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.investment.config.ObjectMapperConfig;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.impl.AbstractSite;
import ru.investment.exceptions.BadDataException;
import ru.investment.exceptions.BrowserException;
import ru.investment.utils.BrowserUtils;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.open;

@Slf4j
public class InvestfundsRu extends AbstractSite {
    private static final String SEARCH = "https://investfunds.ru/stocks/?searchString=";
    private static final String VERIFY_HASH = "&verifyHash=813dd92bf207bbcdc65be773606698fe"; // &verifyHash=136690883ecab2ec4d5ec54d0d11b873
    private static String SOURCE = "https://investfunds.ru";
    private final UUID uuid = UUID.randomUUID();
    private final RestTemplate restTemplate = new RestTemplate();

    public InvestfundsRu(String ticket) {
        super.setName(ticket);
        isActive = true;
        getDto().setSource("www.investfunds.ru");
        getDto().setTicker(ticket);
    }

    @Override
    public ShareDTO task() throws BadDataException, BrowserException {
        if (!BrowserUtils.openNewBrowser()) {
            throw new BrowserException("Не удалось открыть окно браузера. Парсер: " + getDto().getSource());
        }

        try {
            boolean isFound = false;
            ResponseEntity<String> result = restTemplate.getForEntity(SEARCH.replace("TICKER", getDto().getTicker()), String.class);
            JsonNode tree = ObjectMapperConfig.getMapper().readTree(result.getBody());
            JsonNode symbols = tree.get("symbols");
//            for (JsonNode symbol : symbols) {
//                if (symbol.get("currency_code").asText().equalsIgnoreCase("RUB")) {
//                    isFound = true;
//                    SOURCE = SOURCE.concat(symbol.get("provider_id").asText()).concat("-").concat(getDto().getTicker());
//                    break;
//                }
//            }

            // open the web page into opened browser:
            if (!isFound) {
                SOURCE += getDto().getTicker().toUpperCase() + VERIFY_HASH; // https://investfunds.ru/stocks/Aqua/
            }
            open(SOURCE);
            if (!checkPageAvailable()) {
                log.error("Страница не доступна. Не пройдена проверка абстрактного родителя.");
                return null;
            }

            SelenideElement content = $("html body");
            content.shouldBe(Condition.visible);

            try {
                getDto().setName("");
                getDto().setSector("");
                getDto().addInfo("");
                getDto().addCoast("");
                getDto().setCostType(null);
                getDto().setLotSize(1);
                getDto().addRecommendation("");
                getDto().addPaySumOnShare(0);
                getDto().addDividend(0);

                // getDto().addPaySum();
                // getDto().addPartOfProfit("");
                // getDto().addStableGrow("");
                // getDto().addStablePay("");
                // getDto().setPayDate(LocalDateTime.now());
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
