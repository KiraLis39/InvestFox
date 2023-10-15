package ru.investment.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.investment.entity.BrokerDataList;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class BrokerDTO {

    private String name;

    @Builder.Default
    private BrokerDataList data = BrokerDataList.builder().build();
}
