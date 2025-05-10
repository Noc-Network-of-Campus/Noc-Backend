package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.base.BaseEntity;
import com.mycompany.myapp.domain.enums.Gender;

import lombok.*;

import javax.persistence.*;

/**
 * 서비스 회원 정보를 나타내는 엔티티
 * - 이메일을 기준으로 식별, 소셜 로그인 기반의 사용자
 */
@Entity
@Builder @Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String nickname;

    private String profileUrl;

    private boolean isRegistered;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}
