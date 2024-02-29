package com.example.boardservice.domain.team;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team,Long> {
    List<Team> findByCreatorIdOrderById(String userId);
}

