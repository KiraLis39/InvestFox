package ru.investment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "broker_data")
public class BrokerDataList {
    @Id
    @GeneratedValue
    private UUID id;

    @Builder.Default
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection(targetClass = Float.class)
    private List<Float> inputs = new ArrayList<>();

    @Builder.Default
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection(targetClass = Float.class)
    private List<Float> outputs = new ArrayList<>();
}
