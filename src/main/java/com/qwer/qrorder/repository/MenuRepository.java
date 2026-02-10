package com.qwer.qrorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.qwer.qrorder.domain.Menu;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
	/**
     * 메뉴 검색 + 카테고리 필터링 + 페이징 처리
     * @param keyword    메뉴 이름 검색어 (없으면 null)
     * @param categories 선택된 카테고리 번호 리스트 (없으면 null)
     * @param pageable   페이지 번호, 정렬 정보 등 페이징 처리 객체
     * @return Page<Menu> 페이징 처리된 메뉴 목록
     * JPQL 설명
     * m.menuName LIKE %:keyword%   → 메뉴명에 검색어가 포함된 항목만 찾음
     * :keyword IS NULL             → 검색어가 없으면 조건을 적용하지 않음
     * m.category.categoryNo IN :categories → 선택한 카테고리만 필터링
     * :categories IS NULL                  → 카테고리를 선택하지 않은 경우 조건 무시
     * 즉, 검색어와 카테고리 값이 있을 때만 해당 조건을 적용하는 동적 쿼리 역할을 함
     */
    @Query("""
    SELECT m FROM Menu m
    WHERE (:keyword IS NULL OR m.menuName LIKE %:keyword%)
      AND (:categories IS NULL OR m.category.categoryNo IN :categories)
    """)
    Page<Menu> findMenus(
            @Param("keyword") String keyword,
            @Param("categories") List<Integer> categories,
            Pageable pageable
    );
}