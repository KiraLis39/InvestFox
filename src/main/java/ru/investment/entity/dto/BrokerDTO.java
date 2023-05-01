package ru.investment.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.investment.entity.BrokerDataList;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class BrokerDTO {

    private String name;

    @Builder.Default
    private BrokerDataList data = BrokerDataList.builder().build();
}
