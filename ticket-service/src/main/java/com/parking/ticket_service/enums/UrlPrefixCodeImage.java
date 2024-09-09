package com.parking.ticket_service.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public enum UrlPrefixCodeImage {
    V1("V1", "https://res.cloudinary.com/dis2ybh5i/image/upload/v1724143269/parking/"),
    ;
    String code;
    String value;
}
