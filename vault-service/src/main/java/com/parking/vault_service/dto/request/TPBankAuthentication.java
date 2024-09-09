package com.parking.vault_service.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TPBankAuthentication {

    @NonFinal
    @Value("${TPBank.username}")
    String username;

    @NonFinal
    @Value("${TPBank.password}")
    String password;
}
