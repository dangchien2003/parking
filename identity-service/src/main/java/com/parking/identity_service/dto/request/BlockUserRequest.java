package com.parking.identity_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class BlockUserRequest {

    @Size(min = 1, message = "INVALID_LIST_UID")
    Set<String> listUid;
}
