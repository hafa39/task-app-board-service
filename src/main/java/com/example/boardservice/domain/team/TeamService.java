package com.example.boardservice.domain.team;


import com.example.boardservice.web.dto.CreateTeamPayload;

import java.util.List;

public interface TeamService {
  /**
   * Find the teams that created by a user
   *
   * @param userId the id of user
   * @return a list of teams or an empty list if none found
   */
  List<Team> findTeamsByCreator(String userId);

   /**
   * Create a new team
   *
   * @param payload the CreateTeamPayload instance
   * @param userId the id of user
   * @return the newly created team or null if user does not exist
   */
  Team createTeam(CreateTeamPayload payload, String userId);

  Team updateTeam(CreateTeamPayload payload,Long teamId);
}
