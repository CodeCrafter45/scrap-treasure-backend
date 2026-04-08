package com.scraptreasure.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scraptreasure.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "scrap_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScrapRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address;

    // Add @Builder.Default to these fields in ScrapRequest.java
@Column(name = "weight_kg")
@Builder.Default
private Double weightKg = 0.0;

@Column(name = "price")
@Builder.Default
private Double price = 0.0;    // ← Double (nullable)

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime preferredTime;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "collector_id")
    private User collector;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "scrap_category_id")   // ← lowercase, consistent
    private ScrapCategory scrapCategory;       // ← this enables .scrapCategory() in builder
}