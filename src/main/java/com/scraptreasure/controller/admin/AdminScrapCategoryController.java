package com.scraptreasure.controller.admin;

import com.scraptreasure.dto.*;
import com.scraptreasure.service.admin.AdminScrapCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/scrap-categories")
@RequiredArgsConstructor
public class AdminScrapCategoryController {

    private final AdminScrapCategoryService service;

    // 🔐 Only ADMIN can create
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ScrapCategoryResponseDto> create(
            @RequestBody ScrapCategoryRequestDto dto) {

        return ApiResponse.<ScrapCategoryResponseDto>builder()
                .success(true)
                .message("Scrap category created")
                .data(service.createCategory(dto))
                .time(LocalDateTime.now())
                .build();
    }

    // 🔓 ADMIN + COLLECTOR can view
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('COLLECTOR')")
    public ApiResponse<List<ScrapCategoryResponseDto>> getAll() {

        return ApiResponse.<List<ScrapCategoryResponseDto>>builder()
                .success(true)
                .message("Scrap categories fetched")
                .data(service.getAllCategories())
                .time(LocalDateTime.now())
                .build();
    }

    // 🔐 Only ADMIN can toggle
    @PostMapping("/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> toggle(
            @PathVariable Long id,
            @RequestParam boolean active) {

        service.toggleCategory(id, active);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Category status updated")
                .data(null)
                .time(LocalDateTime.now())
                .build();
    }
}