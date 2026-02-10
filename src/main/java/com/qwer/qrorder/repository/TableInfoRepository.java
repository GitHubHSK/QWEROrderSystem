package com.qwer.qrorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qwer.qrorder.domain.TableInfo;

@Repository
public interface TableInfoRepository extends JpaRepository<TableInfo, Integer> {
	
}