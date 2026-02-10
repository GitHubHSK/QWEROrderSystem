package com.qwer.qrorder.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MenuDTO {

    /** 메뉴 번호 (고유 번호) */
    private Integer menuNo;

    /** 메뉴 이름 */
    private String menuName;

    /** 가격 */
    private int price;

    /** 메뉴 이미지 경로 또는 URL */
    private String imageUrl;

    /** 카테고리 이름 (예: 메인메뉴 / 사이드 / 음료) */
    private String categoryName;
}
