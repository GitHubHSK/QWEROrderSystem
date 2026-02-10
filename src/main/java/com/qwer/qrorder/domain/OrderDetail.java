package com.qwer.qrorder.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 注文詳細情報を保存するエンティティ
 * 한 주문(OrderMain) 안에는 여러 개의 메뉴(OrderDetail)가 포함될 수 있음
 */
@Entity
@Table(name = "ORDER_DETAIL") // 실제 DB 테이블 명
@Getter
@Setter
public class OrderDetail {

    /**
     * 주문 상세 번호 (PK)
     * OrderDetailの一意識別子
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DETAIL_NO")
    private Integer detailNo;

    /**
     * 주문 기본 정보(OrderMain)와의 연관관계
     * ManyToOne: 하나의 OrderMain(주문)에 여러 OrderDetail이 포함
     * fetch = LAZY: OrderDetail을 조회할 때 OrderMain 전체를 즉시 가져오지 않고, 실제로 필요할 때 쿼리 발생
     * ORDER_NOカラムを外部キーとして使用
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_NO", nullable = false)
    private OrderMain orderMain;

    /**
     * 메뉴(Menu)와의 연관관계
     * ManyToOne: 하나의 메뉴(Menu)는 여러 주문 상세(OrderDetail)에 포함될 수 있음
     * fetch = LAZY: 지연 로딩을 사용하여 성능 최적화
     * MENU_NOカラムを外部キーとして使用
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_NO", nullable = false)
    private Menu menu;

    /**
     * 注文数量
     */
    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    /**
     * メニュー価格
     * 메뉴 가격이 나중에 변경되더라도, 주문 시점의 가격을 저장해서 기록을 유지할 수 있음
     */
    @Column(name = "PRICE", nullable = false)
    private int price;

    /**
     * 메모 (특이사항)
     * アレルギー情報
     * 사용자 요청 사항
     */
    @Column(name = "MEMO", length = 200)
    private String memo;

    /**
     * メニュー名
     * 메뉴 이름이 나중에 바뀌더라도 주문 내역에 영향을 주지 않도록 저장
     */
    @Column(name = "MENU_NAME", nullable = false, length = 60)
    private String menuName;
}