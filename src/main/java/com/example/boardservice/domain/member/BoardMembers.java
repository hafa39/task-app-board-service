package com.example.boardservice.domain.member;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("board_members")
public record BoardMembers(
        @Id
        Long id,
        @Column("board_id")
        Long boardId,
        @Column("user_id")
        String userId
) {
        public static BoardMembers of(Long boardId, String userId){
                return new BoardMembers(null,boardId,userId);
        }
}
