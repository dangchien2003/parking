package com.parking.identity_service.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum EBlock {
    BLOCKED(1),
    NOT_BLOCK(0),
    ;
    final int value;
}
