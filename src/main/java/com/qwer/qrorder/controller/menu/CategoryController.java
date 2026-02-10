package com.qwer.qrorder.controller.menu;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.qwer.qrorder.domain.Category;
import com.qwer.qrorder.service.CategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    // Category 관련 비즈니스 로직을 처리하는 서비스
    private final CategoryService categoryService;

    /**
     * 카테고리 목록 조회
     * 프론트(메뉴 페이지 등)에서 AJAX로 호출하여 카테고리 목록을 받음
     * JSON 형태로 Category 리스트가 반환
     */
    @GetMapping("/list")
    public List<Category> list() {
        return categoryService.getAllCategories();
    }
}