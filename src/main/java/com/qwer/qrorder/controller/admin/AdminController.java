package com.qwer.qrorder.controller.admin;

import com.qwer.qrorder.domain.Admin;
import com.qwer.qrorder.service.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 관리자 로그인 페이지 요청 처리
    // 단순히 로그인 화면만 보여주는 역할
    @GetMapping("/login")
    public String loginForm() {
        return "admin/login";
    }

    // 로그인 검증 처리
    // 사용자가 입력한 아이디와 비밀번호를 받아서 AdminService로 로그인 검증
    @PostMapping("/login")
    public String login(@RequestParam String userId,
                        @RequestParam String userPwd,
                        HttpSession session,
                        Model model) {

        // 입력된 아이디와 비밀번호로 관리자 정보 조회
        Admin admin = adminService.login(userId, userPwd);

        // 로그인 성공한 경우
        if (admin != null) {
            // 로그인한 관리자 정보를 세션에 저장
            session.setAttribute("admin", admin);

            // 로그인 후 관리자 메뉴 리스트 페이지로 이동
            return "redirect:/admin/menu/list";
        } 
        // 로그인 실패한 경우
        else {
            // 오류 메시지를 모델에 담아 다시 로그인 페이지로 전달
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "admin/login";
        }
    }

    // 로그아웃 처리
    // 세션에 저장된 관리자 정보를 삭제하고 로그인 페이지로 이동
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션 전체 무효화 → 로그인 정보 삭제
        session.invalidate();

        // 로그아웃 후 로그인 페이지로 이동
        return "redirect:/admin/login";
    }
}