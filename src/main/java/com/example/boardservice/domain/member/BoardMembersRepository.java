package com.example.boardservice.domain.member;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardMembersRepository extends CrudRepository<BoardMembers,Long> {

    @Transactional(readOnly = true)
    List<BoardMembers> findByUserId(String userId);

    @Transactional(readOnly = true)
    List<BoardMembers> findByBoardId(Long boardId);
}
