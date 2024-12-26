package com.community.world.service;

import com.community.world.domain.Member;
import com.community.world.dto.member.MemberDto;
import com.community.world.dto.member.MemberUpdateDto;
import com.community.world.exception.MemberException;
import com.community.world.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.community.world.exception.exceptionType.MemberExceptionCode.LOGIN_ERROR;
import static com.community.world.exception.exceptionType.MemberExceptionCode.NOTEXISTID;
import static com.community.world.validation.Validation.*;

@Service
public class MemberApiService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MemberApiService(
            MemberRepository memberRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public MemberDto.Response addMember(MemberDto.Request request) {
        validationExistsEmail(memberRepository, request.getEmail());
        Member member = Member.builder()
                .email(request.getEmail())
                .password(bCryptPasswordEncoder
                        .encode(request.getPassword()))
                .name(request.getName())
                .age(request.getAge())
                .gender(request.getGender())
                .build();

        return MemberDto.Response
                .getMemberResponse(memberRepository.save(member));
    }
    public MemberDto.Response findById(Long id) {
        return MemberDto.Response
                .getMemberResponse(memberRepository.findById(id)
                .orElseThrow(() ->
                    new MemberException(NOTEXISTID)
                ));
    }
    public List<MemberDto.Response> findMemberEmail(
            MemberDto.Request request, Member member) {
        return memberRepository
                .findByMemberEmail(request.getEmail())
                .stream()
                .filter(m -> !(m.getEmail().equals(member.getEmail())))
                .map(MemberDto.Response::getMemberResponse)
                .collect(Collectors.toList());
    }

    public Member findMember(MemberDto.Request request) {
        return memberRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new MemberException(LOGIN_ERROR)
                );
    }

    @Transactional
    public MemberUpdateDto.Response updateEmailMember(MemberUpdateDto.Request request) {
        Member member = memberRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new MemberException(LOGIN_ERROR)
                );
        member.setEmail(request.getUpdateEmail());
        return MemberUpdateDto.Response.getMemberUpdateDto(member);
    }

    @Transactional
    public void updatePasswordMember(MemberUpdateDto.Request request) {
        Member member = memberRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new MemberException(LOGIN_ERROR)
                );
        member.setPassword(request.getUpdatePassword());
        memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(MemberDto.Request request) {
        Member member = findMember(request);

        memberRepository.delete(member);
    }

    public MemberDto.Response loginMember(
            MemberDto.Request request) {

        Member member = findMember(request);
        checkValidationPassword(
                bCryptPasswordEncoder.encode(request.getPassword())
                , member.getPassword());

        return MemberDto.Response.getMemberResponse(member);
    }

    @Override
    public UserDetails loadUserByUsername(
            String email) throws UsernameNotFoundException {

        System.out.println("이메일 : " + email);

        return memberRepository.findByEmail(email)
                .orElseThrow(()->
                        new MemberException(NOTEXISTID));
    }

    public MemberDto.Response getMember(String memberEmail) {
        return MemberDto.Response
                .getMemberResponse(memberRepository
                        .findByEmail(memberEmail)
                        .orElseThrow(() ->
                                new MemberException(NOTEXISTID)));
    }
}
