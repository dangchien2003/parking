package com.parking.vault_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PACKAGE)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddFuctuationResponse {

    String id;

    String depositId;

    String ownerId;

    String description;

    int amount;

    String transaction;

    long createAt;
}
