package com.parking.notification_service.mapper;

import com.parking.event.dto.EventCustomerCreate;
import com.parking.notification_service.dto.request.DataCreateCustomer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailMapper {
    DataCreateCustomer toDataCreateCustomer(EventCustomerCreate event);
}
