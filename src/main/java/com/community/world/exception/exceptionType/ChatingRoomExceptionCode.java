package com.community.world.exception.exceptionType;

import lombok.Getter;

@Getter
public enum ChatingRoomExceptionCode {
    EXISTSCHATINGROOM("채팅룸이 이미 존재합니다."),
    NOEXISTSCHATINGROOM("채팅룸이 존재 하지 않습니다.");
    private String description;

    ChatingRoomExceptionCode(String description) {
        this.description = description;
    }
}