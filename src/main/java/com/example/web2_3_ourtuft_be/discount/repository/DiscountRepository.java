package com.example.web2_3_ourtuft_be.discount.repository;

import com.example.web2_3_ourtuft_be.discount.entity.Discount;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    List<Discount> findByStartDate(LocalDate startDate);

    List<Discount> findByEndDate(LocalDate endDate);
}
