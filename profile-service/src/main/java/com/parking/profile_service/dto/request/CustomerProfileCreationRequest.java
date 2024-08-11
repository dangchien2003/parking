package com.parking.profile_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerProfileCreationRequest {

    String uid;

    String name;

    String phone;

    int isPhoneActive;
}
