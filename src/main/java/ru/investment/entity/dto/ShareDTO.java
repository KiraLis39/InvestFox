package ru.investment.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.investment.enums.CostType;
import ru.investment.utils.UniversalNumberParser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Data
@Builder
@AllArgsConstructor
public class ShareDTO {
    @Builder.Default
    private final ArrayList<Float> coasts = new ArrayList<>(); // Стоимость
    @Builder.Default
    private final ArrayList<Double> dividends = new ArrayList<>(); // Дивиденты
    @Builder.Default
    private final ArrayList<Float> paySums = new ArrayList<>(); // Выплата
    @Builder.Default
    private final ArrayList<Float> paySumsOnShare = new ArrayList<>(); // Выплата на акцию
    @Builder.Default
    private final ArrayList<Float> partsOfProfit = new ArrayList<>(); // Часть дохода
    @Builder.Default
    private final ArrayList<Float> stablePays = new ArrayList<>(); // Стабильность выплат
    @Builder.Default
    private final ArrayList<Float> stableGrows = new ArrayList<>(); // Стабильность роста
    @Builder.Default
    private final ArrayList<String> infos = new ArrayList<>(); // Информация
    @Builder.Default
    private final ArrayList<String> recomendations = new ArrayList<>(); // Рекомендация

    private String ticker; // Тикет

    private String source; // Источник
    private String name; // Наименование

    private String sector; // Сектор

    private CostType costType; // Валюта

    private int lotSize; // Размер лота
    private LocalDateTime payDate; // Дата след. выплаты

    private LocalDateTime lastRefreshDate; // Обновлено

    public String getName() {
        return this.name == null ? "NA" : this.name;
    }

    public LocalDate getPayDate() {
        return this.payDate != null ? this.payDate.toLocalDate() : null;
    }

    public void addCoast(String... coast) {
        this.coasts.addAll(Arrays.stream(coast).map(UniversalNumberParser::parseFloat).toList());
    }

    public void addDividend(String dividend) {
        if (dividend == null) {
            return;
        }

        String[] data = dividend.split(" ");
        double div = 0;
        if (data.length > 0) {
            for (String datum : data) {
                try {
                    div = UniversalNumberParser.parseFloat(datum.replace(",", ".").replace("%", "").trim());
                } catch (Exception e) {
                    log.warn("Ошибка в цикле парсинга: {}", e.getMessage());
                }
            }
        }
        this.dividends.add(div);
    }

    public void addDividend(double dividend) {
        this.dividends.add(dividend);
    }

    public void addPaySum(String paySum) {
        this.paySums.add(UniversalNumberParser.parseFloat(paySum));
    }

    public void addPaySum(float paySum) {
        this.paySums.add(paySum);
    }

    public void addPaySumOnShare(String paySumOnShare) {
        this.paySumsOnShare.add(UniversalNumberParser.parseFloat(paySumOnShare));
    }

    public void addPaySumOnShare(float paySumOnShare) {
        this.paySumsOnShare.add(paySumOnShare);
    }

    public void addPartOfProfit(String partOfProfit) {
        this.partsOfProfit.add(UniversalNumberParser.parseFloat(partOfProfit));
    }

    public void addStablePay(String stablePay) {
        this.stablePays.add(UniversalNumberParser.parseFloat(stablePay));
    }

    public void addStableGrow(String stableGrow) {
        this.stableGrows.add(UniversalNumberParser.parseFloat(stableGrow));
    }

    public void addInfo(String info) {
        this.infos.add(info);
    }

    public void addRecommendation(String recommendation) {
        this.recomendations.add(recommendation);
    }

    public String getDividendsStringized() {
        return String.format("%,.2f", dividends.stream().toArray()) + (dividends.isEmpty() ? "" : "%");
    }
}
