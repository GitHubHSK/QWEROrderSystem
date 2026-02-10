package com.qwer.qrorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.qwer.qrorder.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}