package com.bookin.bookin.domain.user.entity;

import com.bookin.bookin.domain.review.entity.Review;
import com.bookin.bookin.domain.user.entity.enums.Provider;
import com.bookin.bookin.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(nullable = false)
    private Long providerId;

    @Column
    private String profilePicture; // 프로필 사진 경로 추가

    @Column(nullable = false)
    private String email; // 이메일 필드 추가

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews;
}
