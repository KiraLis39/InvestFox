package ru.investment.mapper;

import org.springframework.stereotype.Component;
import ru.investment.ShareCollectedDTO;
import ru.investment.entity.Share;

import java.util.List;

@Component
public class ShareMapper {
    public Share toEntity(ShareCollectedDTO dto) {
        return Share.builder()
                .id(dto.getId())
                .name(dto.getName())
                .comment(dto.getComment())
                .cost(dto.getCost())
                .costType(dto.getCostType())
                .count(dto.getCount())
                .dividend(dto.getDividend())
                .index(dto.getIndex())
                .info(dto.getInfo())
                .lotCost(dto.getLotCost())
                .lotSize(dto.getLotSize())
                .nextPayDate(dto.getNextPayDate())
                .partOfProfit(dto.getPartOfProfit())
                .paySum(dto.getPaySum())
                .paySumOnShare(dto.getPaySumOnShare())
                .recommendation(dto.getRecommendation())
                .sectors(dto.getSectors())
                .showedName(dto.getShowedName())
                .source(dto.getSource())
                .stableGrow(dto.getStableGrow())
                .stablePay(dto.getStablePay())
                .ticker(dto.getTicker())
                .updatedDate(dto.getUpdatedDate())
                .build();
    }

    public List<Share> toEntity(List<ShareCollectedDTO> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }

    public ShareCollectedDTO toDto(Share entity) {
        return ShareCollectedDTO.builder()
                .id(entity.getId())
                .comment(entity.getComment())
                .cost(entity.getCost())
                .costType(entity.getCostType())
                .count(entity.getCount())
                .dividend(entity.getDividend())
                .index(entity.getIndex())
                .info(entity.getInfo())
                .lotCost(entity.getLotCost())
                .lotSize(entity.getLotSize())
                .name(entity.getName())
                .nextPayDate(entity.getNextPayDate())
                .partOfProfit(entity.getPartOfProfit())
                .paySum(entity.getPaySum())
                .paySumOnShare(entity.getPaySumOnShare())
                .recommendation(entity.getRecommendation())
                .sectors(entity.getSectors())
                .showedName(entity.getShowedName())
                .source(entity.getSource())
                .stableGrow(entity.getStableGrow())
                .stablePay(entity.getStablePay())
                .ticker(entity.getTicker())
                .updatedDate(entity.getUpdatedDate())
                .build();
    }
}
