package com.parking.vault_service.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ETransaction {
    CREDIT("Credit", "Increase balance"),
    DEBIT("Debit", "Decrease balance"),

    ;
    String value;

    String description;

}
