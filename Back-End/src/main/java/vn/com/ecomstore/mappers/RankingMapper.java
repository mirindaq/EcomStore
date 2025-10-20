package vn.com.ecomstore.mappers;

import vn.com.ecomstore.dtos.response.rank.RankResponse;
import vn.com.ecomstore.dtos.response.voucher.RankVoucherResponse;
import vn.com.ecomstore.entities.Ranking;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface RankingMapper {

    RankVoucherResponse toRankVoucherResponse(Ranking ranking);

    RankResponse toRankResponse(Ranking ranking);
}
