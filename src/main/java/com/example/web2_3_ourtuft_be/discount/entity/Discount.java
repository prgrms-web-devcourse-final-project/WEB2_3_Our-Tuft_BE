package com.example.web2_3_ourtuft_be.discount.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private int value;
    private LocalDate startDate;
    private LocalDate endDate;

    public void update(String type, int value, LocalDate startDate, LocalDate endDate) {
        this.type = type;
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
