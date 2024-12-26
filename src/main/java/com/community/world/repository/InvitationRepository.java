package com.community.world.repository;

import com.community.world.domain.InvitationMessage;
import com.community.world.domain.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository
        extends JpaRepository<InvitationMessage, Long> {

    List<InvitationMessage> findByToMemberEmail(String email);

    //INSERT, UPDATE, DELETE임을 명시하는 어노테이션
    @Modifying
    @Transactional
    @Query(value =
            "DELETE " +
            "FROM INVITATION_MESSAGE i " +
            "WHERE TO_MEMBER_EMAIL = :email " +
                    "AND CHATING_ROOM_KEY = :roomKey " +
                    "AND CHATING_ROOM_NAME = :roomName", nativeQuery = true)
    void deleteByInvitation(
            @Param("email") String email,
            @Param("roomKey") String roomKey,
            @Param("roomName") String roomName);

}
