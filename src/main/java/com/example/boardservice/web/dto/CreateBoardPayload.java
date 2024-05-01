package com.example.boardservice.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBoardPayload(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotNull
        long teamId
) {

}
