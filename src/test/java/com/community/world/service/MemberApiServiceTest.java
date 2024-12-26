package com.community.world.service;

import com.community.world.domain.Member;
import com.community.world.dto.member.MemberDto;
import com.community.world.dto.member.MemberUpdateDto;
import com.community.world.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberApiServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    MemberApiService memberApiService;

    private MemberDto.Request defaultReq;
    private Member defaultMember;
    private MemberDto.Response defaultRes;
    MemberUpdateDto.Request updateReq;

    @BeforeEach
    public void setTest() {
        defaultMember = Member.builder()
                .id(1L)
                .email("aaa@naver.com")
                .password("123")
                .name("홍길동")
                .age(34)
                .gender("여")
                .build();
        defaultReq = MemberDto.Request.builder()
                .email(defaultMember.getEmail())
                .password(defaultMember.getPassword())
                .name(defaultMember.getName())
                .age(defaultMember.getAge())
                .gender(defaultMember.getGender())
                .build();
        defaultRes = MemberDto.Response.builder()
                .email(defaultMember.getEmail())
                .name(defaultMember.getName())
                .age(defaultMember.getAge())
                .build();
        updateReq = MemberUpdateDto.Request.builder()
                .email(defaultMember.getEmail())
                .updateEmail("bbb@naver.com")
                .updatePassword("321")
                .name(defaultMember.getName())
                .age(defaultMember.getAge())
                .build();
    }

    @DisplayName("회원을 추가한다.")
    @Test
    void addMember() {
        //given
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class)))
                .willReturn(defaultMember);
        //when
        MemberDto.Response result = memberApiService.addMember(defaultReq);
        //then
        assertEquals(defaultMember.getEmail(), result.getEmail());
        assertEquals(defaultMember.getName(), result.getName());
        assertEquals(defaultMember.getAge(), result.getAge());
        assertEquals(defaultMember.getGender(), result.getGender());

    }
    @DisplayName("회원을 찾는다.")
    @Test
    void findMember() {
        //given
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(defaultMember));
        //when
        Member result = memberApiService.findMember(defaultReq);
        //then
        assertEquals(defaultMember.getEmail(), result.getEmail());
        assertEquals(defaultMember.getName(), result.getName());
        assertEquals(defaultMember.getAge(), result.getAge());
        assertEquals(defaultMember.getGender(), result.getGender());

    }
    @DisplayName("회원 이메일을 변경한다.")
    @Test
    void updateEmailMember() {
        //given
        given(memberRepository.findByEmail(updateReq.getEmail()))
                .willReturn(Optional.of(defaultMember));
        //when
        MemberUpdateDto.Response result = memberApiService
                .updateEmailMember(updateReq);
        //then
        assertEquals(updateReq.getUpdateEmail(),
                result.getEmail());
    }

    @DisplayName("회원 비밀번호를 변경한다.")
    @Test
    void updatePasswordMember() {
        //given
        given(memberRepository.findByEmail(updateReq.getEmail()))
                .willReturn(Optional.of(defaultMember));
        //when
        memberApiService.updatePasswordMember(updateReq);
        verify(memberRepository,times(1))
                .findByEmail(updateReq.getEmail());
        //then
        assertEquals(updateReq.getUpdatePassword()
                ,defaultMember.getPassword());
        assertNotNull(defaultMember.getUpdateAt());

    }
    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        //given
        given(memberRepository.findByEmail(defaultReq.getEmail()))
                .willReturn(Optional.of(defaultMember));
        //when
        memberApiService.deleteMember(defaultReq);
        verify(memberRepository,times(1))
                .findByEmail(defaultReq.getEmail());
        verify(memberRepository,times(1))
                .delete(defaultMember);
        //then

    }
    @DisplayName("로그인을 성공한다.")
    @Test
    void loginMember() {
        given(memberRepository.findByEmail(defaultReq.getEmail()))
                .willReturn(Optional.of(defaultMember));

        MemberDto.Response result = memberApiService
                .loginMember(defaultReq);

        assertNotNull(result);
    }
}