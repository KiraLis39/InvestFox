package ru.investment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.enums.CostType;
import ru.investment.exceptions.BadDataException;
import ru.investment.exceptions.VariableLotException;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Slf4j
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
public class ShareCollectedDTO implements Serializable, Comparable<ShareCollectedDTO> {
    private UUID id; // id from DataBase

    private LocalDateTime updatedDate = LocalDateTime.now(); // Обновлено
    private short index; // Индекс в таблице
    @Max(value = 8, message = "Тикет не может быть длиннее восьми букв")
    private String ticker; // Тикет
    private String source; // Источник
    private String name; // Наименования
    private String showedName; // Отображаемое имя
    private String sector; // Сектор
    private CostType costType; // Валюта
    private short lotSize = 1; // Размер лота
    private double cost; // Стоимость
    private double lotCost; // Стоимость лота
    private double dividend; // Дивиденты
    private int count; // Количество
    private double paySum; // Выплата
    private double paySumOnShare; // Выплата на акцию
    private String partOfProfit; // Часть дохода
    private String stablePay; // Стабильность выплат
    private String stableGrow; // Стабильность роста
    private String info; // Информация
    private String recommendation; // Рекомендация
    private LocalDateTime nextPayDate; // Дата след. выплаты
    private String comment; // Комментарий

    public synchronized void update(String ticker, ShareDTO newData) {
        if (newData == null) {
            return;
        }

        this.ticker = ticker;
        this.name = this.name == null ? (newData.getName().equals("NA") ? null : newData.getName()) : this.name.concat(";" + newData.getName());
        if (this.name != null) {
            for (String n : this.name.split(";")) {
                if (!n.equals("NA") && (this.showedName == null || n.length() < this.showedName.length())) {
                    this.showedName = n;
                }
            }
        }

        try {
            if (!newData.getCoasts().isEmpty()) {
                calcResultCost(newData.getCoasts());
            }
            calcResultLot(newData.getLotSize());
            calcResultLotCost(cost == 0 ? 0D : cost, lotSize == -1 ? 1 : lotSize);

            calcResultCostType(newData.getCostType());
            calcResultDiv(newData.getDividends());
            calcResultSector(newData.getSector());

//          this.setInfo(newData.getInfo().toString());
//          this.setPaySumOnShare(newData.getPaySumOnShare().toString());
//          this.setPaySum(newData.getPaySum().toString());
//          this.setPartOfProfit(newData.getPartOfProfit().toString());
//          this.setStableGrow(newData.getStableGrow().toString());
//          this.setStablePay(newData.getStablePay().toString());

            calcResultRecommendation(newData.getRecomendations());
            calcResultPayDate(newData.getPayDate());
        } catch (Exception e) {
            log.error("exception here: {}", e.getMessage());
        }
    }

    private void calcResultCost(List<Float> newCosts) {
        for (float cost : newCosts) {
            if (cost > 0) {
                this.cost = this.cost == 0 ? cost : (this.cost + cost) / 2f;
            }
        }
    }

    private void calcResultLot(int newLotData) throws VariableLotException {
        if (newLotData == -1) {
            return;
        }
        if (newLotData > 0) {
            if (lotSize == 1) {
                lotSize = (short) newLotData;
            } else if (lotSize != newLotData) {
                lotSize = (short) Math.min(lotSize, newLotData);
                log.warn("Множественный результат. Лот: {} или {}", newLotData, lotSize);
            }
        }
    }

    private void calcResultLotCost(double cost, int lot) {
        lotCost = cost * lot;
    }

    private void calcResultDiv(List<Float> newDivDataList) {
        for (Float div : newDivDataList) {
            dividend = (dividend + div) / 2f;
        }
    }

    private void calcResultCostType(CostType type) throws BadDataException {
        if (type == null) {
            return;
        }
        if (costType == null) {
            costType = type;
        } else if (!costType.equals(type)) {
            throw new BadDataException("Cost type is multiply: " + type + " or " + costType);
        }
    }

    private void calcResultSector(String newData) {
        if (sector == null) {
            sector = newData;
        } else if (newData != null && !newData.equalsIgnoreCase("null")) {
            sector = sector.concat(";" + newData);
        }
    }

    private void calcResultRecommendation(List<String> newRecList) {
        if (newRecList.toString().equals("[]")) {
            return;
        }

        if (recommendation == null) {
            if (!newRecList.isEmpty()) {
                recommendation = newRecList.toString().replace("[", "").replace("]", "").trim();
            }
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
        if (nextPayDate == null) {
            nextPayDate = LocalDateTime.of(newData, LocalDateTime.now().toLocalTime());
        }
    }

    @Override
    public int compareTo(ShareCollectedDTO other) {
        int indComp = Short.compare(index, other.index);
        if (indComp != 0) {
            return indComp;
        }
        return Double.compare(other.lotCost, lotCost);
    }
}
