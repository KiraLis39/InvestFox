package ru.investment.entity.sites;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.databind.JsonNode;
import fox.components.FOptionPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.investment.config.ObjectMapperConfig;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.impl.AbstractSite;
import ru.investment.enums.CostType;
import ru.investment.exceptions.BrowserException;
import ru.investment.exceptions.root.ParsingException;
import ru.investment.utils.BrowserUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.back;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

@Slf4j
public class RuInvestingCom extends AbstractSite {
    private static final String SEARCH = "https://api.investing.com/api/search/v2/search?t=Equities&q="; // LNZL | MAGN
    private final RestTemplate restTemplate = new RestTemplate();
    private final UUID uuid = UUID.randomUUID();
    @Value("${app.selenide.tab_click_sleep}")
    private short tabClickSleep;
    private String SOURCE = "https://ru.investing.com";

    public RuInvestingCom(String ticket) {
        super.setName(ticket);
        isActive = true;
        getDto().setSource("ru.investing.com");
        getDto().setTicker(ticket);
    }

    @Override
    public ShareDTO task() throws Exception {
        if (!BrowserUtils.openNewBrowser()) {
            throw new BrowserException("Не удалось открыть окно браузера. Парсер: " + getDto().getSource());
        }

        try {
            ResponseEntity<String> result = restTemplate.getForEntity(SEARCH + getDto().getTicker(), String.class);
            JsonNode tree = ObjectMapperConfig.getMapper().readTree(result.getBody());
            if (tree.get("quotes").isEmpty() && getDto().getTicker().endsWith("P")) {
                result = restTemplate.getForEntity(SEARCH + getDto().getTicker().replace("P", "_p"), String.class);
                tree = ObjectMapperConfig.getMapper().readTree(result.getBody());
            }
            String realUrl = tree.get("quotes").get(0).get("url").asText();

            // open the web page into opened browser:
            SOURCE += realUrl;
            open(SOURCE);
            sleep(4500);
            if (!checkPageAvailable()) {
                log.error("Страница не доступна. Не пройдена проверка абстрактного родителя.");
                return null;
            }

            SelenideElement xPathRoot = $x("//main/div");
            try {
                xPathRoot.shouldBe(Condition.exist, Duration.of(3500, ChronoUnit.MILLIS));
            } catch (Throwable e) {
                xPathRoot = $x("//*[@id='__next']/div[2]/div[2]/div/div/div");
                try {
                    xPathRoot.shouldBe(Condition.exist, Duration.of(3500, ChronoUnit.MILLIS));
                } catch (Throwable e2) {
                    log.error("Страница {} так и не была отображена? {}", SOURCE + realUrl, e2.getMessage());
                    return null;
                }
            }

            try {
                getDto().setName(xPathRoot.$x("./div[1]/div[1]//h1").text());

                SelenideElement costBlock = xPathRoot.$x("./div[1]/div[2]/div[1]/span");
                if (costBlock.exists()) {
                    getDto().addCoast(costBlock.text());
                } else {
                    getDto().addCoast($("div.text-5xl.font-bold.leading-9").text());
                }

                SelenideElement costTypeBlock = xPathRoot.$x("./div[1]/div[2]/div[2]/div[3]/span[2]");
                try {
                    if (costTypeBlock.exists()) {
                        getDto().setCostType(CostType.valueOf(costTypeBlock.text()));
                    } else {
                        costTypeBlock = xPathRoot.$x(".//div/div/div/div[2]").$("span");
                        if (costTypeBlock.exists()) {
                            getDto().setCostType(CostType.valueOf(costTypeBlock.text()));
                        } else {
                            costTypeBlock = xPathRoot.$x(".//div/div/div/div[2]").$("span");
                            log.error("\nFix it");
                        }
                    }
                } catch (Exception e) {
                    log.warn("Тип валюты не определён: '{}'", costTypeBlock.exists() ? costTypeBlock.text() : xPathRoot.$x(".//div/div/div/div[2]").$("span").text());
                    getDto().setCostType(CostType.UNKNOWN);
                }

                ElementsCollection divData = $$x("//dl/div").filter(Condition.text("Дивиденды"));
                if (divData.size() == 1) {
                    SelenideElement divBlock = divData.get(0).$x("./dd/div/div/span[2]/span");
                    if (divBlock.exists()) {
                        getDto().addDividend(divBlock.text());
                    } else {
                        divData = $$x("//dl//div").filter(Condition.text("Дивиденды"));
                        if (divData.size() == 1) {
                            getDto().addDividend(divData.get(0).text());
                        } else {
                            getDto().addDividend($$x("//dt/..")
                                    .filter(Condition.text("Дивиденды")).get(0).text()
                                    .split("\\(")[1].replace(")", ""));
                        }
                    }
                } else {
                    log.error("Not found the div table");
                }

                // getDto().setLotSize(1);
                // getDto().addPaySumOnShare(1f);
                // getDto().addPaySum();
                // getDto().addPartOfProfit("");
                // getDto().addStableGrow("");
                // getDto().addStablePay("");
                // getDto().setPayDate(LocalDateTime.now());

                // other tab click:
                ElementsCollection profileTab = xPathRoot.$$x("./div[6]/nav/ul//li").filter(Condition.text("Профиль"));
                if (profileTab.size() != 1) {
                    profileTab = $$x("//div//a").filter(Condition.text("Профиль"));
                    if (profileTab.size() != 1) {
                        log.error("\nFix it");
                    }
                }
                profileTab.get(0).click();
                sleep(tabClickSleep);

                ElementsCollection sectorBlock = $$x("//*[@id='leftColumn']/div[8]//div/a");
                sleep(1500);

                boolean isEmpty = sectorBlock.isEmpty();
                if (isEmpty) {
                    sleep(1500);
                    sectorBlock = $$x("//*[@id='leftColumn']/div[8]//div/a");
                    isEmpty = sectorBlock.isEmpty();
                    if (isEmpty) {
                        if ($x("//title").innerText().equals("Application error: a client-side exception has occurred")) {
                            throw new ParsingException("client-side exception");
                        } else {
                            log.error("\nFix it");
                        }
                    }
                }
                getDto().setSector(sectorBlock.get(1).text() + ";" + sectorBlock.get(0).text());


                SelenideElement infoBlock = $x("//*[@id='leftColumn']/div[9]/p");
                if (infoBlock.exists()) {
                    getDto().addInfo(infoBlock.text());
                } else {
                    infoBlock = $x("//*[@id='profile-fullStory-showhide']");
                    if (infoBlock.exists()) {
                        getDto().addInfo(infoBlock.text());
                    } else {
                        log.warn("Инфо по тикеру '{}' не обнаружена на странице профиля акции", getDto().getTicker());
                    }
                }

                back();
                sleep(tabClickSleep);

                // other tab click:
                parseTechAnalyzeTab(xPathRoot);
            } catch (Exception e) {
                log.error("Exception here: {}", e.getMessage());
            }

            getDto().setLastRefreshDate(LocalDateTime.now());
            return getDto();
        } catch (Exception e) {
            log.error(getDto().getSource() + " не нашла тикер " + getDto().getTicker() + ". Ex: {}", e.getMessage());
            throw e;
        } finally {
            BrowserUtils.closeAndClearAll();
        }
    }

    private void parseTechAnalyzeTab(SelenideElement xPathRoot) {
        ElementsCollection techAnalyseTab = xPathRoot.$$x(".//nav/div/ul//li/a").filter(Condition.text("Теханализ"));
        sleep(500);
        int tabSize = techAnalyseTab.size();
        if (tabSize == 1) {
            techAnalyseTab.get(0).click();
            sleep(tabClickSleep);
            SelenideElement recomBlock = $x("//*[@id='techStudiesInnerWrap']/div[1]/span");
            if (recomBlock.exists()) {
                getDto().addRecommendation(recomBlock.text());
            } else {
                log.error("\nFix it");
            }
        } else {
            log.error("\nFix it");
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
