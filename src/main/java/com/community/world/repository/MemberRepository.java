package com.community.world.repository;

import com.community.world.domain.ChatingRoom;
import com.community.world.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Query(value = "SELECT * FROM MEMBER m WHERE email LIKE CONCAT(:query, '%')", nativeQuery = true)
    List<Member> findByMemberEmail(@Param("query")String email);

}
