package com.community.world.service;

import com.community.world.domain.Member;
import com.community.world.domain.Message;
import com.community.world.dto.message.MessageDto;
import com.community.world.exception.MemberException;
import com.community.world.repository.MemberRepository;
import com.community.world.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.community.world.exception.exceptionType.MemberExceptionCode.NOMATCHEMAIL;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    public MessageService(
            MessageRepository messageRepository,
            MemberRepository memberRepository
    ) {
        this.messageRepository = messageRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MessageDto.Response addMessage(
            MessageDto.Request request
    ) {

        Member member = memberRepository
                .findByEmail(request.getMemberEmail())
                .orElseThrow(() -> new MemberException(NOMATCHEMAIL));

        Message message = Message.builder()
                .memberEmail(request.getMemberEmail())
                .chatingRoomName(request.getChatingRoomName())
                .memberName(member.getName())
                .chatingRoomKey(request.getChatingRoomKey())
                .message(request.getMessage())
                .build();
        ;
        return  MessageDto.Response
                .getMessageDtoResponse(messageRepository.save(message));
    }

    public List<MessageDto.Response> searchMessages(
            MessageDto.Request request
    ) {

        return messageRepository
                .findAllByChatingRoomKey(request.getChatingRoomKey())
                .stream()
                .map(MessageDto.Response::getMessageDtoResponse)
                .sorted()
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMessages(MessageDto.Request request) {
        messageRepository
                .deleteByMemberEmailAndChatingRoomNameAndChatingRoomKey(
                        request.getMemberEmail(),
                        request.getChatingRoomName(),
                        request.getChatingRoomKey()
                );
    }
}
