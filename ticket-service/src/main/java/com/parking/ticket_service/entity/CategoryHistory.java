package com.parking.ticket_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "category_history")
public class CategoryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    Category category;

    @NotNull
    String name;

    int price;

    int quantity;

    @NotNull
    String unit;

    @NotNull
    String status;

    long createAt;
}
