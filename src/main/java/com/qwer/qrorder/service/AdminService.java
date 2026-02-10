package com.qwer.qrorder.service;

import com.qwer.qrorder.domain.Admin;

public interface AdminService {
	

    /**
     * 관리자 로그인 처리
     * @param userId 입력한 아이디
     * @param userPwd 입력한 비밀번호
     * @return 로그인 성공 시 Admin 객체, 실패 시 null
     */
    Admin login(String userId, String userPwd);
}