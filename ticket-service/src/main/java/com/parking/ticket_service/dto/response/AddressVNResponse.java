package com.parking.ticket_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressVNResponse {

    int error;

    String error_text;

    String data_name;

    List<DataAddress> data;
}
