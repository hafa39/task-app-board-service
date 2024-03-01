package com.example.boardservice.domain.exc;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);}
}
