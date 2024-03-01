package com.example.boardservice.domain.member;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
@Component
public class MemberClient {
    @Value("${user.service.url}")
    private String USER_SERVICE_URL;

    public MemberOfBoard getMemberInfoById(String id){
        return WebClient.create(USER_SERVICE_URL)
                .get()
                .uri("/users/"+id)
                .retrieve()
                .bodyToMono(MemberOfBoard.class)
                .block();
    }

    public MemberOfBoard getMemberInfoByUsername(String username){
        return WebClient.create(USER_SERVICE_URL)
                .get()
                .uri("/users?username="+username)
                .retrieve()
                .bodyToMono(MemberOfBoard.class)
                .block();

    }
}
