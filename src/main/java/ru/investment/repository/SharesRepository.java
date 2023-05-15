package ru.investment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.investment.entity.Share;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SharesRepository extends JpaRepository<Share, UUID>, JpaSpecificationExecutor<Share> {

    Optional<Share> findByTicker(String ticker);
}
