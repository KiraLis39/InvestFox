package ru.investment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrokerDataList {
    @Id
    @GeneratedValue
    public UUID id;

    @Builder.Default
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection(targetClass = Float.class)
    public List<Float> inputs = new ArrayList<>();

    @Builder.Default
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection(targetClass = Float.class)
    public List<Float> outputs = new ArrayList<>();
}
