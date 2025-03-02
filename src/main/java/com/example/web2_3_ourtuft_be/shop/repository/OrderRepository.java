package com.example.web2_3_ourtuft_be.shop.repository;

import com.example.web2_3_ourtuft_be.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {}
