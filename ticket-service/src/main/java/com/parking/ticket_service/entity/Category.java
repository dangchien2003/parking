package com.parking.ticket_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @NotNull
    @Column(unique = true)
    String name;

    int price;

    int quantity;

    @NotNull
    String unit;

    @NotNull
    String status;

    long createAt;

    long modifiedAt;

    @ManyToMany
    Set<Station> stations;
}
