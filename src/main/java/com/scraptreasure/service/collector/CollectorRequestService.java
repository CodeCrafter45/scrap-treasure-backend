package com.scraptreasure.service.collector;
import com.scraptreasure.dto.CollectorRequestDto;
import com.scraptreasure.entity.ScrapRequest;
import com.scraptreasure.entity.User;
import com.scraptreasure.enums.RequestStatus;
import com.scraptreasure.exception.BadRequestException;
import com.scraptreasure.exception.ResourceNotFoundException;
import com.scraptreasure.repository.ScrapRequestRepository;
import com.scraptreasure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectorRequestService {

        private final ScrapRequestRepository scrapRequestRepository;
        private final UserRepository userRepository;

        // ✅ ACCEPT REQUEST
        public ScrapRequest acceptRequest(Long requestId, String collectorEmail) {

                User collector = userRepository.findByEmail(collectorEmail)
                                .orElseThrow(() -> new ResourceNotFoundException("Collector not found"));

                if (!collector.isEnabled()) {
                        throw new BadRequestException("Collector is not verified by admin");
                }

                ScrapRequest request = scrapRequestRepository
                                .findByIdAndStatus(requestId, RequestStatus.REQUESTED)
                                .orElseThrow(() -> new ResourceNotFoundException("Request not available"));

                request.setCollector(collector);
                request.setStatus(RequestStatus.ACCEPTED);

                return scrapRequestRepository.save(request);
        }

        public List<CollectorRequestDto> getAvailableRequests(String collectorEmail) {

                User collector = userRepository.findByEmail(collectorEmail)
                                .orElseThrow(() -> new ResourceNotFoundException("Collector not found"));

                // get all requests (simple approach)
                List<ScrapRequest> requests = scrapRequestRepository.findAll();

                return requests.stream()
                                .filter(r ->
                                // show REQUESTED to everyone
                                r.getStatus() == RequestStatus.REQUESTED ||

                                // show ACCEPTED only to assigned collector
                                                (r.getStatus() == RequestStatus.ACCEPTED &&
                                                                r.getCollector() != null &&
                                                                r.getCollector().getEmail().equals(collectorEmail))
                                                ||

                                                // show COLLECTED also to assigned collector
                                                (r.getStatus() == RequestStatus.COLLECTED &&
                                                                r.getCollector() != null &&
                                                                r.getCollector().getEmail().equals(collectorEmail)))
                                .map(r -> CollectorRequestDto.builder()
                                                .requestId(r.getId())
                                                .address(r.getAddress())
                                                .status(r.getStatus())
                                                .weightKg(r.getWeightKg())
                                                .price(r.getPrice())
                                                .build())
                                .toList();
        }
}