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
    public void loadBoards() {

        if (teamRepository.count() == 0 && boardRepository.count()==0){
            String bjornID = "fb32a905-3bf5-4bd0-836e-94a98faa9fec";

            Team team1 = teamRepository.save(Team.of("team1", bjornID));
            Team team2 = teamRepository.save(Team.of("team2", bjornID));


            Board b1 = new Board(null, "b1", "desc b1", team1.id(), false, null, null, bjornID, bjornID, 0);
            Board b2 = new Board(null, "b2", "desc b2", team1.id(), false, null, null, bjornID, bjornID, 0);
            Board b3 = new Board(null, "b3", "desc b3", team2.id(), false, null, null, "test", "test", 0);
            Board b1_saved = boardRepository.save(b1);
            Board b2_saved = boardRepository.save(b2);
            Board b3_saved = boardRepository.save(b3);

            BoardMembers b1_bjorn = new BoardMembers(null,b1_saved.id(), bjornID);
            BoardMembers b2_bjorn = new BoardMembers(null,b2_saved.id(), bjornID);
            BoardMembers b3_bjorn = new BoardMembers(null,b3_saved.id(), bjornID);
            boardMembersRepository.saveAll(List.of(b1_bjorn, b2_bjorn, b3_bjorn));

            Board privateBoard = new Board(null, "private", "desc private", 0L, false, null, null, bjornID, bjornID, 0);
            Board savedPrivate = boardRepository.save(privateBoard);
            BoardMembers private_bjorn = new BoardMembers(null,savedPrivate.id(), bjornID);
            boardMembersRepository.save(private_bjorn);
        }
    }
}
