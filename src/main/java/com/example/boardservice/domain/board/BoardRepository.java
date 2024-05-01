package com.example.boardservice.domain.board;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardRepository extends CrudRepository<Board,Long> {
    List<Board> findByTeamId(Long teamId);
}
