package com.parking.ticket_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Coupon {
    @Id
    String id;

    @NotNull
    String categoryDiscount;

    int value;

    Integer maxDiscount;

    Integer minOrderAmount;

    int quantity;

    long useAt;

    long expireAt;

    boolean approved;

    boolean applyAllUser;

    boolean applyAllCategory;
}
