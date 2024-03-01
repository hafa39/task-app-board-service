package com.example.boardservice.board;

import com.example.boardservice.domain.exc.TeamNotFoundException;
import com.example.boardservice.domain.member.BoardMembers;
import com.example.boardservice.domain.team.Team;
import com.example.boardservice.domain.team.TeamRepository;
import com.example.boardservice.domain.team.TeamService;
import com.example.boardservice.domain.team.TeamServiceImpl;
import com.example.boardservice.web.dto.CreateTeamPayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    TeamRepository teamRepository;

    @InjectMocks
    TeamServiceImpl teamService;

    @Test
    void whenUpdateExistingTeamShouldReturnUpdatedTeam(){
        CreateTeamPayload payload = new CreateTeamPayload("team");
        Long teamId = 1L;
        Team existingTeam = new Team(teamId,"teamname",false,null,null,null,null,0);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));

        Team updated = Team.updateName(payload.name(), existingTeam);
        when(teamRepository.save(updated)).thenReturn(updated);

        Team updatedEntity = teamService.updateTeam(payload, teamId);
        assertThat(updatedEntity).isEqualTo(updated);

        verify(teamRepository).findById(teamId);
        verify(teamRepository).save(updated);
    }

    @Test
    void whenUpdateNotExistedTeamThatShouldThrowExc(){
        Long teamId = 1L;
        CreateTeamPayload payload = new CreateTeamPayload("New Team Name");

        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class, () -> teamService.updateTeam(payload, teamId));

        verify(teamRepository).findById(teamId);
        verifyNoMoreInteractions(teamRepository);
    }
}
