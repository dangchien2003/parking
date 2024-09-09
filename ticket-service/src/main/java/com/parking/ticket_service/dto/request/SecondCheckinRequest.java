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
public class SecondCheckinRequest {
    @NotBlank(message = "FIELD_INFORMATION_MISSING")
    String qr;

    @NotBlank(message = "FIELD_INFORMATION_MISSING")
    String image;
}
