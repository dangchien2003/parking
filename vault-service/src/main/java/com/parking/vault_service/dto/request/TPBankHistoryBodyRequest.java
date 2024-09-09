package com.parking.vault_service.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TPBankHistoryBodyRequest {

    int pageNumber = 1;

    int pageSize = 400;

    @Value("${TPBank.accountNo}")
    String accountNo;

    String currency = "VND";

    String fromDate;

    String toDate;
}
