package com.parking.vault_service.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum EReason {
    APPROVE("APPROVE", "Accept deposit order into the system"),
    UNAPPROVED("UNAPPROVED", "Revoke previously accepted amount"),
    BUY_TICKET("BUY_TICKET", "Buy parking ticket"),
    EXTEND_TICKET("EXTEND_TICKET", "Renew parking ticket when expired"),
    CANCEL_TICKET("CANCEL_TICKET", "Cancel ticket"),

    ;
    String value;
    String description;
}
