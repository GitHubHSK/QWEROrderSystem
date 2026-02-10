package com.qwer.qrorder.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문의 정보를 저장하는 엔티티
 * 하나의 주문(OrderMain) 안에는 여러 개의 메뉴 주문(OrderDetail)이 포함
 */
@Entity
@Table(name = "ORDER_MAIN")
@Getter
@Setter
public class OrderMain {

    /**
     * 注文番号 (PK)
     * 주문의 고유 아이디
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_NO")
    private Integer orderNo;

    /**
     * 테이블 정보(TableInfo)와의 연관관계
     * ManyToOne: 한 테이블은 여러 주문을 받을 수 있나, 하나의 주문은 반드시 하나의 테이블에 속함
     * fetch = LAZY: 주문만 조회할 때 테이블 정보를 즉시 불러오지 않고, 실제 사용할 때만 쿼리가 실행되어 성능 최적화에 도움됨
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TABLE_NO", nullable = false)
    private TableInfo table;

    /**
     * 人数
     */
    @Column(name = "PEOPLE_COUNT")
    private int peopleCount;

    /**
     * 合計金額
     */
    @Column(name = "TOTAL_PRICE")
    private int totalPrice;

    /**
     * 注文状態
     */
    @Column(name = "ORDER_STATUS", length = 20)
    private String orderStatus = "WAIT";

    /**
     * 注文作成時間
     * 주문이 생성될 때 자동으로 현재 시간 설정
     */
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate = LocalDateTime.now();

    /**
     * 注文完了時間
     * 주문 상태가 완료되면 시간이 기록됨
     */
    @Column(name = "COMPLETED_DATE")
    private LocalDateTime completedDate;

    /**
     * メニュー名 (단일 메뉴 주문일 때 사용 가능한 필드)
     */
    @Column(name = "MENU_NAME", length = 20)
    private String menuName;

    /**
     * 注文詳細(OrderDetail) 목록 (1:N 관계)
     * mappedBy = "orderMain"
     * OrderDetail 엔티티의 'orderMain' 필드가 외래키를 관리한다는 뜻
     * cascade = PERSIST
     * OrderMain을 저장할 때 OrderDetail도 함께 저장되도록 함
     * 삭제를 함께 하고 싶다면 REMOVE 옵션도 추가할 수 있음
     */
    @OneToMany(mappedBy = "orderMain", cascade = CascadeType.PERSIST)
    private List<OrderDetail> details = new ArrayList<>();

    /**
     * 양방향 연관관계 설정 시 항상 두 객체에 값을 넣어주는 것이 안전
     * OrderMainにdetailを追加すると、detail側にもOrderMainが自動的に設定される。
     */
    public void addDetail(OrderDetail detail) {
        this.details.add(detail);
        detail.setOrderMain(this);
    }
}