package com.parking.vault_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OwnerCreationRequest {

    @NotBlank(message = "FIELD_INFORMATION_MISSING")
    String uid;
}
