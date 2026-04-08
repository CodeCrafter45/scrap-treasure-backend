package com.scraptreasure.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateScrapRequestDto {

    private String address;
    private LocalDateTime preferredTime;
    private Long categoryId;
    // NO manual getCategoryId() method here
}
