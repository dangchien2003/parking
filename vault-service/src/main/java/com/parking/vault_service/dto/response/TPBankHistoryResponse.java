package com.parking.vault_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TPBankHistoryResponse {
    int totalRows;

    long maxAcentrysmo;

    List<TransactionInfo> transactionInfos;
}

