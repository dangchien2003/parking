package com.parking.vault_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletStatusCreationRequest {

    String ownerId;

    String status;

    String modifiedBy;

    String description;
}
