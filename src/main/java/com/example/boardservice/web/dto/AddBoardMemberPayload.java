package com.example.boardservice.web.dto;

import jakarta.validation.constraints.NotBlank;

public record AddBoardMemberPayload(
        @NotBlank
        String username
){
}
