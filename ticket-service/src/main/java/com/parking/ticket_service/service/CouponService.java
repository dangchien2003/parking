package com.parking.ticket_service.service;

import com.parking.ticket_service.dto.request.ApproveCouponRequest;
import com.parking.ticket_service.dto.request.CouponCreationRequest;
import com.parking.ticket_service.entity.Category;
import com.parking.ticket_service.entity.Coupon;
import com.parking.ticket_service.entity.CouponCategory;
import com.parking.ticket_service.entity.CouponUser;
import com.parking.ticket_service.enums.CategoryCoupon;
import com.parking.ticket_service.exception.AppException;
import com.parking.ticket_service.exception.ErrorCode;
import com.parking.ticket_service.mapper.CouponMapper;
import com.parking.ticket_service.repository.CategoryRepository;
import com.parking.ticket_service.repository.CouponCategoryRepository;
import com.parking.ticket_service.repository.CouponRepository;
import com.parking.ticket_service.repository.CouponUserRepository;
import com.parking.ticket_service.utils.ENumUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CouponService {

    CouponRepository couponRepository;
    CouponMapper couponMapper;
    CategoryRepository categoryRepository;
    CouponCategoryRepository couponCategoryRepository;
    CouponUserRepository couponUserRepository;
    static Random random = new Random();

    @PreAuthorize("hasAnyAuthority('STAFF')")
    @Transactional
    public void deleteCoupon(String code) {
        Coupon coupon = couponRepository.findById(code.toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        // error when coupon approved
        if (coupon.isApproved())
            throw new AppException(ErrorCode.COUPON_APPROVED);

        couponRepository.deleteById(coupon.getId());
        if (!coupon.isApplyAllUser())
            couponUserRepository.deleteByCouponId(coupon.getId());
        if (!coupon.isApplyAllCategory())
            couponCategoryRepository.deleteByCouponId(coupon.getId());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void approveCoupon(ApproveCouponRequest request) {
        Coupon coupon = couponRepository.findById(request.getCoupon())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        // check time between useAt and present
        if (coupon.getUseAt() < Instant.now().plus(15, ChronoUnit.MINUTES).toEpochMilli())
            throw new AppException(ErrorCode.TIME_USE_TOO_CLOSE);

        coupon.setApproved(true);
        couponRepository.save(coupon);
    }

    @PreAuthorize("hasAnyAuthority('STAFF')")
    public String createCoupon(CouponCreationRequest request) {
        request.setCategoryDis(
                ENumUtils.getType(CategoryCoupon.class, request.getCategoryDis())
                        .name());

        validateDiscount(request.getCategoryDis(), request.getValue());
        validateMaxDiscount(request.getCategoryDis(), request.getMaxDis());
        validateMinOrder(request.getMinOrder());
        validateTime(request.getUseAt(), request.getExpireAt());

        request = generateCodeIfEmpty(request);

        Coupon coupon = couponRepository.findById(request.getCode()).orElse(null);
        if (!Objects.isNull(coupon))
            throw new AppException(ErrorCode.DATA_EXISTED);

        coupon = couponRepository.save(convertCoupon(request));

        if (!Objects.isNull(request.getApplyCategories()))
            addCategory(coupon.getId(), request.getApplyCategories());

        if (!Objects.isNull(request.getApplyUsers()))
            addUser(coupon.getId(), request.getApplyUsers());

        return coupon.getId();
    }

    void addCategory(String code, List<String> categoriesInput) {
        List<Category> categories = categoryRepository.findAllById(categoriesInput);
        if (categories.isEmpty() || categories.size() < categoriesInput.size())
            throw new AppException(ErrorCode.DATA_NOT_FOUND);

        List<CouponCategory> couponCategories = categories.stream().map(category ->
                CouponCategory.builder()
                        .couponId(code)
                        .category(category.getId())
                        .build()
        ).toList();

        couponCategoryRepository.saveAll(couponCategories);
    }

    void addUser(String code, List<String> usersInput) {
        // check exist user

        List<CouponUser> couponUsers = usersInput.stream().map(user ->
                CouponUser.builder()
                        .couponId(code)
                        .userId(user)
                        .build()
        ).toList();

        couponUserRepository.saveAll(couponUsers);
    }

    Coupon convertCoupon(CouponCreationRequest request) {
        Coupon coupon = couponMapper.toCoupon(request);

        if (Objects.isNull(request.getApplyCategories()))
            coupon.setApplyAllCategory(true);
        if (Objects.isNull(request.getApplyUsers()))
            coupon.setApplyAllUser(true);

        return coupon;
    }

    void validateDiscount(String categoryDis, int valueDis) {
        if (valueDis < 1 ||
                (CategoryCoupon.PERCENT.name().equals(categoryDis) && valueDis > 100))
            throw new AppException(ErrorCode.INVALID_DISCOUNT_VALUE);
    }

    void validateMaxDiscount(String categoryDis, Integer maxDis) {

        if ((CategoryCoupon.PERCENT.name().equals(categoryDis) &&
                Objects.isNull(maxDis)) ||
                (CategoryCoupon.DIRECTLY.name().equals(categoryDis) &&
                        !Objects.isNull(maxDis) &&
                        (maxDis < 1 || maxDis > 100))
        )
            throw new AppException(ErrorCode.INVALID_DISCOUNT_MAX);

    }

    void validateMinOrder(Integer minOrder) {
        if (Objects.isNull(minOrder))
            return;

        if (minOrder < 0)
            throw new AppException(ErrorCode.INVALID_DISCOUNT_MIN);
    }

    void validateTime(long useAt, long expireAt) {
        long now = Instant.now().toEpochMilli();

        if (useAt < now ||
                expireAt < new Date(useAt).toInstant()
                        .plus(20, ChronoUnit.MINUTES)
                        .toEpochMilli())
            throw new AppException(ErrorCode.INVALID_COUPON_TIME);
    }

    CouponCreationRequest generateCodeIfEmpty(CouponCreationRequest request) {
        String code = request.getCode();
        if (Objects.isNull(code) || code.isBlank())
            code = generateCouponCode();
        else {
            code = code.trim().toUpperCase();
            if (code.length() < 5)
                throw new AppException(ErrorCode.INVALID_CODE);
        }

        request.setCode(code);
        return request;
    }

    String generateCouponCode() {
        int length = 8;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            result.append(randomChar);
        }
        return result.toString();
    }
}
