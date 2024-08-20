package com.parking.ticket_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.parking.ticket_service.entity.TicketId;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketResponse {
    TicketId ticketId;

    int turnTotal;

    String contentPlate;

    long buyAt;

    long expireAt;

    long cancleAt;
}
