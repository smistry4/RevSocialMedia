package com.example.socialmedia.exception;

public class ClientErrorException extends RuntimeException{
    public ClientErrorException(String message) {
        super(message);
    }
}
