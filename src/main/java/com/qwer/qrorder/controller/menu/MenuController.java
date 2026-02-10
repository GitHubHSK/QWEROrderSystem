package com.qwer.qrorder.controller.menu;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qwer.qrorder.domain.Menu;
import com.qwer.qrorder.dto.MenuDTO;
import com.qwer.qrorder.service.MenuService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {
    
    // 메뉴 관련 비즈니스 로직 처리 서비스
    private final MenuService menuService;

    /**
     * 메뉴 리스트 페이지
     * 회원(손님)이 QR을 찍고 들어온 뒤 메뉴 목록을 보여주는 화면
     * 세션에서 tableNo, allergy 정보를 읽어와 화면에 전달
     */
    @GetMapping("/list")
    public String menuList(HttpSession session, Model model) {

        // 세션에서 테이블 번호 가져오기
        Integer tableNo = (Integer) session.getAttribute("tableNo");
        System.out.println("session.tableNo = " + tableNo);

        // 메뉴 전체 목록 조회
        List<Menu> menuList = menuService.getAllMenus();
        model.addAttribute("menuList", menuList);

        // 테이블 번호, 알레르기 정보 모델에 전달
        model.addAttribute("tableNo", tableNo);
        model.addAttribute("allergy", session.getAttribute("allergy"));

        // 메뉴 리스트 화면 경로 반환
        return "menu/list";
    }

    /**
     * 메뉴 리스트 AJAX 요청
     * 프론트에서 비동기로 메뉴 데이터를 가져갈 때 사용하는 API
     * JSON 형태로 MenuDTO 목록을 반환
     */
    @GetMapping("/list/data")
    @ResponseBody
    public List<MenuDTO> getMenuListAjax() {
        return menuService.getMenuDTOList();
    }

    /**
     * 장바구니 페이지
     * 사용자가 선택한 메뉴 목록을 확인하는 화면
     */
    @GetMapping("/cart")
    public String cartPage() {
        return "/menu/cart";
    }
}