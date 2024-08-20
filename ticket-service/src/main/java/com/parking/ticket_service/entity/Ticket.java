package com.parking.ticket_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "ticket")
public class Ticket {

    @Id
    String id;

    String uid;

    @ManyToOne
    @JoinColumn(name = "categoryHistoryId")
    CategoryHistory category;

    int turnTotal;

    String contentPlate;

    long buyAt;

    long expireAt;

    long cancleAt;
}
