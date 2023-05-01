package ru.investment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.investment.entity.Share;
import ru.investment.repository.SharesRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShareService {
    private final SharesRepository sharesRepository;

    public void saveAll(List<Share> shares) {
        sharesRepository.saveAll(shares);
    }

    public List<Share> findAll() {
        return sharesRepository.findAll();
    }
}
