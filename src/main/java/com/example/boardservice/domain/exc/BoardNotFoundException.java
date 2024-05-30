package com.example.boardservice.domain.exc;

public class BoardNotFoundException extends RuntimeException{
    public BoardNotFoundException(Long boardId) {
        super("Board not found with ID: " + boardId);
    }
}
