package ru.investment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.investment.enums.CostType;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "shares")
@AllArgsConstructor
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Builder.Default
    @Column(name = "updated_date")
    private LocalDateTime updatedDate = LocalDateTime.now(); // Обновлено

    @Column(name = "index")
    private short index; // Индекс в таблице

    @Max(value = 8, message = "Тикет не может быть длиннее восьми букв")
    @Column(name = "ticker", nullable = false, unique = true)
    private String ticker; // Тикет

    @Column(name = "source")
    private String source; // Источник

    @Column(name = "name", unique = true)
    private String name; // Наименования

    @Column(name = "showed_name")
    private String showedName; // Отображаемое имя

    @Builder.Default
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> sectors = new HashSet<>(); // Сектор

    @Builder.Default
    @Column(name = "cost_type")
    private CostType costType = CostType.UNKNOWN; // Валюта

    @Min(1)
    @Builder.Default
    @Column(name = "lot_size")
    private short lotSize = 1; // Размер лота

    @Column(name = "cost")
    private double cost; // Стоимость

    @Column(name = "lot_cost")
    private double lotCost; // Стоимость лота

    @Column(name = "dividend")
    private double dividend; // Дивиденты

    @Column(name = "count")
    private int count; // Количество

    @Column(name = "pay_sum")
    private double paySum; // Выплата

    @Column(name = "pay_sum_on_share")
    private double paySumOnShare; // Выплата на акцию

    @Column(name = "part_of_profit")
    private String partOfProfit; // Часть дохода

    @Column(name = "stable_pay")
    private String stablePay; // Стабильность выплат

    @Column(name = "stable_grow")
    private String stableGrow; // Стабильность роста

    @Column(name = "info", columnDefinition = "text")
    private String info; // Информация

    @Column(name = "recommendation")
    private String recommendation; // Рекомендация

    @Column(name = "next_pay_date")
    private LocalDateTime nextPayDate; // Дата след. выплаты

    @Column(name = "comment")
    private String comment; // Комментарий
}
