package com.example.boardservice.board;

import com.example.boardservice.config.DataConfig;
import com.example.boardservice.domain.board.Board;
import com.example.boardservice.domain.board.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Import(DataConfig.class)
public class BoardRepositoryTests {

    @Autowired
    BoardRepository boardRepository;
    @Container
    static PostgreSQLContainer<?> database = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.4"));

    static {
        database.start();
    }
    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }

    @Test
    @WithMockUser(username = "Alex")
    public void whenFindByTeamIdTheReturnBoards(){
        Long expectedTeamId = 1L;
        Long otherTeamId = 2L;
        Board b1 = Board.of("b1", "d1", expectedTeamId);
        Board b2 = Board.of("b2", "d1", expectedTeamId);
        Board b3 = Board.of("b3", "d1", otherTeamId);
        boardRepository.saveAll(List.of(b1,b2,b3));

        List<Board> byTeamId = boardRepository.findByTeamId(expectedTeamId);
        assertThat(byTeamId).hasSize(2);
        assertThat(byTeamId).allMatch(b->b.name().equals(b1.name())||b.name().equals(b2.name()));
    }
}
