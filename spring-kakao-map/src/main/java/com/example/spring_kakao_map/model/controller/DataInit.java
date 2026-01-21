package com.example.spring_kakao_map.model.controller;



import com.example.spring_kakao_map.model.Matjip;
import com.example.spring_kakao_map.model.key.ApiKey;
import com.example.spring_kakao_map.model.repository.ApiKeyRepository;
import com.example.spring_kakao_map.model.repository.MatjipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

@Slf4j // 로그를 찍기 위해 추가
@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final MatjipRepository placeRepository;
    private final ApiKeyRepository apiKeyRepository;

    // JSON 파싱을 도와주는 도구 (스프링 부트 기본 내장)
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {

        // 1. 이미 데이터가 있다면 초기화하지 않음 (중복 방지)
        if (placeRepository.count() > 0) {
            log.info("ℹ️ 데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
        } else {
            // 2. resources/data/places.json 파일 읽기
            InputStream inputStream = TypeReference.class.getResourceAsStream("/data/places.json");

            // 3. JSON -> List<Place> 변환 (역직렬화)
            List<Matjip> places = objectMapper.readValue(inputStream, new TypeReference<List<Matjip>>(){});

            // 4. DB에 한방에 저장
            placeRepository.saveAll(places);
            log.info("✅ JSON 파일에서 맛집 데이터 {}개를 로드했습니다.", places.size());
        }

        // API Key는 간단하니까 그냥 유지 (원하면 이것도 json으로 분리 가능)
        if (apiKeyRepository.count() == 0) {
            apiKeyRepository.save(new ApiKey("test-key-1234", "학생 개발자"));
        }
    }
}