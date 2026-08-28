package com.mehmetkatr.account_service.entity;

import com.mehmetkatr.account_service.entity.base.JCreatedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "categories", indexes = { @Index(name = "idx_categories_type", columnList = "type") })
public class Category extends JCreatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_seq")
    @SequenceGenerator(name = "categories_seq", sequenceName = "categories_seq", allocationSize = 1)
    private Long id;

    @NotBlank @Column(length = 50, nullable = false)
    private String name;
    @NotNull @Enumerated(EnumType.STRING)
    private CategoryType type;
    @Column(length = 50, nullable = true) private String icon;
    @Column(length = 7, nullable = true) private String color;
    @Column(name = "is_default") private boolean isDefault = true;

    public enum CategoryType { INCOME, EXPENSE }
}