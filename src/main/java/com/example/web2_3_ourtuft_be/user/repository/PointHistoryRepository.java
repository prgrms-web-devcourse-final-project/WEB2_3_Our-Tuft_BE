package com.example.web2_3_ourtuft_be.user.repository;

import com.example.web2_3_ourtuft_be.user.entity.PointHistory;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findByMemberPointId(Long memberPointId);

    List<PointHistory> findByMemberPointIdAndPointChangeTimeBetween(
            Long userId, LocalDateTime start, LocalDateTime end);
}
