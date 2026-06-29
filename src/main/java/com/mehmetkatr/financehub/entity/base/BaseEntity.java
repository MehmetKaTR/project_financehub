package com.mehmetkatr.financehub.entity.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass //Bu sinifin kendisi bir tablo degil ancak  bunu extend edenler tablo
@Getter
@Setter
public abstract class BaseEntity {

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist // ilk kayit olusunca tetiklenir
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate // kayit guncellenirken tetiklenir hepsi otomatik
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
