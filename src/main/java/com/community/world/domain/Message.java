package com.community.world.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "message",
        indexes = @Index(
                name = "idx_message_chating_room_key",
                columnList = "chating_room_key"
        )
)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 70)
    @Column(name = "member_email", nullable = false)
    private String memberEmail;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Size(max = 60)
    @Column(name = "chating_room_name", nullable = false)
    private String chatingRoomName;

    @Column(
            name = "chating_room_key",
            nullable = false)
    private String chatingRoomKey;

    @Column(columnDefinition = "TEXT")
    private String message;

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
//insertable=false, updatable=false: Message 엔티티에서 member_email과
//member_name을 설정할 때, 해당 값은 이미 Member 테이블에 존재해야 합니다.
//Message 엔티티는 Member 테이블에 없는 이메일과 이름을 가질 수 없습니다.
//insertable=true, updatable=true: Message 엔티티에서 member_email과 m
//ember_name을 설정할 때, 해당 값이 Member 테이블에 존재하지 않아도 됩니다.
//이 경우 Message 엔티티는 독립적으로 이 필드들을 관리할 수 있습니다.
//member테이블에 없는 name과 email은 message에서 안받겠다 이말