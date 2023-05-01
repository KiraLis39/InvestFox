package ru.investment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "brokers")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Broker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", unique = true, nullable = false, length = 32)
    private String name;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = BrokerDataList.class, orphanRemoval = true, cascade = CascadeType.ALL)
//    @JoinColumn(name = "data_id")
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = BrokerDataList.class, fetch = FetchType.LAZY)
    private BrokerDataList data;
}
