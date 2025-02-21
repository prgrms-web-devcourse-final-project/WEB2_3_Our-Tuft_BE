package com.example.web2_3_ourtuft_be.user.repository;

import com.example.web2_3_ourtuft_be.user.entity.MemberPoint;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPointRepository extends JpaRepository<MemberPoint, Long> {

    Optional<MemberPoint> findByUserId(Long userId);
}
