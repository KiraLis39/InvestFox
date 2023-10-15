package ru.investment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.investment.ShareCollectedDTO;
import ru.investment.entity.Share;
import ru.investment.gui.TablePane;
import ru.investment.gui.components.ShareTableRow;
import ru.investment.mapper.ShareMapper;
import ru.investment.repository.SharesRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShareService {
    private final SharesRepository sharesRepository;
    private final ShareMapper shareMapper;
    private TablePane tablePane;

    @Autowired
    public void setTablePane(@Lazy TablePane tablePane) {
        this.tablePane = tablePane;
    }

    public List<Share> findAll() {
        return sharesRepository.findAll();
    }

    public Optional<Share> findShareByUUID(UUID id) {
        return sharesRepository.findById(id);
    }

    public void updateOrSave(List<ShareCollectedDTO> shares) {
        for (ShareCollectedDTO share : shares) {
            Optional<Share> exists = findShareByTicker(share.getTicker());
            if (exists.isPresent()) {
                // update exists share:
                Share updated = update(exists.get(), share);
                sharesRepository.save(updated);
            } else {
                // create a new share into DB:
                sharesRepository.save(shareMapper.toEntity(share));
            }
        }
    }

    public Optional<Share> findShareByTicker(String ticker) {
        return sharesRepository.findByTicker(ticker);
    }

    private Share update(Share share, ShareCollectedDTO dto) {
        share.setComment(dto.getComment());
        share.setCost(dto.getCost());
        share.setCostType(dto.getCostType());
        share.setCount(dto.getCount());
        share.setDividend(dto.getDividend());
        share.setIndex(dto.getIndex());
        share.setInfo(dto.getInfo());
        share.setLotCost(dto.getLotCost());
        share.setLotSize(dto.getLotSize());
        share.setName(dto.getName());
        share.setNextPayDate(dto.getNextPayDate());
        share.setPartOfProfit(dto.getPartOfProfit());
        share.setPaySum(dto.getPaySum());
        share.setPaySumOnShare(dto.getPaySumOnShare());
        share.setRecommendation(dto.getRecommendation());
        share.setSectors(dto.getSectors());
        share.setShowedName(dto.getShowedName());
        share.setSource(dto.getSource());
        share.setStableGrow(dto.getStableGrow());
        share.setStablePay(dto.getStablePay());
        share.setTicker(dto.getTicker());
        share.setUpdatedDate(dto.getUpdatedDate());
        return share;
    }

    public void saveTable(List<ShareTableRow> shareTableRows) {
        log.info("Saving shares..");
        List<ShareCollectedDTO> shares = shareTableRows.stream().map(ShareTableRow::getResultDto).toList();
        updateOrSave(shares);
    }

    public void reload() throws IOException {
        saveTable(tablePane.getRows());
        TablePane.clearRows();
        loadTable();
    }

    public void loadTable() throws IOException {
        log.info("Loading shares..");
        List<ShareCollectedDTO> loading = new ArrayList<>(findAll().stream().map(shareMapper::toDto).toList());
        Collections.sort(loading);
        tablePane.addShares(loading);
    }
}
