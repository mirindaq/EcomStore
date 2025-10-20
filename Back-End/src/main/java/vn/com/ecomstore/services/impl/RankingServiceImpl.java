package vn.com.ecomstore.services.impl;

import vn.com.ecomstore.dtos.response.rank.RankResponse;
import vn.com.ecomstore.entities.Ranking;
import vn.com.ecomstore.exceptions.custom.ResourceNotFoundException;
import vn.com.ecomstore.mappers.RankingMapper;
import vn.com.ecomstore.repositories.RankingRepository;
import vn.com.ecomstore.services.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final RankingRepository rankingRepository;
    private final RankingMapper rankingMapper;

    @Override
    public Ranking getRankingEntityById(Long id) {
        return rankingRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Ranking not found with id " + id));
    }

    @Override
    public List<RankResponse> getAllRankings() {
        return rankingRepository.findAll()
                .stream()
                .map(rankingMapper::toRankResponse)
                .toList();
    }
}
