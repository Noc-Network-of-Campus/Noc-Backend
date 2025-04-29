package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder @Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NameCard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(columnDefinition = "Boolean default false")
    private Boolean isUser;

    private String name;

    private String company;

    private String department;

    private String address;

    private String position;

    private String mobile;

    private String tel;

    private String fax;

    private String email;

    private String homepage;
}
