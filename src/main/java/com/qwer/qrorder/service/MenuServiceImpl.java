package com.qwer.qrorder.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qwer.qrorder.domain.Category;
import com.qwer.qrorder.domain.Menu;
import com.qwer.qrorder.dto.MenuDTO;
import com.qwer.qrorder.repository.CategoryRepository;
import com.qwer.qrorder.repository.MenuRepository;

import lombok.RequiredArgsConstructor;
/**
 * 메뉴 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 관리자 메뉴 등록/수정/삭제
 * 사용자 화면에서 필요한 메뉴 데이터 조회
 * 페이징 처리된 메뉴 목록 제공
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    
    /**
     * 관리자 화면에서 전체 메뉴 목록 조회 (Thymeleaf 렌더링용)
     */
    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    /**
     * 사용자 화면에서 AJAX로 사용되는 메뉴 데이터 조회
     * Menu 엔티티 → MenuDTO 로 변환하여 필요한 정보만 전송
     */
    @Override
    public List<MenuDTO> getMenuDTOList() {
        return menuRepository.findAll().stream()
                .map(menu -> {
                    MenuDTO dto = new MenuDTO();
                    dto.setMenuNo(menu.getMenuNo());
                    dto.setMenuName(menu.getMenuName());
                    dto.setPrice(menu.getPrice());
                    dto.setCategoryName(menu.getCategory().getCategoryName());
                    
                    // 이미지 경로는 /uploads/ 저장된 파일명 형식
                    dto.setImageUrl("/uploads/" + menu.getMenuImage());
                    return dto;
                })
                .toList();
    }


    /**
     * 메뉴 등록 처리
     * 업로드된 이미지 처리
     * DB에는 파일명만 저장
     */
    @Override
    @Transactional
    public void insertMenu(Menu menu, MultipartFile file) throws IOException {

    	// 이미지 파일이 업로드된 경우만 처리
        if (!file.isEmpty()) {

        	// 업로드 폴더 위치 설정 (프로젝트 내부 uploads 폴더)
            String uploadDir = System.getProperty("user.dir") + "/uploads/";

            // uploads 폴더가 없다면 생성
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 파일명 중복 방지를 위해 UUID 추가
            String originalFilename = file.getOriginalFilename();
            String storedFilename = UUID.randomUUID() + "_" + originalFilename;

            File dest = new File(dir, storedFilename);
            file.transferTo(dest); // 실제 파일 저장

            // DB에는 파일명만 저장
            menu.setMenuImage(storedFilename);
        }

        // 기본 활성화 상태로 설정
        menu.setIsActive("Y");
        
        // 메뉴 저장
        menuRepository.save(menu);
    }
    
    // Menu 조회
    @Override
    public Menu getMenuById(Integer menuNo) {
        return menuRepository.findById(menuNo)
            .orElseThrow(() -> new IllegalArgumentException("該当するメニューが見つかりません。menuNo=" + menuNo));
    }

    /**
     * 메뉴 번호로 메뉴 조회
     * 없는 번호 요청 시 예외 발생
     */
    @Override
    @Transactional
    public void updateMenu(Menu menu, MultipartFile file) throws IOException {
    	
    	// 기존 메뉴 데이터 조회
        Menu existingMenu = menuRepository.findById(menu.getMenuNo())
                .orElseThrow(() -> new IllegalArgumentException("メニューなし: " + menu.getMenuNo()));

        // 기본 정보 수정
        existingMenu.setMenuName(menu.getMenuName());
        existingMenu.setPrice(menu.getPrice());
        existingMenu.setIsActive(menu.getIsActive());

        // 카테고리 변경 시, categoryNo를 기준으로 Category 엔티티 조회 후 교체
        Integer newCategoryNo = menu.getCategory().getCategoryNo();
        if (newCategoryNo != null) {
            Category newCategory = categoryRepository.findById(newCategoryNo)
                    .orElseThrow(() -> new IllegalArgumentException("カテゴリーなし: " + newCategoryNo));
            existingMenu.setCategory(newCategory);
        }

        // 이미지 파일이 포함된 경우 이미지 교체 처리
        if (!file.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";

            // 기존 이미지 삭제
            if (existingMenu.getMenuImage() != null) {
                File oldFile = new File(uploadDir + existingMenu.getMenuImage());
                if (oldFile.exists()) oldFile.delete();
            }

            // 새로운 파일 저장
            String originalFilename = file.getOriginalFilename();
            String storedFilename = UUID.randomUUID() + "_" + originalFilename;
            file.transferTo(new File(uploadDir + storedFilename));

            existingMenu.setMenuImage(storedFilename);
        }

        // 수정된 메뉴 저장
        menuRepository.save(existingMenu);
    }
    
    /**
     * 메뉴 삭제 처리
     * DB 삭제 전, 업로드된 이미지 파일 삭제
     */
    @Override
    @Transactional
    public void deleteMenu(Integer menuNo) {
        Menu menu = menuRepository.findById(menuNo)
                .orElseThrow(() -> new IllegalArgumentException("メニューなし: " + menuNo));

        // 이미지 파일 삭제
        if (menu.getMenuImage() != null) {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File imgFile = new File(uploadDir + menu.getMenuImage());
            if (imgFile.exists()) {
                imgFile.delete();
            }
        }
        
        // 메뉴 데이터 삭제
        menuRepository.delete(menu);
    }

    /**
     * 관리자 페이지 메뉴 목록 조회 (페이징 + 검색 + 카테고리 필터)
     * @param page       페이지 번호 (0부터 시작)
     * @param categories 선택된 카테고리 목록
     * @param keyword    검색어
     */
    @Override
    public Page<Menu> getMenuList(int page, List<Integer> categories, String keyword) {

        Pageable pageable = PageRequest.of(page, 5); // 페이지 크기: 10개

        // 카테고리 필터가 빈 리스트라면 null 처리 (전체 조회)
        if (categories != null && categories.isEmpty()) {
            categories = null;
        }

        return menuRepository.findMenus(keyword, categories, pageable);
    }
}