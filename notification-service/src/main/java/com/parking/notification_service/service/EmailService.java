package com.parking.notification_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.event.dto.EventCustomerCreate;
import com.parking.notification_service.dto.request.*;
import com.parking.notification_service.dto.response.EmailResponse;
import com.parking.notification_service.enums.ETemplate;
import com.parking.notification_service.mapper.EmailMapper;
import com.parking.notification_service.repository.httpclient.EmailClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {

    EmailClient emailClient;
    TemplateEngine templateEngine;
    ObjectMapper objectMapper;
    EmailMapper emailMapper;


    @NonFinal
    @Value("${notification.email.brevo-apikey}")
    String apiKey;

    @NonFinal
    @Value("${notification.email.sender-address}")
    String emailSender;

    @PreAuthorize("hasAnyAuthority('SEND_MAIL', 'ROLE_ADMIN')")
    public EmailResponse send(SendEmailRequest request) {

        Sender sender = Sender.builder()
                .email(emailSender)
                .name("dang chien")
                .build();

        EmailRequest emailRequest = EmailRequest.builder()
                .sender(sender)
                .to(request.getTo())
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();

        return emailClient.sendEmail(apiKey, emailRequest);
    }

    public void sendMailCreateCustomer(EventCustomerCreate event) {

        Receiver receiver = Receiver.builder()
                .email(event.getEmail())
                .build();

        DataCreateCustomer dataCreateCustomer = emailMapper.toDataCreateCustomer(event);

        sendEmail(List.of(receiver), ETemplate.CREATE_CUSTOMER, dataCreateCustomer);
    }

    String genTemplate(String fileName, Object data) {

        Context context = convertContext(data);
        return templateEngine.process(fileName, context);
    }

    Context convertContext(Object data) {

        Context context = new Context();

        if (Objects.isNull(data)) {
            return context;
        }

        Map<String, Object> map;

        map = objectMapper.convertValue(data, Map.class);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }

        return context;
    }

    void sendEmail(List<Receiver> receivers, ETemplate template, Object data) {

        String htmlContent = genTemplate(template.getFileName(), data);

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .subject(template.getSubject())
                .htmlContent(htmlContent)
                .to(receivers)
                .build();

        send(sendEmailRequest);
    }
}
