package com.scraptreasure.repository;

import com.scraptreasure.entity.ScrapRequest;
import com.scraptreasure.entity.User;
import com.scraptreasure.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScrapRequestRepository extends JpaRepository<ScrapRequest, Long> {

    List<ScrapRequest> findByStatus(RequestStatus status);

    List<ScrapRequest> findByClient(User client);          // ← ADDED

    List<ScrapRequest> findByClientId(Long clientId);      // ← KEPT existing

    Optional<ScrapRequest> findByIdAndStatus(Long id, RequestStatus status);

    @Query("SELECT r FROM ScrapRequest r WHERE r.status = :requestedStatus OR " +
           "(r.status = :acceptedStatus AND r.collector.email = :collectorEmail)")
    List<ScrapRequest> findRequestsForCollector(
            @Param("requestedStatus") RequestStatus requestedStatus,
            @Param("acceptedStatus") RequestStatus acceptedStatus,
            @Param("collectorEmail") String collectorEmail
    );
}