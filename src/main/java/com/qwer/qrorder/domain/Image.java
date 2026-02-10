package com.qwer.qrorder.domain;

import java.time.LocalDateTime;

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
 * 메뉴에 업로드된 이미지 파일 정보를 저장하는 엔티티
 * 이미지 파일 이름, 저장 경로, 용량 등을 DB에 기록해 관리
 */
@Entity
@Table(name = "IMAGE") // 실제 DB 테이블 이름
@Getter @Setter
public class Image {

    /**
     * 이미지 고유 번호 (PK)
     * 자동 증가되는 기본키
     * 이미지 레코드 한 건을 구별하는 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_NO")
    private Integer imageNo;

    /**
     * 어떤 메뉴(Menu)에 속한 이미지인지 나타내는 관계
     * 다대일(ManyToOne): 하나의 메뉴에 여러 이미지가 붙을 수 있음
     * fetch = LAZY: 실제 이미지가 필요할 때만 메뉴 정보를 조회
     * MENU_NO 컬럼을 외래키로 사용
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_NO", nullable = false)
    private Menu menu;

    /**
     * 사용자가 업로드한 원본 파일명
     */
    @Column(name = "ORIGIN_NAME", nullable = false)
    private String originName;

    /**
     * 서버에 저장될 파일명
     * 예) UUID를 붙여 "f3a8d2e1_tempura.png" 처럼 중복 방지를 위해 별도로 저장함
     */
    @Column(name = "STORED_NAME", nullable = false)
    private String storedName;

    /**
     * 파일이 저장된 실제 경로
     */
    @Column(name = "FILE_PATH", nullable = false)
    private String filePath;

    /**
     * 파일의 크기 (byte 단위)
     */
    @Column(name = "FILE_SIZE", nullable = false)
    private int fileSize;

    /**
     * 이미지 등록 날짜
     * 기본값: 현재 시간(LocalDateTime.now())
     */
    @Column(name = "REG_DATE")
    private LocalDateTime regDate = LocalDateTime.now();
}