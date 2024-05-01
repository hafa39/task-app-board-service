package com.example.boardservice.web.dto;

import com.example.boardservice.domain.member.MemberOfBoard;

public class MemberData {
    private String userId;
    private String shortName;

    private String username;

    private String fullName;


    public MemberData(MemberOfBoard user) {
        this.userId = user.id();
        this.shortName = user.getInitials().toUpperCase();
        this.username = user.username();
        this.fullName= user.firstName()+ " "+user.lastName();


    }

    public String getUserId() {
        return userId;
    }

    public String getShortName() {
        return shortName;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }
}
