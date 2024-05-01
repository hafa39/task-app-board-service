package com.example.boardservice.domain.exc;

public class BoardNotFoundException extends NotFoundException{
    public BoardNotFoundException(Long boardId) {
        super("Board not found with ID: " + boardId);
    }
}
