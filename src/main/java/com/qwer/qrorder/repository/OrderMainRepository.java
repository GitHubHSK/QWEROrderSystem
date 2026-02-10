package com.qwer.qrorder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qwer.qrorder.domain.OrderMain;

@Repository
public interface OrderMainRepository extends JpaRepository<OrderMain, Integer> {
    // createdDate 기준으로 최신순 정렬
    List<OrderMain> findAllByOrderByCreatedDateDesc();
}