package com.parking.ticket_service.repository;

import com.parking.ticket_service.entity.CouponCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponCategoryRepository extends JpaRepository<CouponCategory, Integer> {
    void deleteByCouponId(String couponCode);
}
