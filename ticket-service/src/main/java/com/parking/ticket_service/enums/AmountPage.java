package com.parking.ticket_service.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum AmountPage {
    FIND_STATION(20),
    FIND_CATEGORY(20),
    ;
    int amount;
}
