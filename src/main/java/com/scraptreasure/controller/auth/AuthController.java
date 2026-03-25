package com.scraptreasure.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scraptreasure.dto.ApiResponse;
import com.scraptreasure.dto.AuthResponse;
import com.scraptreasure.dto.LoginRequest;
import com.scraptreasure.dto.RegisterRequest;
import com.scraptreasure.service.auth.AuthService;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@CrossOrigin(
    origins = {"http://127.0.0.1:5500", "http://localhost:5500"},
    allowedHeaders = "*",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS}
)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid LoginRequest request) {
        AuthResponse token = authService.login(request);

        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .data(new AuthResponse(token))
                .time(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
