package com.parking.vault_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffCancelDepositRequest {

    @NotNull(message = "FIELD_INFORMATION_MISSING")
    @Size(min = 1, message = "LENGTH_TOO_SHORT")
    List<String> depositsId;
}
