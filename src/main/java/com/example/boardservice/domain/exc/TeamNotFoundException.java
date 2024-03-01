package com.example.boardservice.domain.exc;

public class TeamNotFoundException extends NotFoundException{
    public TeamNotFoundException(Long teamId) {
        super("Board not found with ID: " + teamId);
    }
}
