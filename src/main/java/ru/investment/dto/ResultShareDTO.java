package ru.investment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.investment.enums.CostType;
import ru.investment.sites.exceptions.VariableLotException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Slf4j
@NoArgsConstructor
public class ResultShareDTO implements Serializable, Comparable<ResultShareDTO> {
    // @JsonProperty("Обновлено")
    private static LocalDateTime lastRefreshDate = LocalDateTime.now();

    // @JsonProperty("Индекс")
    private short index;

    // @JsonProperty("Тикет")
    private String ticker;

    // @JsonProperty("Источник")
    private String source;

    // @JsonProperty("Наименования")
    private String name;

    // @JsonProperty("Отображаемое имя")
    private String showedName;

    // @JsonProperty("Сектор")
    private String sector;

    // @JsonProperty("Стоимость")
    private Double cost;

    // @JsonProperty("Валюта")
    private CostType costType;

    // @JsonProperty("Размер лота")
    private Integer lotSize = 1;

    // @JsonProperty("Дивиденты")
    private double dividend;

    // @JsonProperty("Количество")
    private int count;

    // @JsonProperty("Выплата")
    private Double paySum;

    // @JsonProperty("Выплата на акцию")
    private Double paySumOnShare;

    // @JsonProperty("Часть дохода")
    private String partOfProfit;

    // @JsonProperty("Стабильность выплат")
    private String stablePay;

    // @JsonProperty("Стабильность роста")
    private String stableGrow;

    // @JsonProperty("Информация")
    private String info;

    // @JsonProperty("Рекомендация")
    private String recommendation;

    // @JsonProperty("Дата след. выплаты")
    private LocalDateTime payDate;

    // @JsonProperty("Стоимость лота")
    private Double lotCost;

    // @JsonProperty("Комментарий")
    private String comment;

    public synchronized void update(String ticker, ShareDTO newData) {
        if (newData == null) {
            return;
        }

        this.ticker = ticker;
        this.name = this.name == null ? (newData.getName().equals("NA") ? null : newData.getName()) : this.name.concat(" :: " + newData.getName());
        if (this.name != null && this.showedName == null) {
            if (!this.name.split(" :: ")[0].equals("NA")) {
                this.showedName = this.name.split(" :: ")[0];
            }
        }

        try {
            if (newData.getCoastList().size() > 0) {
                calcResultCost(newData.getCoastList());
            }
            calcResultLot(newData.getLotSize());
            calcResultLotCost(cost == null ? 0D : cost, lotSize == -1 ? 1 : lotSize);

            calcResultCostType(newData.getCostType());
            calcResultDiv(newData.getDividendList());
            calcResultSector(newData.getSector());

//          this.setInfo(newData.getInfo().toString());
//          this.setPaySumOnShare(newData.getPaySumOnShare().toString());
//          this.setPaySum(newData.getPaySum().toString());
//          this.setPartOfProfit(newData.getPartOfProfit().toString());
//          this.setStableGrow(newData.getStableGrow().toString());
//          this.setStablePay(newData.getStablePay().toString());

            calcResultRecommendation(newData.getRecommendation());
            calcResultPayDate(newData.getPayDate());
        } catch (Exception e) {
            log.error("exception here: {}", e.getMessage());
        }
    }

    private void calcResultCost(List<String> newCostList) {
        for (String cost : newCostList) {
            double aNew = Double.parseDouble(cost
                    .replaceAll("\\[", "")
                    .replaceAll("]", "")
                    .replaceAll(",", ".")
                    .replaceAll(" ", ""));
            if (aNew > 0) {
                this.cost = this.cost == null ? aNew : (this.cost + aNew) / 2D;
            }
        }
    }

    private void calcResultLot(Integer newLotData) throws VariableLotException {
        if (newLotData == -1) {
            return;
        }
        if (lotSize == 1) {
            lotSize = newLotData;
        } else if (lotSize.intValue() != newLotData.intValue()) {
            throw new VariableLotException("Множественный результат. Лот: " + newLotData + " или " + lotSize, null);
        }
    }

    private void calcResultLotCost(double cost, int lot) {
        lotCost = cost * lot;
    }

    private void calcResultDiv(List<String> newDivDataList) {
        for (String div : newDivDataList) {
            double d = Double.parseDouble(div
                    .replaceAll("\\[", "")
                    .replaceAll("]", "")
                    .replaceAll(",", ".")
            );
            dividend = (dividend + d) / 2D;
        }

    }

    private void calcResultCostType(CostType type) throws Exception {
        if (type == null) {
            return;
        }
        if (costType == null) {
            costType = type;
        } else if (!costType.equals(type)) {
            throw new Exception("Cost type is multiply: " + type + " or " + costType);
        }
    }

    private void calcResultSector(String newData) {
        if (sector == null) {
            sector = newData;
        } else if (newData != null && !newData.equalsIgnoreCase("null")) {
            sector = sector.concat(" " + newData);
        }
    }

    private void calcResultRecommendation(List<String> newRecList) {
        if (newRecList.toString().equals("[]")) {
            return;
        }

        if (recommendation == null && !newRecList.isEmpty()) {
            recommendation = newRecList.toString().replace("[", "").replace("]", "").trim();
        } else {
            for (String rec : newRecList) {
                if (rec != null && !rec.equals("null")) {
                    recommendation = recommendation.concat(", " + rec);
                }
            }
        }
    }

    private void calcResultPayDate(LocalDate newData) {
        if (newData == null) {
            return;
        }
        if (payDate == null) {
            payDate = LocalDateTime.of(newData, LocalDateTime.now().toLocalTime());
        }
    }

    @Override
    public int compareTo(ResultShareDTO other) {
        int indComp = Short.compare(index, other.index);
        if (indComp != 0) {
            return indComp;
        }
        return Double.compare(other.lotCost, lotCost);
    }
}
