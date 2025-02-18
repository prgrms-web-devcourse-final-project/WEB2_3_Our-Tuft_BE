package com.example.web2_3_ourtuft_be.user.repository;

import com.example.web2_3_ourtuft_be.user.entity.MemberProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {

    Optional<MemberProfile> findByUserId(Long userId);
}
