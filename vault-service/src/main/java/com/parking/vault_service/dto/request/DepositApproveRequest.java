package com.parking.vault_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepositApproveRequest {

    @NotNull(message = "FIELD_INFORMATION_MISSING")
    @Size(min = 1, message = "LENGTH_TOO_SHORT")
    String[] deposits;
}
