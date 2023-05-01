package ru.investment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.investment.entity.Broker;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BrokerRepository extends JpaRepository<Broker, UUID>, JpaSpecificationExecutor<Broker> {

    Optional<Broker> findBrokerByName(String name);
}
