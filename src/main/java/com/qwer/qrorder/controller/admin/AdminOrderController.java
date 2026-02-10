package com.qwer.qrorder.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qwer.qrorder.domain.OrderMain;
import com.qwer.qrorder.service.OrderService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/order/orders")
public class AdminOrderController {
    
    // 주문 관련 비즈니스 로직을 처리하는 서비스
    private final OrderService orderService;

    /**
     * 관리자 주문 내역 조회 화면
     * DB에서 모든 주문 목록을 조회
     * 정렬된 주문 리스트를 화면에 전달
     */
    @GetMapping
    public String orderList(HttpSession session, Model model) {

        // DB에서 전체 주문 목록 가져오기
        List<OrderMain> orders = orderService.getAllOrders();

        // 정렬된 리스트를 HTML로 전달
        model.addAttribute("orders", orders);

        // templates/menu/orderList.html 파일로 이동
        return "menu/orderList";
    }
}
