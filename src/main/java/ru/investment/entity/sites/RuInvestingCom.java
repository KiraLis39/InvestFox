package ru.investment.entity.sites;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.investment.config.ObjectMapperConfig;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.impl.AbstractSite;
import ru.investment.enums.CostType;
import ru.investment.exceptions.root.ParsingException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.codeborne.selenide.Selenide.*;

@Slf4j
public class RuInvestingCom extends AbstractSite {
    private final UUID uuid = UUID.randomUUID();
    private static final String SOURCE = "https://ru.investing.com";
    private static final String SEARCH = "https://api.investing.com/api/search/v2/search?t=Equities&q="; // LNZL | MAGN
    private final RestTemplate restTemplate = new RestTemplate();

    public RuInvestingCom(String ticket) {
        super.setName(ticket);
        isActive = true;
        getDto().setSource("ru.investing.com");
        getDto().setTicker(ticket);
    }

    @Override
    public ShareDTO task() throws ParsingException {
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
            ResponseEntity<String> result = restTemplate.getForEntity(
                    SEARCH + getDto().getTicker(),
                    String.class);
            JsonNode tree = ObjectMapperConfig.getMapper().readTree(result.getBody());
            String realUrl = tree.get("quotes").get(0).get("url").asText();

            // open the web page into opened browser:
            open(SOURCE + realUrl);
            if (!checkPageAvailable()) {
                log.error("Страница не доступна. Не пройдена проверка абстрактного родителя.");
                return null;
            }

            SelenideElement content = $x(".//*[@id='__next']");
            content.shouldBe(Condition.visible);

            try {

                getDto().setName("");
                getDto().setSector("");
                getDto().addInfo("");
                getDto().addCoast("");
                getDto().setCostType(CostType.UNKNOWN);
                getDto().setLotSize(1);
                getDto().addRecommendation("");
                getDto().addPaySumOnShare(1f);
                // getDto().addPaySum();
                getDto().addDividend(1D);
                //getDto().addPartOfProfit("");
                //getDto().addStableGrow("");
                //getDto().addStablePay("");
                //getDto().setPayDate(LocalDateTime.now());

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RuInvestingCom that = (RuInvestingCom) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uuid);
    }
}
