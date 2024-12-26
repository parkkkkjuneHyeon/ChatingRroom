package com.community.world.exception;

import com.community.world.exception.exceptionType.MemberExceptionCode;
import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {
    MemberExceptionCode memberExceptionCode;
    private String description;

    public MemberException(String message) {
        super(message);
    }

    public MemberException(MemberExceptionCode memberExceptionCode) {
        super(memberExceptionCode.getDescription());
        this.description = memberExceptionCode.getDescription();
        this.memberExceptionCode = memberExceptionCode;
    }
    @Override
    public String toString() {
        return description;
    }
}
