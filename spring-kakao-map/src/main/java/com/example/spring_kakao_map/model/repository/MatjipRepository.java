package com.example.spring_kakao_map.model.repository;

import com.example.spring_kakao_map.model.Matjip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatjipRepository extends JpaRepository<Matjip, Long> {
    // 장소 이름에 키워드가 포함된 데이터를 모두 찾는 메서드 (SQL의 LIKE %keyword% 와 같음)
    List<Matjip> findByNameContaining(String keyword);
}
