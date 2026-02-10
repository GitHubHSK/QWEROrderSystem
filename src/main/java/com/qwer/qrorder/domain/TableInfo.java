package com.qwer.qrorder.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 매장 내 테이블 정보를 저장하는 엔티티
 */
@Entity
@Table(name = "TABLE_INFO")
@Getter @Setter
public class TableInfo {

    /**
     * 테이블 번호(PK)
     * 매장 테이블의 고유 번호
     * DB에서 자동 증가(AUTO_INCREMENT)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TABLE_NO")
    private Integer tableNo;

    /**
     * 테이블 이름
     * 화면에 보여줄 때 사용
     */
    @Column(name = "TABLE_NAME", nullable = false, length = 50)
    private String tableName;

    /**
     * 테이블에 붙어 있는 QR 코드 주소
     * 손님이 QR을 찍으면 이 URL로 접속하게 된됨
     * 일반적으로 "도메인/menu/테이블번호"처럼 구성됨
     */
    @Column(name = "QR_CODE_URL", nullable = false)
    private String qrCodeUrl;

    /**
     * 테이블 상태
     */
    @Column(name = "STATUS", nullable = false, length = 20)
    private String status = "EMPTY";
}