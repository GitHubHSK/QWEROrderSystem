package com.qwer.qrorder.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class SelectTableController {

    /**
     * 테이블 선택 페이지 이동
     * 고객이 QR을 찍기 전에, 관리자나 직원이 테이블 번호를 선택할 수 있는 화면
     * selectTable.html 페이지를 반환
     */
    @GetMapping("/selectTable")
    public String selectTablePage() {
        return "member/selectTable";
    }
}