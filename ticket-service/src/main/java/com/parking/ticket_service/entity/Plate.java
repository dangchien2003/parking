package com.parking.ticket_service.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "plate")
public class Plate {
    @EmbeddedId
    PlateId id;

    String contentPlate;
    
    @NotNull
    String urlPrefixCode;

    String imageIn;

    long checkinAt;

    String imageOut;

    long checkoutAt;

    String acceptBy;

    @OneToOne
    Station station;
}
