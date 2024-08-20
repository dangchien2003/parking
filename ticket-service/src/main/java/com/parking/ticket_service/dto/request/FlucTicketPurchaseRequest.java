package com.parking.ticket_service.dto.request;

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
public class FlucTicketPurchaseRequest {
    @Min(value = 0, message = "INCORRECT_DATA")
    int amount;

    @NotBlank(message = "FIELD_INFORMATION_MISSING")
    String ticketId;
}
