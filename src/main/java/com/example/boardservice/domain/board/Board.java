package com.example.boardservice.domain.board;

import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
@Table("boards")
public record Board(
        @Id
        Long id,
        String name,
        String description,
        Long teamId,
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
        int version ) {
        public static Board of(String name, String description, Long teamId){
                return new Board(null,name,description,teamId,false,null,null,null,null,0);
        }

        public boolean isPersonal() {
                return this.teamId == 0;
        }
}
