package com.pcp.funeralsvc.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreationTimestamp
    //@CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    //@LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}