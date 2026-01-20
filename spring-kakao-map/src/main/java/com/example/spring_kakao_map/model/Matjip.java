package com.example.spring_kakao_map.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @NoArgsConstructor
public class Matjip {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;      // 장소명 (예: 스타벅스 강남점)
    private String category;  // 카테고리 (예: 카페)
    private double lat;       // 위도
    private double lon;       // 경도

    public Matjip(String name, String category, double lat, double lon) {
        this.name = name;
        this.category = category;
        this.lat = lat;
        this.lon = lon;
    }
}
