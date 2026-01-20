package com.example.spring_kakao_map.model.repository;

import com.example.spring_kakao_map.model.key.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, String> {
    // 기본 제공되는 findById(String id)를 통해 API Key 존재 여부를 확인할 수 있습니다.
}
