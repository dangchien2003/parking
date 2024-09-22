package com.parking.ticket_service.repository;

import com.parking.ticket_service.entity.CouponUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponUserRepository extends JpaRepository<CouponUser, Integer> {
    void deleteByCouponId(String couponCode);
}
