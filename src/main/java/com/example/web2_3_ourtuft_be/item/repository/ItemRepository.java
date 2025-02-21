package com.example.web2_3_ourtuft_be.item.repository;

import com.example.web2_3_ourtuft_be.item.entity.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Slice<Item> findByCategoryAndNameContaining(String category, String keyword, Pageable pageable);

    Slice<Item> findByCategory(String category, Pageable pageable);

    Slice<Item> findByNameContaining(String keyword, Pageable pageable);
}
