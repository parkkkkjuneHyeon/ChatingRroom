package com.community.world.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(
        name="member",
        uniqueConstraints = {@UniqueConstraint(columnNames = "email")},
        indexes = {@Index(name = "idx_member_email", columnList = "email")}
)
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max=45)
    @Column(name="email", unique = true, nullable = false)
    @NotBlank
    private String email;

    @Size(min=2, max=60)
    @Column(nullable = false)
    @NotBlank
    private String password;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Min(0)
    @Max(200)
    private int age;

    @Column
    private String gender;

    @Column
    ZonedDateTime createAt;

    @Column
    ZonedDateTime updateAt;

    @PrePersist
    public void prePersist() {
        this.createAt = ZonedDateTime.now();
        this.updateAt = this.createAt;
    }
    @PreUpdate
    public void PreUpdate() {
        this.createAt = ZonedDateTime.now();
    }

    @OneToMany(
            mappedBy = "member",
            cascade = CascadeType.ALL,
            //부모 엔티티와의 관계가 끊어진 자식엔티티를 자동으로 삭제
            orphanRemoval = true,
            //연관된 엔티티를 실제로 사용할 때까지 로딩하지 않고 필요할 때 로딩함.
            fetch = FetchType.LAZY
    )
    private List<ChatingRoom> chatingRoom;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
