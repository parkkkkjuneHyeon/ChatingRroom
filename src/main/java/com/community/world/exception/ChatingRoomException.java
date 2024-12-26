package com.community.world.exception;

import com.community.world.exception.exceptionType.ChatingRoomExceptionCode;
import lombok.Getter;

@Getter
public class ChatingRoomException extends RuntimeException{
    ChatingRoomExceptionCode chatingRoomExceptionCode;
    private String description;

    public ChatingRoomException(String message) {
        super(message);
    }
    public ChatingRoomException(ChatingRoomExceptionCode chatingRoomExceptionCode) {
        super(chatingRoomExceptionCode.getDescription());
        this.chatingRoomExceptionCode = chatingRoomExceptionCode;
        this.description = chatingRoomExceptionCode.getDescription();
    }

    @Override
    public String toString() {
        return description;
    }
}
