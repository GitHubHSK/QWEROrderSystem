package com.qwer.qrorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.qwer.qrorder.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Integer> {
}