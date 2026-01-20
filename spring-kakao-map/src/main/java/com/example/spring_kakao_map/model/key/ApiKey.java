package com.example.spring_kakao_map.model.key;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @NoArgsConstructor @AllArgsConstructor
public class ApiKey {
    @Id
    private String keyValue; // 실제 API Key (예: "kakao-1234")
    private String owner;    // 주인 이름 (예: "홍길동")
}
