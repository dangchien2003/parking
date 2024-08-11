package com.parking.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String uid;

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    int isBlocked;

    @Column(nullable = false)
    @ManyToMany
    Set<Role> roles;
}
