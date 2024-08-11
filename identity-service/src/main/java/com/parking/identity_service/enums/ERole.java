package com.parking.identity_service.enums;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ERole {
    ADMIN,
    STAFF,
    CUSTOMER;
}
