package com.parking.profile_service.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ECloudinary {
    FOLDER_AVATAR("parking/avatar"),

    ;
    String value;
}
