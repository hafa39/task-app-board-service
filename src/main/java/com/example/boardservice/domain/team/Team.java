package com.example.boardservice.domain.team;

import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Objects;

@Table("teams")
public record Team(
        @Id
        Long id,
        String name,
        boolean archived,
        @CreatedDate
        Instant createdDate,
        @LastModifiedDate
        Instant lastModifiedDate,
        @CreatedBy
        String creatorId,
        @LastModifiedBy
        String lastModifiedBy,
        @Version
        int version

) {
    public static Team of(String name) {
        return new Team(null, name, false, null,null,null,null,0);
    }

    public static Team of(String name,String creatorId) {
        return new Team(null, name, false, null,null,creatorId,creatorId,0);
    }
    public static Team updateName(String nameToUpdate,Team team){
        return new Team(team.id(), nameToUpdate, team.archived(),team.createdDate(),
                team.lastModifiedDate(), team.creatorId(), team.lastModifiedBy(), team.version());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return archived == team.archived && version == team.version && Objects.equals(id, team.id) && Objects.equals(name, team.name) && Objects.equals(createdDate, team.createdDate) && Objects.equals(lastModifiedDate, team.lastModifiedDate) && Objects.equals(creatorId, team.creatorId) && Objects.equals(lastModifiedBy, team.lastModifiedBy);
    }

}

