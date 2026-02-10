package com.qwer.qrorder.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 관리자(Admin) 정보를 저장하는 엔티티 클래스
 * Spring Data JPA가 이 클래스를 보고 DB 테이블과 자동 매핑
 */
@Entity
@Table(name = "ADMIN")  // DB 테이블 이름 지정
@Getter
@Setter
public class Admin {

    /**
     * 관리자 고유번호 (PK)
     * 자동 증가(AUTO_INCREMENT)
     * 로그인 인증 시 실제로는 사용되지 않고, 내부 식별자 용도
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userNo;

    /**
     * 관리자 로그인용 아이디
     * 로그인 폼에서 입력한 값과 비교하여 인증
     */
    private String userId;

    /**
     * 관리자 비밀번호
     * 실제 서비스에서는 반드시 암호화 필요(BCrypt 등)
     */
    private String userPwd;
}