package dto;

import gui.InvestFrame;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ShareDTO {
    //    @JsonProperty("Тикет")
    private String TICKET;
    //    @JsonProperty("Источник")
    private String SOURCE;
    //    @JsonProperty("Наименование")
    private String NAME;
    //    @JsonProperty("Сектор")
    private String SECTOR;
    //    @JsonProperty("Валюта")
    private String COST_TYPE;
    //    @JsonProperty("Размер лота")
    private int LOT_SIZE = -1;

    //    @JsonProperty("Стоимость")
    private ArrayList<String> COAST_LIST;
    //    @JsonProperty("Дивиденты")
    private ArrayList<String> DIVIDEND_LIST;
    //    @JsonProperty("Выплата")
    private ArrayList<String> PAY_SUM_LIST;
    //    @JsonProperty("Выплата на акцию")
    private ArrayList<String> PAY_SUM_ON_SHARE_LIST;
    //    @JsonProperty("Часть дохода")
    private ArrayList<String> PART_OF_PROFIT_LIST;
    //    @JsonProperty("Стабильность выплат")
    private ArrayList<String> STABLE_PAY_LIST;
    //    @JsonProperty("Стабильность роста")
    private ArrayList<String> STABLE_GROW_LIST;

    //    @JsonProperty("Информация")
    private ArrayList<String> INFO_LIST;
    //    @JsonProperty("Рекомендация")
    private ArrayList<String> RECOMENDATION_LIST;

    //    @JsonProperty("Дата след. выплаты")
    private LocalDateTime PAY_DATE;
    //    @JsonProperty("Обновлено")
    private LocalDateTime LAST_REFRESH;


    public ShareDTO() {
        this.COAST_LIST = new ArrayList<>();
        this.DIVIDEND_LIST = new ArrayList<>();
        this.PAY_SUM_LIST = new ArrayList<>();
        this.PAY_SUM_ON_SHARE_LIST = new ArrayList<>();
        this.PART_OF_PROFIT_LIST = new ArrayList<>();
        this.STABLE_PAY_LIST = new ArrayList<>();
        this.STABLE_GROW_LIST = new ArrayList<>();
        this.INFO_LIST = new ArrayList<>();
        this.RECOMENDATION_LIST = new ArrayList<>();
    }

    public String getTicket() {
        return this.TICKET;
    }
    public void setTicket(String TICKET) {
        this.TICKET = TICKET;
    }

    public String getSource() {
        return this.SOURCE;
    }
    public void setSource(String source) {
        this.SOURCE = source;
    }

    public String getName() {
        if (this.NAME == null) {return "NA";}
        return this.NAME;
    }
    public void setName(String NAME) {
        this.NAME = NAME;
    }

    public String getSector() {
        return this.SECTOR;
    }
    public void setSector(String SECTOR) {
        this.SECTOR = SECTOR;
    }

    public String getCostType() {
        return this.COST_TYPE;
    }
    public void setCostType(String costType) {
        this.COST_TYPE = costType;
    }

    public LocalDate getPayDate() {
        return this.PAY_DATE != null ? this.PAY_DATE.toLocalDate() : null;
    }
    public void setPayDate(LocalDateTime payDate) {
        this.PAY_DATE = payDate;
    }

    public LocalDateTime getLastRefresh() {
        return this.LAST_REFRESH;
    }
    public void setLastRefresh(LocalDateTime lastRefresh) {
        this.LAST_REFRESH = lastRefresh;
    }

    public void addCoast(String... coast) {
        for (String s : coast) {
            this.COAST_LIST.add(s);
        }
    }
    public ArrayList<String> getCoastList() {
        return this.COAST_LIST;
    }

    public void setLotSize(int size) {
        this.LOT_SIZE = size;
    }

    public int getLotSize() {
        return LOT_SIZE;
    }

    public void addDividend(String dividend) {
        if (dividend == null) {return;}

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
        this.DIVIDEND_LIST.add(div == null ? "0" : div.toString());
    }
    public ArrayList<String> getDividendList() {
        return this.DIVIDEND_LIST;
    }

    public ArrayList<String> getPaySum() {
        return this.PAY_SUM_LIST;
    }
    public void addPaySum(String paySum) {
        this.PAY_SUM_LIST.add(paySum);
    }

    public ArrayList<String> getPaySumOnShare() {
        return this.PAY_SUM_ON_SHARE_LIST;
    }
    public void addPaySumOnShare(String paySumOnShare) {
        this.PAY_SUM_ON_SHARE_LIST.add(paySumOnShare);
    }

    public ArrayList<String> getPartOfProfit() {
        return this.PART_OF_PROFIT_LIST;
    }
    public void addPartOfProfit(String partOfProfit) {
        this.PART_OF_PROFIT_LIST.add(partOfProfit);
    }

    public ArrayList<String> getStablePay() {
        return this.STABLE_PAY_LIST;
    }
    public void addStablePay(String stablePay) {
        this.STABLE_PAY_LIST.add(stablePay);
    }

    public ArrayList<String> getStableGrow() {
        return this.STABLE_GROW_LIST;
    }
    public void addStableGrow(String stableGrow) {
        this.STABLE_GROW_LIST.add(stableGrow);
    }

    public ArrayList<String> getInfo() {
        return this.INFO_LIST;
    }
    public void addInfo(String info) {
        this.INFO_LIST.add(info);
    }

    public ArrayList<String> getRecommendation() {
        return this.RECOMENDATION_LIST;
    }
    public void addRecommendation(String recommendation) {
        this.RECOMENDATION_LIST.add(recommendation);
    }
}
