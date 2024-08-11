package com.parking.profile_service.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum EPhoneActice {
    NO_ACTIVE(0),
    ACTIVED(1),

    ;
    int value;
}
