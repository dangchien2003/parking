package com.parking.vault_service.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "wallet")
public class Wallet {
    @Id
    String id;

    String ownerId;

    String status;

    Long modifiedAt;

    String modifiedBy;

    String description;
}
