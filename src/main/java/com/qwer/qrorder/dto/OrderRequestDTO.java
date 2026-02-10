package com.qwer.qrorder.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequestDTO {
	
    /** 주문한 테이블 번호 */
    private Integer tableNo;

    /** 손님 인원 수 */
    private Integer peopleCount;

    /** 주문한 메뉴 목록 (각 메뉴는 OrderItemDTO 형태) */
    private List<OrderItemDTO> orderItems;

    /** 알레르기 정보 (손님이 선택한 문자열 리스트) */
    private List<String> allergy;
}