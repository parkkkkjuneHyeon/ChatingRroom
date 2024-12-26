package com.community.world.service;

import com.community.world.domain.ChatingRoom;
import com.community.world.domain.Member;
import com.community.world.dto.chatingroom.ChatRoomDto;
import com.community.world.exception.ChatingRoomException;
import com.community.world.exception.MemberException;
import com.community.world.exception.exceptionType.MemberExceptionCode;
import com.community.world.repository.ChatRoomRepository;
import com.community.world.repository.MemberRepository;
import com.community.world.util.RoomKey;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.community.world.exception.exceptionType.ChatingRoomExceptionCode.NOEXISTSCHATINGROOM;
import static com.community.world.validation.Validation.validationFindEmail;
import static com.community.world.validation.Validation.validationRoomName;

@Service
public class ChatApiService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public ChatApiService(
            ChatRoomRepository chatRoomRepository,
            MemberRepository memberRepository
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.memberRepository = memberRepository;
    }


    @Transactional
    public ChatRoomDto.Response addChatingRoom(
            ChatRoomDto.Request request
    ) {
        validationFindEmail(memberRepository, request.getMemberEmail());
        validationRoomName(chatRoomRepository, request.getChatingName());

        ChatingRoom chatingRoom = ChatingRoom.builder()
                .memberEmail(request.getMemberEmail())
                .chatingName(request.getChatingName())
                .roomKey(RoomKey.getRoomKey(request.getMemberEmail()))
                .build();

        return ChatRoomDto.Response.getChatRoomDto(
                        chatRoomRepository.save(chatingRoom));
    }

    public List<ChatRoomDto.Response> searchChatingRoom(
            ChatRoomDto.Request request,
            Member member) {

        List<ChatingRoom> chatingRoomList =
                chatRoomRepository
                        .findAllByMemberEmail(member.getEmail());

        return chatingRoomList
                .stream()
                .map(ChatRoomDto.Response::getChatRoomDto)
                .toList();
    }

    @Transactional
    public ChatRoomDto.Response updateChatingRoom(
            ChatRoomDto.Request request
    ) {
        //이메일 룸키 채팅방 이름이 같은것만 찾기
        ChatingRoom chatingRoom = chatRoomRepository
                .findByRoomKey(request.getRoomKey())
                .orElseThrow(() ->
                        new ChatingRoomException(NOEXISTSCHATINGROOM)
                );

        chatingRoom.setChatingName(request.getChatingName());

        return ChatRoomDto.Response.getChatRoomDto(chatingRoom);
    }
    @Transactional
    public void deleteChatingRoom(
            ChatRoomDto.Request request
    ) {
        //이메일 룸키 채팅방 이름이 같은것만 찾아서 삭제
        chatRoomRepository.deleteByRoomKey(request.getRoomKey());
    }

    @Transactional
    public ChatRoomDto.Response joinChatingRoom(ChatRoomDto.Request request) {
        ChatingRoom chatingRoom = ChatingRoom.builder()
                .memberEmail(request.getMemberEmail())
                .chatingName(request.getChatingName())
                .roomKey(request.getRoomKey())
                .build();

        return ChatRoomDto.Response.getChatRoomDto(
                chatRoomRepository.save(chatingRoom));
    }
}
