package com.parking.notification_service.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ETemplate {
    CREATE_CUSTOMER("Welcome to parking", "create-customer"),

    ;
    String subject;
    String fileName;

}
