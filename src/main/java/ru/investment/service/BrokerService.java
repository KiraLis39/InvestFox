package ru.investment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.investment.entity.Broker;
import ru.investment.entity.dto.BrokerDTO;
import ru.investment.mapper.BrokerMapper;
import ru.investment.repository.BrokerRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BrokerService {
    private final BrokerMapper brokerMapper;
    private final BrokerRepository brokerRepository;

    public void save(BrokerDTO toSave) {
        log.info("Saving the broker {}", toSave.getName());
        Optional<Broker> brok = brokerRepository.findBrokerByName(toSave.getName());
        if (brok.isEmpty()) {
            brokerRepository.save(brokerMapper.toEntity(toSave));
        } else {
            Broker exist = brok.get();
            exist.setData(toSave.getData());
        }
        log.info("The broker {} was saved successfully.", toSave.getName());
    }

    public Optional<BrokerDTO> findBrokerByName(String name) {
        Optional<Broker> found = brokerRepository.findBrokerByName(name);
        return found.map(brokerMapper::toDto).or(Optional::empty);
    }
}
