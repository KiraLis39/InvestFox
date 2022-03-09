package dto;

import components.FOptionPane;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sites.exceptions.VariableLotException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResultShareDTO implements Serializable, Comparable<ResultShareDTO> {
    //    @JsonProperty("Индекс")
    private short INDEX;
    //    @JsonProperty("Тикет")
    private String TICKER;
    //    @JsonProperty("Источник")
    private String SOURCE;
    //    @JsonProperty("Наименования")
    private String NAME;
    //    @JsonProperty("Отображаемое имя")
    private String SHOWED_NAME;
    //    @JsonProperty("Сектор")
    private String SECTOR;
    //    @JsonProperty("Стоимость")
    private Double COST;
    //    @JsonProperty("Валюта")
    private String COST_TYPE;
    //    @JsonProperty("Размер лота")
    private Integer LOT_SIZE = 1;
    //    @JsonProperty("Дивиденты")
    private Double DIVIDEND;
    //    @JsonProperty("Количество")
    private int COUNT;
    //    @JsonProperty("Выплата")
    private Double PAY_SUM;
    //    @JsonProperty("Выплата на акцию")
    private Double PAY_SUM_ON_SHARE;
    //    @JsonProperty("Часть дохода")
    private String PART_OF_PROFIT;
    //    @JsonProperty("Стабильность выплат")
    private String STABLE_PAY;
    //    @JsonProperty("Стабильность роста")
    private String STABLE_GROW;
    //    @JsonProperty("Информация")
    private String INFO;
    //    @JsonProperty("Рекомендация")
    private String RECOMMENDATION;
    //    @JsonProperty("Дата след. выплаты")
    private LocalDateTime PAY_DATE;
    //    @JsonProperty("Стоимость лота")
    private Double LOT_COST;
    //    @JsonProperty("Комментарий")
    private String COMMENT;
    //    @JsonProperty("Обновлено")
    private static LocalDateTime LAST_REFRESH = LocalDateTime.now();


    public synchronized void update(String ticker, ShareDTO newData) {
        if (newData == null) {return;}

        this.TICKER = ticker;
        this.NAME = this.NAME == null ? (newData.getName().equals("NA") ? null : newData.getName()) : this.NAME.concat(" :: " + newData.getName());
        if (this.NAME != null && this.SHOWED_NAME == null) {
            if (!this.NAME.split(" :: ")[0].equals("NA")) {
                this.SHOWED_NAME = this.NAME.split(" :: ")[0];
            }
        }

        try {
            if (newData.getCoastList().size() > 0) {
                calcResultCost(newData.getCoastList());
            }
            calcResultLot(newData.getLotSize());
            calcResultLotCost(COST == null ? 0D : COST, LOT_SIZE == -1 ? 1 : LOT_SIZE);

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
        } catch (VariableLotException vle) {
            vle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calcResultCost(ArrayList<String> newCostList) {
        for (String cost : newCostList) {
            double aNew = Double.parseDouble(cost
                            .replaceAll("\\[", "")
                            .replaceAll("]", "")
                            .replaceAll(",", ".")
                            .replaceAll(" ", ""));
            if (aNew > 0) {
                COST = COST == null ? aNew : (COST + aNew) / 2D;
            }
        }
    }

    private void calcResultLot(Integer newLotData) throws VariableLotException {
        if (newLotData == -1) {return;}
        if (LOT_SIZE == 1) {
            LOT_SIZE = newLotData;
        } else if (LOT_SIZE.intValue() != newLotData.intValue()) {
            throw new VariableLotException("Множественный результат. Лот: " + newLotData + " или " + LOT_SIZE);
        }
    }

    private void calcResultLotCost(double cost, int lot) {
        LOT_COST = cost * lot;
    }

    private void calcResultDiv(ArrayList<String> newDivDataList) {
        for (String div : newDivDataList) {
            Double d = Double.parseDouble(div
                    .replaceAll("\\[", "")
                    .replaceAll("]", "")
                    .replaceAll(",", ".")
            );
            DIVIDEND = DIVIDEND == null ? d : (DIVIDEND + d) / 2D;
        }

    }

    private void calcResultCostType(String newData) throws Exception {
        if (newData == null) {return;}
        if (COST_TYPE == null) {
            COST_TYPE = newData;
        } else if (!COST_TYPE.equals(newData)) {
            throw new Exception("Cost type is multiply: " + newData + " or " + COST_TYPE);
        }
    }

    private void calcResultSector(String newData) {
        if (SECTOR == null) {
            SECTOR = newData;
        } else {
            SECTOR = SECTOR.concat(" " + newData);
        }
    }

    private void calcResultRecommendation(ArrayList<String> newRecList) {
        if (newRecList.toString().equals("[]")) {return;}

        if (RECOMMENDATION == null && newRecList.size() > 0) {
            RECOMMENDATION = newRecList.toString().replace("[", "").replace("]", "").trim();
        } else {
            for (String rec : newRecList) {
                if (rec != null && !rec.equals("null")) {
                    RECOMMENDATION = RECOMMENDATION.concat(", " + rec);
                }
            }
        }
    }

    private void calcResultPayDate(LocalDate newData) {
        if (newData == null) {return;}
        if (PAY_DATE == null) {
            PAY_DATE = LocalDateTime.of(newData, LocalDateTime.now().toLocalTime());
        }
    }

    @Override
    public int compareTo(ResultShareDTO other) {
        return Short.compare(INDEX, other.INDEX);
    }
}
