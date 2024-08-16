package com.parking.vault_service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepositCreationRequest {

    @NotBlank(message = "FIELD_INFORMATION_MISSING")
    String code;

    @Min(value = 10_000, message = "TOO_SMALL_AMOUNT")
    int amount;
}
