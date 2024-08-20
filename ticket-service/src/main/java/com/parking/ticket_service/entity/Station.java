package com.parking.ticket_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "station")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String stationId;
    
    @NotNull
    String province;

    @NotNull
    String district;

    @NotNull
    String commune;

    @NotNull
    String road;

    @NotNull
    String status;

    long createAt;

    long modifiedAt;
}
