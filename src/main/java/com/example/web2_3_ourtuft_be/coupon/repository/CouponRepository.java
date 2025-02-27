package com.example.web2_3_ourtuft_be.coupon.repository;

import com.example.web2_3_ourtuft_be.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {}
