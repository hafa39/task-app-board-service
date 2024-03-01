package com.example.boardservice.domain.team;

import com.example.boardservice.domain.exc.TeamNotFoundException;
import com.example.boardservice.web.dto.CreateTeamPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeamServiceImpl implements TeamService{

    private static final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);
    private TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public List<Team> findTeamsByCreator(String userId) {
        log.info("Fetching teams for user with id: {}",userId);
        List<Team> teams = teamRepository.findByCreatorIdOrderById(userId);
        log.info("Teams fetched: {}",teams.size());
        return teams;
    }

    @Override
    public Team createTeam(CreateTeamPayload payload, String userId) {
        log.info("Creating team from user with id: {}",userId);
        Team team = Team.of(payload.name(), userId);
        Team saved = teamRepository.save(team);
        log.info("team with id: {} is successfully created",saved.id());
        return saved;
    }

    @Override
    public Team updateTeam(CreateTeamPayload payload, Long teamId) {
        log.info("Updating team with id: {}",teamId);
        Team toUpdate = teamRepository
                .findById(teamId)
                .orElseThrow(()->new TeamNotFoundException(teamId));
        Team updated = Team.updateName(payload.name(), toUpdate);
        Team updatedEntity = teamRepository.save(updated);
        log.info("Team with id: {} is successfully updated",updatedEntity.id());
        return updatedEntity;
    }

    @Override
    public boolean isTeamCreator(Long teamId, String userId) {
        log.info("Checking if user with id: {} id is creator of team with id: {}",userId,teamId);
        Team team = teamRepository.findById(teamId).orElseThrow(()->new TeamNotFoundException(teamId));
        return team.creatorId().equals(userId);
    }
}
