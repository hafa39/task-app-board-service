package com.example.boardservice.domain.board;

import com.example.boardservice.domain.member.BoardMembers;
import com.example.boardservice.domain.member.BoardMembersRepository;
import com.example.boardservice.domain.member.MemberClient;
import com.example.boardservice.domain.member.MemberOfBoard;
import com.example.boardservice.web.dto.AddBoardMemberPayload;
import com.example.boardservice.web.dto.CreateBoardPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService{

    private BoardRepository boardRepository;

    private BoardMembersRepository boardMembersRepository;

    private static final Logger log =
            LoggerFactory.getLogger(BoardServiceImpl.class);

    private MemberClient memberClient;

    public BoardServiceImpl(BoardRepository boardRepository,
                            BoardMembersRepository boardMembersRepository,
                            MemberClient memberClient) {
        this.boardRepository = boardRepository;
        this.boardMembersRepository = boardMembersRepository;
        this.memberClient = memberClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Board> findBoardsByMembership(String userId) {
        log.info("Finding boards where user with id: {} is a member",userId);

        List<BoardMembers> boardMembersList = boardMembersRepository.findByUserId(userId);
        Set<Long> boardIds = boardMembersList.stream().map(BoardMembers::boardId).collect(Collectors.toSet());

        List<Board> boards = new ArrayList<>();
        boardRepository.findAllById(boardIds).forEach(boards::add);
        return boards;
    }

    @Override
    @Transactional
    public Board createBoard(CreateBoardPayload payload) {
        log.info("Creating a new board");

        Board board = Board.of(payload.name(), payload.description(), payload.teamId());
        Board savedBoard = boardRepository.save(board);

        log.info("Board created successfully with ID '{}' for team ID '{}'", savedBoard.id(), payload.teamId());

        BoardMembers boardMembers = BoardMembers.of(savedBoard.id(), savedBoard.creatorId());
        boardMembersRepository.save(boardMembers);

        log.info("User with ID '{}' added as a member to the board", savedBoard.creatorId());

        return null;
    }

    @Override
    public Optional<Board> findById(Long boardId) {
        return boardRepository.findById(boardId);
    }

    @Override
    public boolean isUserMemberOfBoard(Long boardId, String userId) {
        log.info("Checking if user '{}' is a member of board with ID '{}'", userId, boardId);

        List<Board> userBoards = findBoardsByMembership(userId);
        return userBoards.stream()
                .anyMatch(board -> board.id().equals(boardId));
    }

    public boolean isBoardCreator(Long boardId, String userId){
        log.info("Checking if user '{}' is the creator of board with ID '{}'", userId, boardId);

        Optional<Board> boardOptional = findById(boardId);
        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            return board.creatorId().equals(userId);
        } else {
            log.error("Board with ID '{}' not found. Cannot check creator.", boardId);
            throw new BoardNotFoundException(boardId);
        }
    }
    @Override
    public List<MemberOfBoard> findMemberByBoardId(Long boardId) {
        log.info("Finding members for board with ID '{}'", boardId);

        List<BoardMembers> boardMembersList = boardMembersRepository.findByBoardId(boardId);

        List<MemberOfBoard> members = boardMembersList.stream()
                .map(boardMember ->
                        memberClient.getMemberInfoById(boardMember.userId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        log.info("Found {} members for board with ID '{}'", members.size(), boardId);
        return members;
    }

    @Override
    @Transactional
    public MemberOfBoard addMember(Long boardId, AddBoardMemberPayload payload) {
        log.info("Adding member with username : {} to the board",payload.username());

        MemberOfBoard memberByUsername = memberClient.getMemberInfoByUsername(payload.username());
        BoardMembers memberToSave = BoardMembers.of(boardId, memberByUsername.id());
        boardMembersRepository.save(memberToSave);
        return memberByUsername;
    }

    @Override
    @Transactional
    public void remove(Long boardId) {
        // TODO: delete related cards
        boardRepository.deleteById(boardId);
    }
}
