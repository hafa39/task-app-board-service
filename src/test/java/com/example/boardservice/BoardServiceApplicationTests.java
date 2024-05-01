package com.example.boardservice;

import com.example.boardservice.domain.team.Team;
import com.example.boardservice.domain.team.TeamRepository;
import com.example.boardservice.web.dto.CreateTeamPayload;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class BoardServiceApplicationTests {

    private static KeycloakToken bjornTokens;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TeamRepository teamRepository;

    @Container
    private static final KeycloakContainer keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:19.0")
            .withRealmImportFile("test-realm.json");

    @Container
    static PostgreSQLContainer<?> database = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.4"));

    static {
        database.start();
        keycloakContainer.start();
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "realms/TaskAgile");
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }
    @BeforeAll
    static void generateAccessTokens() {
        WebClient webClient = WebClient.builder()
                .baseUrl(keycloakContainer.getAuthServerUrl() + "realms/TaskAgile/protocol/openid-connect/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        //isabelleTokens = authenticateWith("isabelle", "password", webClient);
        bjornTokens = authenticateWith("bjorn", "password", webClient);
    }
    @AfterEach
    void dropTeamRepository(){
        teamRepository.deleteAll();
    }
    @Test
    void whenPostTeamThenTeamCreated(){
        String teamName = "Test team";
        var payload = new CreateTeamPayload(teamName);

        webTestClient.post()
                .uri("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .headers(headers -> headers.setBearerAuth(bjornTokens.accessToken()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Team.class)
                .value(team ->{
                    assertThat(team).isNotNull();
                    assertThat(team.creatorId()).isEqualTo(bjornTokens.extractSubject());
                    assertThat(team.name()).isEqualTo(teamName);
                });
    }

    @Test()
    void whenGetTeamsByUserThenTeamsReturned(){
        String teamName1 = "Test team 1";
        var p1 = new CreateTeamPayload(teamName1);

        String teamName2 = "Test team 2";
        var p2 = new CreateTeamPayload(teamName2);

        webTestClient.post()
                .uri("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(p1)
                .headers(headers -> headers.setBearerAuth(bjornTokens.accessToken()))
                .exchange()
                .expectBody(Team.class)
                .value(team ->{
                    assertThat(team).isNotNull();
                });

        webTestClient.post()
                .uri("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(p2)
                .headers(headers -> headers.setBearerAuth(bjornTokens.accessToken()))
                .exchange()
                .expectBody(Team.class)
                .value(team ->{
                    assertThat(team).isNotNull();
                });

        webTestClient.get()
                .uri("/teams")
                .headers(headers -> headers.setBearerAuth(bjornTokens.accessToken()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Team.class)
                .value(teams -> {
                    assertThat(teams).hasSize(2);
                    teams.forEach(team ->
                            assertThat(team.creatorId()).isEqualTo(bjornTokens.extractSubject()));
                });
    }

    private record KeycloakToken(String accessToken) {

        @JsonCreator
        private KeycloakToken(@JsonProperty("access_token") final String accessToken) {
            this.accessToken = accessToken;
        }

        public String extractSubject() {
            try {
                JWT jwt = JWTParser.parse(accessToken);
                JWTClaimsSet jwtClaimsSet = jwt.getJWTClaimsSet();
                return jwtClaimsSet.getSubject();
            } catch (Exception e) {
                // Handle parsing exceptions
                throw new RuntimeException("Failed to extract subject from JWT", e);
            }
        }
    }

    private static KeycloakToken authenticateWith(String username, String password, WebClient webClient) {
        return webClient
                .post()
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", "polar-test")
                        .with("username", username)
                        .with("password", password)
                )
                .retrieve()
                .bodyToMono(KeycloakToken.class)
                .block();
    }
}
