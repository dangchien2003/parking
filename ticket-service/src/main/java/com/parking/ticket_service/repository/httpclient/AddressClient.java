package com.parking.ticket_service.repository.httpclient;

import com.parking.ticket_service.dto.response.AddressVNResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "address-client", url = "https://esgoo.net/api-tinhthanh")
public interface AddressClient {
    @GetMapping(value = "/2/{province}.htm")
    AddressVNResponse getDistrict(@PathVariable(name = "province") String province);

    @GetMapping(value = "/3/{commune}.htm")
    AddressVNResponse getCommune(@PathVariable(name = "commune") String commune);
}
