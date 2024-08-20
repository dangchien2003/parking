package com.parking.vault_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddFluctuationRequest {

    String depositId;

    String ownerId;

    String description;

    int amount;

    String transaction;
}
