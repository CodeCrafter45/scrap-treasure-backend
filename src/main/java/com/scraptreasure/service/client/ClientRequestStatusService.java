package com.scraptreasure.service.client;

import com.scraptreasure.dto.ClientRequestStatusDto;
import com.scraptreasure.entity.ScrapRequest;
import com.scraptreasure.entity.User;
import com.scraptreasure.exception.ResourceNotFoundException;
import com.scraptreasure.repository.ScrapRequestRepository;
import com.scraptreasure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientRequestStatusService {

    private final ScrapRequestRepository scrapRequestRepository;
    private final UserRepository userRepository;

    public List<ClientRequestStatusDto> getMyRequests(String clientEmail) {

        User client = userRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        List<ScrapRequest> requests =
                Optional.ofNullable(
                        scrapRequestRepository.findByClientId(client.getId())
                ).orElse(List.of());

        return requests.stream().map(request ->

                ClientRequestStatusDto.builder()
                        .requestId(request.getId())
                        .address(request.getAddress())
                        .preferredTime(request.getPreferredTime())
                        .status(request.getStatus())

                        // 🔥 IMPORTANT FIX (direct from request)
                        .weightKg(request.getWeightKg())
                        .price(request.getPrice())

                        .build()

        ).toList();
    }
}