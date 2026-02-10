package com.qwer.qrorder.service;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.qwer.qrorder.domain.Category;
import com.qwer.qrorder.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryService {

	// CategoryRepository 주입 (생성자 자동 생성)
    private final CategoryRepository categoryRepository;

    /**
     * DB에 저장된 모든 카테고리를 조회하여 반환
     * @return List<Category> 전체 카테고리 목록
     * categoryRepository.findAll() → JPA에서 제공하는 기본 메서드로, CATEGORY 테이블의 모든 데이터를 가져옴
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}