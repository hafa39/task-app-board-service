package com.example.boardservice.web.api;

import com.example.boardservice.domain.team.Team;
import com.example.boardservice.domain.team.TeamService;
import com.example.boardservice.web.dto.CreateTeamPayload;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/teams")
public class TeamController {

    private static final Logger log = LoggerFactory.getLogger(TeamController.class);

    private TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public List<Team> getTeamsByUser(@AuthenticationPrincipal Jwt jwt){
        return teamService.findTeamsByCreator(jwt.getSubject());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Team postTeam(@AuthenticationPrincipal Jwt jwt,@Valid @RequestBody CreateTeamPayload payload){
        log.info("Creating team from user with id: {}",jwt.getSubject());
        return teamService.createTeam(payload, jwt.getSubject());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@teamServiceImpl.isTeamCreator(#id,#jwt.subject)")
    public Team updateTeam(@AuthenticationPrincipal Jwt jwt,@Valid @RequestBody CreateTeamPayload payload, @PathVariable Long id){
        log.info("Updating team from payload: {}",payload);
        return teamService.updateTeam(payload,id);
    }
}
