package com.example.web2_3_ourtuft_be.user.repository;

import com.example.web2_3_ourtuft_be.user.entity.PreviousDayPoint;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreviousDayPointRepository extends JpaRepository<PreviousDayPoint, Long> {

    Optional<PreviousDayPoint> findByUserId(long userId);
}
