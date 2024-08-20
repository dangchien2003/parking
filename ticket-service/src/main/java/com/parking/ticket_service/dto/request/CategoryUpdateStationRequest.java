package com.parking.ticket_service.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class CategoryUpdateStationRequest {

    @NotBlank(message = "FIELD_INFORMATION_MISSING")
    String category;

    @Size(min = 1, message = "INVALID_DATA")
    @NotNull(message = "FIELD_INFORMATION_MISSING")
    String[] stations;
}
