package com.parking.ticket_service.controller;

import com.parking.ticket_service.dto.request.ApproveCouponRequest;
import com.parking.ticket_service.dto.request.CouponCreationRequest;
import com.parking.ticket_service.dto.response.ApiResponse;
import com.parking.ticket_service.service.CouponService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/coupon")
public class CouponController {
    CouponService couponService;

    @PostMapping
    ApiResponse<String> createCoupon(@Valid @RequestBody CouponCreationRequest request) {
        return ApiResponse.<String>builder()
                .result(couponService.createCoupon(request))
                .build();
    }

    @PutMapping("/approve")
    ApiResponse<Void> approve(@Valid @RequestBody ApproveCouponRequest request) {
        couponService.approveCoupon(request);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/{code}")
    ApiResponse<Void> delete(@PathVariable(name = "code") String code) {
        couponService.deleteCoupon(code);
        return ApiResponse.<Void>builder().build();
    }
}
