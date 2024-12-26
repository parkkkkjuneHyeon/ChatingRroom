package com.community.world.service;

import com.community.world.domain.InvitationMessage;
import com.community.world.domain.Member;
import com.community.world.dto.chatingroom.ChatRoomDto;
import com.community.world.dto.invitatationMessage.InvitationDto;
import com.community.world.repository.InvitationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitationService {
    private final InvitationRepository invitationRepository;

    public InvitationService(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    public InvitationDto.Response addInviteMessage(
            InvitationDto.Request request, Member fromMember) {
        request.setFromMemberEmail(fromMember.getEmail());//초대를 보낸 사람
        InvitationMessage invitationMessage = InvitationDto.Request
                .getInvitationMessage(request);
        return InvitationDto.Response
                .getInvitateDtoResponse(
                        invitationRepository.save(invitationMessage));
    }

    public List<InvitationDto.Response> getInvitationMessage(String email) {
        return invitationRepository
                .findByToMemberEmail(email)
                .stream().map(InvitationDto.Response::getInvitateDtoResponse)
                .toList();
    }

    public void deleteInvitationMessage(
            ChatRoomDto.Request request, Member member) {
        invitationRepository
                .deleteByInvitation(
                        member.getEmail(),
                        request.getRoomKey(),
                        request.getChatingName());
    }
}
