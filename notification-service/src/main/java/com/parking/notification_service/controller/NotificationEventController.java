package com.parking.notification_service.controller;

import com.parking.event.dto.EventCustomerCreate;
import com.parking.notification_service.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationEventController {

    EmailService emailService;

    @KafkaListener(topics = "create-customer")
    public void listen(EventCustomerCreate event) {
        log.info("create customer: {}", event.getEmail());
        emailService.sendMailCreateCustomer(event);
    }

}
