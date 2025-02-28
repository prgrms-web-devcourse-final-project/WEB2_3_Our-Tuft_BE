package com.example.web2_3_ourtuft_be.shop.repository;

import com.example.web2_3_ourtuft_be.shop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}
