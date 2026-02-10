package com.qwer.qrorder.service;
import org.springframework.stereotype.Service;

import com.qwer.qrorder.domain.Admin;
import com.qwer.qrorder.repository.AdminRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	// Admin 테이블에 접근하기 위한 Repository
    private final AdminRepository adminRepository;

    /**
     * 관리자 로그인 처리 메서드
     * 입력한 userId로 관리자 정보를 조회
     * 조회 결과가 있을 경우, 비밀번호가 일치하는지 확인
     * 비밀번호가 맞으면 Admin 객체 반환, 틀리면 null 반환
     * Optional의 filter()를 이용해 간단하게 구현
     */
    @Override
    public Admin login(String userId, String userPwd) {
        return adminRepository.findByUserId(userId)
                .filter(a -> a.getUserPwd().equals(userPwd)) // 비밀번호 검증
                .orElse(null); // 실패 시 null 반환
    }
}