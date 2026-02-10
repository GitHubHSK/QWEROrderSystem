package com.qwer.qrorder.controller.admin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;

import com.qwer.qrorder.domain.Category;
import com.qwer.qrorder.domain.Menu;
import com.qwer.qrorder.domain.OrderMain;
import com.qwer.qrorder.service.MenuService;
import com.qwer.qrorder.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/menu")
public class AdminMenuController {

    // 서비스 의존성 주입
    private final MenuService menuService;
    private final OrderService orderService;
    
    /**
     * 메뉴 리스트 페이지
     * 페이징 처리
     * 카테고리 필터
     * 검색 기능 포함
     */
    @GetMapping("/list")
    public String getMenuList(
        @RequestParam(defaultValue = "1") int page,   // 기본값 1페이지
        @RequestParam(required = false) List<Integer> category,  // 선택된 카테고리
        @RequestParam(required = false) String keyword,          // 검색 키워드
        Model model
    ) {

        // page-1 하는 이유: Spring Data JPA는 0페이지부터 시작하기 때문
        Page<Menu> menuPage = menuService.getMenuList(page - 1, category, keyword);

        // 화면에 전달할 데이터 모델에 저장
        model.addAttribute("menuList", menuPage.getContent());
        model.addAttribute("page", menuPage);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("keyword", keyword);

        return "admin/menu/list";
    }

    /**
     * 메뉴 등록 폼 이동
     * 빈 Menu 객체를 넣어서 폼 바인딩 준비
     */
    @GetMapping("/insert")
    public String insertForm(Model model) {
        model.addAttribute("menu", new Menu());
        return "admin/menu/insertMenu";
    }

    /**
     * 메뉴 등록 처리
     * 이미지 파일 업로드
     * Category 번호를 Category 객체로 변환해서 Menu에 설정
     */
    @PostMapping("/insert")
    public String insertMenu(@RequestParam("categoryNo") int categoryNo,
                              Menu menu,
                              @RequestParam("menuImageFile") MultipartFile file) throws IOException {

        // categoryNo → Category 객체로 변환하여 menu에 설정
        Category category = new Category();
        category.setCategoryNo(categoryNo);
        menu.setCategory(category);

        // 프로젝트 내부 uploads 폴더에 이미지 저장
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs(); // 폴더가 없으면 생성
        }

        // 이미지 파일이 존재한다면 저장 처리
        if (!file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            // 파일명 중복 방지를 위해 UUID 사용
            String storedFilename = UUID.randomUUID() + "_" + originalFilename;

            // 파일 저장 (임시폴더 문제 방지)
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, 
                           Paths.get(uploadDir, storedFilename),
                           StandardCopyOption.REPLACE_EXISTING);
            }

            // DB에 저장될 파일명
            menu.setMenuImage(storedFilename);
        }

        // 기본 활성 상태로 설정
        menu.setIsActive("Y");

        // 서비스에 저장 요청
        menuService.insertMenu(menu, file);

        return "redirect:/admin/menu/list";
    }
    
    /**
     * 메뉴 수정 폼
     * 기존 메뉴 데이터 조회하여 화면에 표시
     */
    @GetMapping("/update/{menuNo}")
    public String updateForm(@PathVariable("menuNo") Integer menuNo, Model model) {

        // 수정하려는 메뉴 조회
        Menu menu = menuService.getMenuById(menuNo);

        model.addAttribute("menu", menu);
        return "admin/menu/updateMenu";
    }

    /**
     * 메뉴 수정 처리
     * 전달된 Menu 객체로 데이터 갱신
     * 새로운 이미지가 있다면 파일 업로드
     */
    @PostMapping("/update/{menuNo}")
    public String updateMenu(@PathVariable("menuNo") Integer menuNo,
                             Menu menu,
                             @RequestParam(value = "menuImageFile", required = false) MultipartFile file)
                             throws IOException {

        // PK 설정
        menu.setMenuNo(menuNo);

        // 메뉴 수정 처리
        menuService.updateMenu(menu, file);

        return "redirect:/admin/menu/list";
    }
    
    /**
     * 메뉴 삭제 처리
     * menuNo 기준으로 삭제
     */
    @GetMapping("/delete/{menuNo}")
    public String deleteMenu(@PathVariable("menuNo") Integer menuNo) {

        menuService.deleteMenu(menuNo);

        return "redirect:/admin/menu/list";
    }
    
    /**
     * 주문 내역 페이지
     * 모든 주문 목록 조회
     * 화면 렌더링
     */
    @GetMapping("/orderList")
    public String orderList(Model model) {

        // 주문 전체 조회
        List<OrderMain> orders = orderService.getAllOrders();
        System.out.println("orders = " + orders);

        model.addAttribute("orders", orders);
        return "admin/menu/orderList";
    }
}