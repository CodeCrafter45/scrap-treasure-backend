package com.scraptreasure.controller.client;

import com.scraptreasure.dto.ApiResponse;
import com.scraptreasure.dto.CreateScrapRequestDto;
import com.scraptreasure.dto.ScrapRequestResponseDto;
import com.scraptreasure.entity.ScrapRequest;
import com.scraptreasure.service.client.ScrapRequestService;  // ← correct import
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client/requests")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class ClientScrapRequestController {

    private final ScrapRequestService scrapRequestService;  // ← correct type

    @PostMapping
    public ResponseEntity<ApiResponse<ScrapRequestResponseDto>> createRequest(
            @RequestBody CreateScrapRequestDto dto,
            Authentication authentication) {

        String email = authentication.getName();
        ScrapRequest request = scrapRequestService.createRequest(dto, email);

        ScrapRequestResponseDto responseDto = ScrapRequestResponseDto.builder()
                .requestId(request.getId())
                .address(request.getAddress())
                .status(request.getStatus())
                .preferredTime(request.getPreferredTime())
                .weightKg(request.getWeightKg())
                .price(request.getPrice())
                .build();

        return ResponseEntity.ok(
                ApiResponse.<ScrapRequestResponseDto>builder()
                        .success(true)
                        .message("Scrap request created successfully")
                        .data(responseDto)
                        .time(LocalDateTime.now())
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ScrapRequestResponseDto>>> getMyRequests(
            Authentication authentication) {

        String email = authentication.getName();
        List<ScrapRequest> requests = scrapRequestService.getRequestsByClient(email);

        List<ScrapRequestResponseDto> responseList = requests.stream()
                .map(req -> ScrapRequestResponseDto.builder()
                        .requestId(req.getId())
                        .address(req.getAddress())
                        .status(req.getStatus())
                        .preferredTime(req.getPreferredTime())
                        .weightKg(req.getWeightKg())
                        .price(req.getPrice())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                ApiResponse.<List<ScrapRequestResponseDto>>builder()
                        .success(true)
                        .message("Requests fetched")
                        .data(responseList)
                        .time(LocalDateTime.now())
                        .build());
    }
}