package com.parking.ticket_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PACKAGE)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataAddress {
    String id;

    String name;

    String name_en;

    String full_name;

    String full_name_en;

    double latitude;

    double longitude;
}
