package com.community.world.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "chating_room",
        uniqueConstraints =
        @UniqueConstraint(
                name = "uc_chating_room_unique",
                columnNames = {
                        "member_email",
                        "chating_name",
                        "room_key"
                }),
        indexes = {
                @Index(name = "idx_chating_room_composite", columnList = "member_email, chating_name, room_key")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class ChatingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 45)
    @Column(name = "member_email", nullable = false)
    private String memberEmail;

    @Column(
            name = "chating_name", nullable = false
    )
    @Size(max = 30)
    private String chatingName;

    @Column(
            name = "room_key",
            nullable = false
    )
    private String roomKey;

    @ManyToOne
    @JoinColumn(
            name = "member_email",
            nullable = false,
            referencedColumnName = "email",
            insertable = false,
            updatable = false
    )
    private Member member;

    @Column
    private ZonedDateTime createAt;

    @Column
    private ZonedDateTime updateAt;

    @PrePersist
    public void prePersist() {
        this.createAt = ZonedDateTime.now();
        this.updateAt = createAt;
    }
    @PreUpdate
    public void preUpdate() {
        this.updateAt = ZonedDateTime.now();
    }
}
