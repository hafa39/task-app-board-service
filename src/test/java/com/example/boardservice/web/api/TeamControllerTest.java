package com.example.boardservice.web.api;

import com.example.boardservice.config.SecurityConfig;
import com.example.boardservice.domain.team.Team;
import com.example.boardservice.domain.team.TeamService;
import com.example.boardservice.web.dto.CreateTeamPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
@Import(SecurityConfig.class)
public class TeamControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    TeamService teamService;

    @MockBean
    JwtDecoder jwtDecoder;

    private static final String ROLE_EMPLOYEE = "ROLE_employee";
    private static final String USER_ID = "user_id";

    private static final String TEAM_NAME = "T1";

    @Test
    void whenGetTeamsByAuthUserShouldReturn200() throws Exception {
        Team expectedTeam = new Team(1L, "T1", false, Instant.now(), Instant.now(), USER_ID, USER_ID, 0);
        given(teamService.findTeamsByCreator(USER_ID)).willReturn(List.of(expectedTeam));

        mockMvc.perform(get("/teams")
                        .with(jwt()
                                .jwt(c ->
                                        c.subject(USER_ID).build()
                                )))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void whenGetTeamsByUnAuthUserShouldReturn401() throws Exception {
        mockMvc.perform(get("/teams"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }


    @Test
    void whenPostTeamThenShouldReturn201() throws Exception {
        var payload = new CreateTeamPayload(TEAM_NAME);
        Team expectedTeam = new Team(1L, "T1", false, Instant.now(), Instant.now(), USER_ID, USER_ID, 0);
        when(teamService.createTeam(payload, USER_ID)).thenReturn(expectedTeam);

        String body = mapper.writeValueAsString(payload);

        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(jwt()
                                .jwt(c -> c.subject(USER_ID))))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void whenPostWithInvalidInputShouldReturn422() throws Exception {
        var payload = new CreateTeamPayload("");
        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload))
                        .with(jwt()
                                .jwt(c -> c.subject(USER_ID))))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }
}
