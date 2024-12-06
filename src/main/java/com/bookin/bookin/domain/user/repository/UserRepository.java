package com.bookin.bookin.domain.user.repository;

import com.bookin.bookin.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId);

    // 사용자 ID로 사용자 조회
    Optional<User> findByUserId(String userId);
}

