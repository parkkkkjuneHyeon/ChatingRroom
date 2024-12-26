package com.community.world.service;

import com.community.world.domain.Member;
import com.community.world.domain.Message;
import com.community.world.dto.message.MessageDto;
import com.community.world.repository.MemberRepository;
import com.community.world.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MessageService messageService;

    private MessageDto.Request defaultReq;
    private MessageDto.Response defaultRes;
    private Message sendMessage;
    private Message receiveMessage;
    private Member member;

    @BeforeEach
    public void setTest() {
        member = Member.builder()
                .id(1L)
                .email("aaa@naver.com")
                .password("123")
                .name("손흥민")
                .age(34)
                .gender("남")
                .build();
        
        sendMessage = Message.builder()
                .memberEmail("aaa@naver.com")
                .chatingRoomName("aaa")
                .memberName("손흥민")
                .chatingRoomKey("111")
                .message("안녕")
                .createAt(LocalDateTime.of(2024,5,31,19,43))
                .build();

        receiveMessage = Message.builder()
                .memberEmail("bbb@naver.com")
                .chatingRoomName("aaa")
                .memberName("손흥민")
                .chatingRoomKey("111")
                .message("그래 나도 반가워")
                .createAt(LocalDateTime.of(2024,5,31,19,44))
                .build();
    }
    @DisplayName("메시지를 추가한다.")
    @Test
    void addMessage() {
        //given
        MessageDto.Request request = MessageDto.Request
                .getMessageDtoRequest(sendMessage);

        given(messageRepository.save(any(Message.class)))
                .willReturn(sendMessage);
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));
        //when
        MessageDto.Response result = messageService.addMessage(request);

        //then
        assertEquals(sendMessage.getMemberEmail(), result.getMemberEmail());
        assertEquals(sendMessage.getChatingRoomName(), result.getChatingRoomName());
        assertEquals(sendMessage.getChatingRoomKey(), result.getChatingRoomKey());
        assertEquals(sendMessage.getMessage(), result.getMessage());
    }
    @DisplayName("메시지를 모두 찾아낸다.")
    @Test
    void searchMessages() {
        //given
        List<Message> list = List.of(sendMessage, receiveMessage);
        List<MessageDto.Response> respList = list
                .stream()
                .map(MessageDto.Response::getMessageDtoResponse)
                .sorted()
                .toList();
        MessageDto.Request request = MessageDto.Request.getMessageDtoRequest(sendMessage);
        given(messageRepository.findAllByChatingRoomKey(anyString()))
                .willReturn(list);

        //when
        List<MessageDto.Response> result = messageService.searchMessages(request);

        //then
        for (int i=0; i<list.size(); i++) {
            assertEquals(
                    respList.get(i).getMemberEmail(),
                    result.get(i).getMemberEmail());

            assertEquals(
                    respList.get(i).getChatingRoomName(),
                    result.get(i).getChatingRoomName());

            assertEquals(
                    respList.get(i).getMemberName(),
                    result.get(i).getMemberName());

            assertEquals(
                    respList.get(i).getChatingRoomKey(),
                    result.get(i).getChatingRoomKey());

            assertEquals(
                    respList.get(i).getMessage(),
                    result.get(i).getMessage());

            assertEquals(
                    respList.get(i).getCreateAt(),
                    result.get(i).getCreateAt()
            );
            //가장 최근 시간대가 먼저 나와야 함.
            System.out.println(result.get(i).getCreateAt());
        }
    }
    @DisplayName("메시지를 모두 삭제한다.")
    @Test
    void deleteMessages() {
        //given
        defaultReq = MessageDto.Request.getMessageDtoRequest(sendMessage);
        //when
        messageService.deleteMessages(defaultReq);
        verify(messageRepository,times(1))
                .deleteByMemberEmailAndChatingRoomNameAndChatingRoomKey(
                        defaultReq.getMemberEmail(),
                        defaultReq.getChatingRoomName(),
                        defaultReq.getChatingRoomKey()
                );
        //then
    }
}