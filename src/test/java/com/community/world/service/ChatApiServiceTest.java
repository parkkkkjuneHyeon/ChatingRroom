package com.community.world.service;

import com.community.world.domain.ChatingRoom;
import com.community.world.domain.Member;
import com.community.world.dto.chatingroom.ChatRoomDto;
import com.community.world.repository.ChatRoomRepository;
import com.community.world.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ChatApiServiceTest {
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private ChatApiService chatApiService;
    private Member defaultMember;
    private ChatRoomDto.Request defaultChatroom;
    private ChatingRoom room;

    @BeforeEach
    public void setTest() {
        defaultMember = Member.builder()
                .id(1L)
                .email("aa@naver.com")
                .password("123")
                .name("손흥민")
                .age(33)
                .gender("남")
                .createAt(LocalDateTime.now())
                .build();
        defaultChatroom = ChatRoomDto.Request.builder()
                .memberEmail("aa@naver.com")
                .chatingName("1번 채팅")
                .roomKey("123456")
                .build();
        room = ChatingRoom.builder()
                .memberEmail(defaultChatroom.getMemberEmail())
                .chatingName(defaultChatroom.getChatingName())
                .roomKey(defaultChatroom.getRoomKey())
                .createAt(LocalDateTime.now())
                .build();
    }


    @DisplayName("채팅룸을 추가한다.")
    @Test
    void addChatingRoom() {
        //given

        given(memberRepository.findByEmail(defaultMember.getEmail()))
                .willReturn(Optional.of(defaultMember));
        given(chatRoomRepository.findByChatingName(defaultChatroom.getChatingName()))
                .willReturn(Optional.empty());
        given(chatRoomRepository.save(any(ChatingRoom.class)))
                .willReturn(room);
        //when
        ChatRoomDto.Response result = chatApiService
                .addChatingRoom(defaultChatroom);

        //then
        assertEquals(defaultChatroom.getMemberEmail(), result.getMemberEmail());
        assertEquals(defaultChatroom.getChatingName(), result.getChatingName());
        assertEquals(defaultChatroom.getRoomKey(), result.getRoomKey());
    }
    @DisplayName("채팅룸 이름을 검색한다.")
    @Test
    void searchChatingRoom() {
        //given
        List<ChatingRoom> list = new ArrayList<>();
        list.add(room);
        given(chatRoomRepository.findAllByMemberEmail(anyString()))
                .willReturn(list);

        //when
        List<ChatRoomDto.Response> result = chatApiService
                .searchChatingRoom(defaultChatroom, (Member) authentication.getPrincipal());

        //then
        assertEquals(
                list.get(0).getMemberEmail(),
                result.get(0).getMemberEmail()
        );
    }
    @DisplayName("채팅룸 이름을 변경 한다.")
    @Test
    void updateChatingRoom() {
        //given
        ChatRoomDto.Request updateChatroom = ChatRoomDto.Request.builder()
                .memberEmail(defaultChatroom.getMemberEmail())
                .chatingName("2번 채팅룸")
                .roomKey(defaultChatroom.getRoomKey())
                .build();

        given(chatRoomRepository.findByRoomKey(updateChatroom.getRoomKey()))
                .willReturn(Optional.of(room));

        //when
        ChatRoomDto.Response result = chatApiService.updateChatingRoom(updateChatroom);

        //then
        assertEquals(updateChatroom.getChatingName(), result.getChatingName());
    }
    @DisplayName("채팅룸을 삭제한다.")
    @Test
    void deleteChatingRoom() {

        //when
        chatApiService.deleteChatingRoom(defaultChatroom);
        //then
        verify(chatRoomRepository,times(1))
                .deleteByRoomKey(defaultChatroom.getRoomKey());
    }
}