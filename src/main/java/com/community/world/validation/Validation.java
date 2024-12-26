package com.community.world.validation;

import com.community.world.domain.Member;
import com.community.world.dto.member.MemberDto;
import com.community.world.exception.ChatingRoomException;
import com.community.world.exception.MemberException;
import com.community.world.repository.ChatRoomRepository;
import com.community.world.repository.MemberRepository;

import static com.community.world.exception.exceptionType.ChatingRoomExceptionCode.EXISTSCHATINGROOM;
import static com.community.world.exception.exceptionType.MemberExceptionCode.*;

public class Validation {



    public static boolean validationFindEmail(
            MemberRepository memberRepository,
            String email) {

        if(memberRepository.findByEmail(email).isEmpty())
            throw new MemberException(NOMATCHEMAIL);
        return true;
    }
    public static boolean validationExistsEmail(
            MemberRepository memberRepository,
            String email) {

        if(memberRepository.findByEmail(email).isPresent())
            throw new MemberException(EXISTSEMAIL);
        return true;
    }

    public static boolean validationRoomName(
            ChatRoomRepository chatRoomRepository,
            String chatingName) {

        if (chatRoomRepository.findByChatingName(chatingName).isPresent())
            throw new ChatingRoomException(EXISTSCHATINGROOM);
        return true;
    }
    public static void checkValidationPassword(
            String requsetPassword,
            String memberPassword) {

        if(memberPassword.matches(requsetPassword))
            throw new MemberException(LOGIN_ERROR);
    }
}
