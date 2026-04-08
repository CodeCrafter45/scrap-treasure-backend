package com.scraptreasure.controller.collector;

import com.scraptreasure.dto.ApiResponse;
import com.scraptreasure.entity.ScrapRequest;
import com.scraptreasure.service.client.ScrapRequestService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.scraptreasure.dto.PickupDTO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
@RestController
@RequestMapping("/api/collector")
@RequiredArgsConstructor
public class CollectorScrapRequestController {

    private final ScrapRequestService scrapRequestService;

   @PostMapping("/pickup/{id}")
public ResponseEntity<?> completePickup(@PathVariable Long id, @RequestBody PickupDTO data) {
    try {
        // Debugging logs to see if data is arriving
        System.out.println("Completing ID: " + id);
        System.out.println("Weight: " + data.getWeightKg() + " | Price: " + data.getPrice());

        if (data.getWeightKg() == null || data.getPrice() == null) {
            return ResponseEntity.badRequest().body("Weight or Price is missing in request body");
        }

        scrapRequestService.completePickup(id, data.getWeightKg(), data.getPrice());
        return ResponseEntity.ok().body("{\"success\": true}");
    } catch (Exception e) {
        e.printStackTrace(); // This prints the RED error in IntelliJ
        return ResponseEntity.status(500).body("Error: " + e.getMessage());
    }
}
}


