package com.community.world.util;

import com.community.world.domain.Member;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Component
public class RoomKey {

    public static String getRoomKey(String memberEmail) {
        return Base64
                .getEncoder()
                .encodeToString(
                        new String(
                                ""+memberEmail
                                        +LocalDateTime.now()
                        ).getBytes(StandardCharsets.UTF_8)
                );
    }
}
