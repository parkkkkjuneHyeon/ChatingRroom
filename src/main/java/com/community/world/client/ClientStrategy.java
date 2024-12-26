package com.community.world.client;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class ClientStrategy {

    private HashMap<ClientList, OauthClient> clientMap;

    public ClientStrategy(List<OauthClient> clientList) {
        HashMap<ClientList, OauthClient> map = new HashMap<>();
        clientList.forEach(client -> map.put(client.getClient(), client));
        this.clientMap = map;
    }

    public OauthClient getOauthClient(ClientList clientList) {
        return clientMap.get(clientList);
    }
}
