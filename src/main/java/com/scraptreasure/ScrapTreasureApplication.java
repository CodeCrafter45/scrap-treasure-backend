package com.scraptreasure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication  // remove scanBasePackages - not needed
@EnableJpaRepositories(basePackages = "com.scraptreasure.repository")
@EntityScan(basePackages = "com.scraptreasure.entity")  // now uses Spring's real @EntityScan
public class ScrapTreasureApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrapTreasureApplication.class, args);
    }
}