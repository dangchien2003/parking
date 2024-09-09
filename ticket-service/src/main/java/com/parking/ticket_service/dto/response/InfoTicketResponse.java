package com.parking.ticket_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InfoTicketResponse {
    String id;
    CategoryResponse category;
    int turnTotal;
    String contentPlate;
    long buyAt;
    long expireAt;
    long cancleAt;
}
