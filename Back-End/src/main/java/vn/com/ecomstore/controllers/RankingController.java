package vn.com.ecomstore.controllers;

import vn.com.ecomstore.dtos.response.base.ResponseSuccess;
import vn.com.ecomstore.dtos.response.rank.RankResponse;
import vn.com.ecomstore.services.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.prefix}/rankings")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("")
    public ResponseEntity<ResponseSuccess<List<RankResponse>>> getAllRank() {
        return ResponseEntity.ok(new ResponseSuccess<>(
                OK,
                "Get rankings success",
                rankingService.getAllRankings()
        ));
    }
}