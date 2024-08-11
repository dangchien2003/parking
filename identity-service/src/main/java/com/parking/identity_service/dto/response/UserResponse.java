package com.parking.identity_service.dto.response;

import com.parking.identity_service.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    String uid;

    String email;

    int isBlocked;

    Set<Role> roles;
}
