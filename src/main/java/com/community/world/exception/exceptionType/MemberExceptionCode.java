package com.community.world.exception.exceptionType;

import com.community.world.exception.MemberException;
import lombok.Getter;

@Getter
public enum MemberExceptionCode {
    NOTEXISTID("아이디가 존재하지 않습니다."),
    LOGIN_ERROR("입력하신 정보가 다릅니다."),
    NOTFINDMEMBER("찾으시는 맴버가 없습니다."),
    NOMATCHEMAIL("이메일이 없습니다."),
    EXISTSEMAIL("이미 이메일이 존재합니다");
    private final String description;

    MemberExceptionCode(String description) {
        this.description = description;
    }
}
