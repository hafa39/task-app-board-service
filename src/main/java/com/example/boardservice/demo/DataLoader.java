package com.example.boardservice.demo;

import com.example.boardservice.domain.board.Board;
import com.example.boardservice.domain.board.BoardRepository;
import com.example.boardservice.domain.member.BoardMembers;
import com.example.boardservice.domain.member.BoardMembersRepository;
import com.example.boardservice.domain.team.Team;
import com.example.boardservice.domain.team.TeamRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Profile("dev")
public class DataLoader {

    private BoardRepository boardRepository;
    private BoardMembersRepository boardMembersRepository;
    private TeamRepository teamRepository;

    public DataLoader(BoardRepository boardRepository, BoardMembersRepository boardMembersRepository, TeamRepository teamRepository) {
        this.boardRepository = boardRepository;
        this.boardMembersRepository = boardMembersRepository;
        this.teamRepository = teamRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void loadInitialData() {
        if (teamRepository.count() == 0 && boardRepository.count() == 0) {
            String defaultUserID = "fb32a905-3bf5-4bd0-836e-94a98faa9fec";

            // Create teams
            Team team1 = teamRepository.save(Team.of("Developer Team", defaultUserID));

            // Create boards for team1
            Board board1 = new Board(null, "Project Board", "Manage ongoing tasks", team1.id(), false, null, null, defaultUserID, defaultUserID, 0);

            // Save boards
            Board savedBoard1 = boardRepository.save(board1);

            // Add members to boards
            BoardMembers board1Member = new BoardMembers(null, savedBoard1.id(), defaultUserID);
            boardMembersRepository.saveAll(List.of(board1Member));
        }
    }
}
