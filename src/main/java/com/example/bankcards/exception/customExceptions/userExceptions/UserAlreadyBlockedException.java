package com.example.bankcards.exception.customExceptions.userExceptions;

public class UserAlreadyBlockedException extends Exception {
    public UserAlreadyBlockedException(final Long userId) {
        super("User already blocked with id: " + userId);
    }
}
