package vn.com.ecomstore.services;

import vn.com.ecomstore.dtos.response.rank.RankResponse;
import vn.com.ecomstore.entities.Ranking;

import java.util.List;

public interface RankingService {
    Ranking getRankingEntityById(Long id);

    List<RankResponse> getAllRankings();
}
