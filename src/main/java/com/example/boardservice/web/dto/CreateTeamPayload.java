package com.example.boardservice.web.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public record CreateTeamPayload(
        @NotBlank
        String name
){
        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                CreateTeamPayload payload = (CreateTeamPayload) o;
                return Objects.equals(name, payload.name);
        }

        @Override
        public int hashCode() {
                return Objects.hash(name);
        }
}
