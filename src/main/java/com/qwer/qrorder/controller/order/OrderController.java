package com.qwer.qrorder.controller.order;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qwer.qrorder.dto.OrderRequestDTO;
import com.qwer.qrorder.service.OrderService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    // 주문 처리 서비스
    private final OrderService orderService;

    /**
     * 주문 요청 처리 (POST)
     * 프론트에서 JSON 형태(orderItems 포함)로 주문 데이터를 보냄
     * 세션에 저장된 알레르기 정보를 memo에 자동 병합하여 저장
     */
    @PostMapping
    public ResponseEntity<String> submitOrder(@RequestBody OrderRequestDTO dto,
                                              HttpSession session) {

        // 세션에 저장된 알레르기 정보 가져오기 (List<String> 형태)
        @SuppressWarnings("unchecked")
        List<String> allergy = (List<String>) session.getAttribute("allergy");

        // 알레르기 정보가 있을 경우 문자열로 변환
        // 예: ["牛乳", "卵"] → "牛乳, 卵"
        String allergyText = (allergy != null && !allergy.isEmpty())
                ? String.join(", ", allergy)
                : "";

        /**
         * 주문 항목에 memo 값이 비어있을 경우
         * → 알레르기 정보를 memo에 자동 입력
         * 
         * memo가 이미 존재한다면
         * → memo + " / 알레르기" 형태로 덧붙임
         */
        if (!allergyText.isEmpty() && dto.getOrderItems() != null) {
            dto.getOrderItems().forEach(item -> {

                // memo가 없으면 알레르기 정보 입력
                if (item.getMemo() == null || item.getMemo().isBlank()) {
                    item.setMemo(allergyText);

                // memo가 있으면 뒤에 연결
                } else {
                    item.setMemo(item.getMemo() + " / " + allergyText);
                }
            });
        }

        // 서비스로 전달하여 실제 DB에 주문 저장
        orderService.createOrder(dto);

        // 클라이언트에 성공 메시지 반환
        return ResponseEntity.ok("ご注文は正常に処理されました。");
    }
}