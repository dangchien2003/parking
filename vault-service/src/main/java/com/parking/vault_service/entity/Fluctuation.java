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
@Document(collection = "fluctuation")
public class Fluctuation {

    @Id
    String id;

    String depositId;

    String ownerId;

    String reason;

    String description;

    int amount;

    String transaction;

    long createAt;
}
