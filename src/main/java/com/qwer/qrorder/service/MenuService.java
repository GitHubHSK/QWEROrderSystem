package com.qwer.qrorder.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;

import com.qwer.qrorder.domain.Menu;
import com.qwer.qrorder.dto.MenuDTO;

public interface MenuService {
    // 모든 메뉴 조회 (관리자/사용자 공용)
	List<Menu> getAllMenus();
	
	// 사용자 AJAX용 DTO 변환 데이터 조회
	List<MenuDTO> getMenuDTOList();
	
	// 메뉴 등록 (관리자용)
    void insertMenu(Menu menu, MultipartFile file) throws IOException;
    
    // 메뉴 번호로 조회 (수정용)
    Menu getMenuById(Integer menuNo);

    // 메뉴 수정 (관리자용)
    void updateMenu(Menu menu, MultipartFile file) throws IOException;
    
    // 메뉴 삭제 (관리자용)
    void deleteMenu(Integer menuNo);

    Page<Menu> getMenuList(int page, List<Integer> categories, String keyword);
}