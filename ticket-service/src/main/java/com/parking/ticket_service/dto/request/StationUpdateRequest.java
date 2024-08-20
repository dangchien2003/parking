package com.parking.ticket_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StationUpdateRequest {

    @NotBlank(message = "FIELD_INFORMATION_MISSING")
    String stationId;

    @NotBlank(message = "FIELD_INFORMATION_MISSING")
    String province;

    @NotBlank(message = "FIELD_INFORMATION_MISSING")
    String district;

    @NotBlank(message = "FIELD_INFORMATION_MISSING")
    String commune;

    @NotBlank(message = "FIELD_INFORMATION_MISSING")
    String road;
}
