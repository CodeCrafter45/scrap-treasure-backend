package com.scraptreasure.service.collector;

import com.scraptreasure.dto.CollectScrapDto;
import com.scraptreasure.entity.*;
import com.scraptreasure.enums.RequestStatus;
import com.scraptreasure.exception.*;
import com.scraptreasure.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor    
public class CollectorPickupService {

    private final ScrapRequestRepository scrapRequestRepository;
    private final ScrapCollectionDetailsRepository detailsRepository;

    public ScrapCollectionDetails collectScrap(Long requestId, CollectScrapDto dto) {

        ScrapRequest request = scrapRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        if (request.getStatus() != RequestStatus.ACCEPTED) {
            throw new BadRequestException("Request is not in ACCEPTED state");
        }

        if (dto.getWeightKg() == null || dto.getWeightKg() <= 0) {
            throw new BadRequestException("Invalid weight");
        }

        // calculate price if category exists, otherwise default to 0
        ScrapCategory category = request.getScrapCategory();
        double price = (category != null)
                ? dto.getWeightKg() * category.getPricePerKg()
                : 0.0;  // ← no crash if category is null

        request.setWeightKg(dto.getWeightKg());
        request.setPrice(price);
        request.setStatus(RequestStatus.COLLECTED);

        scrapRequestRepository.save(request);  // ← saves weight + price

        ScrapCollectionDetails details = ScrapCollectionDetails.builder()
                .scrapRequest(request)
                .weightKg(dto.getWeightKg())
                .price(price)
                .build();

        return detailsRepository.save(details);
    }
}