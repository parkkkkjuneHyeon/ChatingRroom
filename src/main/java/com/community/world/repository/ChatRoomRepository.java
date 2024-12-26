package com.community.world.repository;

import com.community.world.domain.ChatingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatingRoom, Long> {
    List<ChatingRoom> findAllByMemberEmail(String memberEmail);

    Optional<ChatingRoom> findByChatingName(String chatingName);

    void deleteByRoomKey(String roomkey);

    Optional<ChatingRoom> findByRoomKey(String roomKey);
}
