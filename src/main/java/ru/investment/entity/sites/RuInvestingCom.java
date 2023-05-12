package ru.investment.entity.sites;

import com.codeborne.selenide.Selenide;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.impl.AbstractSite;
import ru.investment.enums.CostType;
import ru.investment.exceptions.root.ParsingException;

import java.time.LocalDateTime;

import static com.codeborne.selenide.Selenide.open;

@Slf4j
public class RuInvestingCom extends AbstractSite {
    private static final String SOURCE = "https://ru.investing.com/";

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
            // open the web page into opened browser:
            open(SOURCE + getDto().getTicker());
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        }

        getDto().setLastRefreshDate(LocalDateTime.now());
        return getDto();
    }
}
