package com.example.web2_3_ourtuft_be.shop.repository;

import com.example.web2_3_ourtuft_be.shop.entity.PurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {}
