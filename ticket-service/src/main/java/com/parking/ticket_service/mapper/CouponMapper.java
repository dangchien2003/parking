package com.parking.ticket_service.mapper;

import com.parking.ticket_service.dto.request.CouponCreationRequest;
import com.parking.ticket_service.entity.Coupon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    @Mapping(source = "code", target = "id")
    @Mapping(source = "categoryDis", target = "categoryDiscount")
    @Mapping(source = "maxDis", target = "maxDiscount")
    @Mapping(source = "minOrder", target = "minOrderAmount")
    Coupon toCoupon(CouponCreationRequest request);
}
