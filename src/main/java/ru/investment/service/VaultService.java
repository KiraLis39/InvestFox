package ru.investment.service;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.entity.sites.impl.AbstractSite;

@Service
public class VaultService {
    private double usdValue, eurValue;

    public void loadVaults() {
        Document doc;
        AbstractSite as = new AbstractSite() {
            @Override
            public ShareDTO task() {
                return null;
            }
        };

        String usdLink = "https://ru.investing.com/currencies/usd-rub";
        as.setUrl(usdLink);
        doc = as.getDoc();
        if (doc != null) {
            String usdCost = doc.getElementsByClass("text-2xl").get(2).text();
            usdValue = Double.parseDouble(usdCost.replace(",", "."));
        }

        String eurLink = "https://ru.investing.com/currencies/eur-rub";
        as.setUrl(eurLink);
        doc = as.getDoc();
        if (doc != null) {
            String eurCost = doc.getElementsByClass("text-2xl").get(2).text();
            eurValue = Double.parseDouble(eurCost.replace(",", "."));
        }
    }

    public double getUSDValue() {
        return usdValue;
    }

    public double getEURValue() {
        return eurValue;
    }
}
