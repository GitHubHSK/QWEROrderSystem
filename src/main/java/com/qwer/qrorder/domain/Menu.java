package com.qwer.qrorder.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * 메뉴 정보를 저장하는 엔티티
 * 메뉴 이름, 가격, 카테고리, 관리자 정보 등을 저장
 */
@Entity
@Table(name = "MENU") // 실제 DB 테이블명
@Getter @Setter
public class Menu {

    /**
     * 메뉴 고유 번호 (PK)
     * 자동 증가되는 기본키
     * 한 메뉴를 식별하는 번호
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_NO")
    private Integer menuNo;

    /**
     * 메뉴가 속한 카테고리 정보
     * 예) 메인, 사이드, 음료 등
     * ManyToOne: 하나의 카테고리에 여러 메뉴가 속할 수 있음
     * fetch = LAZY: 메뉴를 불러올 때 카테고리 정보를 즉시 불러오지 않고, 실제로 접근할 때만 조회
     * CATEGORY_NO 컬럼을 외래키로 사용
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_NO", nullable = false)
    private Category category;

    /**
     * 메뉴를 등록한 관리자(Admin)
     * 선택값(optional)
     * USER_NO 컬럼을 외래키로 사용
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO")
    private Admin admin;

    /**
     * 메뉴 이름
     * 최대 60자 제한
     */
    @Column(name = "MENU_NAME", nullable = false, length = 60)
    private String menuName;

    /**
     * 메뉴 가격
     */
    @Column(name = "PRICE", nullable = false)
    private int price;

    /**
     * 메뉴 이미지 파일명
     * 실제 파일은 서버 uploads 폴더에 저장
     * DB에는 파일명만 저장
     */
    @Column(name = "MENU_IMAGE", length = 255)
    private String menuImage;

    /**
     * 메뉴 활성화 여부 (Y/N)
     * 기본값 "Y": 메뉴 활성 상태
     * 관리자가 메뉴를 숨기거나 비활성화할 때 사용 가능
     */
    @Column(name = "IS_ACTIVE", length = 1)
    private String isActive = "Y";
}
