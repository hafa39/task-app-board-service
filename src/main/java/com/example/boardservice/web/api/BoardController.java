package com.example.boardservice.web.api;

import com.example.boardservice.domain.board.Board;
import com.example.boardservice.domain.board.BoardService;
import com.example.boardservice.domain.member.MemberOfBoard;
import com.example.boardservice.web.dto.AddBoardMemberPayload;
import com.example.boardservice.web.dto.BoardResult;
import com.example.boardservice.web.dto.CreateBoardPayload;
import com.example.boardservice.web.dto.MemberData;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private static final Logger log =
            LoggerFactory.getLogger(BoardController.class);
    private BoardService boardService;
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping()
    public List<Board> getBoardsByUser(@AuthenticationPrincipal Jwt jwt){
        return boardService.findBoardsByMembership(jwt.getSubject());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Board postBoard(@RequestBody @Valid CreateBoardPayload payload){
        return boardService.createBoard(payload);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@boardServiceImpl.isUserMemberOfBoard(#id,#jwt.subject)")
    public Map<String,Object> getBoardById(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt){
        Board board = boardService.findById(id).orElseThrow();
        List<MemberOfBoard> membersByBoard = boardService.findMemberByBoardId(board.id());
        return BoardResult.build(board,membersByBoard);
    }

    @PreAuthorize("@boardServiceImpl.isBoardCreator(#boardId,#jwt.subject)")
    @PostMapping("/{boardId}/members")
    public MemberData addMember(@PathVariable Long boardId,
                                @RequestBody AddBoardMemberPayload payload,
                                @AuthenticationPrincipal Jwt jwt){
        Board board = boardService.findById(boardId).orElseThrow();
        MemberOfBoard memberOfBoard = boardService.addMember(board.id(), payload);
        return new MemberData(memberOfBoard);
    }

    @PreAuthorize("@boardServiceImpl.isUserMemberOfBoard(#id,#jwt.subject)")
    @DeleteMapping("/{id}")
    public void removeBoard(@PathVariable Long id,@AuthenticationPrincipal Jwt jwt){
        boardService.remove(id);
    }

}
