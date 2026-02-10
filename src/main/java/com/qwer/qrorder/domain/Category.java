package com.qwer.qrorder.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 메뉴의 카테고리 정보를 저장하는 엔티티 클래스
 */
@Entity
@Table(name = "CATEGORY")  // DB 테이블 이름 매핑
@Getter @Setter
public class Category {

    /**
     * 카테고리 고유번호(PK)
     * 자동 증가되는 식별자
     * 다른 메뉴(Menu) 엔티티에서 외래키로 참조
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_NO")
    private Integer categoryNo;

    /**
     * 카테고리 이름
     * 카테고리 표시용 텍스트
     * NOT NULL + 최대 20글자로 제한
     */
    @Column(name = "CATEGORY_NAME", nullable = false, length = 20)
    private String categoryName;
}