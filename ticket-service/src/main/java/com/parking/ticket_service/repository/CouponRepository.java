package com.parking.ticket_service.repository;

import com.parking.ticket_service.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, String> {
}
