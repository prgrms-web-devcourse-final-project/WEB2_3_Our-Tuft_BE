package com.example.web2_3_ourtuft_be.item.repository;

import com.example.web2_3_ourtuft_be.item.entity.ItemAppearance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemAppearanceRepository extends JpaRepository<ItemAppearance, Long> {}
