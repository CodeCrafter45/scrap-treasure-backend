package com.scraptreasure.service.client;

import com.scraptreasure.dto.CreateScrapRequestDto;
import com.scraptreasure.entity.ScrapCategory;
import com.scraptreasure.entity.ScrapRequest;
import com.scraptreasure.entity.User;
import com.scraptreasure.enums.RequestStatus;
import com.scraptreasure.repository.ScrapCategoryRepository;
import com.scraptreasure.repository.ScrapRequestRepository;
import com.scraptreasure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import com.scraptreasure.dto.PickupCompleteDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapRequestService {

    private final ScrapRequestRepository scrapRequestRepository;
    private final UserRepository userRepository;
    private final ScrapCategoryRepository scrapCategoryRepository;

   public ScrapRequest createRequest(CreateScrapRequestDto dto, String clientEmail) {

    User client = userRepository.findByEmail(clientEmail)
            .orElseThrow(() -> new RuntimeException("Client not found"));

    // ✅ STEP 1: GET CATEGORY FIRST
    ScrapCategory category = scrapCategoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found"));

    // ✅ STEP 2: CREATE REQUEST
    ScrapRequest request = ScrapRequest.builder()
            .client(client)
            .address(dto.getAddress())
            .preferredTime(dto.getPreferredTime())
            .scrapCategory(category)   // ✅ NOW IT WORKS
            .status(RequestStatus.REQUESTED)
            .price(0.0)                // ✅ REQUIRED
            .weightKg(0.0)             // ✅ REQUIRED
            .createdAt(LocalDateTime.now())
            .build();

    return scrapRequestRepository.save(request);
}

    @Transactional
    public List<ScrapRequest> getRequestsByClient(String email) {
        User client = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return scrapRequestRepository.findByClient(client);
    }

    // Inside ScrapRequestService class
@Transactional
public void completePickup(Long requestId, Double weight, Double price) {
    try {
        ScrapRequest request = scrapRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));

        request.setWeightKg(weight);
        request.setPrice(price);
        request.setStatus(RequestStatus.COMPLETED);

        scrapRequestRepository.save(request);
        System.out.println("✅ Successfully saved COMPLETED status for ID: " + requestId);
        
    } catch (Exception e) {
        // This will print the REAL error in your VS Code terminal
        System.err.println("❌ CRASH IN SERVICE: " + e.getMessage());
        e.printStackTrace(); 
        throw e; // Re-throw so the controller knows it failed
    }
}
}
