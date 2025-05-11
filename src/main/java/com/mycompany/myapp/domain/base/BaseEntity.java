package com.mycompany.myapp.domain.base;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 공통 엔티티의 생성일자/수정일자를 자동 관리하는 추상 클래스
 * - 엔티티는 이 클래스를 상속하여 createdAt, updatedAt 필드를 가짐
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

    @CreatedDate
    @Column(columnDefinition = "datetime default now()", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(columnDefinition = "datetime default now()")
    private LocalDateTime updatedAt;

    /**
     * 엔티티 저장 직전에 생성일과 수정일을 초기화
     */
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }

    /**
     * 엔티티 업데이트 직전에 수정일을 갱신
     */
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
