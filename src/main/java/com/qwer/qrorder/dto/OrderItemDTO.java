package com.qwer.qrorder.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
	
	 /** 주문한 메뉴 번호 */
    private Integer menuNo;

    /** 주문 수량 */
    private Integer quantity;

    /** 주문 당시 인원 수 (필요 시 활용) */
    private Integer peopleCount;

    /** 메모 또는 알레르기 정보 */
    private String memo;

    /** 메뉴 이름 (프론트에서 사용하기 위해 추가됨) */
    private String menuName;
}