package com.example.bankcards.exception.customExceptions.userExceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(final Long userId) {
        super("User not found with id: " + userId);
    }
}
