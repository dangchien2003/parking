package com.parking.ticket_service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponCreationRequest {
    String code;

    @NotBlank(message = "FIELD_INFORMATION_MISSING")
    String categoryDis;

    int value;

    Integer maxDis;

    Integer minOrder;

    @Min(value = 1, message = "FIELD_INFORMATION_MISSING")
    int quantity;

    long useAt;

    long expireAt;

    List<String> applyCategories;

    List<String> applyUsers;

}
