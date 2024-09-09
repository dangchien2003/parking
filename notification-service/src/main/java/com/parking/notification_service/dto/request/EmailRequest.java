package com.parking.notification_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {

    Sender sender;

    List<Receiver> to;

    String htmlContent;

    String subject;
}
