package com.parking.vault_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionInfo {
    String id;

    String arrangementId;

    String reference;

    String xref;

    String description;

    String bookingDate;

    String valueDate;

    int amount;

    String currency;

    String creditDebitIndicator;

    int runningBalance;

}


