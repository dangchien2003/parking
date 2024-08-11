package com.parking.identity_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class PermissionCreationRequest {

    @NotBlank(message = "FIELD_INFORMATION_MISSING")
    String name;

    @NotBlank(message = "FIELD_INFORMATION_MISSING")
    String description;
}
