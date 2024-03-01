package com.example.boardservice.domain.board;

import com.example.boardservice.domain.member.MemberOfBoard;
import com.example.boardservice.web.dto.AddBoardMemberPayload;
import com.example.boardservice.web.dto.CreateBoardPayload;

import java.util.List;
import java.util.Optional;

public interface BoardService {

    /**
     * Find the boards that a user is a member, including those boards
     * the user created as well as joined.
     *
     * @param userId id of user
     * @return a list of boards or an empty list if none found
     */
    List<Board> findBoardsByMembership(String userId);

    /**
     * Create a new board
     *
     * @param payload the CreateBoardPayload instance
    //   * @param userId the id of user
     * @return the new board just created
     */
    Board createBoard(CreateBoardPayload payload);

    /**
     * Find board by its id
     *
     * @param boardId the id of the board
     * @return the optional board instance, optional null if not found
     */
    Optional<Board> findById(Long boardId);

    /**
     * Check the user is a member of the board
     * @param boardId
     * @param userId
     * @return
     */
    boolean isUserMemberOfBoard(Long boardId, String userId);

    /**
     *
     * @param boardId
     * @return
     */
    List<MemberOfBoard> findMemberByBoardId(Long boardId);


    MemberOfBoard addMember(Long boardId, AddBoardMemberPayload payload);

    void remove(Long boardId);

    boolean isBoardCreator(Long boardId, String userId);
}
