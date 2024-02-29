package com.example.boardservice.domain.member;

public record MemberOfBoard(
        String id,
        String username,
        String firstName,
        String lastName
){

    public String getInitials() {
        return username.substring(0,1) + username.substring(username.length()-1);
    }
}
