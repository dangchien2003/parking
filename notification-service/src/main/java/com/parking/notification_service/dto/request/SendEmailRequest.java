package com.parking.notification_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PACKAGE)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest {

    List<Receiver> to;

    String htmlContent;

    String subject;
}
