package com.example.boardservice.web.api;

import com.example.boardservice.domain.board.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/intern")
@RestController
public class BoardInternController {
    private static final Logger log =
            LoggerFactory.getLogger(BoardInternController.class);
    private BoardService boardService;

    public BoardInternController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/isMember")
    public Boolean isMemberOfBoard(@RequestParam(value = "userId")String userId,
                                   @RequestParam(value = "boardId")Long boardId){

        log.info("Checking if user {} is a member of board {}", userId, boardId);
        return boardService.isUserMemberOfBoard(boardId, userId);
    }
}
