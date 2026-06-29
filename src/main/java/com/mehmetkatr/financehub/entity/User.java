package com.mehmetkatr.financehub.entity;

import com.mehmetkatr.financehub.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_users_email", columnList = "email"),
                @Index(name = "idx_users_phone", columnList = "phone")
        }
)
/*
Veritabanında bir kolonu çok sık sorguluyorsan Oracle o kolonu baştan sona tarar — buna full table scan denir. Tablo büyüdükçe bu yavaşlar.
Index = o kolona özel bir "rehber" oluşturur, Oracle direkt oraya atlar.

WHERE sorgusu cok yapiyoruz bunlarla diye aslinda
 */

public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq") //oraclede sequence var mysqldeki identity karsiligi
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Geçerli bir email giriniz")
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @NotBlank(message = "Ad soyad boş olamaz")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Pattern(
            regexp = "^(\\+90|0)?[5][0-9]{9}$",
            message = "Geçerli bir Türk telefon numarası giriniz. Örnek: +905551234567"
    )
    @Column(nullable = true)
    private String phone;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
