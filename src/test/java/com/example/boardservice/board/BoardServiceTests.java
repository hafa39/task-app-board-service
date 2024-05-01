package com.example.boardservice.board;

import com.example.boardservice.domain.board.BoardRepository;
import com.example.boardservice.domain.board.BoardService;
import com.example.boardservice.domain.board.BoardServiceImpl;
import com.example.boardservice.domain.member.BoardMembers;
import com.example.boardservice.domain.member.BoardMembersRepository;
import com.example.boardservice.domain.member.MemberClient;
import com.example.boardservice.domain.member.MemberOfBoard;
import com.example.boardservice.web.dto.AddBoardMemberPayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTests {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private BoardMembersRepository boardMembersRepository;

    @Mock
    private MemberClient memberClient;

    @InjectMocks
    private BoardServiceImpl boardService;

    @Test
    void whenFindMemberByExistingBoardShouldReturnMembers(){
        Long boardId = 1L;
        String userId1 = "user1";
        String userId2 = "user2";

        BoardMembers boardUser1 = BoardMembers.of(boardId, userId1);
        BoardMembers boardUser2 = BoardMembers.of(boardId, userId2);

        MemberOfBoard member1 = new MemberOfBoard(userId1, "u1", "f1", "l1");
        MemberOfBoard member2 = new MemberOfBoard(userId2, "u2", "f2", "l2");

        when(boardMembersRepository.findByBoardId(boardId)).thenReturn(List.of(boardUser1,boardUser2));
        when(memberClient.getMemberInfoById(userId1)).thenReturn(member1);
        when(memberClient.getMemberInfoById(userId2)).thenReturn(member2);

        List<MemberOfBoard> membersByBoardId = boardService.findMemberByBoardId(boardId);
        assertThat(membersByBoardId).hasSize(2);
        assertThat(membersByBoardId).containsExactlyInAnyOrder(member1,member2);
    }

    @Test
    void testAddMember(){
        var boardId = 1L;
        var payload = new AddBoardMemberPayload("username");
        MemberOfBoard memberOfBoard = new MemberOfBoard("userId", "username", "fn", "ln");

        when(memberClient.getMemberInfoByUsername(payload.username())).thenReturn(memberOfBoard);

        // Method invocation
        MemberOfBoard addedMember = boardService.addMember(boardId, payload);

        verify(memberClient).getMemberInfoByUsername(payload.username());
        verify(boardMembersRepository).save(any(BoardMembers.class));
    }
}
