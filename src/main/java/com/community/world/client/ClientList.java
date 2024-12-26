package com.community.world.client;

public enum ClientList {
    GOOGLE,
    NAVER,
    KAKAO;

    public static ClientList getClient(String platFrom) {
        return ClientList.valueOf(platFrom.toUpperCase());
    }
}
