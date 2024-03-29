package ru.investment.entity.sites;

import lombok.extern.slf4j.Slf4j;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.impl.AbstractSite;
import ru.investment.exceptions.root.ParsingException;

@Slf4j
public class InvestmintRu extends AbstractSite {

    private static final String SOURCE = "https://investmint.ru/"; // aqua, lnzl

    public InvestmintRu(String ticker) {
        super.setName(ticker);
        isActive = false;
        getDto().setSource(SOURCE);
        getDto().setTicker(ticker);
    }

    @Override
    public ShareDTO task() throws ParsingException {
        return null;
    }
}
