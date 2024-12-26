package com.community.world.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EnableJpaAuditing
@Table(
        name = "invitation_message",
        uniqueConstraints =
            @UniqueConstraint(
                    name = "uk_invitation_email_room_Key",
                    columnNames = {
                    "from_member_email",
                    "to_member_email",
                    "chating_room_key"}),
        indexes = {
                @Index(name = "idx_invitate_chating_room_key",
                        columnList = "chating_room_key"),
        }
)
public class InvitationMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inviteId;

    @Column(name = "from_member_email", nullable = false)
    private String fromMemberEmail;

    @Column(name = "to_member_email", nullable = false)
    private String toMemberEmail;

    @Column(name = "chating_room_name", nullable = false)
    private String chatingRoomName;

    @Column(
            name = "chating_room_key",
            nullable = false)
    private String chatingRoomKey;
// 생각을 해보니 초대가 거부되거나 초대에 응했으면 바로 삭제를 해야해서
// 외래키로 지정할 필요가 없음.
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "to_member_email",
//            referencedColumnName = "email",
//            nullable = false,
//            insertable = false,
//            updatable = false
//    )
//    private Member toMember;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "from_member_email",
//            referencedColumnName = "email",
//            nullable = false,
//            insertable = false,
//            updatable = false
//    )
//    private Member fromMember;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "chating_room_name",
//            referencedColumnName = "chating_name",
//            nullable = false,
//            insertable = false,
//            updatable = false
//    )
//    private ChatingRoom chatRoomName;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "chating_room_key",
//            referencedColumnName = "room_key",
//            nullable = false,
//            insertable = false,
//            updatable = false
//    )
//    private ChatingRoom chatRoomKey;
}
