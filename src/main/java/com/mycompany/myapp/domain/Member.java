package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.base.BaseEntity;
import com.mycompany.myapp.domain.enums.Gender;

import lombok.*;

import javax.persistence.*;

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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
