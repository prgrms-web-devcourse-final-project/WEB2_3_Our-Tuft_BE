package com.example.web2_3_ourtuft_be.user.repository;

import com.example.web2_3_ourtuft_be.user.entity.MemberExp;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberExpRepository extends JpaRepository<MemberExp, Long> {
    Optional<MemberExp> findByUserId(Long userId);
}
