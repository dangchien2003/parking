package com.parking.vault_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TPBankAuthenticationResponse {
    String access_token;

    String token_type;

    long expires_in;
}
