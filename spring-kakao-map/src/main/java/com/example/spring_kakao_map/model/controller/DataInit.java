package com.example.kakaomap;

import com.example.spring_kakao_map.model.Matjip;
import com.example.spring_kakao_map.model.key.ApiKey;
import com.example.spring_kakao_map.model.repository.ApiKeyRepository;
import com.example.spring_kakao_map.model.repository.MatjipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final MatjipRepository placeRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ResourceLoader resourceLoader; // 스프링의 리소스 로더 추가
    private final ObjectMapper objectMapper;     // 빈으로 주입받는 방식으로 변경 가능

    @Override
    public void run(String... args) throws Exception {

        if (placeRepository.count() > 0) {
            log.info("Skipping data initialization: Database already has records.");
            return;
        }

        // ✅ 파일 읽기 방식을 스프링 표준으로 변경 (클래스패스 명시)
        Resource resource = resourceLoader.getResource("classpath:data/places.json");

        try {
            List<Matjip> places = objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<List<Matjip>>() {}
            );

            placeRepository.saveAll(places);
            log.info("🚀 Successfully loaded {} places from JSON resource.", places.size());
        } catch (Exception e) {
            log.error("❌ Failed to load seed data: {}", e.getMessage());
        }

        // API Key 초기화 (생략 가능)
        if (apiKeyRepository.count() == 0) {
            apiKeyRepository.save(new ApiKey("test-key-1234", "Default User"));
        }
    }
}