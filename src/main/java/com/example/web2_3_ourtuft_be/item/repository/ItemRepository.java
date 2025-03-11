package com.example.web2_3_ourtuft_be.item.repository;

import com.example.web2_3_ourtuft_be.item.entity.Item;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Slice<Item> findByCategory(String category, Pageable pageable);

    Slice<Item> findAllBy(Pageable pageable);

    Slice<Item> findAllByIdIn(List<Long> itemIds, Pageable pageable);

    List<Item> findByDiscountId(Long discountId);

    boolean existsByName(String name);

    long countByCategory(String category);

    @Query(
            "SELECT i FROM Item i WHERE "
                    + "(:category IS NULL OR i.category = :category) AND "
                    + "(:keyword IS NULL OR i.name LIKE %:keyword%) AND "
                    + "i.name NOT LIKE %:exclude%")
    Slice<Item> findFilteredItems(
            String category, String keyword, String exclude, Pageable pageable);
}
