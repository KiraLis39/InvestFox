package ru.investment.entity.sites;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.investment.config.ObjectMapperConfig;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.impl.AbstractSite;
import ru.investment.enums.CostType;
import ru.investment.exceptions.BadDataException;
import ru.investment.exceptions.BrowserException;
import ru.investment.utils.BrowserUtils;
import ru.investment.utils.UniversalNumberParser;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.codeborne.selenide.Selenide.*;

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
    public ShareDTO task() throws BadDataException, BrowserException, JsonProcessingException {
        if (!BrowserUtils.openNewBrowser()) {
            throw new BrowserException("Не удалось открыть окно браузера. Парсер: " + getDto().getSource());
        }

        try {
            ResponseEntity<String> result = restTemplate
                    .getForEntity(SEARCH + getDto().getTicker(), String.class);
            JsonNode tree = ObjectMapperConfig.getMapper().readTree(result.getBody());
            JsonNode results = tree.get("currentResults");
            if (!results.isEmpty()) {
                SOURCE += results.get(0).get("url").asText();
            }

            // open the web page into opened browser:
            open(SOURCE);
            if (!checkPageAvailable()) {
                log.error("Страница не доступна. Не пройдена проверка абстрактного родителя.");
                return null;
            }

            SelenideElement content = $x("/html/body/div[2]");
            content.shouldBe(Condition.visible);

            try {
                getDto().setName($x("//*[@class='widget_info_ttl']").text().replace("Акции ", ""));

                ElementsCollection infoLine = $$x("//*[@class='mainParam']//li")
                        .filter(Condition.have(Condition.text("ОТРАСЛЬ")));
                if (!infoLine.isEmpty() && infoLine.get(0).$x(".//*[@class='value']").exists()) {
                    getDto().setSector(infoLine.get(0).$x(".//*[@class='value']").text());
                } else {
                    log.error("Fix it!");
                }

                ElementsCollection infoBlock = $$x(".//div/p").filter(Condition.not(Condition.hidden));
                if (infoBlock.size() == 1) {
                    getDto().addInfo(infoBlock.get(0).text());
                } else {
                    log.error("Fix it!");
                }

                ElementsCollection costBlock = $$x("//*[@class='lftPnl']//div")
                        .filter(Condition.cssClass("mainPrice"));
                if (!costBlock.isEmpty()) {
                    String cost = costBlock.get(0).getOwnText().split("\\(")[0].trim();
                    getDto().addCoast(cost.substring(0, cost.length() - 4)); // vault is always 3 symbols + space.
                    getDto().setCostType(CostType.valueOf(cost.substring(cost.length() - 3)));
                } else {
                    log.error("Fix it!");
                }

                ElementsCollection lotBlock = $$x("//div[@data-modul='options']//div/ul/li//span")
                        .filter(Condition.text("Торговый лот"));
                if (!lotBlock.isEmpty()) {
                    getDto().setLotSize(UniversalNumberParser.parseInt(lotBlock.get(0).$x("../div").text()));
                } else {
                    log.error("Fix it!");
                }

                // getDto().addDividend(0);
                // getDto().addRecommendation("");
                // getDto().addPaySumOnShare(0);
                // getDto().addPaySum();
                // getDto().addPartOfProfit("");
                // getDto().addStableGrow("");
                // getDto().addStablePay("");
                // getDto().setPayDate(LocalDateTime.now());
            } catch (Exception e) {
                log.error("Exception here: {}", e.getMessage());
            }
        } catch (Exception e) {
            log.error(getDto().getSource() + " не нашла тикер " + getDto().getTicker() + ". Ex: {}", e.getMessage());
            throw e;
        } finally {
            BrowserUtils.closeAndClearAll();
            getDto().setLastRefreshDate(LocalDateTime.now());
            return getDto();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InvestfundsRu that = (InvestfundsRu) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uuid);
    }
}
