package com.agribank.baseproject.controller;

import com.agribank.baseproject.request.AuthenticationRequest;
import com.agribank.baseproject.request.RegisterRequest;
import com.agribank.baseproject.response.AuthenticationResponse;
import com.agribank.baseproject.response.DefaultResponse;
import com.agribank.baseproject.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<DefaultResponse<AuthenticationResponse>> register(
            @RequestBody RegisterRequest request
    ) {
        return DefaultResponse.success(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<DefaultResponse<AuthenticationResponse>> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return DefaultResponse.success(authenticationService .authenticate(request));
    }

    @GetMapping("/health")
    public ResponseEntity<DefaultResponse<String>> healthCheck() {
        return DefaultResponse.success("Hello from unsecured endpoint" );
    }
}
