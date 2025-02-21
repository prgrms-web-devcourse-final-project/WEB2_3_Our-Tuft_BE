package com.example.web2_3_ourtuft_be.user.repository;

import com.example.web2_3_ourtuft_be.user.entity.Inventory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findByUserId(Long userId);
}
