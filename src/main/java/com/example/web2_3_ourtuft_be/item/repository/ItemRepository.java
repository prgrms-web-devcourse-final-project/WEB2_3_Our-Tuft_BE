package com.example.web2_3_ourtuft_be.item.repository;

import com.example.web2_3_ourtuft_be.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {}
