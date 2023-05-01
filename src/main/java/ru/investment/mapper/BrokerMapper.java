package ru.investment.mapper;

import org.springframework.stereotype.Component;
import ru.investment.entity.Broker;
import ru.investment.entity.dto.BrokerDTO;

@Component
public class BrokerMapper {
    public Broker toEntity(BrokerDTO dto) {
        return Broker.builder()
                .name(dto.getName())
                .data(dto.getData())
                .build();
    }

    public BrokerDTO toDto(Broker entity) {
        return BrokerDTO.builder()
                .name(entity.getName())
                .data(entity.getData())
                .build();
    }
}
