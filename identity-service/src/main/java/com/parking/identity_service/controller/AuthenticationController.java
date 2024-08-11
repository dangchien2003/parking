package com.parking.identity_service.controller;

import com.nimbusds.jose.JOSEException;
import com.parking.identity_service.dto.request.AuthenticationRequest;
import com.parking.identity_service.dto.request.IntrospectRequest;
import com.parking.identity_service.dto.request.LogoutRequest;
import com.parking.identity_service.dto.request.RefreshTokenRequest;
import com.parking.identity_service.dto.response.ApiResponse;
import com.parking.identity_service.dto.response.AuthenticationResponse;
import com.parking.identity_service.dto.response.IntrospectResponse;
import com.parking.identity_service.dto.response.RefreshTokenResponse;
import com.parking.identity_service.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping
    ApiResponse<AuthenticationResponse> authentication(@Valid @RequestBody AuthenticationRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authentication(request))
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@Valid @RequestBody IntrospectRequest request)
            throws JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@Valid @RequestBody LogoutRequest request) {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/refresh")
    ApiResponse<RefreshTokenResponse> logout(@Valid @RequestBody RefreshTokenRequest request)
            throws JOSEException {
        return ApiResponse.<RefreshTokenResponse>builder()
                .result(authenticationService.refreshToken(request))
                .build();
    }

}
