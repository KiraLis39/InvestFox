package ru.investment.dto;

import ru.investment.enums.CostType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShareDTO {
    // @JsonProperty("Стоимость")
    private final ArrayList<String> coasts;
    // @JsonProperty("Дивиденты")
    private final ArrayList<String> dividends;
    // @JsonProperty("Выплата")
    private final ArrayList<String> paySums;
    // @JsonProperty("Выплата на акцию")
    private final ArrayList<String> paySumsOnShare;
    // @JsonProperty("Часть дохода")
    private final ArrayList<String> partsOfProfit;
    // @JsonProperty("Стабильность выплат")
    private final ArrayList<String> stablePays;
    // @JsonProperty("Стабильность роста")
    private final ArrayList<String> stableGrows;
    // @JsonProperty("Информация")
    private final ArrayList<String> infos;
    // @JsonProperty("Рекомендация")
    private final ArrayList<String> recomendations;
    // @JsonProperty("Тикет")
    private String ticker;
    // @JsonProperty("Источник")
    private String source;
    // @JsonProperty("Наименование")
    private String name;
    // @JsonProperty("Сектор")
    private String sector;
    // @JsonProperty("Валюта")
    private CostType costType;
    // @JsonProperty("Размер лота")
    private int lotSize = -1;
    // @JsonProperty("Дата след. выплаты")
    private LocalDateTime payDate;

    // @JsonProperty("Обновлено")
    private LocalDateTime lastRefreshDate;


    public ShareDTO() {
        this.coasts = new ArrayList<>();
        this.dividends = new ArrayList<>();
        this.paySums = new ArrayList<>();
        this.paySumsOnShare = new ArrayList<>();
        this.partsOfProfit = new ArrayList<>();
        this.stablePays = new ArrayList<>();
        this.stableGrows = new ArrayList<>();
        this.infos = new ArrayList<>();
        this.recomendations = new ArrayList<>();
    }

    public String getTicket() {
        return this.ticker;
    }

    public void setTicket(String TICKET) {
        this.ticker = TICKET;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        if (this.name == null) {
            return "NA";
        }
        return this.name;
    }

    public void setName(String NAME) {
        this.name = NAME;
    }

    public String getSector() {
        return this.sector;
    }

    public void setSector(String SECTOR) {
        this.sector = SECTOR;
    }

    public CostType getCostType() {
        return this.costType;
    }

    public void setCostType(CostType costType) {
        this.costType = costType;
    }

    public LocalDate getPayDate() {
        return this.payDate != null ? this.payDate.toLocalDate() : null;
    }

    public void setPayDate(LocalDateTime payDate) {
        this.payDate = payDate;
    }

    public LocalDateTime getLastRefresh() {
        return this.lastRefreshDate;
    }

    public void setLastRefresh(LocalDateTime lastRefresh) {
        this.lastRefreshDate = lastRefresh;
    }

    public void addCoast(String... coast) {
        for (String s : coast) {
            this.coasts.add(s);
        }
    }

    public List<String> getCoastList() {
        return this.coasts;
    }

    public int getLotSize() {
        return lotSize;
    }

    public void setLotSize(int size) {
        this.lotSize = size;
    }

    public void addDividend(String dividend) {
        if (dividend == null) {
            return;
        }

        String[] data = dividend.split(" ");
        Float div = null;
        if (data.length > 0) {
            for (String datum : data) {
                try {
                    div = Float.parseFloat(datum.replace(",", ".").replace("%", "").trim());
                } catch (Exception e) {
                    continue;
                }
            }
        }
        this.dividends.add(div == null ? "0" : div.toString());
    }

    public List<String> getDividendList() {
        return this.dividends;
    }

    public List<String> getPaySum() {
        return this.paySums;
    }

    public void addPaySum(String paySum) {
        this.paySums.add(paySum);
    }

    public List<String> getPaySumOnShare() {
        return this.paySumsOnShare;
    }

    public void addPaySumOnShare(String paySumOnShare) {
        this.paySumsOnShare.add(paySumOnShare);
    }

    public List<String> getPartOfProfit() {
        return this.partsOfProfit;
    }

    public void addPartOfProfit(String partOfProfit) {
        this.partsOfProfit.add(partOfProfit);
    }

    public List<String> getStablePay() {
        return this.stablePays;
    }

    public void addStablePay(String stablePay) {
        this.stablePays.add(stablePay);
    }

    public List<String> getStableGrow() {
        return this.stableGrows;
    }

    public void addStableGrow(String stableGrow) {
        this.stableGrows.add(stableGrow);
    }

    public List<String> getInfo() {
        return this.infos;
    }

    public void addInfo(String info) {
        this.infos.add(info);
    }

    public List<String> getRecommendation() {
        return this.recomendations;
    }

    public void addRecommendation(String recommendation) {
        this.recomendations.add(recommendation);
    }
}
