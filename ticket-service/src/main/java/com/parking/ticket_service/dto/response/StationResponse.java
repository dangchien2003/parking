package com.parking.ticket_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StationResponse {

    String stationId;

    String province;

    String district;

    String commune;

    String road;

    String status;

    long createAt;

    long modifiedAt;
}
