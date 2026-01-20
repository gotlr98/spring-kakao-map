package com.example.spring_kakao_map.model.controller;



import com.example.spring_kakao_map.model.Matjip;
import com.example.spring_kakao_map.model.key.ApiKey;
import com.example.spring_kakao_map.model.repository.ApiKeyRepository;
import com.example.spring_kakao_map.model.repository.MatjipRepository;
import com.example.spring_kakao_map.model.service.RateLimiterService;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class MapApiController {

    private final MatjipRepository placeRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final RateLimiterService rateLimiterService;

    @GetMapping
    public ResponseEntity<?> searchPlaces(
            @RequestHeader(value = "X-API-KEY") String requestKey,
            @RequestParam String keyword
    ) {
        // 1. API Key가 DB에 존재하는지 검사 (인증)
        ApiKey apiKey = apiKeyRepository.findById(requestKey)
                .orElse(null);

        if (apiKey == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 API Key입니다.");
        }

        // 2. 사용량 제한 검사 (Rate Limiting)
        Bucket bucket = rateLimiterService.resolveBucket(requestKey);

        if (bucket.tryConsume(1)) { // 토큰 1개를 소모해본다. 성공하면 True
            // 3. 성공 시 데이터 조회 (비즈니스 로직)
            List<Matjip> places = placeRepository.findByNameContaining(keyword);
            return ResponseEntity.ok(places);
        } else {
            // 4. 실패 시 (토큰 부족) 429 Too Many Requests 리턴
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("API 호출 한도를 초과했습니다. (1분에 5회)");
        }
    }
}
