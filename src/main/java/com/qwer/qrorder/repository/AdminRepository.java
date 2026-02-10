package com.qwer.qrorder.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.qwer.qrorder.domain.Admin;

/**
 * AdminRepository
 * Spring Data JPA가 제공하는 기본 CRUD 기능을 사용하기 위한 Repository 인터페이스
 * JpaRepository<엔티티, ID타입> 을 상속하면, save(), findById(), findAll(), deleteById()등 기본적인 데이터베이스 작업을 자동으로 사용할 수 있음
 */
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    /**
     * findByUserId()
     * Spring Data JPA는 메서드 이름만 보고 자동으로 쿼리를 생성
     * 여기서는 "USER_ID 컬럼으로 관리자 정보 조회"하는 쿼리를 만들어줌
     * 결과는 Optional 로 감싸서 null 처리에 안전하게 사용 가능
     */
    Optional<Admin> findByUserId(String userId);
}
